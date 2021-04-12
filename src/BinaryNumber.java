public class BinaryNumber {

    private int decimalValue;

    public BinaryNumber(int decimalNumber) {
        decimalValue = decimalNumber;
    }

    public BinaryNumber(String binaryNumber) {
        decimalValue = BinaryNumber.toDecimal(binaryNumber);
    }

    public void setDecimalValue(int decimalNumber) {
        decimalValue = decimalNumber;
    }

    public void setBinaryValue(String binaryNumber) {
        decimalValue = BinaryNumber.toDecimal(binaryNumber);
    }

    public int getDecimalValue() {
        return decimalValue;
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(decimalValue);
    }

    public String toString(int numberOfBits) {
        String binaryString = Integer.toBinaryString(decimalValue);
        String leadingZeroes = "";
        for (int i = binaryString.length() ; i < numberOfBits ; i ++)
            leadingZeroes = leadingZeroes.concat("0");
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
