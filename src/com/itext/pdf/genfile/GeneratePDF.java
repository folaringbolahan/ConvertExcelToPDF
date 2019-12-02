/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import com.itext.pdf.dao.DaoConnection;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author emmanuel.idoko
 */
public class GeneratePDF {

    public static void main(String[] args) {
        try {
            List<String> columnheaders = new ArrayList<>();
            List<Object[]> datacontent = new ArrayList<>();
            List records = new ArrayList();

            String file_name = "C:\\etc\\index\\test.pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));

            Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font font2 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            doc.open();

            Paragraph para_title = new Paragraph("Easy Coop PDF Generator.", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            para_title.setAlignment(Element.ALIGN_CENTER);
            doc.add(para_title);

            Paragraph p1 = new Paragraph(" ");
            doc.add(p1);

            DaoConnection con_model = new DaoConnection();

            java.sql.Connection con_sql=null; //= con_model.prepareSqlServerConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            String query = "select type,description,keyword,preference,tax_classification from HOLDER_TYPE";
            ps = con_sql.prepareStatement(query);
            rs = ps.executeQuery();

            records = new GeneratePDF().extractDataFromResultset(rs);
            columnheaders = (List<String>) records.get(0);
            datacontent = (List<Object[]>) records.get(1);

            //float[] columnWidths = {10, 20, 20, 50, 18, 18};
            PdfPTable table = new PdfPTable(columnheaders.size());
            table.setWidthPercentage(100);
            //table.setWidths(columnWidths);
            int rownumber = 0;
            //table.addCell(new Phrase(String.valueOf(rownumber), font1));
            for (String h : columnheaders) {
                table.addCell(new Phrase(h, font1));
            }

            for (Object[] objArray : datacontent) {
                rownumber++;
                table.addCell(new Phrase(String.valueOf(rownumber), font1));

                Object[] data = objArray;
                for (Object obj : data) {
                    table.addCell(new Phrase((String.valueOf(obj)), font1));
                }

            }

            doc.add(table);
            doc.close();
            System.out.println("Finished generating file.");
        } catch (Exception ex) {
            System.out.println("error:::" + ex);
        }

    }

    /**
     * Extracts the headers and data content from the prepared statement's
     * result set.
     *
     * @param resultset the result set
     * @return the list containing the headers and data content
     * @throws Exception
     */
    private List extractDataFromResultset(ResultSet resultset) throws Exception {
        List<String> columnheaders = new ArrayList<>();
        List<Object[]> datacontent = new ArrayList<>();
        List records = new ArrayList();

        if (resultset != null) {
            int noOfColumns = resultset.getMetaData().getColumnCount();

            //get columns
            columnheaders.add("S/N");
            for (int i = 0; i < noOfColumns; i++) {
                columnheaders.add(resultset.getMetaData().getColumnLabel(i + 1));//value in result set starts from 1
            }

            //get data contents
            while (resultset.next()) {
                if (noOfColumns <= 0) {
                    System.out.println("Result set is empty");
                }

                Object[] resultsetArray = new Object[noOfColumns];
                for (int i = 0; i < noOfColumns; i++) {
                    //gather each column content into a row
                    //value in result set starts from 1, hence the i + 1
                    resultsetArray[i] = resultset.getObject(i + 1);
                }
                //insert row into data content list
                datacontent.add(resultsetArray);
            }
            records.add(columnheaders);
            records.add(datacontent);

            resultset.close();
        }

        return records;
    }

}
