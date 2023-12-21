package org.stockphone;

import org.stockphone.controllers.FileProcessor;
import org.stockphone.controllers.InstructionController;

import java.io.IOException;
import java.util.Arrays;

public class App 
{
    public static void main(String[] args) {

        FileProcessor fileProcessor = new FileProcessor();
        System.out.println("File processing started");

        if (!fileProcessor.validateArguments(args)) {
            System.out.println("Invalid arguments");
            return;
        }
        InstructionController instructionController = new InstructionController();

        Arrays.sort(args);
        for (String instructionFilePath : args) {
            System.out.println("Processing file: " + instructionFilePath);
            try {
                fileProcessor.processFile(instructionFilePath, instructionController);

            } catch (IOException e) {
                System.out.println("Error reading file: " + instructionFilePath);
            }
        }

        System.out.println("File processed successfully");
    }}
