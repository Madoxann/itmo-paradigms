package expression.generic;

import expression.exceptions.OverflowException;

public class IntCalculator implements Calculator<Integer> {
    public Integer parseConst(String str) {
        return Integer.parseInt(str);
    }

    @Override
    public Integer add(Integer left, Integer right) {
        if ((left > 0 && Integer.MAX_VALUE - left < right) ||
                (left < 0 && Integer.MIN_VALUE - left > right)) {
            throw new OverflowException();
        }
        return left + right;
    }

    @Override
    public Integer negate(Integer negExpr) {
        if (negExpr == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
        return -negExpr;
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        if ((left >= 0 && right < 0 && left - Integer.MAX_VALUE > right) ||
                (left <= 0 && right > 0 && Integer.MIN_VALUE - left > -right)) {
            throw new OverflowException();
        }
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        // conditions MUST be in this order, otherwise speculative execution will fail tests
        if (right > 0 && left > 0 && Integer.MAX_VALUE / left <  right) throw new OverflowException();
        if (right < 0 && left < 0 && Integer.MAX_VALUE / left >  right) throw new OverflowException();
        if (right > 0 && left < 0 && Integer.MIN_VALUE /  right > left) throw new OverflowException();
        if (right < 0 && left > 0 && Integer.MIN_VALUE / left >  right) throw new OverflowException();
        return left * right;
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException();
        }
        return left / right;
    }

    @Override
    public Integer count(Integer cntExpr) {
        return Integer.bitCount(cntExpr);
    }

    @Override
    public Integer max(Integer left, Integer right) {
        return left < right ? right : left;
    }

    @Override
    public Integer min(Integer left, Integer right) {
        return left > right ? right : left;
    }
}
