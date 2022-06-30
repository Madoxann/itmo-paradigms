package expression.generic;

public class Count<T> extends UnaryOperation<T> {
    public Count(GenericExpression<T> term, Calculator<T> calculator) {
        super(term, calculator);
    }

    @Override
    protected T calc(T term) {
        return calculator.count(term);
    }

    @Override
    public String toString() {
        return "count(" + expression + ")";
    }
}

