package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;
import java.util.function.DoubleUnaryOperator;

/**
 * Button which performs a binary operation. ie sin(x), cos(x), ...
 *
 * @author Boris
 * @version 1.0
 */
public class UnaryOperationButton extends AbstractButton {

    private static final long serialVersionUID = 6229733981279749107L;
    protected DoubleUnaryOperator operator;

    /**
     * Creates a new {@link UnaryOperationButton}
     *
     * @param label label on the button
     * @param operator operator that this button represents
     * @param status {@link CalculatorBackend} used
     */
    public UnaryOperationButton(String label, DoubleUnaryOperator operator,
        CalculatorBackend calc) {
        super(label, calc);
        this.operator = operator;
    }

    @Override
    protected void execute() {
        calc.setNewValue(operator.applyAsDouble(calc.getCurrentValue()));
        calc.setUnaryOperation(true);
        calc.setBinaryOperation(false);
        calc.setEqualsPressed(false);
    }
}
