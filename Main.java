import java.util.Scanner;

/**
 * Driver class for the HuffmanEncoding class.
 *
 * This project 'compresses' text and stores it inside of a tree.
 * It then 'decompresses' the text and relays the information back to the user.
 *
 * This class takes in a string from a user, shows the huffman code and finally prints the original string back out.
 *
 * 2020 Thomas Wessel
 */
public class Main {

    public static void main(String[] args) {

        HuffmanEncoding enc;
        Scanner scanner = new Scanner(System.in);
        String toEncode;

        System.out.print("Please enter a string you would like to encode: ");
        toEncode = scanner.nextLine();

        enc = new HuffmanEncoding(toEncode);
        enc.printCodeTable();

        System.out.println("The encoded message is: " + enc.getEncodedMessage());

        System.out.println("The decoded message is: " + enc.decode(enc.getEncodedMessage()));

    }
}
