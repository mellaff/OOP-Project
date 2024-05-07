import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class LinearExpression extends Expression{
    public LinearExpression(String expression){
        super(expression);
    }
    public HashMap<Character, Double> coefficientsOfLinearExpression(){
        HashMap<Character, Double> map = new HashMap<>();
        boolean otherSide = false;
        if (getExpression().isEmpty()){
            return map;
        }
        int i = 0;
        while (i<getExpression().length()){
            char currentChar = getExpression().charAt(i);
            if (currentChar == '='){
                otherSide = true;
            }
            if (Character.isAlphabetic(currentChar)){
                double value = getCoefficientAtIndex(i);
                if (otherSide){
                    value = -value;
                }
                if (map.containsKey(currentChar)) {
                    double currentValue = map.get(currentChar);
                    map.put(currentChar, currentValue + value);
                } else {
                    map.put(currentChar, value);
                }
            }
            else if (Character.isDigit(currentChar)){
                double[] zeroCoefficientAndEndIndex = zeroDegreeCoefficient(i);
                double value = zeroCoefficientAndEndIndex[0];
                if (otherSide){
                    value = -value;
                }
                if (map.containsKey('\0')) {
                    map.put('\0', map.get('\0') + value);
                } else {
                    map.put('\0', value);
                }
                i = (int)zeroCoefficientAndEndIndex[1];
                continue;
            }
            i++;
        }
        if (!map.containsKey('\0')){
            map.put('\0', 0.0);
            return map;
        }
        if (map.get('\0') != 0){
            map.put('\0', -map.get('\0'));
        }
        return map;
    }
    public static Expression coefficientsToLinearExpression(HashMap<Character, Double> coefficients){
        StringBuilder stringBuilder = new StringBuilder();
        Set<Character> set = coefficients.keySet();
        boolean first = true;
        for (Character element : set) {
            if (element == '\0'){
                continue;
            }
            double currentCoefficient = coefficients.get(element);
            if (currentCoefficient > 0 && !first){
                stringBuilder.append("+");
            }
            else if (currentCoefficient < 0 && (first || currentCoefficient == -1)){
                stringBuilder.append("-");
            }
            if (Math.abs(currentCoefficient) != 1){
                stringBuilder.append(currentCoefficient);
            }
            stringBuilder.append(element);
            first = false;
        }
        stringBuilder.append("=");
        stringBuilder.append(coefficients.get('\0'));
        return new LinearExpression(stringBuilder.toString());
    }

    public boolean isValid() {
        String pattern = "^([+-]?(\\d*[a-zA-Z]|\\d+))([+-](\\d*[a-zA-Z]|\\d+))*(=[+-]?(\\d*[a-zA-Z]|\\d+))([+-](\\d*[a-zA-Z]|\\d+))*$";
        return Pattern.matches(pattern, getExpression());
    }
}
