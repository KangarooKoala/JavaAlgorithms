package main;

import java.util.Scanner;
import java.math.RoundingMode;
import main.math.divmod.DivMod;
import main.math.divmod.UnsignedDivMod;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        testDivMod(scanner);
        scanner.close();
    }

    /***
     * Tests the DivMod class.
     * 
     * @param scanner   the scanner to read test input from
     */
    public static void testDivMod(Scanner scanner) {
        int dividend, divisor;
        DivMod divMod;
        String roundingModeName;
        RoundingMode roundingMode = RoundingMode.UP;
        String cont;
        do {
            /* Set dividend and divisor */
            System.out.print("Enter dividend: ");
            dividend = scanner.nextInt();
            scanner.nextLine();                     // Discard line of input
            System.out.print("Enter divisor: ");
            divisor = scanner.nextInt();
            scanner.nextLine();                     // Discard line of input
            /* Set rounding mode */
            System.out.print("Enter optional new rounding mode: ");
            roundingModeName = scanner.nextLine().toUpperCase();
            if (!roundingModeName.isEmpty()) {
                roundingMode = RoundingMode.valueOf(roundingModeName);
            }
            /* Output result of division */
            divMod = DivMod.fromDivision(dividend, divisor,
                                         UnsignedDivMod::fromDivision,
                                         roundingMode);
            System.out.println(divMod);
            /* Check if should continue */
            System.out.print("Continue? (y/n) ");
            cont = scanner.next();
            scanner.nextLine();                     // Discard line of input
        } while (cont.equalsIgnoreCase("y"));
    }
}
