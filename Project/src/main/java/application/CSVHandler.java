package application;

import entities.Node;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CSVHandler {
    /**
     * Reads a CSV file containing data about the hospital.
     * Ignores the column headers in the first line.
     * Each subsequent line of the file must contain data for one node.
     * Each row must have 8 columns.
     * The second, third, and fourth columns must contain integers.
     * Stores this data in a list of nodes, one node for each row.
     * @param fileName the name of the CSV file to be read
     * @return the list of nodes created that contain the data read in from the file
     */
    public static List<Node> readFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\r\\n");
            for(int i = 0; i < 8; i++) {    // Ignore the column headers.
                scanner.next();
            }
            List<Node> nodes = new LinkedList<>();
            Node node = readLine(scanner);
            while(node != null) {
                nodes.add(node);
                node = readLine(scanner);
            }
            return nodes;
        }
        catch(FileNotFoundException e) {
            System.err.println("Cannot read file: " + fileName);
            e.printStackTrace();
            return null;
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
        String floor = scanner.next();
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

    /**
     * Creates a new file with the given file name.
     * Adds column headers identical to the ones in the sample CSV file and the field names in the Node class.
     * Adds a row for every node in the given list.
     * Adds the data from each field of the node into a cell.
     * @param fileName the name of the file to be created
     * @param nodes a list of nodes to add in the file
     */
    public static void createFile(String fileName, List<Node> nodes) {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\r\n");    // Add the column headers.
            for(Node node : nodes) {
                writeLine(writer, node);
            }
            writer.close();
        }
        catch(IOException e) {
            System.err.println("Cannot create file: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Writes one row into a CSV file.
     * Writes data from one node into the row.
     * @param writer a buffered writer for the CSV file
     * @param node the data to be written in the file
     * @throws IOException
     */
    private static void writeLine(BufferedWriter writer, Node node) throws IOException {
        writer.write(node.getNodeID() + ",");
        writer.write(node.getXcoord() + ",");
        writer.write(node.getYcoord() + ",");
        writer.write(node.getFloor() + ",");
        writer.write(node.getBuilding() + ",");
        writer.write(node.getNodeType() + ",");
        writer.write(node.getLongName() + ",");
        writer.write(node.getShortName() + "\r\n");
    }
}