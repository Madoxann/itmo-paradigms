package expression.generic;

import java.util.Objects;

public abstract class BinaryOperation<T> implements GenericExpression<T> {
    protected final GenericExpression<T> rightTerm;
    protected final GenericExpression<T> leftTerm;

    protected final Calculator<T> calculator;

    public BinaryOperation(GenericExpression<T> l, GenericExpression<T> r, Calculator<T> calc) {
        rightTerm = r;
        leftTerm = l;
        calculator = calc;
    }

    protected abstract T calc(T left, T right);

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(leftTerm.evaluate(x, y, z), rightTerm.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryOperation<?> that = (BinaryOperation<?>) o;
        return Objects.equals(rightTerm, that.rightTerm) && Objects.equals(leftTerm, that.leftTerm) && Objects.equals(calculator, that.calculator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rightTerm, leftTerm, getClass());
    }
}
