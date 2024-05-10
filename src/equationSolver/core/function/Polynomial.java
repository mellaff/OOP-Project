package equationSolver.core.function;

import equationSolver.core.expression.Expression;
import equationSolver.core.expression.PolynomialExpression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a polynomial equationSolver.core.function.
 */
public class Polynomial extends Function implements Cloneable {

    private static final Polynomial MINUS_ONE = generateFromCoefficients(new double[] {-1.0});
    private boolean isSimplified = false;
    public static final int HIGHEST_POSSIBLE_DEGREE = 100000;
    private double[] coefficients;
    private Polynomial derivative;
    private int numberOfRoots;
    private ArrayList<Polynomial> sturmSequence;
    private double[] solutions;

    /**
     * Constructs a polynomial from a given polynomial expression.
     *
     * @param expression The polynomial expression.
     */
    public Polynomial(PolynomialExpression expression) {
        super(expression);
        coefficients = expression.coefficientsOfPolynomialExpression();
    }

    /**
     * Returns the polynomial expression.
     *
     * @return The polynomial expression.
     */
    public Expression getExpression() {
        if (!isSimplified) {
            setExpression(PolynomialExpression.coefficientsToPolynomialExpression(coefficients));
            isSimplified = true;
        }
        return super.getExpression();
    }

