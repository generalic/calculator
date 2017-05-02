package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;
import java.util.function.DoubleBinaryOperator;

/**
 * Button which performs a binary operation. ie +, -, ...
 *
 * @author Boris
 * @version 1.0
 */
public class BinaryOperationButton extends AbstractButton {

    private static final long serialVersionUID = 1L;
    /** Which operator it is */
    protected DoubleBinaryOperator operator;

    /**
     * Creates a new {@link BinaryOperationButton}
     *
     * @param label label on the button
     * @param operator operator that this button represents
     * @param status {@link CalculatorBackend} used
     */
    public BinaryOperationButton(final String label, final DoubleBinaryOperator operator,
        final CalculatorBackend calc) {
        super(label, calc);
        this.operator = operator;
    }

    @Override
    protected void execute() {
        calc.setStoredValue(calc.getCurrentValue());
        calc.setOperator(operator);
        calc.setBinaryOperation(true);
        calc.setEqualsPressed(false);
        calc.setUnaryOperation(false);
    }
}
