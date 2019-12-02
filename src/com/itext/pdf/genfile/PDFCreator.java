/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 *
 * @author emmanuel.idoko
 */
public class PDFCreator {

    public boolean craetePDF(String reportTitle, String file_name, List<String> headers, List<Object[]> body) {
        boolean created = false;
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));

            Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            doc.open();

            Paragraph para_title = new Paragraph(reportTitle, new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            para_title.setAlignment(Element.ALIGN_CENTER);
            doc.add(para_title);

            //leave a paragraph below populating the records in a table
            Paragraph p1 = new Paragraph(" ");
            doc.add(p1);

            //float[] columnWidths = {10, 20, 20, 50, 18, 18};
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);
            //table.setWidths(columnWidths);

            int rownumber = 0;
            //create the table headers
            for (String header : headers) {
                table.addCell(new Phrase(header, font1));
            }

            //populate the table records here
            PdfPCell cell = new PdfPCell();
            for (Object[] objArray : body) {
                rownumber++;
                table.addCell(new Phrase(String.valueOf(rownumber), font1));

                Object[] data = objArray;
                for (Object obj : data) {
                    if (obj instanceof Date) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else if (obj instanceof Boolean) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else if (obj instanceof String) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else if (obj instanceof Double) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else if (obj instanceof Integer) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else if (obj instanceof Long) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else if (obj instanceof Byte) {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    } else {
                        table.addCell(new Phrase((String.valueOf(obj)), font1));
                    }

                }

            }

        } catch (Exception ex) {
        }
        return created;
    }

}
