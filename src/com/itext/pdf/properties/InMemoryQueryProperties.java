/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Akinwale Agbaje
 * used when testing within library
 */
public class InMemoryQueryProperties extends Properties {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryQueryProperties.class);
    private static InMemoryQueryProperties INSTANCE;
    
    private InMemoryQueryProperties() {
        loadPropertiesFromMemory();
    }
    
    public static InMemoryQueryProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryQueryProperties();
        }
        return INSTANCE;
    }
    
    /**
     * Loads the available in-memory xml query properties file.
     */
    private void loadPropertiesFromMemory() {
        try {
            String inmemoryfile = "inmemoryquery.xml";
            
            System.out.println("in-memory file is streaming...");
            InputStream instream = InMemoryQueryProperties.class.getClassLoader().getResourceAsStream(inmemoryfile);
            
            loadFromXML(instream);
            System.out.println("in-memory file is loaded...");
            instream.close();
        } catch (IOException ex) {
            logger.error("Error reading in-memory file to load for jdbc properties", ex);
            System.out.println("Error reading in-memory file to load for jdbc properties: " + ex);
        }
    }
}
