package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;

/**
 * Class which represents digit button.
 *
 * @author Boris
 */
public class DigitButton extends AbstractButton {

    private static final long serialVersionUID = 1L;
    private String value;

    /**
     * Return new {@link DigitButton}.
     *
     * @param label name of the button
     * @param value value of digit
     * @param calc {@link CalculatorBackend}
     */
    public DigitButton(String label, String value, CalculatorBackend calc) {
        super(label, calc);
        this.value = value;
    }

    @Override
    protected void execute() {
        calc.addToBuffer(value);
    }
}
