import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVReader {
   public static void read(String fileName) {
        File file = new File(fileName);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            scanner.useDelimiter(",");
            while(scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }
}