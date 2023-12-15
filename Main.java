
public class Main {
    public static void main(String[] args) {

        FileProcessor fileProcessor = new FileProcessor();
        System.out.println("File processing started");

        fileProcessor.validateArguments(args);

        String instructionFilePath = args[0];
        InstructionController instructionController = new InstructionController();
        fileProcessor.processFile(instructionFilePath, instructionController);

        System.out.println("File processed successfully");
    }
}
