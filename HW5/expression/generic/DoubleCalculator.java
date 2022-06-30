package expression.generic;

public class DoubleCalculator implements Calculator<Double>{

    @Override
    public Double parseConst(String str) {
        return Double.parseDouble(str);
    }

    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double negate(Double negExpr) {
        return -negExpr;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double count(Double cntExpr) {
        return (double) Long.bitCount(Double.doubleToLongBits(cntExpr));
    }

    @Override
    public Double max(Double left, Double right) {
        return Double.max(left, right);
    }

    @Override
    public Double min(Double left, Double right) {
        return Double.min(left, right);
    }
}
