/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import com.itext.pdf.dao.DaoConnection;
import com.itext.pdf.model.MemberProfile;
import com.itext.pdf.model.EmailPlaceholder;
import com.itextpdf.text.BaseColor;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author emmanuel.idoko
 */
public class SendLoginCredentials {

    public static void main(String[] args) throws Exception {
        DaoConnection con_model = new DaoConnection();
        EmailSender sender = new EmailSender();

        Set<String> passwords = new HashSet<>();

        java.sql.Connection con_sql = con_model.SqlServerConnection();
        PreparedStatement ps = null;
        //PreparedStatement ps_u = null;
        ResultSet rs = null;
        //ResultSet rs_u = null;
        String query = "select id,username,first_name,last_name,password,email_address from MEMBER_PROFILE where id = 3330485";
        
        ps = con_sql.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        rs = ps.executeQuery();
        List<MemberProfile> all_apr_list = new ArrayList<>();
        //records = con_model.extractDataFromResultset(rs);

        //String file_name = "C:\\etc\\index\\apr_members_itsupport.pdf";
        String file_name = "apr_members_itsupport.pdf";
        String path = "C:\\etc\\index\\";
        System.out.println("preparing members details for pdf export.");
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(path + file_name));

        Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        doc.open();

        Paragraph para_title = new Paragraph("Aiico Pension Managers Limited (APML) Cooperative", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE));
        para_title.setAlignment(Element.ALIGN_CENTER);
        doc.add(para_title);

        Paragraph p1 = new Paragraph(" ");
        doc.add(p1);

        float[] columnWidths = {10, 25, 15, 15, 15};
        PdfPTable table = new PdfPTable(columnWidths.length);
        table.setWidthPercentage(100);
        table.setWidths(columnWidths);
        int rownumber = 0;
        table.addCell(new Phrase("S/N", font1));
        table.addCell(new Phrase("USERNAME", font1));
        table.addCell(new Phrase("FIRST NAME", font1));
        table.addCell(new Phrase("LAST NAME", font1));
        table.addCell(new Phrase("PASSWORD", font1));

        EmailPlaceholder placeholder;
        List<EmailPlaceholder> placeholder_list = new ArrayList<>();
        //int size = 0;
//        if (rs != null) {
//            rs.last();    // moves cursor to the last row
//            size = rs.getRow(); // get row id 
//        }
        //System.out.println("size of applications::::" + size);
        while (rs.next()) {
            rownumber++;
            System.out.println("i am in....");
            MemberProfile profile = new MemberProfile();
            profile.setMember_id(rs.getInt("id"));
            profile.setUsername(rs.getString("username"));
            profile.setFirstName(rs.getString("first_name").toLowerCase());
            profile.setLastName(rs.getString("last_name").toLowerCase());
            profile.setEmailAddress(rs.getString("email_address").toLowerCase());
            profile.setPassword(con_model.randomAlphaNumeric(10));
            profile.setHashedPassword(con_model.hashPassword(profile.getPassword()));
            System.out.println("username>>" + profile.getUsername());

            String firstName_caps = profile.getFirstName().toUpperCase().charAt(0) + profile.getFirstName().substring(1, profile.getFirstName().length());
            String lastName_caps = profile.getLastName().toUpperCase().charAt(0) + profile.getLastName().substring(1, profile.getLastName().length());
            //populating pdf document with members information
            table.addCell(new Phrase(String.valueOf(rownumber), font2));
            table.addCell(new Phrase(profile.getUsername(), font2));
            table.addCell(new Phrase(firstName_caps, font2));
            table.addCell(new Phrase(lastName_caps, font2));
            table.addCell(new Phrase(profile.getPassword(), font2));

            placeholder = new EmailPlaceholder();
            //preparing email details to be sent to member
            placeholder.setFirst_name(firstName_caps);
            placeholder.setLast_name(lastName_caps);
            placeholder.setUsername(profile.getUsername());
            placeholder.setPassword(profile.getPassword());
            placeholder.setEmail_address(profile.getEmailAddress());
            placeholder.setCoop_name("Aiico Pension Managers Limited (APML) Cooperative");

            passwords.add(profile.getPassword());

            //updating member password with new random generated password
            String update = "update MEMBER_PROFILE set password=? where id=?";
            PreparedStatement ps_u = con_sql.prepareStatement(update);
            ps_u.setString(1, profile.getHashedPassword());
            ps_u.setInt(2, profile.getMember_id());
            ps_u.executeUpdate();
            ps_u.close();

            placeholder_list.add(placeholder);

        }
        ps.close();
        rs.close();
        con_model.closeConnection();
        System.out.println("sending of email to members in progress...");
        System.out.println("passwords generated.." + passwords.size());
        for (EmailPlaceholder ph : placeholder_list) {
            //sender.sendSingleEmail("easycoop14@gmail.com", "Capital1#", ph.getUsername(), "Easycoop Account Profile Creation", ph);
           sender.sendSingleEmailOfiice365(ph.getEmail_address(), "Easycoop Account Profile Creation", ph);

        }
        System.out.println("completed sending of emails to members.");

        doc.add(table);
        doc.close();
        System.out.println("Finished generating pdf file.");
    }
}
