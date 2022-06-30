package expression.generic;

public class Divide<T> extends BinaryOperation<T> {
    public Divide(GenericExpression<T> lTerm, GenericExpression<T> rTerm, Calculator<T> calculator) {
        super(lTerm, rTerm, calculator);
    }

    @Override
    protected T calc(T left, T right) {
        return calculator.divide(left, right);
    }

    @Override
    public String toString() {
        return "(" + leftTerm + " / " + rightTerm + ")";
    }
}

