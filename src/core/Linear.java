package core;

public class Linear extends Polynomial{
    public Linear(PolynomialExpression equation){
        super(equation);
    }
    public double[] solve(){
        double[] coefficients = getCoefficients();
        double[] solutions = new double[1];
        solutions[0] = -(double)coefficients[0]/coefficients[1];
        return solutions;
    }
    public boolean isSolvable() {
        return true;
    }
}
