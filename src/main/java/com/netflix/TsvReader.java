package com.netflix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;

public class TsvReader {
    Logger logger = LoggerFactory.getLogger(TsvReader.class);

    String srcFile;
    BufferedReader fileReader;
    boolean skippedFirstLine = false;


    public TsvReader(String srcFile) {
        try {
            this.srcFile = srcFile;
            this.fileReader = new BufferedReader(new FileReader(this.srcFile));
        } catch (Exception e) {
            logger.error("throws {}", e);
            System.exit(1);
        }
    }

    public String[] getNextLine() throws Exception {
        if (!skippedFirstLine) {
            fileReader.readLine();
            skippedFirstLine = true;
        }

        String nextLine = fileReader.readLine();

        if (nextLine == null) {
            return null;
        }

        String[] components = nextLine.split("\t");
        return components;
    }
}
