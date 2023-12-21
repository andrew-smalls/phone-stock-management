package org.stockphone.controllers;

import java.io.*;

public class FileProcessor {

    public void processFile(String filePath, InstructionController instructionController) throws IOException {
        String line = "";
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while ((line = bufferedReader.readLine()) != null) {
            String[] lineArray = line.split(" ");
            String option = lineArray[0];
            String[] arguments = new String[lineArray.length - 1];
            System.arraycopy(lineArray, 1, arguments, 0, lineArray.length - 1);

            instructionController.executeOption(option, arguments);
        }

        bufferedReader.close();
    }

    public boolean validateArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments provided");
            return false;
        }

        for (String fileName : args) {
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("File does not exist: " + fileName);
                return false;
            }
        }
        return true;
    }

}
