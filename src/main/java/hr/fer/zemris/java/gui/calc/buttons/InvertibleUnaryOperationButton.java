package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;
import java.util.function.DoubleUnaryOperator;

/**
 * Class which represents {@link UnaryOperationButton} whis is also {@link Invertible}.
 *
 * @author Boris
 * @version 1.0
 */
public class InvertibleUnaryOperationButton extends UnaryOperationButton implements Invertible {

    private static final long serialVersionUID = 1L;
    /** Original button name. */
    private String label;
    /** Inverted button name. */
    private String invLabel;
    private DoubleUnaryOperator invOperator;

    /**
     * Creates new instance of {@link InvertibleUnaryOperationButton}.
     *
     * @param label name of the button
     * @param operator original state operator
     * @param invLabel name of the inverted button
     * @param invOperator inverted state operator
     * @param calc {@link CalculatorBackend}
     */
    public InvertibleUnaryOperationButton(
        String label, DoubleUnaryOperator operator,
        String invLabel, DoubleUnaryOperator invOperator,
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

        DoubleUnaryOperator op = operator;
        operator = invOperator;
        invOperator = op;
    }
}
