package expression.generic;

public class Negate<T> extends UnaryOperation<T> {
    public Negate(GenericExpression<T> term, Calculator<T> calculator) {
        super(term, calculator);
    }

    @Override
    protected T calc(T term) {
        return calculator.negate(term);
    }

    @Override
    public String toString() {
        return "-(" + expression + ")";
    }
}

