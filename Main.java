import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        FileProcessor fileProcessor = new FileProcessor();
        System.out.println("File processing started");

        fileProcessor.validateArguments(args);
        InstructionController instructionController = new InstructionController();

        String[] fileNames = args;
        Arrays.sort(fileNames);
        for (String instructionFilePath : fileNames) {
            System.out.println("Processing file: " + instructionFilePath);
            fileProcessor.processFile(instructionFilePath, instructionController);
        }

        System.out.println("File processed successfully");
    }
}
