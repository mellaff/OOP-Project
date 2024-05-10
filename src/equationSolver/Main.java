package equationSolver;

import javax.swing.*;
import equationSolver.ui.PolynomialSolverUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PolynomialSolverUI::new);
    }
}