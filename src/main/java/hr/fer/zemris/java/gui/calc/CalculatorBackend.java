package hr.fer.zemris.java.gui.calc;

import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * This class is used to provide methods for operating with {@link Calculator}.
 *
 * @author Boris
 * @version 1.0
 */
public class CalculatorBackend {

    /** Instance of calculator window. */
    private Calculator calc;
    /** Used for inputing numbers. */
    private StringBuilder buffer;
    /** Used for operations push/pop. */
    private Stack<Double> stack;
    /** Used to perform binary operations. */
    private DoubleBinaryOperator operator;
    /** Value that is stored in the calculator. */
    private double storedValue;
    /** It is set to true if last button pressed was a binary operator. */
    private boolean binaryOperation;
    /** It is true if the last button pressed was a unary operator. */
    private boolean unaryOperation;
    /** It is true if the last button pressed was equals button. */
    private boolean equalsPressed;
    /** Display of the calulator. */
    private JLabel display;
    /** Checkbox used to invert operations. */
    private JCheckBox box;

    /**
     * Creates a new {@link CalculatorBackend} that operates with labela and box.
     *
     * @param calc calculator window
     * @param display label used for result showing
     * @param box checkbox
     */
    public CalculatorBackend(Calculator calc, JLabel display, JCheckBox box) {

        this.calc = calc;
        this.display = display;
        this.box = box;

        buffer = new StringBuilder();
        stack = new Stack<>();
    }

    /**
     * If the buffer is empty, return the last stored value. Otherwise, return the value of the
     * buffer.
     *
     * @return current value in the calculator
     */
    public Double getCurrentValue() {
        double result;
        if (buffer.length() != 0) {
            result = Double.parseDouble(buffer.toString());
        } else {
            result = storedValue;
        }
        return result;
    }

    /**
     * Sets the buffer to the newValue. Used when performing functions.
     *
     * @param newValue value to set
     */
    public void setNewValue(double newValue) {
        buffer = new StringBuilder(String.valueOf(newValue));
    }

    /**
     * Puts the given value at the end of the buffer.
     *
     * @param value string to append to the buffer
     */
    public void addToBuffer(String value) {

        if (equalsPressed) {
            buffer.setLength(0);
            equalsPressed = false;
        } else if (binaryOperation) {
            buffer.setLength(0);
            binaryOperation = false;
        } else if (unaryOperation) {
            buffer.setLength(0);
            unaryOperation = false;
        }

        if (value.equals(".")) {
            if (!buffer.toString().contains(".")) {
                buffer.append(".");
            }
        } else {
            buffer.append(value);
        }
    }

    /**
     * Changes the text on the label.
     */
    public void updateLabel(Double value) {
        double newValue = (value == null) ? getCurrentValue() : value;
        String valueToSet = String.valueOf(newValue);
        SwingUtilities.invokeLater(
            () -> display.setText("<html><h1>" + valueToSet + "&nbsp</h1></html>"));
    }

    /**
     * When equals button is pressed method calculates result based on previously given operator.
     */
    public void equalsPressed() {
        if (!isEqualsPressed()) {
            double a = getStoredValue();
            double b = getCurrentValue();

            double result = getOperator().applyAsDouble(a, b);

            setStoredValue(0);
            setNewValue(result);

            setEqualsPressed(true);
            setBinaryOperation(false);
            setUnaryOperation(false);
        }
    }

    /**
     * Method changes the sign of currently displayed number.
     */
    public void changeSign() {
        double value = getCurrentValue();
        value *= (value == 0) ? 1 : -1;
        setNewValue(value);
    }

    /**
     * Restarts this {@link CalculatorBackend}, puts it in the initial state.
     */
    public void restart() {
        buffer = new StringBuilder();
        stack = new Stack<>();
        storedValue = 0;
        operator = (a, b) -> a + b;
        binaryOperation = false;
        unaryOperation = false;
        equalsPressed = false;

        if (display != null) {
            updateLabel(null);
        }
        if (box != null) {
            if (box.isSelected()) {
                box.doClick();
            }
        }
    }

    /**
     * Clears the buffer.
     */
    public void clear() {
        buffer.setLength(0);
    }

    /**
     * Pushes the current number onto the stack.
     */
    public void push() {
        stack.push(getCurrentValue());
    }

    /**
     * Pops last element that was pushed onto the stack.
     */
    public void pop() {
        if (stack.isEmpty()) {
            JOptionPane.showMessageDialog(calc, "Stack is empty!", "Empty stack",
                JOptionPane.WARNING_MESSAGE);
            buffer = new StringBuilder("Stack is empty.");
            restart();
        } else {
            setNewValue(stack.pop());
        }
    }

    /**
     * @return the storedValue
     */
    public double getStoredValue() {
        return storedValue;
    }

    /**
     * @param storedValue the storedValue to set
     */
    public void setStoredValue(double storedValue) {
        this.storedValue = storedValue;
    }

    /**
     * @return the operator
     */
    public DoubleBinaryOperator getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(DoubleBinaryOperator operator) {
        this.operator = operator;
    }

    /**
     * @return the binaryOperation
     */
    public boolean isBinaryOperation() {
        return binaryOperation;
    }

    /**
     * @return the unaryOperation
     */
    public boolean isUnaryOperation() {
        return unaryOperation;
    }

    /**
     * @param unaryOperation the unaryOperation to set
     */
    public void setUnaryOperation(boolean unaryOperation) {
        this.unaryOperation = unaryOperation;
    }

    /**
     * @param binaryOperation the binaryOperation to set
     */
    public void setBinaryOperation(boolean binaryOperation) {
        this.binaryOperation = binaryOperation;
    }

    /**
     * @return the equalsPressed
     */
    public boolean isEqualsPressed() {
        return equalsPressed;
    }

    /**
     * @param equalsPressed the equalsPressed to set
     */
    public void setEqualsPressed(boolean equalsPressed) {
        this.equalsPressed = equalsPressed;
    }
}
