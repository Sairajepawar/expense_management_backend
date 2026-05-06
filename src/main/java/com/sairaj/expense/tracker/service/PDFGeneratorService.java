package com.sairaj.expense.tracker.service;
import com.sairaj.expense.tracker.dto.ExpensesXml;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class PDFGeneratorService {

    @Value("${aws.s3.template-bucket-name}")
    private String S3_TEMPLATE_BUCKET;

    @Value("${templates.pdf}")
    private String TEMPLATE_NAME;

    private final FopFactory fopFactory;
    private final TransformerFactory transformerFactory;
    private final S3Client s3Client;

    public PDFGeneratorService(S3Client s3Client) throws Exception {
        // FopFactory is expensive — create once as a singleton
        this.fopFactory = FopFactory.newInstance(new File(".").toURI());
        this.transformerFactory = TransformerFactory.newInstance();
        this.s3Client=s3Client;
    }

    public byte[] generateExpenseReport(ExpensesXml reportDTO) throws Exception {
        Source xmlSource = marshalToSource(reportDTO);
        try(ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream()){
            log.info("Key is {}",TEMPLATE_NAME);
            Transformer transformer = getTransformer();
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

    private Transformer getTransformer() throws TransformerConfigurationException, IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(S3_TEMPLATE_BUCKET)
                .key(TEMPLATE_NAME)
                .build();
        ResponseBytes<GetObjectResponse> response = s3Client.getObject(request, ResponseTransformer.toBytes());
        try(InputStream is = response.asInputStream()) {
            Source xslSource = new StreamSource(is);
            Transformer transformer = transformerFactory.newTransformer(xslSource);
            return transformer;
        }
    }
}