import java.util.Scanner;
import java.lang.Math;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Give desired number of the message's bits (k), up to 31 bits are supported:");
        int numberOfBits = Integer.parseInt(in.nextLine());
        System.out.println("\nGive binary number to divide the messages with (P):");
        BinaryNumber P = new BinaryNumber(in.nextLine());
        System.out.println("\nGive bit error rate (E) as a decimal:");
        double errorRate = Double.parseDouble(in.nextLine());
        System.out.println("\nGive amount of samples to use:");
        int sampleCount = Integer.parseInt(in.nextLine());

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
            BinaryNumber crcValue = BinaryNumber.xorDivide(beforeSentMessage.toString(numberOfBits + P.toString().length() - 1), P.toString());
            beforeSentMessage.setBinaryValue(originalMessage.toString(numberOfBits) + crcValue.toString(P.toString().length() - 1));

            StringBuilder temp = new StringBuilder(beforeSentMessage.toString(numberOfBits + P.toString().length() - 1));
            for (int j = temp.length() - 1 ; j >= 0 ; j--) {
                if (Math.random() < errorRate)
                    temp.setCharAt(j, temp.charAt(j) == '0' ? '1' : '0');
            }

            BinaryNumber afterSentMessage = new BinaryNumber(temp.toString());
            BinaryNumber sentQuotient = BinaryNumber.xorDivide(afterSentMessage.toString(numberOfBits + P.toString().length() - 1), P.toString());

            if (!(beforeSentMessage.toString(numberOfBits + P.toString().length() - 1).equals(afterSentMessage.toString(numberOfBits + P.toString().length() - 1)))) {
                changedCount++;
                if (sentQuotient.toString().equals("0"))
                    notCaughtChangedCount++;
                else
                    caughtChangedCount++;
            }

        }
        System.out.printf("%nPercent of changed during transmission: %.2f%%%n", ((double)changedCount/sampleCount)*100);
        System.out.printf("Percent of changed during transmission that were caught: %.2f%%%n", ((double)caughtChangedCount/sampleCount)*100);
        System.out.printf("Percent of changed during transmission that were not caught: %.2f%%%n", ((double)notCaughtChangedCount/sampleCount)*100);
    }
}
