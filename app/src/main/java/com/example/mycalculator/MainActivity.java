package com.example.mycalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // TextView to display the calculation and result.
    private TextView tvInput;
    // True if the last input was a number, false otherwise.
    private boolean lastNumeric;
    // True if the calculator is in an error state.
    private boolean stateError;
    // True if a decimal point has been used in the current number.
    private boolean lastDot;
    // True if an equal sign was just pressed.
    private boolean lastEqual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInput = findViewById(R.id.textView_result);
    }

    /**
     * Handles clicks on digit buttons (0-9).
     */
    public void onDigit(View view) {
        // If the last action was pressing equals, start a new calculation.
        if (lastEqual) {
            tvInput.setText("");
            lastEqual = false;
        }
        // If there was an error, clear it and start fresh.
        if (stateError) {
            tvInput.setText(((Button) view).getText());
            stateError = false;
        } else {
            // Otherwise, just append the new digit.
            tvInput.append(((Button) view).getText());
        }
        // The last input is now a number.
        lastNumeric = true;
    }

    /**
     * Handles clicks on the decimal point button.
     */
    public void onDecimalPoint(View view) {
        // Can only add a decimal if the last input was a number and a decimal hasn't been added yet.
        if (lastNumeric && !stateError && !lastDot) {
            tvInput.append(".");
            lastNumeric = false;
            lastDot = true;
        }
    }

    /**
     * Handles clicks on operator buttons (+, -, *, /).
     */
    public void onOperator(View view) {
        // Can only add an operator if the last input was a number and there's no error.
        if (lastNumeric && !stateError) {
            tvInput.append(((Button) view).getText());
            lastNumeric = false;
            lastDot = false;    // Reset the dot flag for the next number.
            lastEqual = false;  // An operator continues the expression.
        }
    }

    /**
     * Handles clicks on the "Clear" button.
     */
    public void onClear(View view) {
        // Reset all state flags.
        this.tvInput.setText("");
        lastNumeric = false;
        stateError = false;
        lastDot = false;
        lastEqual = false;
    }

    /**
     * Handles clicks on the "Equal" button to evaluate the expression.
     */
    public void onEqual(View view) {
        // Can only evaluate if the last input was a number and there's no error.
        if (lastNumeric && !stateError) {
            String txt = tvInput.getText().toString();
            try {
                // Use exp4j library to build and evaluate the expression.
                Expression expression = new ExpressionBuilder(txt).build();
                double result = expression.evaluate();

                // Format the result to avoid scientific notation and trailing zeros.
                DecimalFormat df = new DecimalFormat("#.##########");
                String resultStr = df.format(result);

                tvInput.setText(resultStr);
                // The result might have a decimal, so check for it.
                lastDot = resultStr.contains(".");
                lastEqual = true;
            } catch (Exception ex) {
                // If the expression is invalid (e.g., division by zero), show an error.
                tvInput.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}

