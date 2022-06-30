"use strict";
const evaluateBin = (func) => (...args) => (x, y, z) => func(args[0](x, y, z), args[1](x, y, z))

const evaluateUn = (func) => (...args) => (x, y, z) => func(args[0](x, y, z))

const cnst = (expr) => (...args) => expr;

const variable = (expr) => (x, y, z) => {
    switch (expr) {
        case "x" :
            return x
        case "y" :
            return y
        case "z" :
            return z
    }
}

const add = evaluateBin((expr1, expr2) => expr1 + expr2);

const subtract = evaluateBin((expr1, expr2) => expr1 - expr2);

const multiply = evaluateBin((expr1, expr2) => expr1 * expr2);

const divide = evaluateBin((expr1, expr2) => expr1 / expr2);

const negate = evaluateUn((expr) => -expr);

const pi = cnst(Math.PI);

const e = cnst(Math.E);
