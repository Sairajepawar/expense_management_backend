package com.sairaj.expense.tracker.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpenseXml{
    @XmlElement(name = "amount")
    private double amount;
    @XmlElement(name = "category")
    private String category;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate expenseDate;
}