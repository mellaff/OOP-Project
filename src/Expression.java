public abstract class Expression implements Cloneable{
    private final String expression;
    public Expression(String expression){
        this.expression = expression;
    }
    public String getExpression() {
        return expression;
    }
    public String toString() {
        return expression;
    }
    public Expression clone(){
        try {
            return (Expression) super.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
    public boolean equals(Object that){
        if (that == null){
            return false;
        }
        if (that.getClass() != getClass()){
            return false;
        }
        Expression givenExpression = (Expression) that;
        return getExpression().equals(givenExpression.getExpression());
    }
    public abstract boolean isValid();
    protected double getCoefficientAtIndex(int index){
        if (index == 0){
            return 1;
        }
        int start = index-1;
        while (start >= 0 && (Character.isDigit(expression.charAt(start)) || expression.charAt(start) == '.')){
            start--;
        }
        if (start == index-1){
            if (expression.charAt(start) == '-'){
                return -1;
            }
            return 1;
        }
        double result = Double.parseDouble(expression.substring(start+1, index));
        if(start >= 0 && expression.charAt(start) == '-'){
            return -result;
        }
        return result;
    }
    protected double[] zeroDegreeCoefficient(int index){
        double[] res = new double[2];
        if((index == 0 || expression.charAt(index-1) != '^')){
            int end = index;
            while (end<expression.length() && (Character.isDigit(expression.charAt(end)) || expression.charAt(end) == '.')){
                end++;
            }
            res[1] = end;
            boolean isPositive = index == 0 || expression.charAt(index-1) == '+' || expression.charAt(index-1) == '=';
            if(end == expression.length() || !Character.isAlphabetic(expression.charAt(end))){
                double result = Double.parseDouble(expression.substring(index, end));
                if (isPositive){
                    res[0] = result;
                }
                else{
                    res[0] = -result;
                }
            }
        }
        return res;
    }

}