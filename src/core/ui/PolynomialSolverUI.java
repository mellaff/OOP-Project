package core.ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import core.function.Point;
import core.expression.Expression;
import core.expression.LinearExpression;
import core.expression.PolynomialExpression;
import core.system.*;
import core.exceptions.InvalidExpressionExceptions;
import core.function.Polynomial;

/**
 * A graphical user interface for solving polynomial equations.
 */
public class PolynomialSolverUI extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Font WELCOME_FONT = new Font("Apple Chancery", Font.BOLD, 30);
    private static final Font CLICK_FONT = new Font("Times New Roman", Font.PLAIN, 15);
    private static final Font MENU_FONT = new Font("Times New Roman", Font.BOLD, 15);
    private AbstractButton interpolationItem;
    private AbstractButton newtonsMethodItem;
    private AbstractButton linearEquationsItem;


    /**
     * Constructs a PolynomialSolverUI object.
     */
    public PolynomialSolverUI() {

        setTitle("Equation Solver");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Equation Solver");
        welcomeLabel.setFont(WELCOME_FONT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(new EmptyBorder(50, 0, 20, 0));
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        welcomePanel.setBackground(BACKGROUND_COLOR);
        welcomePanel.add(welcomeLabel);
        contentPane.add(welcomeLabel, BorderLayout.NORTH);


        JLabel clickLabel = new JLabel("Click anywhere to continue");
        clickLabel.setFont(CLICK_FONT);
        clickLabel.setForeground(Color.GRAY);
        clickLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(clickLabel, BorderLayout.CENTER);


        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMenuBar(contentPane);
            }
        });

        contentPane.setBackground(BACKGROUND_COLOR);

        setContentPane(contentPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showMenuBar(JPanel contentPane) {

        contentPane.remove(0);
        contentPane.remove(0);

        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        menuPanel.setBackground(BACKGROUND_COLOR);
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JMenuItem interpolationItem = createMenuItem("Interpolation", "interpolation.png");
        JMenuItem newtonsMethodItem = createMenuItem("Solve by Newton's method", "newtons_method.png");
        JMenuItem linearEquationsItem = createMenuItem("Linear system of equations", "linear_equations.png");

        menuPanel.add(interpolationItem);
        menuPanel.add(newtonsMethodItem);
        menuPanel.add(linearEquationsItem);

        contentPane.add(menuPanel, BorderLayout.CENTER);

        JPanel menuBorderPanel = new JPanel(new BorderLayout());
        menuBorderPanel.setBorder(BorderFactory.createTitledBorder("Choose your preferred option"));
        menuBorderPanel.add(menuPanel, BorderLayout.CENTER);

        contentPane.add(menuBorderPanel, BorderLayout.CENTER);

        contentPane.revalidate();
        contentPane.repaint();
    }

    private JMenuItem createMenuItem(String text, String iconFileName) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(MENU_FONT);
        menuItem.setForeground(Color.DARK_GRAY);
        menuItem.setIcon(new ImageIcon(iconFileName));
        menuItem.setHorizontalTextPosition(SwingConstants.RIGHT);
        menuItem.addActionListener(e -> {
            if (text.equals("Interpolation")) {
                ArrayList<Point> points = new ArrayList<>();
                int numPoints = 0;
                boolean validInput = false;
                while (!validInput) {
                    try {
                        String numPointsInput = JOptionPane.showInputDialog("Enter the number of points:");
                        if (numPointsInput == null) {
                            if (interpolationItem != null) {
                                interpolationItem.setSelected(false);
                            }
                            return;
                        }
                        numPoints = Integer.parseInt(numPointsInput);
                        validInput = true;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid integer number of points.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                }
                for (int i = 0; i < numPoints; i++) {
                    boolean validInput1 = false;
                    while (!validInput1) {
                        try {
                            double x = Double.parseDouble(JOptionPane.showInputDialog("Enter x for point " + (i + 1) + ":"));
                            double y = Double.parseDouble(JOptionPane.showInputDialog("Enter y for point " + (i + 1) + ":"));
                            points.add(new Point(x, y));
                            validInput1 = true;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Please enter valid numeric coordinates for the point.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                Polynomial interpolatingPolynomial = new Polynomial(new PolynomialExpression(""));
                Polynomial interpolated = interpolatingPolynomial.interpolate(points);
                boolean validInput2 = false;
                int numInterpolatedValues = 0;

                while (!validInput2) {
                    try {
                        numInterpolatedValues = Integer.parseInt(JOptionPane.showInputDialog("How many interpolated values you'd like to know?"));
                        validInput2 = true;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid numeric value for the number of interpolated values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                }

                StringBuilder result = new StringBuilder();
                result.append("Interpolating polynomial: ").append(interpolated).append("\n");

                for (int i = 0; i < numInterpolatedValues; i++) {
                    validInput = false;
                    while (!validInput) {
                        try {
                            double xValue = Double.parseDouble(JOptionPane.showInputDialog("Enter x value for interpolation " + (i + 1) + ":"));
                            double interpolatedValue = interpolatingPolynomial.interpolateValue(xValue, points);
                            result.append("Interpolated value at x = ").append(xValue).append(": ").append(interpolatedValue).append("\n");
                            validInput = true;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Please enter valid numeric x value for interpolation.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, result.toString(), "Interpolation Result", JOptionPane.INFORMATION_MESSAGE);

            } else if (text.equals("Solve by Newton's method")) {
                boolean validExpression = false;
                Expression polynomialExpression = null;

                while (!validExpression) {
                    try {
                        String input = JOptionPane.showInputDialog("Enter a polynomial expression:");
                        if (input == null) {
                            if (newtonsMethodItem != null) {
                                newtonsMethodItem.setSelected(false);
                            }
                            return;
                        }
                        if (!PolynomialExpression.isValid(input)) {
                            throw new InvalidExpressionExceptions("Invalid polynomial expression format.");
                        }

                        polynomialExpression = new PolynomialExpression(input);
                        if (!polynomialExpression.getExpression().isEmpty()) {
                            validExpression = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Please enter a valid polynomial expression.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (InvalidExpressionExceptions ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                Polynomial polynomial = new Polynomial((PolynomialExpression) polynomialExpression);
                double[] solutions = polynomial.getSolutions();
                StringBuilder solutionText = new StringBuilder();
                solutionText.append("Solutions for ").append(polynomialExpression).append(" are:\n");
                for (int i = 0; i < solutions.length; i++) {
                    solutionText.append("x").append(i + 1).append(" = ").append(solutions[i]);
                    if (i < solutions.length - 1) {
                        solutionText.append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, solutionText.toString(), "Newton's Method Result", JOptionPane.INFORMATION_MESSAGE);
            } else if (text.equals("Linear system of equations")) {
                boolean validInput = false;
                int numEquations = 0;

                while (!validInput) {
                    try {
                        String numEquationsInput = JOptionPane.showInputDialog("Enter the number of equations:");
                        if (numEquationsInput == null) {
                            if (linearEquationsItem != null) {
                                linearEquationsItem.setSelected(false);
                            }
                            return;
                        }
                        numEquations = Integer.parseInt(numEquationsInput);
                        validInput = true;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid integer number of equations.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                }

                LinearExpression[] equations = new LinearExpression[numEquations];
                for (int i = 0; i < numEquations; i++) {
                    String equationInput = JOptionPane.showInputDialog("Enter equation " + (i + 1) + ":");
                    if (equationInput == null) {
                        if (linearEquationsItem != null) {
                            linearEquationsItem.setSelected(false);
                        }
                        return;
                    } else if (!LinearExpression.isValid(equationInput)) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid equation format.", "Invalid Equation", JOptionPane.ERROR_MESSAGE);
                        i--;
                        continue;
                    }
                    equations[i] = new LinearExpression(equationInput);
                    System.out.println(equations[i]);
                }

                SystemOfEquations system = new SystemOfEquations((LinearExpression[]) equations);
                String[] solutions = system.getSolutions();
                StringBuilder solutionText = new StringBuilder();
                solutionText.append("Solutions:\n");
                for (String solution : solutions) {
                    solutionText.append(solution).append("\n");
                }
                JOptionPane.showMessageDialog(null, solutionText.toString(), "Linear Equations Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "This feature is not yet implemented.", "Feature Not Implemented", JOptionPane.WARNING_MESSAGE);
            }
        });
        return menuItem;
    }
}

