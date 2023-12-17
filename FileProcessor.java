import java.io.*;

public class FileProcessor {

    public void processFile(String filePath, InstructionController instructionController) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                String[] lineArray = line.split(" ");
                String option = lineArray[0];
                String[] arguments = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, arguments, 0, lineArray.length - 1);

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

        String[] fileNames = args;
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("File does not exist: " + fileName);
                System.exit(-1);
            }
        }
    }

}
