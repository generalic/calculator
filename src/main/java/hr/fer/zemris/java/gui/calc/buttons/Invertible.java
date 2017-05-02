package hr.fer.zemris.java.gui.calc.buttons;

/**
 * Interface which represents something what is invertible.
 *
 * @author Boris
 * @version v1.0
 */
@FunctionalInterface
public interface Invertible {

    /**
     * Method used for inverting status.
     */
    void invert();
}
