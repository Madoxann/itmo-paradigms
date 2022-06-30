package expression.generic;

public class Add<T> extends BinaryOperation<T> {
    public Add(GenericExpression<T> lTerm, GenericExpression<T> rTerm, Calculator<T> calculator) {
        super(lTerm, rTerm, calculator);
    }

    @Override
    protected T calc(T left, T right) {
        return calculator.add(left, right);
    }

    @Override
    public String toString() {
        return "(" + leftTerm + " + " + rightTerm + ")";
    }
}
