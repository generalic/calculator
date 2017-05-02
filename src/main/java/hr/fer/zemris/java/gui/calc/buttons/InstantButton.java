package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;
import java.util.function.Consumer;

/**
 * This class represents a button that when pressed instantly performs some kind of action on the
 * calculator.
 *
 * @author Boris
 * @version 1.0
 */
public class InstantButton extends AbstractButton {

    private static final long serialVersionUID = 1L;
    /** Defined job for this button. */
    private final Consumer<CalculatorBackend> consumer;

    /**
     * Creates new instance of {@link InstantButton}.
     */
    public InstantButton(final String label, final Consumer<CalculatorBackend> consumer,
        final CalculatorBackend calc) {
        super(label, calc);
        this.consumer = consumer;
    }

    @Override
    protected void execute() {
        consumer.accept(calc);
    }
}
