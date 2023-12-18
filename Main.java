import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        FileProcessor fileProcessor = new FileProcessor();
        System.out.println("File processing started");

        fileProcessor.validateArguments(args);
        InstructionController instructionController = new InstructionController();

        Arrays.sort(args);
        for (String instructionFilePath : args) {
            System.out.println("Processing file: " + instructionFilePath);
            fileProcessor.processFile(instructionFilePath, instructionController);
        }

        System.out.println("File processed successfully");
    }
}
