public abstract class Function {
    public static final double PRECISION = 0.0000001;
    public static final int MAX_ITERATIONS = 1000000;
    private Expression expression;
    public Function(Expression expression){
        setExpression(expression);
    }
    protected void setExpression(Expression expression) {
        this.expression = expression.clone();
    }
    public Expression getExpression() {
        return expression.clone();
    }
    public String toString(){
        return getExpression().toString();
    }
    public abstract boolean isSolvable();
    protected abstract double[] solve();
    protected abstract Function derivative();
    public abstract double valueAt(double x);
}
