public class Polynomial extends Function {
    public static final int HIGHEST_POSSIBLE_DEGREE = 100;
    private int[] coefficients;
    public Polynomial(){
        super();
    }
    public Polynomial(String equation){
        super(equation);
        coefficients = coefficientsOfExpression();
    }

    public int[] getCoefficients() {
        int[] array = new int[coefficients.length];
        System.arraycopy(coefficients, 0, array, 0, coefficients.length);
        return array;
    }

    public int getHighestDegreeOfPolynomial(){
        return coefficients.length - 1;
    }
    public int[] coefficientsOfExpression(){
        String expression = getExpression();
        int[] coefficients = new int[HIGHEST_POSSIBLE_DEGREE];
        int highestDegree = 0;
        int i = 0;
        while (i<expression.length()){
            if(expression.charAt(i) == 'x'){
                int[] degreeAndEndIndex = getDegreeAtIndex(i);
                coefficients[degreeAndEndIndex[0]] += getCoefficientAtIndex(i);
                if(degreeAndEndIndex[0]>highestDegree){
                    highestDegree = degreeAndEndIndex[0];
                }
                i = degreeAndEndIndex[1]+1;
                continue;
            }
            else if(Character.isDigit(expression.charAt(i))){
                int[] zeroCoefficientAndEndIndex = zeroDegreeCoefficient(i);
                coefficients[0] += zeroCoefficientAndEndIndex[0];
                i = zeroCoefficientAndEndIndex[1];
                continue;
            }
            i++;
        }
        int[] newArr = new int[highestDegree+1];
        System.arraycopy(coefficients, 0, newArr, 0, highestDegree+1);
        return newArr;
    }
    public int[] getDegreeAtIndex(int index){
        String expression = getExpression();
        int[] result = new int[2];
        if(index+1 >= expression.length() || expression.charAt(index+1) != '^'){
            result[0] = 1;
            result[1] = index+1;
        }
        else{
            index+= 2;
            int end = index + 1;
            while (end < expression.length() && Character.isDigit(expression.charAt(end))){
                end++;
            }
            result[0] = Integer.parseInt(expression.substring(index, end));
            result[1] = end;
        }
        return result;
    }

    public int getCoefficientAtIndex(int index){
        String expression = getExpression();
        if (index == 0){
            return 1;
        }
        int start = index-1;
        while (start >= 0 && Character.isDigit(expression.charAt(start))){
            start--;
        }
        if (start == index-1){
            if (expression.charAt(start) == '-'){
                return -1;
            }
            return 1;
        }
        int result = Integer.parseInt(expression.substring(start+1, index));
        if(start >= 0 && expression.charAt(start) == '-'){
            return -result;
        }
        return result;
    }
    public int[] zeroDegreeCoefficient(int index){
        String expression = getExpression();
        int[] res = new int[2];
        int end = index;
        while (end<expression.length() && Character.isDigit(expression.charAt(end))){
            end++;
        }
        res[1] = end;
        boolean isPositive = index == 0 || expression.charAt(index-1) == '+';
        if((isPositive || expression.charAt(index-1) == '-') && (expression.charAt(end) != 'x')){
            int result = Integer.parseInt(expression.substring(index, end));
            if (isPositive){
                res[0] = result;
            }
            else{
                res[0] = -result;
            }
        }
        return res;
    }
}
