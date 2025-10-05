package com.example.mycalculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public final class MathEval {
    private MathEval() {}

    public static String eval(String expr) {
        try {
            Expression e = new ExpressionBuilder(expr).build();
            double result = e.evaluate();
            // strip trailing .0 for integers
            if (Math.rint(result) == result) {
                return String.valueOf((long) result);
            }
            return String.valueOf(result);
        } catch (Exception ex) {
            return "Error";
        }
    }
}
