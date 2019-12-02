/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author emmanuel.idoko
 */
public class BasicCsvReader {
   public static void main(String[] args) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("C:\\Users\\emmanuel.idoko\\Desktop\\Excel Files\\NIMCOSProfileTemplate (1).csv"));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("first Name","middle Name", "last Name","username", "coopadmin").withIgnoreHeaderCase().withTrim());
        for (CSVRecord csvRecord: csvParser) {
            // Accessing Values by Column Index
            String firstName = csvRecord.get(0);
            String middleName = csvRecord.get(1);
            String lastName = csvRecord.get(2);
            String username = csvRecord.get(3);
            String iscoopAdmin = csvRecord.get(4);
            //Accessing the values by column header name
            //String fees = csvRecord.get(1);
            //Printing the record 
            //System.out.println("Total records in file - " + csvRecord.getRecordNumber());
            System.out.println("Name : " + firstName+", Middle Name: "+middleName+", last name: "+lastName+", username: "+username+", iscoopAdmin: "+iscoopAdmin);
            //System.out.println("Fees : " + fees);
            System.out.println("\n\n");
        }
    } 
}
