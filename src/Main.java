import core.Polynomial;
import core.PolynomialExpression;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PolynomialExpression expression = new PolynomialExpression("-2+x+x^2-x");
        //System.out.println(expression);
        Polynomial polynomial = new Polynomial(expression);
        double firstStart = System.nanoTime();
        System.out.println(Arrays.toString(polynomial.getSolutions()));
        double firstEnd = System.nanoTime();
        double secondStart = System.nanoTime();
        System.out.println(Arrays.toString(polynomial.solveByBisection()));
        double secondEnd = System.nanoTime();
        System.out.println(firstEnd - firstStart);
        System.out.println(secondEnd - secondStart);
    }
}