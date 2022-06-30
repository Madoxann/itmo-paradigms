package expression.generic;

import java.util.Objects;

public class Const<T> implements GenericExpression<T> {
    protected T value;

    public Const(T val) {
        value = val;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const<?> aConst = (Const<?>) o;
        return value == aConst.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
