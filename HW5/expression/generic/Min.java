package expression.generic;

public class Min<T> extends BinaryOperation<T> {
    public Min(GenericExpression<T> lTerm, GenericExpression<T> rTerm, Calculator<T> calculator) {
        super(lTerm, rTerm, calculator);
    }

    @Override
    protected T calc(T left, T right) {
        return calculator.min(left, right);
    }

    @Override
    public String toString() {
        return "(" + leftTerm + " min " + rightTerm + ")";
    }
}
