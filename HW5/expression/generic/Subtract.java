package expression.generic;

public class Subtract<T> extends BinaryOperation<T> {
    public Subtract(GenericExpression<T> lTerm, GenericExpression<T> rTerm, Calculator<T> calculator) {
        super(lTerm, rTerm, calculator);
    }

    @Override
    protected T calc(T left, T right) {
        return calculator.subtract(left, right);
    }

    @Override
    public String toString() {
        return "(" + leftTerm + " - " + rightTerm + ")";
    }
}

