import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Polynomial extends Function implements Cloneable{
    private static final Polynomial MINUS_ONE = generateFromCoefficients(new double[] {-1.0});
    private boolean isSimplified = false;
    public static final int HIGHEST_POSSIBLE_DEGREE = 100000;
    private double[] coefficients;
    private Polynomial derivative;
    private int numberOfRoots;
    private ArrayList<Polynomial> sturmSequence;
    private double[] solutions;

    public Polynomial(PolynomialExpression expression){
        super(expression);
        coefficients = expression.coefficientsOfPolynomialExpression();
    }
    public Expression getExpression() {
        if(!isSimplified){
            setExpression(PolynomialExpression.coefficientsToPolynomialExpression(coefficients));
            isSimplified = true;
        }
        return super.getExpression();
    }
    public Polynomial clone(){
        try {
            Polynomial clone = (Polynomial) super.clone();
            clone.coefficients = getCoefficients().clone();
            if (derivative != null){
                clone.derivative = derivative.clone();
            }
            if (sturmSequence != null){
                clone.sturmSequence = (ArrayList<Polynomial>) sturmSequence.clone();
            }
            return clone;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static Polynomial generateFromCoefficients(double[] coefficients){
        Polynomial polynomial = new Polynomial(PolynomialExpression.coefficientsToPolynomialExpression(coefficients));
        polynomial.coefficients = coefficients.clone();
        return polynomial;
    }
    public double[] getCoefficients() {
        double[] array = new double[coefficients.length];
        System.arraycopy(coefficients, 0, array, 0, coefficients.length);
        return array;
    }
    public Polynomial getDerivative(){
        if (derivative == null){
            derivative = derivative();
        }
        return derivative.clone();
    }
    private ArrayList<Polynomial> getSturmSequence(){
        if (sturmSequence == null){
            sturmSequence = sturmSequenceGenerator();
        }
        return (ArrayList<Polynomial>)sturmSequence.clone();
    }

    public double[] getSolutions() {
        if (solutions == null){
            solutions = this.solve();
        }
        return solutions;
    }
    public int getNumberOfRoots(){
        if (numberOfRoots == 0){
            numberOfRoots = numberOfRootsOn();
        }
        return numberOfRoots;
    }
    public double valueAt(double x){
        double result = coefficients[0];
        for(int i = 1; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }
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
    public int getHighestDegree(){
        return coefficients.length - 1;
    }
    public static Polynomial multiplication(Polynomial firstPolynomial, Polynomial secondPolynomial){
        return generateFromCoefficients(multiplication(firstPolynomial.getCoefficients(),secondPolynomial.getCoefficients()));
    }
    private static double[] multiplication(double[] first, double[] second){
        double[] result = new double[first.length + second.length - 1];
        for (int i = 0; i < first.length; i++) {
            for (int j = 0; j < second.length; j++) {
                result[i + j] += first[i] * second[j];
            }
        }
        return result;
    }
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
    private ArrayList<Polynomial> sturmSequenceGenerator(){
        ArrayList<Polynomial> sturmSequence = new ArrayList<>();
        Polynomial firstTerm = generateFromCoefficients(getRidOfFirstZeroes(getCoefficients()));
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

    protected double[] solve(){
        int n = getNumberOfRoots();
        int i = 0;
        double[] solutions = new double[n];
        double mid;
        double bound = Math.pow(Double.MAX_VALUE, 1.0/getHighestDegree())/getCoefficients()[getHighestDegree()];
        ArrayList<Double> intervals = new ArrayList<>(n+1);
        binarySearch(-bound, bound, intervals);
        if (getCoefficients()[0] == 0){
            solutions[0] = 0;
            i = 1;
        }
        for(; i < n; i++) {
            mid = (intervals.get(i) + intervals.get(i+1)) / 2;
            //solutions[i] = BigDecimal.valueOf(newtonMethod(mid)).setScale(8, RoundingMode.HALF_UP).doubleValue();
            //newtoneMethod to be implemented
        }
        return solutions;
    }
    public boolean isSolvable(){
        return numberOfRootsOn() != 0;
    }
}