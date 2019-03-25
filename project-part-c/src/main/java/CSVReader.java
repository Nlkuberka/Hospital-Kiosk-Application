import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVReader {
    /**
     * Reads a CSV file containing data about the hostpital.
     * Each line of the file must contain data for one node.
     * Each row must have 8 columns.
     * The second, third, and fourth columns must contain integers.
     * @param fileName the name of the CSV file to be read
     */
    public static void readFile(String fileName) {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\r\\n");
            for(int i = 0; i < 8; i++) {    // Ignore the column headers.
                scanner.next();
            }
            Node node = readLine(scanner);
            while(node != null) {
                System.out.println(node);
                node = readLine(scanner);
            }
        }
        catch(FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }
    }

    /**
     * Reads a single line from a CSV file.
     * Stores data into a Node object.
     * The scanner must be positioned at the beginning of a line.
     * @param scanner a scanner for the CSV file to be read
     * @return the node object, null if the end of the file was reached before filling all the fields
     */
    @SuppressWarnings("Duplicates")
    private static Node readLine(Scanner scanner) {
        if(!scanner.hasNext()) {
            return null;
        }
        String nodeID = scanner.next();
        if(!scanner.hasNext()) {
            return null;
        }
        int xcoord = scanner.nextInt();
        if(!scanner.hasNext()) {
            return null;
        }
        int ycoord = scanner.nextInt();
        if(!scanner.hasNext()) {
            return null;
        }
        int floor = scanner.nextInt();
        if(!scanner.hasNext()) {
            return null;
        }
        String building = scanner.next();
        if(!scanner.hasNext()) {
            return null;
        }
        String nodeType = scanner.next();
        if(!scanner.hasNext()) {
            return null;
        }
        String longName = scanner.next();
        if(!scanner.hasNext()) {
            return null;
        }
        String shortName = scanner.next();
        Node node = new Node(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
        return node;
    }
}