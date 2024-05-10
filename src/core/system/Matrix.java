package core.system;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a matrix and provides methods for matrix operations.
 * <p>
 * This class implements the Cloneable interface to support cloning of matrix objects.
 */
public class Matrix implements Cloneable{
    private double[][] coefficients;
    private Matrix rref;
    /**
     * Constructs a Matrix object with the specified matrix.
     *
     * @param matrix The 2D array representing the matrix.
     */
    public Matrix(double[][] matrix)
    {
        coefficients = matrix.clone();
    }
    /**
     * Retrieves the coefficient at the specified row and column of the matrix.
     *
     * @param row    The row index.
     * @param column The column index.
     * @return The coefficient at the specified position.
     */
    public double getCoefficient(int row, int column){
        return coefficients[row][column];
    }
    /**
     * Retrieves a copy of the specified row of the matrix.
     *
     * @param row The index of the row to retrieve.
     * @return A copy of the specified row.
     */
    public double[] getRow(int row){
        return coefficients[row].clone();
    }
    /**
     * Sets the coefficient at the specified row and column of the matrix.
     *
     * @param row    The row index.
     * @param column The column index.
     * @param value  The value to set.
     */
    public void setCoefficient(int row, int column, double value){
        coefficients[row][column] = value;
    }
    /**
     * Retrieves the number of rows in the matrix.
     *
     * @return The number of rows.
     */
    public int getNumberOfRows(){
        return coefficients.length;
    }
    /**
     * Retrieves the number of columns in the matrix.
     *
     * @return The number of columns.
     */
    public int getNumberOfColumns(){
        return coefficients[0].length;
    }
    /**
     * Gets the reduced row echelon form (RREF) of the matrix.
     *
     * @return The RREF of the matrix.
     */
    public Matrix getRREF(){
        if (rref == null){
            rref = reduceToRREF();
        }
        return rref.clone();
    }
    /**
     * Returns a string representation of the matrix.
     *
     * @return A string representation of the matrix.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getNumberOfRows(); i++) {
            stringBuilder.append(Arrays.toString(coefficients[i]));
            if (i == getNumberOfRows() - 1){
                break;
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    /**
     * Creates a copy of the Matrix object.
     *
     * @return A clone of the Matrix object.
     */
    @Override
    public Matrix clone(){
        try {
            Matrix clone = (Matrix) super.clone();
            clone.coefficients = this.coefficients;
            return clone;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
    /**
     * Swaps two rows of the matrix.
     *
     * @param i The index of the first row.
     * @param j The index of the second row.
     */
    public void swap(int i, int j){
        if (i == j){
            return;
        }
        double[] temp = getRow(i);
        coefficients[i] = getRow(j);
        coefficients[j] = temp;
    }
    /**
     * Scales a row of the matrix by a scalar value.
     *
     * @param row    The index of the row to scale.
     * @param scalar The scalar value.
     */
    public void scale(int row, double scalar){
        for (int i = 0; i < getNumberOfColumns(); i++) {
            setCoefficient(row, i, getCoefficient(row, i) * scalar);
        }
    }
    /**
     * Reduces the matrix to its reduced row echelon form (RREF) using Gaussian elimination.
     *
     * @return The matrix in reduced row echelon form.
     */
    private Matrix reduceToRREF() {
        Matrix matrix = this.clone();
        int lead = 0;
        int rowCount = matrix.getNumberOfRows();
        int colCount = matrix.getNumberOfColumns();
        for (int r = 0; r < rowCount; r++) {
            if (colCount <= lead) {
                break;
            }
            int i = r;
            while (matrix.getCoefficient(i, lead) == 0) {
                i++;
                if (i == rowCount) {
                    i = r;
                    lead++;
                    if (colCount == lead) {
                        lead--;
                        break;
                    }
                }
            }
            matrix.swap(i, r);
            if (matrix.getCoefficient(r,lead) != 0) {
                matrix.scale(r, 1.0 / matrix.coefficients[r][lead]);
            }
            for (i = 0; i < rowCount; i++) {
                if (i != r) {
                    double leadFactor = matrix.coefficients[i][lead];
                    for (int j = 0; j < colCount; j++) {
                        matrix.coefficients[i][j] -= leadFactor * matrix.coefficients[r][j];
                    }
                }
            }
            lead++;
        }
        return matrix;
    }
    /**
     * Checks if the system of equations represented by the matrix is inconsistent.
     *
     * @return {@code true} if the system is inconsistent, {@code false} otherwise.
     */
    public boolean isInconsistent(){
        Matrix rref = getRREF();
        int rowCount = rref.getNumberOfRows();
        int colCount = rref.getNumberOfColumns()-1;
        for (int i = 0; i < rowCount; i++) {
            boolean isAllZero = true;
            for (int j = 0; j < colCount; j++) {
                if (rref.getCoefficient(i, j) != 0) {
                    isAllZero = false;
                    break;
                }
            }
            if (isAllZero && rref.getCoefficient(i, colCount) != 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * Determines the indices of free variables in the solution of the system of equations represented by the matrix.
     *
     * @return An array indicating whether each variable is free or not.
     */
    private boolean[] freeVariableIndexes(){
        Matrix rref = getRREF();
        int rowCount = rref.getNumberOfRows();
        int colCount = rref.getNumberOfColumns() - 1;
        boolean[] isFreeVariable = new boolean[colCount];
        Arrays.fill(isFreeVariable, true);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if (rref.getCoefficient(i, j) == 1) {
                    isFreeVariable[j] = false;
                    break;
                }
            }
        }
        return isFreeVariable;
    }
    /**
     * Checks if the variable at the specified column index is a free variable in the solution.
     *
     * @param column The column index representing the variable.
     * @return {@code true} if the variable is free, {@code false} otherwise.
     */
    public boolean isFreeVariable(int column){
        return freeVariableIndexes()[column];
    }
    /**
     * Checks if the variable at the specified column index is a pivot variable in the solution.
     *
     * @param column The column index representing the variable.
     * @return {@code true} if the variable is a pivot variable, {@code false} otherwise.
     */
    public boolean isPivotVariable(int column){
        return !isFreeVariable(column);
    }
    /**
     * Solves the system of linear equations represented by the matrix.
     *
     * @param listOfVariables A list of variables used in the equations.
     * @return An array of strings representing the solutions to the equations.
     */
    public String[] solve(ArrayList<Character> listOfVariables) {
        String[] solutions = new String[listOfVariables.size()];
        if(isInconsistent()){
            return solutions;
        }
        Matrix rref = getRREF();
        for (int j = 0; j < solutions.length; j++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(listOfVariables.get(j));
            if (isFreeVariable(j)) {
                stringBuilder.append(" is a free variable");
                solutions[j] = stringBuilder.toString();
                stringBuilder.setLength(0);
            }
            else {
                stringBuilder.append(" = ");
                for (int i = 0; i < rref.getNumberOfRows(); i++) {
                    if(rref.getCoefficient(i, j) == 1){
                        if (rref.getCoefficient(i, solutions.length) != 0){
                            stringBuilder.append(rref.getCoefficient(i, solutions.length));
                        }
                        for (int k = j+1; k < solutions.length; k++) {
                            double currentCoefficient = -rref.getCoefficient(i, k);
                            if (currentCoefficient > 0){
                                stringBuilder.append("+");
                            }
                            if (currentCoefficient != 0){
                                stringBuilder.append(currentCoefficient);
                                stringBuilder.append(listOfVariables.get(k));
                            }
                        }
                        if (stringBuilder.toString().equals(listOfVariables.get(i) + " = ")){
                            stringBuilder.append(0.0);
                        }
                        solutions[j] = stringBuilder.toString();
                        stringBuilder.setLength(0);
                        break;
                    }
                }
            }
        }
        return solutions;
    }
}

