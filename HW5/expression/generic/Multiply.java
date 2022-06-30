package expression.generic;

public class Multiply<T> extends BinaryOperation<T> {
    public Multiply(GenericExpression<T> lTerm, GenericExpression<T> rTerm, Calculator<T> calculator) {
        super(lTerm, rTerm, calculator);
    }

    @Override
    protected T calc(T left, T right) {
        return calculator.multiply(left, right);
    }

    @Override
    public String toString() {
        return "(" + leftTerm + " * " + rightTerm + ")";
    }
}
