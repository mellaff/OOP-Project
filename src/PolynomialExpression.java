import java.util.regex.Pattern;

public class PolynomialExpression extends Expression{
    public PolynomialExpression(String expression){
        super(expression);
    }

    public boolean isValid() {
        String pattern = "^[+-]?(\\d*x(\\^\\d+)?|\\d+)([+-](\\d*x(\\^\\d+)?|\\d+))*$";
        return Pattern.matches(pattern, getExpression());
    }
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
