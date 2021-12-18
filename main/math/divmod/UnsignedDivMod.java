package main.math.divmod;

public class UnsignedDivMod {
    public final int quotient;
    public final int remainder;

    /**
     * Constructs an UnsignedDivMod instance with the given quotient and
     * remainder.
     * 
     * @param quotient    the quotient
     * @param remainder   the remainder
     * @throws IllegalArgumentException   if quotient or remainder are
     *                                    negative
     */
    public UnsignedDivMod(int quotient, int remainder) {
        if (quotient < 0) {
            throw new IllegalArgumentException(
                          "quotient must be nonnegative");
        }
        if (remainder < 0) {
            throw new IllegalArgumentException(
                          "remainder must be nonnegative");
        }
        this.quotient = quotient;
        this.remainder = remainder;
    }

    @Override
    public String toString() {
        return "Quotient: " + quotient + ", Remainder: " + remainder;
    }

    /**
     * Creates and returns an UnsignedDivMod instance whose quotient and
     * remainder are the results of dividing dividend by divisor.
     * 
     * @param dividend   the number to divide
     * @param divisor    the number to divide by
     * @return           an UnsignedDivMod instance containing the results of
     *                   the division
     */
    public static UnsignedDivMod fromDivision(int dividend, int divisor) {
        /* Check input values */
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (dividend < 0) {
            throw new IllegalArgumentException("dividend must be positive");
        }
        if (divisor < 0) {
            throw new IllegalArgumentException("divisor must be positive");
        }
        /* Check if divisor is a power of 2 */
        if ((divisor & divisor-1) == 0) {
            /* Set quotient to dividend right-shifted by number of bits in
            divisor */
            int quotient = dividend, remainder = dividend & divisor-1;
            for (int i = 0; 1<<i < dividend; i++) {
                quotient >>= 1;
            }
            return new UnsignedDivMod(quotient, remainder);
        }
        /* Call actual method */
        return fromDivisionDeltas(dividend, divisor);
    }

    /**
     * Creates and returns an UnsignedDivMod instance whose quotient and
     * remainder are the results of dividing dividend by divisor. The
     * method does this by looping through each bit of dividend and increasing
     * the quotient and remainder by the appropriate amount for the current
     * place value if the current bit is 1.
     * 
     * @param dividend   the number to divide
     * @param divisor    the number to divide by
     * @return           an UnsignedDivMod instance containing the results of
     *                   the division
     * @throws ArithmeticException        when divisor is 0
     * @throws IllegalArgumentException   if dividend or divisor are negative
     */
    public static UnsignedDivMod fromDivisionDeltas(int dividend, int divisor) {
        /* Check input values */
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (dividend < 0) {
            throw new IllegalArgumentException("dividend must be positive");
        }
        if (divisor < 0) {
            throw new IllegalArgumentException("divisor must be positive");
        }
        /* Initialize variables */
        int quotient = 0,
            remainder = 0,
            deltaQuotient = 0,              // Place value divided by divisor
            deltaRemainder = 1;             // Place value modulo divisor
        /* Loop through each bit of dividend */
        for (; dividend != 0; dividend >>= 1) {
            /* Update quotient and remainder */
            if ((dividend & 1) != 0) {
                quotient += deltaQuotient;
                remainder += deltaRemainder;
                /* Handle overflow of remainder */
                if (remainder > divisor) {
                    quotient++;
                    remainder -= divisor;
                }
            }
            /* Multiply deltaQuotient and deltaRemainder by 2 for next
            iteration */
            deltaQuotient <<= 1;
            deltaRemainder <<= 1;
            /* Handle overflow of deltaRemainder */
            if (deltaRemainder > divisor) {
                deltaQuotient++;
                deltaRemainder -= divisor;
            }
        }
        return new UnsignedDivMod(quotient, remainder);
    }

    /**
     * Creates and returns an UnsignedDivMod instance whose quotient and
     * remainder are the results of dividing dividend by divisor. The method
     * does this by looping from the lowest bit of the dividend to the
     * highest, decreasing dividend by a multiple of divisor to make the
     * current bit a 0 while preserving the remainder.
     * 
     * @param dividend   the number to divide
     * @param divisor    the number to divide by
     * @return           an UnsignedDivMod instance containing the results of
     *                   the division
     * @throws ArithmeticException        when divisor is 0
     * @throws IllegalArgumentException   if dividend or divisor are negative
     */
    public static UnsignedDivMod fromDivisionClearLowestBit(int dividend,
                                                            int divisor) {
        /* Check input values */
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (dividend < 0) {
            throw new IllegalArgumentException("dividend must be positive");
        }
        if (divisor < 0) {
            throw new IllegalArgumentException("divisor must be positive");
        }
        /* Initialize variables */
        int quotient = 0;
        /* Loop through bits of dividend (while modifying it) */
        for (int i = 0; dividend>>i < divisor; i++) {
            /* Check if i-th bit of dividend is a 1 */
            if ((dividend & 1<<i) != 0) {
                /* Decrease dividend to make its lowest bit a 0 while
                preserving its remainder */
                dividend -= divisor<<i;
                /* Increase quotient by place value */
                quotient += 1<<i;
            }
        }
        return new UnsignedDivMod(quotient, dividend);
    }
}
