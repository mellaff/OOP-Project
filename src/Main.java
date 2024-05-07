import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PolynomialExpression expression = new PolynomialExpression("-2+x+x^2-x");
        //System.out.println(expression);
        Polynomial polynomial = new Polynomial(expression);
        //System.out.println(Arrays.toString(polynomial.solve()));
        System.out.println(polynomial);
    }
}