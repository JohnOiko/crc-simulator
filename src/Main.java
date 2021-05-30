import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

// Main class that is to be run.
public class Main {

    // Main function.
    public static void main(String[] args) {
        // Scanner to read input from the user.
        Scanner in = new Scanner(System.in);
        String input;
        // Default number of bits is 1.
        int numberOfBits = 1;
        System.out.println("Give desired number of the message's bits (k), up to 29 bits are supported:");
        // Flag that is used to stop the while loop that reads input from the user.
        boolean flag = true;
        // While loop that stops when the input is valid by making the flag false.
        while (flag) {
            // Read the next line.
            input = in.nextLine();
            // If the input is an integer and has 1-4 digits and has a value between 2 and 29, make the flag false to break the loop and save the number of bits.
            if (isInteger(input)) {
                if (input.length() > 0 && input.length() < 5) {
                    if (Integer.parseInt(input) > 1 && Integer.parseInt(input) < 30 ) {
                        flag = false;
                        numberOfBits = Integer.parseInt(input);
                    }
                }
            }
            // If the flag is still true that means the given input was not valid and a message requesting input must be shown again.
            if (flag)
                System.out.println("Please give a valid number of bits (k) that is between 2 and 29:");
        }

        // Default P is 0.
        BinaryNumber P = new BinaryNumber(0);
        System.out.println("\nGive binary number to divide the messages with (P):");
        // Reset the flag to true.
        flag = true;
        // While loop that stops when the input is valid by making the flag false.
        while (flag) {
            input = in.nextLine();
            // If the input is a binary number, has between 2 to 32 - numberOfBits digits (because P must be less or equal to the message that is to be sent) and
            // starts and ends with a 1, make the flag false to break the loop and save the value of P.
            if (isBinary(input)) {
                if (input.length() > 1 && input.length() < 32 - numberOfBits && input.charAt(0) == '1' && input.charAt(input.length() - 1) == '1') {
                    flag = false;
                    P.setBinaryValue(input);
                }
            }
            // If the flag is still true that means the given input was not valid and a message requesting input must be shown again.
            if (flag)
                System.out.println("Please give a valid binary number to divide the messages with (P) that starts and ends with 1 and has between 2 and " + (31 - numberOfBits) + " digits:");
        }

        // Default error rate is 0.
        double errorRate = 0;
        System.out.println("\nGive bit error rate (BER) as a decimal:");
        // Reset the flag to true.
        flag = true;
        // While loop that stops when the input is valid by making the flag false.
        while (flag) {
            input = in.nextLine();
            // If the input is a decimal percentage make the flag false to break the loop and save the value of the error rate.
            if (isDecimalPercentage(input)) {
                    flag = false;
                    errorRate = Double.parseDouble(input);
            }
            // If the flag is still true that means the given input was not valid and a message requesting input must be shown again.
            if (flag)
                System.out.println("Please give a valid bit error rate (BER) as a decimal that is between 0 and 1:");
        }

        // Default sample count is 1.
        int sampleCount = 1;
        System.out.println("\nGive amount of samples to use:");
        // Reset the flag to true.
        flag = true;
        // While loop that stops when the input is valid by making the flag false.
        while (flag) {
            input = in.nextLine();
            // If the input is a positive non negative integer and has less than 10 digits, make the flag false to break the loop and save the value of the sample count.
            if (isInteger(input) && input.length() < 10) {
                if (Integer.parseInt(input) > 0) {
                    flag = false;
                    sampleCount = Integer.parseInt(input);
                }
            }
            // If the flag is still true that means the given input was not valid and a message requesting input must be shown again.
            if (flag)
                System.out.println("Please give a valid amount of samples to use:");
        }

        // Default state is to now show messages.
        boolean showMessages = false;
        System.out.println("\nDo you want individual messages to be shown? Enter 1 for yes or 0 for no:");
        // Reset the flag to true.
        flag = true;
        // While loop that stops when the input is valid by making the flag false.
        while (flag) {
            input = in.nextLine();
            // If the input is either 0 or 1, make the flag false to break the loop and save the value of the showMessages variable.
            if (input.equals("1")) {
                flag = false;
                showMessages = true;
            }
            else if (input.equals("0")) {
                flag = false;
                showMessages = false;
            }
            // If the flag is still true that means the given input was not valid and a message requesting input must be shown again.
            if (flag)
                System.out.println("Please enter 1 for yes or 0 for no:");
        }

        // The total number of bits of messages that will be transmitted.
        int totalBitCount = numberOfBits + P.toString().length() - 1;
        // Counter for the amount of messages that changed/had errors while being transmitted.
        int changedCount = 0;
        // Counter for the amount of messages that changed/had errors while being transmitted and were recognized by the algorithm.
        int caughtChangedCount = 0;
        // Counter for the amount of messages that changed/had errors while being transmitted and were not recognized by the algorithm.
        int notCaughtChangedCount = 0;

        // For loop that creates  sampleCount number of random messages with numberOfBits bits and simulates their transmission.
        for (int i = 0 ; i < sampleCount ; i++) {
            // The original message without the FCS sequence initialized to 0.
            BinaryNumber originalMessage = new BinaryNumber(0);
            // The message that will be transmitted (including the FCS sequence) initialized to 0.
            BinaryNumber messageTransmitted = new BinaryNumber(0);

            // For loop that is executed numberOfBits times and where the index counts downwards which creates a random message with numberOfBits bits.
            for (int j = numberOfBits - 1 ; j >= 0 ; j--) {
                // If Math.random returns a number that is less than 0.5 (it returns a number such that 0.0 <= Math.random() < 1.0, so Math.random() < 0.5 gives a 50% chance),
                // then set the j digit of the originalMessage to 1 by doing originalMessage = originalMessage + 2 ^ j (using decimal values). Else if Math.random returns a
                // number that is more or equal to 0.5, do nothing in which case the loop moves to the next bit since j is decreased by the for loop.
                if (Math.random() < 0.5)
                    originalMessage.setDecimalValue(originalMessage.getDecimalValue() + (int) Math.pow(2, j));
            }

            // Set the message that will be transmitted to the original message and add P.length - 1 (so n - k) zeros to the end of it (Math.max is used to make sure that a
            // negative amount of zeros isn't added and causes an error) so as to use it to do the calculation messageTransmitted xor P.
            messageTransmitted.setBinaryValue(originalMessage.toString(numberOfBits) + "0".repeat(Math.max(0, P.toString().length() - 1)));
            // Calculate the FCS sequence using the method BinaryNumber.xorDivide by doing messageTransmitted xor P.
            BinaryNumber FCS = BinaryNumber.xorDivide(messageTransmitted.toString(totalBitCount), P.toString());
            // Set the message that will be transmitted to the original message and add the FCS sequence to the end of it. Also call the method BinaryNumber.toString with
            // P.length - 1 as parameter so as to get the FCS sequence with P.length - 1 bits (with added zeros at the front if the FCS sequence has less bits).
            messageTransmitted.setBinaryValue(originalMessage.toString(numberOfBits) + FCS.toString(P.toString().length() - 1));

            // Make a new StringBuilder object that has the message that will be transmitted, including the FCS sequence, as its value so as to make editing it easier.
            StringBuilder temp = new StringBuilder(messageTransmitted.toString(totalBitCount));
            // For loop that simulates the appearance of an error in the messages while they are being transmitted. The loop is executed once for each bit of the message and
            // changes the current bit of the message with an errorRate chance.
            for (int j = temp.length() - 1 ; j >= 0 ; j--) {
                // If Math.random returns a number that is less than errorRate (it returns a number such that 0.0 <= Math.random() < 1.0, so Math.random() < errorRate gives
                // a chance of (errorRate * 100)%), then set the j digit of the message to the opposite of its current value. Else if Math.random returns a number that is
                // more or equal to errorRate, do nothing in which case the loop moves to the next bit since j is decreased by the for loop.
                if (Math.random() < errorRate)
                    temp.setCharAt(j, temp.charAt(j) == '0' ? '1' : '0');
            }

            // The message that was received (with any possible errors) given the value of the corresponding String value of the previous StringBuilder object.
            BinaryNumber messageReceived = new BinaryNumber(temp.toString());
            // The result of the calculation messageReceived xor P (if it's 0, then no error is detected).
            BinaryNumber R = BinaryNumber.xorDivide(messageReceived.toString(totalBitCount), P.toString());

            // If the message before being transmitted is different from the message that was received there was at least one error during transmission.
            if (!(messageTransmitted.toString(totalBitCount).equals(messageReceived.toString(totalBitCount)))) {
                // Since there was at least one error increase the counter for the amount of messages that changed/had errors while being transmitted by one.
                changedCount++;
                // If the result of xor dividing the message that was received with P is 0 (which means the algorithm does not recognize any errors to the message while being
                // transmitted), increase the counter for the amount of messages that changed/had errors while being transmitted and were recognized by the algorithm.
                if (R.toString().equals("0"))
                    notCaughtChangedCount++;
                // Else if R != 0 (which means the algorithm recognizes errors to the message while being transmitted), increase the counter for the amount of messages that
                // changed/had errors while being transmitted and were not recognized by the algorithm.
                else
                    caughtChangedCount++;
            }

            // If the user chose to show each individual message print three appropriate messages.
            if (showMessages) {
                // Prints the message that was transmitted.
                System.out.println("\n" + (i+1) + ") Message sent:     " + messageTransmitted.toString(totalBitCount));
                // Prints the message that was received.
                System.out.println(" ".repeat(Integer.toString(i + 1).length()) + "  Message received: " + messageReceived.toString(totalBitCount));
                // Prints the FCS sequence (R) of the message that was received.
                System.out.println(" ".repeat(Integer.toString(i + 1).length()) + "  Received FCS: " + R + ((messageTransmitted.toString(totalBitCount).equals(messageReceived.toString(totalBitCount))) ? " (no change happened - " : " (change happened - ") + (R.toString().equals("0") ? "no change detected)" : "change detected)"));
            }

        }
        // Prints the percentage and total number of messages that had errors/changed while being transmitted.
        System.out.println("\n-Percent of changed messages during transmission: " + BigDecimal.valueOf(((double)changedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "% (" + changedCount + "/" + sampleCount + ")");
        // Prints the percentage and total number of messages that changed/had errors while being transmitted and were recognized by the algorithm.
        System.out.println("-Percent of changed messages during transmission that were caught: " + BigDecimal.valueOf(((double)caughtChangedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "% (" + caughtChangedCount + "/" + sampleCount + ")");
        // Prints the percentage and total number of messages that changed/had errors while being transmitted and were not recognized by the algorithm.
        System.out.println("-Percent of changed messages during transmission that were not caught: " + BigDecimal.valueOf(((double)notCaughtChangedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "% (" + notCaughtChangedCount + "/" + sampleCount + ")");
    }

    // Static method that returns true if the given String is an integer number, otherwise it returns false.
    public static boolean isInteger(String string) {
        // For loop that checks if any of the String's characters aren't digits and if that is true it returns false as the String cannot be an integer number.
        for (int i = 0 ; i < string.length() ; i++) {
            if (!(Character.isDigit(string.charAt(i))))
                return false;
        }
        // Return true if the string has at least one character (it was already confirmed that it is an integer number since the statement return false; was not executed).
        // Else return false if the string was empty.
        return string.length() > 0;
    }

    // Static method that returns true if the given String is a binary number, otherwise it returns false.
    public static boolean isBinary(String string) {
        // For loop that checks if the first character of the string is not 1 and any of the rest of the String's characters aren't 0 or 1 and if that is true
        // it returns false as the String cannot be a binary number.
        for (int i = 0 ; i < string.length() ; i++) {
            if (i == 0 && string.charAt(i) != '1')
                return false;
            if (!(string.charAt(i) == '0' || string.charAt(i) == '1'))
                return false;
        }
        // Return true if the string has at least one character (it was already confirmed that it is a binary number since the statement return false; was not executed).
        // Else return false if the string was empty.
        return string.length() > 0;
    }

    // Static method that returns true if the given String is a decimal number that can be converted to a percentage, otherwise it returns false.
    public static boolean isDecimalPercentage(String string) {
        // If the String has one or less characters, return true only if it's 0 or 1.
        if (string.length() <= 1) {
            return string.equals("1") || string.equals("0");
        }
        // If the String has at least 2 characters and the first one is not 0 or the second one is not a ., the return true only if the string is one of the values
        // 1.0 or 1.00 or 0.0 or 0.00
        if (string.charAt(0) != '0' || string.charAt(1) != '.') {
            return string.equals("1.0") || string.equals("1.00") || string.equals("0.0") || string.equals("0.00");
        }
        // If none of the above two if statements were true, use a for loop that checks if any of the String's characters aren't digits and if that is true it returns
        // false as the String cannot be a decimal number.
        for (int i = 2 ; i < string.length() ; i++) {
            if (!(Character.isDigit(string.charAt(i))))
                return false;
        }
        // Since the method hasn't returned false yet, that means that the given String is indeed a decimal number that can be converted to a percentage, in which case
        // return true.
        return true;
    }
}