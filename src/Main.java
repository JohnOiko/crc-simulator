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
            // If the input is an integer and has less than 10 digits, make the flag false to break the loop and save the value of the sample count.
            if (isInteger(input) && input.length() < 10) {
                flag = false;
                sampleCount = Integer.parseInt(input);
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
        // Counter for the amount of messages that changed through transmission.
        int changedCount = 0;
        // Counter for the amount of messages that changed through transmission and were recognized by the algorithm.
        int caughtChangedCount = 0;
        // Counter for the amount of messages that changed through transmission and were not recognized by the algorithm.
        int notCaughtChangedCount = 0;

        // For loop that simulates the transmission of sampleCount number of messages.
        for (int i = 0 ; i < sampleCount ; i++) {
            // The original message without the FCS initialized to 0.
            BinaryNumber originalMessage = new BinaryNumber(0);
            // The message before being transmitted (with the FCS) initialized to 0.
            BinaryNumber beforeSentMessage = new BinaryNumber(0);

            // For loop that is executed numberOfBits times and where the index counts downwards.
            for (int j = numberOfBits - 1 ; j >= 0 ; j--) {
                // If Math.random returns a number that is less than 0.5 (it returns a number such that 0.0 <= Math.random() < 1.0, so Math.random() < 0.5 gives a 50% chance),
                // then set the j digit of the originalMessage to 1 by doing originalMessage = originalMessage + 2 ^ j (using decimal values). Else if Math.random returns a
                // number that is more or equal to 0.5, do nothing in which case the loop moves to the next bit since j is decreased by the for loop.
                if (Math.random() < 0.5)
                    originalMessage.setDecimalValue(originalMessage.getDecimalValue() + (int) Math.pow(2, j));
            }

            beforeSentMessage.setBinaryValue(originalMessage.toString(numberOfBits) + "0".repeat(Math.max(0, P.toString().length() - 1)));
            BinaryNumber crcValue = BinaryNumber.xorDivide(beforeSentMessage.toString(totalBitCount), P.toString());
            beforeSentMessage.setBinaryValue(originalMessage.toString(numberOfBits) + crcValue.toString(P.toString().length() - 1));

            StringBuilder temp = new StringBuilder(beforeSentMessage.toString(totalBitCount));
            for (int j = temp.length() - 1 ; j >= 0 ; j--) {
                if (Math.random() < errorRate)
                    temp.setCharAt(j, temp.charAt(j) == '0' ? '1' : '0');
            }

            BinaryNumber afterSentMessage = new BinaryNumber(temp.toString());
            BinaryNumber sentQuotient = BinaryNumber.xorDivide(afterSentMessage.toString(totalBitCount), P.toString());

            if (!(beforeSentMessage.toString(totalBitCount).equals(afterSentMessage.toString(totalBitCount)))) {
                changedCount++;
                if (sentQuotient.toString().equals("0"))
                    notCaughtChangedCount++;
                else
                    caughtChangedCount++;
            }

            if (showMessages) {
                System.out.println("\n" + (i+1) + ") Message sent:     " + beforeSentMessage.toString(totalBitCount));
                System.out.println(" ".repeat(Integer.toString(i + 1).length()) + "  Message received: " + afterSentMessage.toString(totalBitCount));
                System.out.println(" ".repeat(Integer.toString(i + 1).length()) + "  Received CRC: " + sentQuotient + ((beforeSentMessage.toString(totalBitCount).equals(afterSentMessage.toString(totalBitCount))) ? " (no change happened - " : " (change happened - ") + (sentQuotient.toString().equals("0") ? "no change detected)" : "change detected)"));
            }

        }
        System.out.println("\n-Percent of changed messages during transmission: " + BigDecimal.valueOf(((double)changedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "% (" + changedCount + "/" + sampleCount + ")");
        System.out.println("-Percent of changed messages during transmission that were caught: " + BigDecimal.valueOf(((double)caughtChangedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "% (" + caughtChangedCount + "/" + sampleCount + ")");
        System.out.println("-Percent of changed messages during transmission that were not caught: " + BigDecimal.valueOf(((double)notCaughtChangedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "% (" + notCaughtChangedCount + "/" + sampleCount + ")");
    }

    public static boolean isInteger(String string) {
        for (int i = 0 ; i < string.length() ; i++) {
            if (!(Character.isDigit(string.charAt(i))))
                return false;
        }
        return string.length() > 0;
    }

    public static boolean isBinary(String string) {
        for (int i = 0 ; i < string.length() ; i++) {
            if (i == 0 && string.charAt(i) != '1')
                return false;
            if (!(string.charAt(i) == '0' || string.charAt(i) == '1'))
                return false;
        }
        return string.length() > 0;
    }

    public static boolean isDecimalPercentage(String string) {
        if (string.length() <= 1) {
            return string.equals("1") || string.equals("0");
        }
        if (string.charAt(0) != '0' || string.charAt(1) != '.') {
            return string.equals("1.0") || string.equals("1.00") || string.equals("0.0") || string.equals("0.00");
        }
        for (int i = 2 ; i < string.length() ; i++) {
            if (!(Character.isDigit(string.charAt(i))))
                return false;
        }
        return true;
    }
}