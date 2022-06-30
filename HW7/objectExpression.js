"use strict";
let mapArg = {
    "+": [2, (...arg) => new Add(...arg)],
    "-": [2, (...arg) => new Subtract(...arg)],
    "*": [2, (...arg) => new Multiply(...arg)],
    "/": [2, (...arg) => new Divide(...arg)],
    "negate": [1, (...arg) => new Negate(...arg)],
    "min3" : [3, (...arg) => new Min3(...arg)],
    "max5" : [5, (...arg) => new Max5(...arg)]
}

function Const(value) {
    this.toString = () => value.toString();
    this.evaluate = () => parseInt(value);
    this.diff = () => new Const(0);
}

function Variable(varName) {
    this.toString = () => varName;
    this.evaluate = (x, y, z) => {
        switch (varName) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
    }
    this.diff = (arg) => {
        if (arg === varName) return new Const(1);
        return new Const(0);
    }
}

function VarargExpression(funcSign, evalFunc, ...args) {
    this.args = args;
    this.funcSign = funcSign;
    this.evalFunc = evalFunc;
}
VarargExpression.prototype.toString = function () {
    return this.args.map((item) => item.toString()).join(" ") + " " + this.funcSign;
};
VarargExpression.prototype.evaluate = function (x, y, z) {
    return this.evalFunc(...this.args.map((item) => item.evaluate(x, y, z)));
}

function Add(leftExpr, rightExpr) {
    VarargExpression.call(this, "+", (l, r) => l + r, leftExpr, rightExpr);
    this.diff = (arg) => new Add(leftExpr.diff(arg), rightExpr.diff(arg));
}
Add.prototype = Object.create(VarargExpression.prototype);

function Subtract(leftExpr, rightExpr) {
    VarargExpression.call(this, "-", (l, r) => l - r, leftExpr, rightExpr);
    this.diff = (arg) => new Subtract(leftExpr.diff(arg), rightExpr.diff(arg));
}
Subtract.prototype = Object.create(VarargExpression.prototype);

function Multiply(leftExpr, rightExpr) {
    VarargExpression.call(this, "*", (l, r) => l * r, leftExpr, rightExpr);
    this.diff = (arg) => new Add(new Multiply(leftExpr.diff(arg), rightExpr), new Multiply(leftExpr, rightExpr.diff(arg)));
}
Multiply.prototype = Object.create(VarargExpression.prototype);

function Divide(leftExpr, rightExpr) {
    VarargExpression.call(this, "/", (l, r) => l / r, leftExpr, rightExpr);
    this.diff = (arg) => new Divide(new Subtract(new Multiply(leftExpr.diff(arg), rightExpr), new Multiply(leftExpr, rightExpr.diff(arg))), new Multiply(rightExpr, rightExpr));
}
Divide.prototype = Object.create(VarargExpression.prototype);

function Negate(expr) {
    VarargExpression.call(this, "negate", (e) => -e, expr);
    this.diff = (arg) => new Negate(expr.diff(arg));
}
Negate.prototype = Object.create(VarargExpression.prototype);

function Min3(...args) {
    VarargExpression.call(this, "min3", Math.min, ...args);
}
Min3.prototype = Object.create(VarargExpression.prototype);

function Max5(...args) {
    VarargExpression.call(this, "max5", Math.max, ...args);
}
Max5.prototype = Object.create(VarargExpression.prototype)

function parse(expr) {
    let expression = expr.split(" ");
    let stack = [];
    expression = expression.filter((item) => item.length > 0 ? item : null);
    expression.forEach(function (item) {
        if (item in mapArg) {
            let array = stack.splice(-mapArg[item][0]);
            stack.push(mapArg[item][1](...array));
        } else if (!isNaN(item)) {
            stack.push(new Const(item));
        } else {
            stack.push(new Variable(item));
        }
    });
    return stack[0];
}
