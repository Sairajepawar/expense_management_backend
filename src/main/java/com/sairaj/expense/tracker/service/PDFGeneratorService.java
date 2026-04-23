package com.sairaj.expense.tracker.service;
import com.sairaj.expense.tracker.dto.ExpensesXml;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class PDFGeneratorService {

    private final FopFactory fopFactory;
    private final TransformerFactory transformerFactory;

    public PDFGeneratorService() throws Exception {
        // FopFactory is expensive — create once as a singleton
        this.fopFactory = FopFactory.newInstance(new File(".").toURI());
        this.transformerFactory = TransformerFactory.newInstance();
    }

    public byte[] generateExpenseReport(ExpensesXml reportDTO) throws Exception {

        Source xmlSource = marshalToSource(reportDTO);

        try(InputStream is = new ClassPathResource("templates/expense-report.xsl")
                .getInputStream();
            ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream()){
            Source xslSource = new StreamSource(is);
            Transformer transformer = transformerFactory.newTransformer(xslSource);
            FOUserAgent userAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(
                    MimeConstants.MIME_PDF,
                    userAgent,
                    pdfOutput
            );
            Result result = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, result);
            return pdfOutput.toByteArray();
        }
        catch(Exception e){
            log.error(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    private Source marshalToSource(ExpensesXml dto) throws Exception {
        // Marshal DTO to XML bytes in memory
        JAXBContext context = JAXBContext.newInstance(ExpensesXml.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
        marshaller.marshal(dto, xmlOutput);
        log.info("Successfully Created XML from DTO");
        // Return as a Source for the transformer
        return new StreamSource(new ByteArrayInputStream(xmlOutput.toByteArray()));
    }

}