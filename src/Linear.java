public class Linear extends Polynomial{
    public Linear(){
        super();
    }
    public Linear(String equation){
        super(equation);
    }
    public double[] solve(){
        int[] coefficients = getCoefficients();
        double[] solutions = new double[1];
        solutions[0] = -(double)coefficients[0]/coefficients[1];
        return solutions;
    }
}
