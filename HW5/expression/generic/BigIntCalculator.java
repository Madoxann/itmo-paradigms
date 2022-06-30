package expression.generic;

import java.math.BigInteger;

public class BigIntCalculator implements Calculator<BigInteger> {

    @Override
    public BigInteger parseConst(String str) {
        return BigInteger.valueOf(Long.parseLong(str));
    }

    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger negate(BigInteger negExpr) {
        return negExpr.negate();
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger count(BigInteger cntExpr) {
        return BigInteger.valueOf(cntExpr.bitCount());
    }

    @Override
    public BigInteger max(BigInteger left, BigInteger right) {
        return left.compareTo(right) > 0 ? left : right;
    }

    @Override
    public BigInteger min(BigInteger left, BigInteger right) {
        return left.compareTo(right) < 0 ? left : right;
    }
}
