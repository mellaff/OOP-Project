public class Function {
    private String expression;
    public Function(){
    }
    public Function(String expression){
        setExpression(expression);
    }
    public void setExpression(String expression) {
        this.expression = expression.replace(" ", "");
    }
    public String getExpression() {
        return expression;
    }
    public String toString(){
        return expression;
    }
    public boolean isSolvable(){
        return true;
    }
    public double[] solve(){
        return null;
    }
}
