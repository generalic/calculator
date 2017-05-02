package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorBackend;
import java.awt.Font;
import javax.swing.JButton;

/**
 * JButton that has the next property: when it's pressed, method execute() is invoked. After that,
 * label is updated.
 *
 * @author Boris
 * @version 1.0
 */
public abstract class AbstractButton extends JButton {

    private static final long serialVersionUID = 1L;

    protected CalculatorBackend calc;

    /**
     * Creates a new JButton. When this button is pressed, execute() method is invoked.
     *
     * @param label label on the button
     * @param status {@link CalculatorBackend} used to update the label
     */
    public AbstractButton(String label, CalculatorBackend calc) {
        super(label);
        this.calc = calc;
        this.setFont(new Font("Arial", Font.BOLD, 25));
        this.addActionListener(e -> {
            execute();
            calc.updateLabel(null);
        });
    }

    /**
     * Determines what happens when this button is pressed.
     */
    protected abstract void execute();
}
