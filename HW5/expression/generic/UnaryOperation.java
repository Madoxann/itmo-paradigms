package expression.generic;

import java.util.Objects;

public abstract class UnaryOperation<T> implements GenericExpression<T> {
    protected final GenericExpression<T> expression;
    protected final Calculator<T> calculator;

    public UnaryOperation(GenericExpression<T> expr, Calculator<T> calc) {
        expression = expr;
        calculator = calc;
    }

    protected abstract T calc(T term);

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(expression.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryOperation<?> that = (UnaryOperation<?>) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
