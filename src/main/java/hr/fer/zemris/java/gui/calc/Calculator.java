package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.buttons.BinaryOperationButton;
import hr.fer.zemris.java.gui.calc.buttons.DigitButton;
import hr.fer.zemris.java.gui.calc.buttons.InstantButton;
import hr.fer.zemris.java.gui.calc.buttons.Invertible;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleBinaryOperationButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleUnaryOperationButton;
import hr.fer.zemris.java.gui.calc.buttons.UnaryOperationButton;
import hr.fer.zemris.java.gui.layouts.CalculatorLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

/**
 * This class implements the functionality of a calculator.
 *
 * @author Boris
 * @version 1.0
 */
public class Calculator extends JFrame {

    private static final long serialVersionUID = 1L;
    /** Color of label's border. */
    private static final Color LABEL_BORDER_COLOR = new Color(91, 130, 222);
    /** Color of label's background. */
    private static final Color LABEL_BACKGROUND_COLOR = new Color(248, 255, 107);

    /** Label used as to show the result of the calculator. */
    private JLabel display;
    /** Checkbox used to invert some functions on the calculator. */
    private JCheckBox box;
    /** Holds status of the calculator and provides methods for operating with calculator. */
    private CalculatorBackend status;
    /** Map used for keeping invertible buttons */
    private Map<String, Invertible> operators;

    /**
     * Creates a new calculator. Initializes the GUI and restarts the calculator to the default
     * settings (as does the
     * button 'res').
     */
    public Calculator() {
        initGUI();
        status.restart();
    }

    /**
     * Creates a new {@link Calculator} and sets it's visibility to <code>true</code>.
     *
     * @param args don't mather
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
    }

    /**
     * Initializes the gui of the calculator. Adds label, checkbox and buttons to the calculator's
     * frame.
     */
    private void initGUI() {
        initWindow();

        display = new JLabel();
        initDisplay();

        box = new JCheckBox("Inv", false);
        initBox();

        status = new CalculatorBackend(this, display, box);

        addButtons(getContentPane());

        pack();
    }

    /**
     * Initializes the check box.
     *
     * @param box check box to initialize.
     */
    private void initBox() {
        box.addActionListener(e -> operators.values().forEach(o -> o.invert()));
        box.setFont(new Font("Arial", Font.BOLD, 14));
        box.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(box, "5,7");
    }

    /**
     * Initializes the window.
     */
    private void initWindow() {
        setLocation(500, 250);
        setTitle("Calculator v1.0");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException ignorable) {
        }
        getContentPane().setLayout(new CalculatorLayout(4));
    }

    /**
     * Initializes the display.
     */
    private void initDisplay() {
        display.setText("<html><h1>0.0&nbsp</h1></html>");
        display.setBorder(BorderFactory.createLineBorder(LABEL_BORDER_COLOR, 2));
        display.setOpaque(true);
        display.setBackground(LABEL_BACKGROUND_COLOR);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(display, "1,1");
    }

    /**
     * Adds all buttons to the container specified. Container should have layout manager : {@link
     * CalculatorLayout}.
     *
     * @param c container to add buttons to
     */
    private void addButtons(Container c) {

        //digits
        c.add(new DigitButton("0", "0", status), "5,3");
        c.add(new DigitButton("1", "1", status), "4,3");
        c.add(new DigitButton("2", "2", status), "4,4");
        c.add(new DigitButton("3", "3", status), "4,5");
        c.add(new DigitButton("4", "4", status), "3,3");
        c.add(new DigitButton("5", "5", status), "3,4");
        c.add(new DigitButton("6", "6", status), "3,5");
        c.add(new DigitButton("7", "7", status), "2,3");
        c.add(new DigitButton("8", "8", status), "2,4");
        c.add(new DigitButton("9", "9", status), "2,5");
        c.add(new DigitButton(".", ".", status), "5,5");

        operators = new HashMap<>();

        //invertible operations
        //        operators.put("2,2", new InvertibleUnaryOperationButton("sin", Math::sin, "asin", Math::asin, status));
        operators.put("2,2",
            new InvertibleUnaryOperationButton("sin", x -> sin(x), "asin", x -> asin(x), status));
        operators.put("3,2",
            new InvertibleUnaryOperationButton("cos", x -> cos(x), "acos", x -> acos(x), status));
        operators.put("4,2",
            new InvertibleUnaryOperationButton("tan", x -> tan(x), "atan", x -> atan(x), status));
        operators.put("5,2",
            new InvertibleUnaryOperationButton("cot", x -> 1 / tan(x), "acot", x -> atan2(1, x),
                status));
        operators.put("3,1",
            new InvertibleUnaryOperationButton("log", x -> log10(x), "10^x", x -> pow(10, x),
                status));
        operators.put("4,1",
            new InvertibleUnaryOperationButton("ln", x -> log(x), "e^x", x -> exp(x), status));
        operators.put("5,1", new InvertibleBinaryOperationButton("x^n", (x, n) -> pow(x, n), "n√x",
            (x, n) -> pow(x, 1 / n), status));

        operators.forEach((t, u) -> c.add((Component) u, t));

        //operations
        c.add(new UnaryOperationButton("1/x", x -> pow(x, -1), status), "2,1");
        c.add(new BinaryOperationButton("/", (x, y) -> x / y, status), "2,6");
        c.add(new BinaryOperationButton("*", (x, y) -> x * y, status), "3,6");
        c.add(new BinaryOperationButton("-", (x, y) -> x - y, status), "4,6");
        c.add(new BinaryOperationButton("+", (x, y) -> x + y, status), "5,6");

        // instant operations
        c.add(new InstantButton("=", t -> t.equalsPressed(), status), "1,6");
        c.add(new InstantButton("clr", t -> t.clear(), status), "1,7");
        c.add(new InstantButton("res", t -> t.restart(), status), "2,7");
        c.add(new InstantButton("push", t -> t.push(), status), "3,7");
        c.add(new InstantButton("pop", t -> t.pop(), status), "4,7");
        c.add(new InstantButton("+/-", t -> t.changeSign(), status), "5,4");
    }
}
