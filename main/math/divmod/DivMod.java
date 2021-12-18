package main.math.divmod;

import java.math.RoundingMode;

public class DivMod {
    public interface BiIntFunction<R> {
        public R apply(int a, int b);
    }
    public final int quotient;
    public final int remainder;

    /**
     * Constructs a DivMod instance with the given quotient and remainder.
     * 
     * @param quotient    the quotient
     * @param remainder   the remainder
     */
    public DivMod(int quotient, int remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }

    @Override
    public String toString() {
        return "Quotient: " + quotient + ", Remainder: " + remainder;
    }

    /**
     * Creates and returns a DivMod instance whose quotient and remainder are
     * the results of dividing dividend by divisor, using the supplied
     * function to do unsigned floor division and rounding the quotient
     * according to the given RoundingMode.
     * 
     * @param dividend       the number to divide
     * @param divisor        the number to divide by
     * @param divMod         the function used to do an unsigned division
     * @param roundingMode   the rounding mode for the quotient
     * @return               a DivMod instance containing the results of the
     *                       division
     * @throws ArithmeticError            when divisor is 0, or if the rounding
     *                                    mode is RoundingMode.UNNECESSARY but
     *                                    the remainder is non-zero
     * @throws IllegalArgumentException   if roundingMode is unrecognized
     */
    public static DivMod fromDivision(int dividend, int divisor,
                                      BiIntFunction<UnsignedDivMod> divMod,
                                      RoundingMode roundingMode) {
        /* Check for division by zero */
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        /* Perform unsigned division on absolute values */
        UnsignedDivMod unsignedResult
            = divMod.apply((dividend < 0) ? -dividend : dividend,
                           (divisor < 0) ? -divisor : divisor);
        /* Extract quotient and remainder */
        int quotient = unsignedResult.quotient,
            remainder = unsignedResult.remainder;
        /* Update quotient and remainder based on rounding mode */
        boolean increaseQuotientAbsoluteValue = false;
        switch (roundingMode) {
            case UP:
                increaseQuotientAbsoluteValue = remainder != 0;
                break;
            case DOWN:
                break;
            case CEILING:
                increaseQuotientAbsoluteValue
                    = remainder != 0 && !(dividend < 0 ^ divisor < 0);
                break;
            case FLOOR:
                increaseQuotientAbsoluteValue
                    = remainder != 0 && (dividend < 0 ^ divisor < 0);
                break;
            case HALF_UP:
                // divisor >> 1 is divisor/2 in Java (right-shifting a
                // negative value is undefined in some languages)
                increaseQuotientAbsoluteValue = remainder >= divisor >> 1;
                break;
            case HALF_DOWN:
                // divisor >> 1 is divisor/2 in Java (right-shifting a
                // negative value is undefined in some languages)
                increaseQuotientAbsoluteValue = remainder > divisor >> 1;
                break;
            case HALF_EVEN:
                // divisor >> 1 is divisor/2 in Java (right-shifting a
                // negative value is undefined in some languages)
                // (quotient & 1) != 0 is true if quotient is odd
                increaseQuotientAbsoluteValue
                    = remainder > divisor >> 1
                      || remainder == divisor >> 1 && (quotient & 1) != 0;
                break;
            case UNNECESSARY:
                if (remainder != 0) {
                    throw new IllegalArgumentException(
                                  "Rounding necessary in"
                                  + " RoundingMode.UNECESSARY rounding mode");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown roundingMode");
        }
        if (increaseQuotientAbsoluteValue) {
            quotient++;
            remainder -= divisor;
        }
        /* Update signs of quotient and remainder */
        if (dividend < 0 ^ divisor < 0) {
            quotient = -quotient;
        }
        if (dividend < 0) {
            remainder = -remainder;
        }
        /* Return results */
        return new DivMod(quotient, remainder);
    }
}