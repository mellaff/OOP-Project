import static java.lang.Math.sqrt;

public class Quadratic extends Polynomial{
    public Quadratic(){
        super();
    }
    public Quadratic(String equation){
        super(equation);
    }
    public double[] solve(){
        if (!isSolvable()){
            return null;
        }
        int[] coefficients = getCoefficients();
        double[] solutions = new double[2];
        double discriminant = Math.pow(coefficients[1], 2)-4*(double)coefficients[0]*coefficients[2];
        if (discriminant == 0){
            solutions[0] = (double)(-coefficients[1]) / (2 * coefficients[0]);
            solutions[1] = solutions[0];
            return solutions;
        }
        solutions[0] = (-coefficients[1] - sqrt(discriminant)) / (2 * coefficients[0]);
        solutions[1] = (-coefficients[1] + sqrt(discriminant)) / (2 * coefficients[0]);
        return solutions;
    }
    public boolean isSolvable() {
        int[] coefficients = getCoefficients();
        return Math.pow(coefficients[1], 2)-4*(double)coefficients[0]*coefficients[2]>0;
    }
}
