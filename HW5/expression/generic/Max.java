package expression.generic;

public class Max<T> extends BinaryOperation<T> {
    public Max(GenericExpression<T> lTerm, GenericExpression<T> rTerm, Calculator<T> calculator) {
        super(lTerm, rTerm, calculator);
    }

    @Override
    protected T calc(T left, T right) {
        return calculator.max(left, right);
    }

    @Override
    public String toString() {
        return "(" + leftTerm + " max " + rightTerm + ")";
    }
}