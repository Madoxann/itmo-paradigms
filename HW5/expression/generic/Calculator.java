package expression.generic;

public interface Calculator <T> {
    T parseConst(String str);
    T add(T left, T right);
    T negate(T negExpr);
    T subtract(T left, T right);
    T multiply(T left, T right);
    T divide(T left, T right);
    T count(T cntExpr);
    T max(T left, T right);
    T min(T left, T right);
}
