package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;
import java.util.function.DoubleBinaryOperator;

/**
 * Class which represents {@link BinaryOperationButton} whis is also {@link Invertible}.
 *
 * @author Boris
 * @version 1.0
 */
public class InvertibleBinaryOperationButton extends BinaryOperationButton implements Invertible {

    private static final long serialVersionUID = 1L;
    /** Original button name. */
    private String label;
    /** Inverted button name. */
    private String invLabel;
    /** Operator for inverted state. */
    private DoubleBinaryOperator invOperator;

    /**
     * Creates new instance of {@link InvertibleBinaryOperationButton}.
     *
     * @param label name of the button
     * @param operator original state operator
     * @param invLabel name of the inverted button
     * @param invOperator inverted state operator
     * @param calc {@link CalculatorBackend}
     */
    public InvertibleBinaryOperationButton(
        String label, DoubleBinaryOperator operator,
        String invLabel, DoubleBinaryOperator invOperator,
        CalculatorBackend calc) {
        super(label, operator, calc);
        this.label = label;
        this.invLabel = invLabel;
        this.invOperator = invOperator;
    }

    @Override
    public void invert() {
        String l = label;
        label = invLabel;
        invLabel = l;
        setText(label);

        DoubleBinaryOperator op = operator;
        operator = invOperator;
        invOperator = op;
    }
}
