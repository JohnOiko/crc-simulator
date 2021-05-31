// Class that represents a binary number.
public class BinaryNumber {

    // The decimal value of the binary number.
    private int decimalValue;

    // Constructor that sets the binary number's value as the equivalent of the decimal value it receives as a parameter.
    public BinaryNumber(int decimalNumber) {
        decimalValue = decimalNumber;
    }

    // Constructor that sets the binary value to the binary number passed as parameter in the form of a String.
    public BinaryNumber(String binaryNumber) {
        decimalValue = BinaryNumber.toDecimal(binaryNumber);
    }

    // Setter for the binary number's decimal value.
    public void setDecimalValue(int decimalNumber) {
        decimalValue = decimalNumber;
    }

    // Setter for the binary number.
    public void setBinaryValue(String binaryNumber) {
        decimalValue = BinaryNumber.toDecimal(binaryNumber);
    }

    // Getter for the binary number's decimal value.
    public int getDecimalValue() {
        return decimalValue;
    }

    // Getter for the binary value of the number in the form of a String.
    @Override
    public String toString() {
        return Integer.toBinaryString(decimalValue);
    }

    // Alternate getter for the binary value of the number in the form of a String with the added option
    // to increase the amount of the returned number's digits by adding zeroes to the left of the number.
    public String toString(int numberOfBits) {
        // Save the decimal value's default corresponding binary number provided by the class Integer to binaryString.
        String binaryString = Integer.toBinaryString(decimalValue);
        // Add zeroes to a String with a loop that ranges from the amount of digits the binary number has to the amount
        // of digits it has to reach.
        String leadingZeroes = "";
        for (int i = binaryString.length() ; i < numberOfBits ; i ++)
            leadingZeroes = leadingZeroes.concat("0");
        // Concat the zeroes to the start of the binary number and then return the new binary number.
        return leadingZeroes.concat(binaryString);
    }

    // Method that returns the result of dividend XOR divisor as a BinaryNumber object.
    public static BinaryNumber xorDivide(String dividend, String divisor) {
        // While the dividend has at least as many digits as the divisor and is not 0 keep dividing.
        while (dividend.length() >= divisor.length() && !(dividend.equals("0"))) {
            // Make a StringBuilder object of the dividend so as to be able to edit it easier (this is the object
            // I refer to as the dividend below).
            StringBuilder temp = new StringBuilder(dividend);
            // Counter for the continuous zeros at the start of the dividend (after applying the xor operator).
            int counter = 0;
            // Flag to stop updating the counter when a non zero is found in the dividend (after applying the xor operator).
            boolean flag = true;
            // Compare the digits of the divisor to the digits of the dividend starting from the dividend's most significant bit
            // and if the digits are the same, change the dividend's bit to 0 (how xor works) and if the flag is true (there are
            // still continuous zeros in the left part of the dividend) increase the counter. Else if the digits are different
            // set the dividend's digit to 1 and set the flag to false to signal a 1 has been found (thus the zeros on the left
            // side of the dividend are no longer continuous).
            for (int i = 0 ; i < divisor.length() ; i++) {
                if ((temp.charAt(i) == divisor.charAt(i))) {
                    temp.setCharAt(i, '0');
                    if (flag)
                        counter++;
                }
                else {
                    temp.setCharAt(i, '1');
                    flag = false;
                }
            }
            // Delete counter number of zeros from the left side of the dividend and update the dividend to the value of the
            // StringBuilder object temp as a String.
            temp.delete(0, counter);
            dividend = temp.toString();
        }
        // Once the while loop has stopped, dividend has the result of the xor calculation and a BinaryNumber is returned with
        // the value of the dividend.
        return new BinaryNumber(dividend);
    }

    // Static method that returns the decimal value that corresponds to the given binary number as a string.
    public static int toDecimal(String binaryString) {
        int decimalValue = 0;
        // For loop that calculates the decimal value that corresponds to the binary number binaryString.
        for (int i = 0 ; i < binaryString.length() ; i++) {
            decimalValue += Integer.parseInt(binaryString.substring(i, i+1)) * Math.pow(2, binaryString.length() - i - 1);
        }
        return decimalValue;
    }
}