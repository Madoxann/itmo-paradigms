package expression.generic;

import expression.exceptions.OverflowException;
import expression.parser.ExpressionParser;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Calculator<?> calc;

        switch (mode) {
            case ("i") -> calc = new IntCalculator();
            case ("d") -> calc = new DoubleCalculator();
            default -> calc = new BigIntCalculator();
        }
        return getGenVal(expression, x1, x2, y1, y2, z1, z2, calc);
    }

    public <T> Object[][][] getGenVal(String expression, int x1, int x2, int y1, int y2, int z1, int z2, Calculator<T> calc) {
        ExpressionParser<T> parser = new ExpressionParser<>();

        Object[][][] table = new Object[x2-x1+1][y2-y1+1][z2-z1+1];
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        table[i][j][k] = parser.parse(expression, calc).evaluate(calc.parseConst(String.valueOf(x1 + i)), calc.parseConst(String.valueOf(y1 + j)), calc.parseConst(String.valueOf(z1 + k)));
                    } catch (Exception ignore){}
                    //TODO: write something better
                }
            }
        }
        return table;
    }
}
