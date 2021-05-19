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

    public static BinaryNumber xorDivide(String dividend, String divisor) {
        while (dividend.length() >= divisor.length() && !(dividend.equals("0"))) {
            StringBuilder temp = new StringBuilder(dividend);
            int counter = 0;
            boolean flag = true;
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
            temp.delete(0, counter);
            dividend = temp.toString();
        }
        return new BinaryNumber(dividend);
    }

        public static int toDecimal(String binaryString) {
        int decimalValue = 0;
        for (int i = 0 ; i < binaryString.length() ; i++) {
            decimalValue += Integer.parseInt(binaryString.substring(i, i+1)) * Math.pow(2, binaryString.length() - i - 1);
        }
        return decimalValue;
    }
}