    /**
     * Clones the polynomial.
     *
     * @return A clone of the polynomial.
     */
    public Polynomial clone() {
        try {
            Polynomial clone = (Polynomial) super.clone();
            clone.coefficients = getCoefficients().clone();
            if (derivative != null) {
                clone.derivative = derivative.clone();
            }
            if (sturmSequence != null) {
                clone.sturmSequence = (ArrayList<Polynomial>) sturmSequence.clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Generates a polynomial from given coefficients.
     *
     * @param coefficients The coefficients of the polynomial.
     * @return The generated polynomial.
     */
    public static Polynomial generateFromCoefficients(double[] coefficients) {
        Polynomial polynomial = new Polynomial(PolynomialExpression.coefficientsToPolynomialExpression(coefficients));
        polynomial.coefficients = coefficients.clone();
        return polynomial;
    }

    /**
     * Returns a clone of the coefficients array.
     *
     * @return A clone of the coefficients array.
     */
    public double[] getCoefficients() {
        double[] array = new double[coefficients.length];
        System.arraycopy(coefficients, 0, array, 0, coefficients.length);
        return array;
    }
    /**
     * Computes the derivative of the polynomial.
     *
     * @return The derivative of the polynomial.
     */
    public Polynomial getDerivative(){
        if (derivative == null){
            derivative = derivative();
        }
        return derivative.clone();
    }
    /**
     * Gets the Sturm sequence of the polynomial.
     *
     * @return The Sturm sequence of the polynomial.
     */
    private ArrayList<Polynomial> getSturmSequence(){
        if (sturmSequence == null){
            sturmSequence = sturmSequenceGenerator();
        }
        return (ArrayList<Polynomial>)sturmSequence.clone();
    }
    /**
     * Gets the solutions of the polynomial equation.
     *
     * @return The solutions of the polynomial equation.
     */
    public double[] getSolutions() {
        if (solutions == null){
            solutions = this.solve();
        }
        return solutions;
    }
    /**
     * Gets the number of roots of the polynomial.
     *
     * @return The number of roots of the polynomial.
     */
    public int getNumberOfRoots(){
        if (numberOfRoots == 0){
            numberOfRoots = numberOfRootsOn();
        }
        return numberOfRoots;
    }
    /**
     * Computes the value of the polynomial at a given point.
     *
     * @param x The value at which to evaluate the polynomial.
     * @return The value of the polynomial at the given point.
     */
    public double valueAt(double x){
        double result = coefficients[0];
        for(int i = 1; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }
    /**
     * Computes the derivative of the polynomial.
     *
     * @return The derivative of the polynomial.
     */
    protected Polynomial derivative(){
        double[] coefficientsOfDerivative;
        if (coefficients.length == 0){
            coefficientsOfDerivative = new double[0];
            return generateFromCoefficients(coefficientsOfDerivative);
        }
        coefficientsOfDerivative = new double[coefficients.length - 1];
        for (int i = 1; i < coefficients.length; i++) {
            coefficientsOfDerivative[i-1] = coefficients[i] * i;
        }
        return generateFromCoefficients(coefficientsOfDerivative);
    }
    /**
     * Gets the highest degree of the polynomial.
     *
     * @return The highest degree of the polynomial.
     */
    public int getHighestDegree(){
        return coefficients.length - 1;
    }
    /**
     * Multiplies two polynomials.
     *
     * @param firstPolynomial  The first polynomial.
     * @param secondPolynomial The second polynomial.
     * @return The product of the two polynomials.
     */
    public static Polynomial multiplication(Polynomial firstPolynomial, Polynomial secondPolynomial){
        return generateFromCoefficients(multiplication(firstPolynomial.getCoefficients(),secondPolynomial.getCoefficients()));
    }
    /**
     * Multiplies two arrays representing polynomials.
     *
     * @param first  The coefficients of the first polynomial.
     * @param second The coefficients of the second polynomial.
     * @return The coefficients of the product polynomial.
     */
    private static double[] multiplication(double[] first, double[] second){
        double[] result = new double[first.length + second.length - 1];
        for (int i = 0; i < first.length; i++) {
            for (int j = 0; j < second.length; j++) {
                result[i + j] += first[i] * second[j];
            }
        }
        return result;
    }
    /**
     * Subtracts one array from another.
     *
     * @param minuend     The array from which to subtract.
     * @param subtrahend  The array to subtract.
     * @return The result of the subtraction.
     */
    private static double[] subtractArray(double[] minuend, double[] subtrahend){
        for (int i = 0; i < subtrahend.length; i++) {
            minuend[i] -= subtrahend[i];
        }
        int newLength = 0;
        for (int i = 0; i < minuend.length; i++) {
            if (minuend[i] != 0) {
                newLength = i + 1;
            }
        }
        if (newLength == 0) {
            return new double[]{0.0};
        }
        double[] result = new double[newLength];
        System.arraycopy(minuend, 0, result, 0, newLength);
        return result;
    }
    /**
     * Computes the remainder of the Euclidean division of two polynomials.
     *
     * <p>This method calculates the remainder of dividing the dividend polynomial by the divisor polynomial
     * using the Euclidean division algorithm. It iteratively divides the leading terms of the dividend by
     * the leading term of the divisor and subtracts the resulting multiple of the divisor from the dividend.
     * The process continues until the degree of the remaining dividend becomes less than the degree of the divisor.
     *
     * @param dividend The polynomial to be divided.
     * @param divisor  The polynomial by which to divide.
     * @return The remainder polynomial after the division.
     */
    public static Polynomial remainderOfEuclideanDivisionOfPolynomials(Polynomial dividend, Polynomial divisor){
        int d = divisor.getHighestDegree();
        double[] remainder = dividend.getCoefficients();
        double[] divisorCoefficients = divisor.getCoefficients();
        while (remainder.length > 1 && remainder.length - 1 >= d){
            double[] temp = new double[remainder.length-divisorCoefficients.length+1];
            temp[temp.length-1] = remainder[remainder.length-1]/divisorCoefficients[divisorCoefficients.length-1];
            remainder = subtractArray(remainder, multiplication(divisorCoefficients,temp));
        }
        return generateFromCoefficients(remainder);
    }
    /**
     * Removes leading zero coefficients from an array of coefficients.
     *
     * @param array The array of coefficients.
     * @return The array of coefficients with leading zero coefficients removed.
     */
    private static double[] getRidOfFirstZeroes(double[] array){
        int firstNonZeroIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                firstNonZeroIndex = i;
                break;
            }
        }
        if (firstNonZeroIndex != 0){
            firstNonZeroIndex--;
        }
        double[] newArray = new double[array.length - firstNonZeroIndex];
        System.arraycopy(array, firstNonZeroIndex, newArray, 0, newArray.length);
        return newArray;
    }
    /**
     * Generates the Sturm sequence for the polynomial.
     *
     * @return The Sturm sequence of the polynomial.
     */
    private ArrayList<Polynomial> sturmSequenceGenerator(){
        ArrayList<Polynomial> sturmSequence = new ArrayList<>();
        Polynomial firstTerm = generateFromCoefficients(getCoefficients());
        Polynomial current = firstTerm.getDerivative();
        sturmSequence.add(firstTerm);
        if (firstTerm.getCoefficients().length == 1){
            return sturmSequence;
        }
        sturmSequence.add(current);
        int i = 1;
        while (current.getHighestDegree()>0){
            current = multiplication(remainderOfEuclideanDivisionOfPolynomials(sturmSequence.get(i-1),sturmSequence.get(i++)),MINUS_ONE);
            sturmSequence.add(current);
        }
        return sturmSequence;
    }
    /**
     * Counts the number of roots of the polynomial within the specified interval.
     *
     * @param start The starting point of the interval.
     * @param end   The ending point of the interval.
     * @return The number of roots within the interval.
     */
    public int numberOfRootsOn(double start, double end){
        ArrayList<Polynomial> sturmSequence = getSturmSequence();
        boolean sign1 = sturmSequence.get(0).valueAt(start) > 0;
        boolean sign2 = sturmSequence.get(0).valueAt(end) > 0;
        int counter1 = 0;
        int counter2 = 0;
        for (int i = 1; i < sturmSequence.size(); i++) {
            if (sturmSequence.get(i).valueAt(start) > 0 ^ sign1){
                counter1++;
                sign1 = !sign1;
            }
            if (sturmSequence.get(i).valueAt(end) > 0 ^ sign2){
                counter2++;
                sign2 = !sign2;
            }
        }
        return counter1-counter2;
    }
    /**
            * Counts the number of roots of the polynomial.
            *
            * @return The number of roots of the polynomial.
     */
    private int numberOfRootsOn(){
        ArrayList<Polynomial> sturmSequence = getSturmSequence();
        double[] current = sturmSequence.get(0).getCoefficients();
        boolean sign1;
        boolean sign2;
        if (current.length % 2 == 1){
            sign1 = current[current.length-1]>0;
            sign2 = sign1;
        }
        else{
            sign1 = current[current.length-1]<0;
            sign2 = !sign1;
        }
        int counter1 = 0;
        int counter2 = 0;
        for (int i = 1; i < sturmSequence.size(); i++) {
            current = sturmSequence.get(i).getCoefficients();
            if (current.length % 2 == 1){
                if (current[current.length-1]>0 ^ sign1){
                    counter1++;
                    sign1 = !sign1;
                }
            }
            else{
                if (current[current.length-1]<0 ^ sign1){
                    counter1++;
                    sign1 = !sign1;
                }
            }
            if (current[current.length-1]>0 ^ sign2){
                counter2++;
                sign2 = !sign2;
            }
        }
        return counter1-counter2;
    }
    /**
     * Performs a binary search to find roots within the specified interval.
     *
     * @param first     The lower bound of the interval.
     * @param second    The upper bound of the interval.
     * @param arrayList The list to store the roots found.
     */
    private void binarySearch(double first, double second, ArrayList<Double> arrayList){
        if (arrayList.size() == (getNumberOfRoots())){
            arrayList.add(first);
            return;
        }
        double lowerBound = first;
        double upperBound = second;
        double mid;
        while (numberOfRootsOn(lowerBound, upperBound) > 1 || (upperBound - lowerBound) > 10){
            mid = (lowerBound + upperBound) / 2;
            if (valueAt(mid) == 0){
                lowerBound = mid-1;
            }
            if (numberOfRootsOn(lowerBound, mid) != 0){
                upperBound = mid;
                continue;
            }
            lowerBound = mid;
        }
        arrayList.add(lowerBound);
        binarySearch(upperBound, second, arrayList);
    }
    /**
     * Implements the bisection method to find roots of the polynomial.
     *
     * @param a The lower bound of the interval.
     * @param b The upper bound of the interval.
     * @return The root found within the interval.
     */
    private double bisectionMethod(double a, double b)
    {
        int iterations = 0;
        double c = a+b/2;
        while(Math.abs(c) > PRECISION && iterations < MAX_ITERATIONS)
        {
            if((valueAt(c) > 0 && valueAt(a) < 0) || (valueAt(c) < 0 && valueAt(a) > 0)){
                b = c;
                c = (a+c)/2;
            }
            else if ((valueAt(c) > 0 && valueAt(b) < 0) || (valueAt(c) < 0 && valueAt(b) > 0)) {
                a = c;
                c = (c + b) / 2;
            }
            iterations++;
        }
        return c;
    }
    /**
     * Solves the polynomial equation using the bisection method.
     *
     * @return An array containing the roots of the polynomial.
     */
    public double[] solveByBisection()
    {
        int n = getNumberOfRoots();
        double[] solutions = new double[n];
        double bound = Math.pow(Double.MAX_VALUE, 1.0/getHighestDegree())/getCoefficients()[getHighestDegree()];
        ArrayList<Double> intervals = new ArrayList<>(n+1);
        binarySearch(-bound, bound, intervals);

        for(int i=0; i<n; i++)
            solutions[i] = bisectionMethod(intervals.get(i), intervals.get(i+1));
        return solutions;
    }

    private double newtonMethod(double x0) {
        double x1 = x0 - valueAt(x0) / getDerivative().valueAt(x0);
        int iteration = 0;
        while (Math.abs(x1 - x0) > PRECISION && iteration < MAX_ITERATIONS) {
            x0 = x1;
            x1 = x0 - valueAt(x0) / getDerivative().valueAt(x0);
            iteration++;
        }
        return x1;
    }
    /**
     * Finds the solutions of the polynomial equation.
     *
     * <p>This method computes the solutions of the polynomial equation by dividing the real number line
     * into intervals based on the changes in the number of roots of the polynomial. It uses binary search
     * within these intervals to find the approximate roots.
     *
     * @return An array containing the solutions of the polynomial equation.
     */
    protected double[] solve(){
        int n = getNumberOfRoots();
        double[] solutions = new double[n];
        double mid;
        double bound = Math.pow(Double.MAX_VALUE, 1.0/getHighestDegree())/getCoefficients()[getHighestDegree()];
        ArrayList<Double> intervals = new ArrayList<>(n+1);
        binarySearch(-bound, bound, intervals);
        for(int i = 0; i < n; i++) {
            mid = (intervals.get(i) + intervals.get(i+1)) / 2;
            solutions[i] = BigDecimal.valueOf(newtonMethod(mid)).setScale(8, RoundingMode.HALF_UP).doubleValue();
        }
        System.out.println(Arrays.toString(solutions));
        return solutions;
    }
    /**
     * Checks if the polynomial equation is solvable.
     *
     * <p>This method determines whether the polynomial equation has any real roots by analyzing its
     * Sturm sequence and counting the number of sign changes in the sequence.
     *
     * @return {@code true} if the polynomial equation is solvable; {@code false} otherwise.
     */
    public boolean isSolvable(){
        return numberOfRootsOn() != 0;
    }

    /**
     * Interpolates a polynomial that passes through a given set of points.
     *
     * <p>This method constructs the interpolating polynomial that passes through the specified points
     * using Lagrange interpolation. It computes the coefficients of the interpolating polynomial by
     * evaluating the divided differences and multiplying them with the corresponding basis polynomials.
     *
     * @param points The list of points through which the polynomial should pass.
     * @return The interpolating polynomial.
     */
    public Polynomial interpolate(List<Point> points) {
        int n = points.size();
        double[] x = new double[n];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = points.get(i).getX();
            y[i] = points.get(i).getY();
        }

        double[][] dividedDifferences = new double[n][n];
        for (int i = 0; i < n; i++) {
            dividedDifferences[0][i] = y[i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n-i ; j++) {
                dividedDifferences[i][j] = (dividedDifferences[i-1][j+1] - dividedDifferences[i-1][j]) / (x[i+j] - x[j]);
            }
        }


        double[][] coeffsArray = new double[n][n];
        coeffsArray[0][0]=1;
        for (int i=1;i<n;i++){
            double[] newTerm = new double[2];
            newTerm[0]=-x[i-1];
            newTerm[1]=1;
            coeffsArray[i]=multiplication(coeffsArray[i-1],newTerm);
        }
        for (int i=0;i<n;i++) {
            for (int j = 0; j <= i; j++) {
                coeffsArray[i][j] = dividedDifferences[i][0] * coeffsArray[i][j];
            }
        }
        double[] columnSums = new double[n];

        for (int j = 0; j < n; j++) {
            // Initialize the sum for this column
            double sum = 0.0;

            for (int i = 0; i < n; i++) {
                sum += coeffsArray[i][j];
            }

            columnSums[j] = sum;
        }

        return generateFromCoefficients(columnSums);
    }

    /**
     * Computes the interpolated value of the polynomial at a given x-coordinate.
     *
     * <p>This method evaluates the interpolating polynomial obtained from the given set of points
     * at the specified x-coordinate to estimate the corresponding y-coordinate.
     *
     * @param xValue The x-coordinate at which to evaluate the interpolating polynomial.
     * @param points The list of points through which the polynomial passes.
     * @return The interpolated value of the polynomial at the specified x-coordinate.
     */
    public double interpolateValue(double xValue, List<Point> points) {
        Polynomial interpolatingPolynomial = interpolate(points);
        return interpolatingPolynomial.valueAt(xValue);
    }

}

