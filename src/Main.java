import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PolynomialExpression expression = new PolynomialExpression("-2+x+x^2");
        System.out.println(expression);
        Function polynomial = new Quadratic(expression);
        System.out.println(Arrays.toString(polynomial.solve()));
    }
}