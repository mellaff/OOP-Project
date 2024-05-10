package core;
import core.expression.LinearExpression;
import core.system.SystemOfEquations;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        LinearExpression expression1 = new LinearExpression("3x+4y=1-x");
        LinearExpression expression2 = new LinearExpression("1-z=x");
        LinearExpression[] array = {expression1, expression2};
        SystemOfEquations system = new SystemOfEquations(array);
        System.out.println(Arrays.toString(system.getSolutions()));
        //SwingUtilities.invokeLater(PolynomialSolverUI::new);
    }
}