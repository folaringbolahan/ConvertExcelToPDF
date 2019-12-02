/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import com.itext.pdf.model.EmailPlaceholder;

/**
 *
 * @author emmanuel.idoko
 */
public class Tester {

    public static void main(String[] args) throws Exception {
        EmailSender sender = new EmailSender();

        EmailPlaceholder placeholder = new EmailPlaceholder();
        //preparing email details to be sent to member
        placeholder.setFirst_name("Emmanuel");
        placeholder.setLast_name("Idoko");
        placeholder.setUsername("emmytoonaiz");
        placeholder.setPassword("emmytoonaiz");
        placeholder.setEmail_address("emmanuel.idoko@africaprudential.com");
        placeholder.setCoop_name("Africa Prudential");
        
        sender.sendSingleEmailOfiice365(placeholder.getEmail_address(), "Easycoop Account Profile Creation", placeholder);
    }
}
