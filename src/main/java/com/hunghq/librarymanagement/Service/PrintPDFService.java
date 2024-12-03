package com.hunghq.librarymanagement.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class PrintPDFService {
    public static void printPdf(
            String directoryName,
            String fileName,
            Map<String, String> headerContent,
            List<String> bodyContent,
            String footerContent,
            String successMessage,
            String errorMessage
    ) {
        String destinationFileName = directoryName + File.separator + fileName;

        try {
            File directory = new File(directoryName);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            PdfWriter pdfWriter = new PdfWriter(destinationFileName);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            com.itextpdf.layout.Document pdfDoc = new com.itextpdf.layout.Document(pdfDocument);
            pdfDoc.setMargins(20, 20, 20, 20);

            FontProvider fontProvider = new FontProvider();
            fontProvider.addStandardPdfFonts();
            pdfDoc.setProperty(Property.FONT_PROVIDER, fontProvider);

            if (headerContent != null) {
                headerContent.forEach((key, value) -> {
                    pdfDoc.add(new Paragraph(value)
                            .setFontSize("Title".equalsIgnoreCase(key) ? 24 : 12)
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginBottom("Title".equalsIgnoreCase(key) ? 20 : 5));
                });
                pdfDoc.add(new LineSeparator(new SolidLine()));
            }

            if (bodyContent != null) {
                for (String bodyLine : bodyContent) {
                    pdfDoc.add(new Paragraph(bodyLine).setFontSize(10));
                }
                pdfDoc.add(new LineSeparator(new SolidLine()));
            }

            if (footerContent != null) {
                pdfDoc.add(new Paragraph(footerContent)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20));
            }

            pdfDoc.close();

            showAlert("Print Successful", successMessage + destinationFileName, Alert.AlertType.INFORMATION);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showAlert("Print Failed", errorMessage, Alert.AlertType.ERROR);
        }
    }

    private static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
