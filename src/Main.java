import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input;
        int numberOfBits = 1;
        System.out.println("Give desired number of the message's bits (k), up to 29 bits are supported:");
        boolean flag = true;
        while (flag) {
            input = in.nextLine();
            if (isInteger(input)) {
                if (input.length() > 0 && input.length() < 5) {
                    if (Integer.parseInt(input) > 1 && Integer.parseInt(input) < 30 ) {
                        flag = false;
                        numberOfBits = Integer.parseInt(input);
                    }
                }
            }
            if (flag)
                System.out.println("Please give a valid number of bits (k) that is between 2 and 29:");
        }

        BinaryNumber P = new BinaryNumber(0);
        System.out.println("\nGive binary number to divide the messages with (P):");
        flag = true;
        while (flag) {
            input = in.nextLine();
            if (isBinary(input)) {
                if (input.length() > 1 && input.length() < 32 - numberOfBits && input.charAt(0) == '1' && input.charAt(input.length() - 1) == '1') {
                    flag = false;
                    P.setBinaryValue(input);
                }
            }
            if (flag)
                System.out.println("Please give a valid binary number to divide the messages with (P) that starts and ends with 1 and has between 2 and " + (31 - numberOfBits) + " digits:");
        }

        double errorRate = 0;
        System.out.println("\nGive bit error rate (BER) as a decimal:");
        flag = true;
        while (flag) {
            input = in.nextLine();
            if (isDecimalPercentage(input)) {
                    flag = false;
                    errorRate = Double.parseDouble(input);
            }
            if (flag)
                System.out.println("Please give a valid bit error rate (BER) as a decimal that is between 0 and 1:");
        }

        int sampleCount = 1;
        System.out.println("\nGive amount of samples to use:");
        flag = true;
        while (flag) {
            input = in.nextLine();
            if (isInteger(input) && input.length() < 10) {
                flag = false;
                sampleCount = Integer.parseInt(input);
            }
            if (flag)
                System.out.println("Please give a valid amount of samples to use:");
        }

        boolean showMessages = false;
        System.out.println("\nDo you want individual messages to be shown? Enter 1 for yes or 0 for no:");
        flag = true;
        while (flag) {
            input = in.nextLine();
            if (input.equals("1")) {
                flag = false;
                showMessages = true;
            }
            else if (input.equals("0")) {
                flag = false;
                showMessages = false;
            }
            if (flag)
                System.out.println("Please enter 1 for yes or 0 for no:");
        }

        int totalBitCount = numberOfBits + P.toString().length() - 1;
        int changedCount = 0;
        int caughtChangedCount = 0;
        int notCaughtChangedCount = 0;

        for (int i = 0 ; i < sampleCount ; i++) {
            BinaryNumber originalMessage = new BinaryNumber(0);
            BinaryNumber beforeSentMessage = new BinaryNumber(0);

            for (int j = numberOfBits - 1 ; j >= 0 ; j--) {
                if (Math.random() < 0.49)
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
        System.out.println("\n-Percent of changed messages during transmission: " + BigDecimal.valueOf(((double)changedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "%");
        System.out.println("-Percent of changed messages during transmission that were caught: " + BigDecimal.valueOf(((double)caughtChangedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "%");
        System.out.println("-Percent of changed messages during transmission that were not caught: " + BigDecimal.valueOf(((double)notCaughtChangedCount/sampleCount)*100).setScale(8, MathContext.DECIMAL64.getRoundingMode()).stripTrailingZeros().toPlainString() + "%");
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