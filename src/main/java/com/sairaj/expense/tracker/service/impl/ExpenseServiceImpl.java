package com.sairaj.expense.tracker.service.impl;

import com.sairaj.expense.tracker.dto.CustomerUserDetail;
import com.sairaj.expense.tracker.dto.ExpenseDetails;
import com.sairaj.expense.tracker.dto.ExpenseXml;
import com.sairaj.expense.tracker.dto.ExpensesXml;
import com.sairaj.expense.tracker.exceptions.OwnershipViolationException;
import com.sairaj.expense.tracker.model.Expense;
import com.sairaj.expense.tracker.repository.ExpenseRepository;
import com.sairaj.expense.tracker.service.ExcelGeneratorService;
import com.sairaj.expense.tracker.service.PDFGeneratorService;
import com.sairaj.expense.tracker.service.S3Service;
import com.sairaj.expense.tracker.service.interfaces.ExpenseService;
import com.sairaj.expense.tracker.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;
    private ModelMapper modelMapper;
    private PDFGeneratorService pdfGeneratorService;
    private S3Service s3Service;
    private UserService userService;
    private ExcelGeneratorService excelGeneratorService;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            ModelMapper modelMappper,
            PDFGeneratorService pdfGeneratorService,
            S3Service s3Service,
            UserService userService,
            ExcelGeneratorService excelGeneratorService){
        this.expenseRepository = expenseRepository;
        this.modelMapper = modelMappper;
        this.pdfGeneratorService = pdfGeneratorService;
        this.s3Service = s3Service;
        this.userService = userService;
        this.excelGeneratorService = excelGeneratorService;
    }

    @Override
    public void createExpense(ExpenseDetails expenseDetails, UUID userId){
        Expense expense = modelMapper.map(expenseDetails,Expense.class);
        expense.setExpenseDate(LocalDate.now());
        expense.setUserid(userId);
        expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(UUID expenseId, UUID userId){
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                ()->new EntityNotFoundException("Expense entry doesn't exists")
        );
        if(!(expense.getUserid().equals(userId))){
            throw new OwnershipViolationException("Access denied: this expense is not associated with the current user.");
        }
        expenseRepository.delete(expense);
    }

    @Override
    public void editExpense(UUID expenseId, Map<String,Object> updates, UUID userId){
        ObjectMapper mapper = new ObjectMapper();
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                ()->new EntityNotFoundException("Expense entry doesn't exists")
        );
        if(!(expense.getUserid().equals(userId))){
            throw new OwnershipViolationException("Access denied: this expense is not associated with the current user.");
        }
        for(Map.Entry<String,Object> update:updates.entrySet()){
            switch (update.getKey()){
                case "amount" -> expense.setAmount(mapper.convertValue(update.getValue(), Double.class));
                case "category" -> expense.setCategory(mapper.convertValue(update.getValue(),String.class));
                case "description" -> expense.setDescription(mapper.convertValue(update.getValue(),String.class));
                default -> throw new IllegalArgumentException("Not allowed: " + update.getKey());
            }
        }
        expenseRepository.save(expense);
    }

    @Override
    public List<ExpenseDetails> listExpenses(
            LocalDate from,
            LocalDate to,
            UUID userId){
        List<Expense> expenses;
        if(from==null && to==null){
            expenses = expenseRepository.findByUserid(userId);
        }
        else if(from==null || to==null){
            throw new IllegalArgumentException("Provide both 'from' and 'to', or neither");
        }
        else if(from.isAfter(to)){
            throw new IllegalArgumentException("'from' must not be after 'to'");
        }
        else{
            expenses = expenseRepository.findByExpenseDateBetweenAndUserid(from,to,userId);
        }
        return expenses.stream()
                .map(expense -> modelMapper.map(expense,ExpenseDetails.class))
                .toList();
    }

    @Override
    public String exportExpenses(
            LocalDate from,
            LocalDate to,
            String fileType,
            UUID userId){
        if(!fileType.equals("pdf") && !fileType.equals("xlsx")){
            throw new IllegalArgumentException("Invalid Filetype");
        }
        String fileName = generateFileName(userId,from,to)+"."+fileType;
        log.info("File name {}",fileName);
        if(!s3Service.fileExists(fileName)){
            log.info("File {} doesn't exists, starting report generation process",fileName);
            List<ExpenseDetails> expenseDetails = listExpenses(from,to,userId);
            byte[] expenseReport=null;
            String contentType="application/";
            if(fileType.equals("pdf")){
                log.info("Starting Creation of PDF file");
                expenseReport = loadPdfFile(expenseDetails);
                contentType+="pdf";
                log.info("Successful creation of PDF file");
            }
            if(fileType.equals("xlsx")){
                log.info("Starting Creation of XLSX file");
                expenseReport = loadXlsxFile(expenseDetails);
                contentType+="vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                log.info("Successful creation of XLSX file");
            }
            log.info("Report generated successfully");
            s3Service.uploadByteArray(expenseReport,fileName,contentType);
            log.info("Report Uploaded Successfully");
        }
        return s3Service.retriveFile(fileName);

    }
    private String generateFileName(UUID userId,LocalDate from,LocalDate to){
        String name = userService.getName(userId);
        name.replace(' ','-');
        name+="-";
        if(from==null || to==null){
            name+="Complete";
        }
        else{
            name+=from.toString()+"/"+to.toString();
        }
        return name;
    }

    private byte[] loadPdfFile(List<ExpenseDetails> expenseDetails){
        ExpensesXml expensesXml = new ExpensesXml();
        expensesXml.setExpenses(expenseDetails
                .stream()
                .map(expense -> modelMapper.map(expense, ExpenseXml.class))
                .toList());
        byte[] expenseReport;
        try {
            expenseReport = pdfGeneratorService.generateExpenseReport(expensesXml);
        } catch (Exception e) {
            log.error("Failed to generate pdf");
            throw new RuntimeException(e);
        }
        return expenseReport;
    }

    private byte[] loadXlsxFile(List<ExpenseDetails> expenseDetails){
        byte[] expenseReport=null;
        try {
            expenseReport = excelGeneratorService.generateExcel(expenseDetails);
        } catch (Exception e) {
            log.error("Failed to generate xlsx");
            throw new RuntimeException(e);
        }
        return expenseReport;
    }
}
