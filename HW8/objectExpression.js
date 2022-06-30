"use strict";
let mapArg = {
    "+": [2, (...arg) => new Add(...arg)],
    "-": [2, (...arg) => new Subtract(...arg)],
    "*": [2, (...arg) => new Multiply(...arg)],
    "/": [2, (...arg) => new Divide(...arg)],
    "negate": [1, (...arg) => new Negate(...arg)],
    "min3": [3, (...arg) => new Min3(...arg)],
    "max5": [5, (...arg) => new Max5(...arg)],
    "sinh": [1, (...arg) => new Sinh(...arg)],
    "cosh": [1, (...arg) => new Cosh(...arg)]
}

let priorityMap = {
    "+": 1,
    "-": 1,
    "*": 2,
    "/": 2,
    "negate": 3,
    "min3": 0,
    "max5": 0,
    "(": -1,
    ")": -1,
    "sinh" : 3,
    "cosh" : 3
}

class Const {
    value;

    constructor(val) {
        this.value = val
    }
    toString = () => this.value.toString();
    prefix = () => this.value.toString();
    evaluate = () => parseInt(this.value);
    diff = () => new Const(0);
}

class Variable {
    var;

    constructor(varName) {
        if (varName === 'x' || varName === 'y' || varName === 'z')
            this.var = varName;
        else {
            throw new UnavailableVariableError();
        }
    }
    toString = () => this.var;
    prefix = () => this.var;
    evaluate = (x, y, z) => {
        switch (this.var) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
    }
    diff = (arg) => {
        if (arg === this.var) return new Const(1);
        return new Const(0);
    }
}

class VarargExpression {
    funcSign;
    evalFunc;
    argsFunc;

    constructor(sign, evalF, ...args) {
        this.funcSign = sign;
        this.evalFunc = evalF;
        this.argsFunc = args;
    }
    toString = () => this.argsFunc.map((item) => item.toString()).join(" ") + " " + this.funcSign;
    evaluate = (x, y, z) => this.evalFunc(...this.argsFunc.map((item) => item.evaluate(x, y, z)));
    prefix = () => "(" + this.funcSign + " " + this.argsFunc.map((item) => item.prefix()).join(" ") + ")";
}

class Add extends VarargExpression {
    constructor(leftExpr, rightExpr) {
        super("+", (l, r) => l + r, leftExpr, rightExpr);
    }
    diff = (arg) => new Add(this.argsFunc[0].diff(arg), this.argsFunc[1].diff(arg));
}

class Subtract extends VarargExpression {
    constructor(leftExpr, rightExpr) {
        super("-", (l, r) => l - r, leftExpr, rightExpr);
    }
    diff = (arg) => new Subtract(this.argsFunc[0].diff(arg), this.argsFunc[1].diff(arg));
}

class Multiply extends VarargExpression {
    constructor(leftExpr, rightExpr) {
        super("*", (l, r) => l * r, leftExpr, rightExpr);
    }
    diff = (arg) => new Add(new Multiply(this.argsFunc[0].diff(arg), this.argsFunc[1]), new Multiply(this.argsFunc[0], this.argsFunc[1].diff(arg)));
}

class Divide extends VarargExpression {
    constructor(leftExpr, rightExpr) {
        super("/", (l, r) => l / r, leftExpr, rightExpr);
    }
    diff = (arg) => new Divide(new Subtract(new Multiply(this.argsFunc[0].diff(arg), this.argsFunc[1]), new Multiply(this.argsFunc[0], this.argsFunc[1].diff(arg))), new Multiply(this.argsFunc[1], this.argsFunc[1]));
}

class Negate extends VarargExpression {
    constructor(expr) {
        super("negate", (e) => -e, expr);
    }
    diff = (arg) => new Negate(this.argsFunc[0].diff(arg));
}

class Min3 extends VarargExpression {
    constructor(...args) {
        super("min3", Math.min, ...args);
    }
}

class Max5 extends VarargExpression {
    constructor(...args) {
        super("max5", Math.max, ...args);
    }
}

class Sinh extends VarargExpression {
    constructor(expr) {
        super("sinh", Math.sinh, expr);
    }
    diff = (arg) => new Multiply(this.argsFunc[0].diff(arg), new Cosh(this.argsFunc[0]));
}

class Cosh extends VarargExpression {
    constructor(expr) {
        super("cosh", Math.cosh, expr);
    }
    diff = (arg) => new Multiply(this.argsFunc[0].diff(arg), new Sinh(this.argsFunc[0]));
}

class ParsingError extends Error {
    constructor(msg) {
        super(msg);
    }
}

class IncorrectBalanceError extends ParsingError {
    constructor() {
        super("Incorrect balance");
    }
}

class InsufficientArgumentsError extends ParsingError {
    constructor() {
        super("Insufficient arguments");
    }
}

class UnavailableVariableError extends ParsingError {
    constructor() {
        super("Unavailable variable name");
    }
}

class MissingOperatorError extends ParsingError {
    constructor() {
        super("Missing operator");
    }
}

function parse(expr) {
    let stack = [];
    let expression = expr.split(" ").filter((item) => item.length > 0 ? item : null);
    expression.forEach(function (item) {
        if (item in mapArg) {
            let array = stack.splice(-mapArg[item][0]);
            if (array.length < mapArg[item][0]) {
                throw new InsufficientArgumentsError();
            }
            stack.push(mapArg[item][1](...array));
        } else if (!isNaN(item)) {
            stack.push(new Const(item));
        } else {
            stack.push(new Variable(item));
        }
    });
    if (stack.length !== 1) throw new MissingOperatorError();
    return stack[0];
}

function parsePrefix(exprReverse) {
    let stackOp = [];
    let reverse = "";
    exprReverse = exprReverse.replaceAll("(", " ( ");
    exprReverse = exprReverse.replaceAll(")", " ) ");
    let expression = exprReverse.split(" ").filter((item) => item.length > 0 ? item : null);
    let balance = 0;
    expression.forEach(function (item) {
        if (item in priorityMap) {
            if (item === ")") {
                balance -= 1;
                if (balance < 0) throw new IncorrectBalanceError();
                let pop = stackOp.pop();
                while (pop !== "(") {
                    reverse += pop + " ";
                    pop = stackOp.pop();
                }
            } else if (item === "(") {
                balance += 1;
                stackOp.push(item);
            } else {
                while (priorityMap[stackOp[stackOp.length-1]] >= priorityMap[item]) {
                    reverse += stackOp.pop() + " ";
                }
                stackOp.push(item);
            }
        } else {
            if (stackOp[stackOp.length - 1] === "(") {
                throw new MissingOperatorError();
            }
            reverse += item + " ";
        }
    });
    if (balance !== 0) throw new IncorrectBalanceError();
    while (stackOp.length > 0) {
        let exprPop = stackOp.pop();
        if (exprPop !== "(") {
            reverse += exprPop + " ";
        }
    }
    return parse(reverse);
}
