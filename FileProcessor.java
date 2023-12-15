import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileProcessor {

    public void processFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String[] lineArray = line.split(" ");
                String option = lineArray[0];
                String[] arguments = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, arguments, 0, lineArray.length - 1);

                InstructionController instructionController = new InstructionController();
                instructionController.executeOption(option, arguments);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validateArguments(String[] args) {

        if (args.length == 0) {
            System.out.println("No arguments provided");
            System.exit(0);
        }

        if (args.length > 1) {
            System.out.println("Too many arguments provided. Only one argument is allowed");
            System.exit(0);
        }

        // check if file exists
        String filePath = args[0];
        try {
            FileReader fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
    }

}
