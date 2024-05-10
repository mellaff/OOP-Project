package equationSolver.core.expression;

/**
 * An abstract class representing mathematical expressions.
 */
public abstract class Expression implements Cloneable {
    private final String expression;

    /**
     * Constructs an Expression object with the given expression string.
     * @param expression The expression string.
     */
    public Expression(String expression) {
        this.expression = expression;
    }

    /**
     * Gets the expression string.
     * @return The expression string.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Returns the expression string representation.
     * @return The expression string representation.
     */
    public String toString() {
        return expression;
    }

    /**
     * Creates a clone of this Expression object.
     * @return A clone of this Expression object.
     */
    public Expression clone() {
        try {
            return (Expression) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Checks if the given object is equal to this Expression object.
     * @param obj The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Expression givenExpression = (Expression) obj;
        return getExpression().equals(givenExpression.getExpression());
    }

    /**
     * Gets the coefficient at the specified index in the expression.
     * @param index The index of the coefficient.
     * @return The coefficient.
     */
    protected double getCoefficientAtIndex(int index) {
        if (index == 0) {
            return 1;
        }
        int start = index - 1;
        while (start >= 0 && (Character.isDigit(expression.charAt(start)) || expression.charAt(start) == '.')) {
            start--;
        }
        if (start == index - 1) {
            if (expression.charAt(start) == '-') {
                return -1;
            }
            return 1;
        }
        double result = Double.parseDouble(expression.substring(start + 1, index));
        if (start >= 0 && expression.charAt(start) == '-') {
            return -result;
        }
        return result;
    }

    /**
     * Gets the zero-degree coefficient information at the specified index in the expression.
     * @param index The index of the zero-degree coefficient.
     * @return An array containing the zero-degree coefficient and the end index of the coefficient in the expression.
     */
    protected double[] zeroDegreeCoefficient(int index) {
        double[] res = new double[2];
        if ((index == 0 || expression.charAt(index - 1) != '^')) {
            int end = index;
            while (end < expression.length() && (Character.isDigit(expression.charAt(end)) || expression.charAt(end) == '.')) {
                end++;
            }
            res[1] = end;
            boolean isPositive = index == 0 || expression.charAt(index - 1) == '+' || expression.charAt(index - 1) == '=';
            if (end == expression.length() || !Character.isAlphabetic(expression.charAt(end))) {
                double result = Double.parseDouble(expression.substring(index, end));
                if (isPositive) {
                    res[0] = result;
                } else {
                    res[0] = -result;
                }
            }
        }
        return res;
    }
}
