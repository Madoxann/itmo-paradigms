package expression.parser;

import expression.generic.*;

public class ExpressionParser<T> {
    public GenericExpression<T> parse(String expression, Calculator<T> calculator) {
        return new TripleParser<T>(new StringSource(expression), calculator).parseExpression();
    }

    private static class TripleParser<T> extends BaseParser {
        Calculator<T> calculator;

        protected TripleParser(CharSource source, Calculator<T> calculator) {
            super(source);
            this.calculator = calculator;
        }

        private GenericExpression<T> parseFirst() {
            GenericExpression<T> currExpr = null;
            skipWS();
            if (take('(')) {
                currExpr = parseExpression();
                take(')');
            }
            if (between('0', '9')) {
                currExpr = parseConst(0);
            }
            if (between('x', 'z')) {
                currExpr = parseVar(0);
            }
            if (currExpr == null) {
                if (take('-')) {
                    currExpr = parseMinus();
                }
                if (take('c') && take('o') && take('u') && take('n') && take('t') ) {
                    currExpr = parseCount();
                }
            }
            return currExpr;
        }

        private GenericExpression<T> parseSecond(GenericExpression<T> currExpr) {
            while (true) {
                skipWS();
                if (take('(')) {
                    currExpr = parseExpression();
                    take(')');
                    continue;
                }
                if (currExpr == null) {
                    currExpr = parseFirst();
                    continue;
                }
                if (take('*')) {
                    currExpr = parseMultiply(currExpr);
                    continue;
                }
                if (take('/')) {
                    currExpr = parseDivide(currExpr);
                    continue;
                }
                return currExpr;
            }
        }

        private GenericExpression<T> parseThird(GenericExpression<T> currExpr) {
            while (true) {
                skipWS();
                if (take('(')) {
                    currExpr = parseExpression();
                    take(')');
                    continue;
                }
                if (currExpr == null) {
                    currExpr = parseFirst();
                    continue;
                }
                if (take('+')) {
                    currExpr = parseAdd(currExpr);
                    continue;
                }
                if (take('-')) {
                    currExpr = parseSubtraction(currExpr);
                    continue;
                }
                if (take('m')) {
                    if (take('i') && take('n')) {
                        currExpr = parseMin(currExpr);
                    }
                    if (take('a') && take('x')) {
                        currExpr = parseMax(currExpr);
                    }
                }
                return currExpr;
            }
        }

        private GenericExpression<T> parseExpression() {
            skipWS();
            GenericExpression<T> expression;
            expression = parseFirst();
            expression = parseSecond(expression);
            expression = parseThird(expression);
            take(BaseParser.END);
            return expression;
        }

        private GenericExpression<T> parseConst(int multiplier) {
            skipWS();
            final StringBuilder sb = new StringBuilder();
            takeInteger(sb);
            try {
                return new Const<>(calculator.parseConst(Math.pow(-1, multiplier) < 0 ? "-" + sb : sb.toString()));
            } catch (final NumberFormatException e) {
                throw error("Invalid number " + sb);
            }
        }

        private GenericExpression<T> parseVar(int multiplier) {
            skipWS();
            char varName = take();
            return Math.pow(-1, multiplier) < 0 ? new Negate<>(new Variable<>(String.valueOf(varName)), calculator) : new Variable<>(String.valueOf(varName));
        }

        private GenericExpression<T> parseMinus() {
            skipWS();
            int multiplier = 1;
            if (take('-')) {
                return new Negate<>(parseMinus(), calculator);
            }
            if (between('0', '9')) {
                return parseConst(multiplier);
            }
            if (between('x', 'z')) {
                return parseVar(multiplier);
            }

            return new Negate<>(parseFirst(), calculator);
        }

        private GenericExpression<T> parseCount() {
            skipWS();
            return new Count<>(parseFirst(), calculator);
        }

        private GenericExpression<T> parseAdd(GenericExpression<T> expression) {
            skipWS();
            GenericExpression<T> getExpression = parseSecond(null);
            return new Add<>(expression, getExpression, calculator);
        }

        private GenericExpression<T> parseSubtraction(GenericExpression<T> expression) {
            skipWS();
            GenericExpression<T> getExpression = parseSecond(null);
            return new Subtract<>(expression, getExpression, calculator);
        }

        private GenericExpression<T> parseMultiply(GenericExpression<T> expression) {
            skipWS();
            GenericExpression<T> getExpression = parseFirst();
            return new Multiply<>(expression, getExpression, calculator);
        }

        private GenericExpression<T> parseDivide(GenericExpression<T> expression) {
            skipWS();
            GenericExpression<T> getExpression = parseFirst();
            return new Divide<>(expression, getExpression, calculator);
        }

        private GenericExpression<T> parseMin(GenericExpression<T> expression) {
            skipWS();
            GenericExpression<T> getExpression = parseFirst();
            return new Min<>(expression, getExpression, calculator);
        }

        private GenericExpression<T> parseMax(GenericExpression<T> expression) {
            skipWS();
            GenericExpression<T> getExpression = parseFirst();
            return new Max<>(expression, getExpression, calculator);
        }

        private void takeDigits(final StringBuilder sb) {
            skipWS();
            while (between('0', '9')) {
                sb.append(take());
            }
        }

        private void takeInteger(final StringBuilder sb) {
            skipWS();
            if (take('0')) {
                sb.append('0');
            } else if (between('1', '9')) {
                takeDigits(sb);
            } else {
                throw error("Invalid number");
            }
        }

    }
}