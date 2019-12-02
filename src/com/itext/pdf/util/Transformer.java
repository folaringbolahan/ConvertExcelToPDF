/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Akinwale Agbaje
 */
public class Transformer {

    private static final Logger logger = LoggerFactory.getLogger(Transformer.class);

    /**
     * Exports data to pdf file.
     *
     * @param fileName the file name to give the exported file
     * @param pathToFile the folder path where the file will be deposited
     * @param headers the headers in the excel file
     * @param body the body besides the headers, in the excel file
     * @return true, if export is successful
     * @throws java.io.FileNotFoundException
     */
    public static boolean exportToPDF(String fileName, String pathToFile, List<String> headers, List<Object[]> body) throws Exception {
        if (fileName == null || fileName.trim().isEmpty()) {
            logger.info("The file name must be provided");
            throw new Exception("The file name must be provided");
        }

        if (pathToFile == null || pathToFile.trim().isEmpty()) {
            logger.info("The folder path must be provided. Cannot create file directly in root directory");
            throw new Exception("The folder path must be provided. Cannot create file directly in root directory");
        }

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));

            Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            doc.open();

            Paragraph para_title = new Paragraph("Easy Coop PDF Generator.", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            para_title.setAlignment(Element.ALIGN_CENTER);
            doc.add(para_title);

            Paragraph p1 = new Paragraph(" ");
            doc.add(p1);

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);
            int rownumber = 0;
            for (String h : headers) {
                table.addCell(new Phrase(h, font1));
            }

            for (Object[] objArray : body) {
                rownumber++;
                table.addCell(new Phrase(String.valueOf(rownumber), font1));

                Object[] data = objArray;
                for (Object obj : data) {
                    table.addCell(new Phrase((String.valueOf(obj)), font2));
                }

            }

            doc.add(table);
            doc.close();
            logger.info("Finished generating file.");
        } catch (Exception ex) {
            logger.info("Error generating file.");
            logger.error("An error occurred while trying to generate pdf file.", ex);
            System.out.println("error:::" + ex);
        }
        return true;
    }
}
