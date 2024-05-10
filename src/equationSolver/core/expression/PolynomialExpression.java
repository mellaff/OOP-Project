package equationSolver.core.expression;

import equationSolver.core.function.Polynomial;

import java.util.regex.Pattern;

/**
 * Represents a polynomial expression.
 * <p>
 * This class extends the {@link Expression} class and provides methods to validate a polynomial expression
 * and to extract its coefficients.
 */
public class PolynomialExpression extends Expression{
    /**
     * Constructs a new PolynomialExpression object with the specified expression.
     *
     * @param expression The polynomial expression.
     */
    public PolynomialExpression(String expression){
        super(expression);
    }
    /**
     * Validates whether the polynomial expression is in a correct format.
     *
     * <p>
     * The method checks if the expression matches the pattern of a valid polynomial expression,
     * which consists of terms separated by addition or subtraction operators.
     *
     * @return {@code true} if the expression is valid, {@code false} otherwise.
     */

    public static boolean isValid(String expression) {
        String pattern = "^[+-]?(\\d*x(\\^\\d+)?|\\d+)([+-](\\d*x(\\^\\d+)?|\\d+))*$";
        return Pattern.matches(pattern, expression);
    }
    /**
     * Extracts the coefficients of the polynomial expression.
     *
     * <p>
     * The method parses the polynomial expression to retrieve the coefficients of each term.
     * It then arranges the coefficients in an array corresponding to the powers of the variable 'x'.
     *
     * @return An array containing the coefficients of the polynomial expression.
     */
    public double[] coefficientsOfPolynomialExpression(){
        if (getExpression().isEmpty()){
            return new double[0];
        }
        double[] coefficients = new double[Polynomial.HIGHEST_POSSIBLE_DEGREE];
        int highestDegree = 0;
        int i = 0;
        while (i<getExpression().length()){
            if(Character.isAlphabetic(getExpression().charAt(i))){
                int[] degreeAndEndIndex = getDegreeAtIndex(i);
                coefficients[degreeAndEndIndex[0]] += getCoefficientAtIndex(i);
                if(degreeAndEndIndex[0]>highestDegree){
                    highestDegree = degreeAndEndIndex[0];
                }
                i = degreeAndEndIndex[1]+1;
                continue;
            }
            else if(Character.isDigit(getExpression().charAt(i))){
                double[] zeroCoefficientAndEndIndex = zeroDegreeCoefficient(i);
                coefficients[0] += zeroCoefficientAndEndIndex[0];
                i = (int)zeroCoefficientAndEndIndex[1];
                continue;
            }
            i++;
        }
        double[] newArr = new double[highestDegree+1];
        System.arraycopy(coefficients, 0, newArr, 0, highestDegree+1);
        return newArr;
    }
    /**
     * Constructs a PolynomialExpression object from an array of coefficients.
     *
     * <p>
     * The method creates a polynomial expression from the given coefficients.
     *
     * @param coefficients The array of coefficients.
     * @return The PolynomialExpression object representing the polynomial.
     */
    public static PolynomialExpression coefficientsToPolynomialExpression(double[] coefficients) {
        if (coefficients == null || coefficients.length == 0){
            return new PolynomialExpression("");
        }
        StringBuilder res = new StringBuilder();
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (coefficients[i] == 0) {
                continue;
            }
            if (!res.isEmpty()) {
                if (coefficients[i] > 0) res.append("+");
                else if (coefficients[i] < 0) res.append("-");
            } else {
                if (coefficients[i] < 0) res.append("-");
            }
            if (Math.abs(coefficients[i]) != 1 || i == 0) {
                res.append(Math.abs(coefficients[i]));
            }
            if (i >= 1) {
                res.append("x");
                if (i != 1) {
                    res.append("^").append(i);
                }
            }
        }
        return new PolynomialExpression(res.toString());
    }
    /**
     * Retrieves the degree of the term at the specified index in the expression.
     *
     * <p>
     * The method parses the expression to determine the degree of the term at the specified index.
     *
     * @param index The index of the term.
     * @return An array containing the degree of the term and the end index of the term in the expression.
     */
    private int[] getDegreeAtIndex(int index){
        int[] result = new int[2];
        if(index+1 >= getExpression().length() || getExpression().charAt(index+1) != '^'){
            result[0] = 1;
            result[1] = index+1;
        }
        else{
            index+= 2;
            int end = index + 1;
            while (end < getExpression().length() && Character.isDigit(getExpression().charAt(end))){
                end++;
            }
            result[0] = Integer.parseInt(getExpression().substring(index, end));
            result[1] = end;
        }
        return result;
    }
}
