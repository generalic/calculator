package hr.fer.zemris.java.gui.layouts;

import hr.fer.zemris.java.gui.calc.Calculator;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Layout manager that is used in the {@link Calculator} class.<p>
 *
 * Dimension of this layout is fixed and it consist of 5 rows and 7 columns.
 * However, not all cells are accessible so please do read about constraints
 * of this layout manager.<p>
 * <b>Constraints:</b>
 * <ul>
 * <li>Cell <code>(x, y)</code>, x is integer number from interval <code>[1, 5]</code> and
 * y is integer number from interval <code>[1, 7]</code>.
 * <li>Unaccessible cells: <code>(1,2)</code>, <code>(1,3)</code>, <code>(1,4)</code> and
 * <code>(1,5)</code></li>
 * </ul>
 *
 * @author Boris
 * @version 1.0
 */
public class CalculatorLayout implements LayoutManager2 {

    // boundaries
    /** Minimal row number */
    public static final int ROW_MIN = 1;
    /** Maximal row number */
    public static final int ROW_MAX = 5;
    /** Minimal column number */
    public static final int COL_MIN = 1;
    /** Maximal column number */
    public static final int COL_MAX = 7;

    /** Minimal forbidden column number */
    public static final int ILLEGAL_COL_MIN = 2;
    /** Maximal forbidden column number */
    public static final int ILLEGAL_COL_MAX = 5;

    /** Components of the layout. */
    private final Component[][] layout;

    /** Gap between components (gap = vgap = hgap). */
    private final int gap;

    /**
     * Creates a new {@link CalculatorLayout} with gap set to 0.
     */
    public CalculatorLayout() {
        this(0);
    }

    /**
     * Creates a new {@link CalculatorLayout} with desired gap.
     *
     * @param gap desired gap
     */
    public CalculatorLayout(final int gap) {
        this.gap = gap;
        this.layout = new Component[ROW_MAX - ROW_MIN + 1][COL_MAX - COL_MIN + 1];
    }

    @Override
    public void addLayoutComponent(final String name, final Component comp) {
        addLayoutComponent(comp, name);
    }

    @Override
    public void removeLayoutComponent(final Component comp) {
        for (int i = ROW_MIN - 1; i < ROW_MAX; i++) {
            for (int j = COL_MIN - 1; j < COL_MAX; j++) {
                if (layout[i][j] == comp) {
                    layout[i][j] = null;
                }
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        return getLayoutSize(parent, Component::getPreferredSize);
    }

    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return getLayoutSize(parent, Component::getMinimumSize);
    }

    @Override
    public Dimension maximumLayoutSize(final Container target) {
        return getLayoutSize(target, Component::getMinimumSize);
    }

    /**
     * Gets the size of the biggest component. If minimal is <code>true</code>, minimal sizes are
     * considered, preferred
     * sizes otherwise.
     *
     * @param parent parent container
     * @param provider minimal sizes provider
     * @return dimension of the biggest component
     */
    private Dimension getSize(final Container parent, final ISizeProvider provider) {
        Dimension dim = null;
        final Dimension result = new Dimension();

        for (final Component c : parent.getComponents()) {
            if (c.isVisible()) {
                dim = provider.getSize(c);
            }
            if (dim == null) {
                //dont care
                continue;
            }
            if (c == layout[0][0]) {
                //calc display
                //how many of them, should be 5
                final int numberOfCells = ILLEGAL_COL_MAX - ILLEGAL_COL_MIN + 2;
                //width of single one(all should be the same), height is always the same
                dim = new Dimension((dim.width - (numberOfCells - 1) * gap) / numberOfCells,
                    dim.height);
            }
            result.width = Math.max(result.width, dim.width);
            result.height = Math.max(result.height, dim.height);
        }

        return result;
    }

    /**
     * Gets the dimension of the layout. If minimal is true, than minimal size, preferred size
     * otherwise.
     *
     * @param parent parent container
     * @param provider minimal size provider
     * @return dimension of the layout
     */
    private Dimension getLayoutSize(final Container parent, final ISizeProvider provider) {
        final Dimension cell = getSize(parent, provider);
        final Dimension result = new Dimension();

        final Insets insets = parent.getInsets();
        final int numberOfRows = ROW_MAX - ROW_MIN + 1;
        final int numberOfCols = COL_MAX - COL_MIN + 1;

        result.width =
            cell.width * numberOfCols + gap * (numberOfCols - 1) + insets.left + insets.right;
        result.height =
            cell.height * numberOfRows + gap * (numberOfRows - 1) + insets.top + insets.bottom;

        return result;
    }

    @Override
    public void layoutContainer(final Container parent) {

        final Insets insets = parent.getInsets();
        final Point topLeft = new Point(insets.left, insets.top);

        final int numberOfRows = ROW_MAX - ROW_MIN + 1;
        final int heightWithoutInsets = parent.getHeight() - (insets.top + insets.bottom);
        final int vgapLenght = gap * (numberOfRows - 1);
        final int cellHeight = (heightWithoutInsets - vgapLenght) / numberOfRows;

        final int numberOfCols = COL_MAX - COL_MIN + 1;
        final int widthWithoutInsets = parent.getWidth() - (insets.left + insets.right);
        final int hgapLength = gap * (numberOfCols - 1);
        final int cellWidth = (widthWithoutInsets - hgapLength) / numberOfCols;

        for (int i = ROW_MIN - 1, y = topLeft.y; i < ROW_MAX; i++, y += cellHeight + gap) {
            for (int j = COL_MIN - 1, x = topLeft.x; j < COL_MAX; j++, x += cellWidth + gap) {
                if (layout[i][j] == null) {
                    continue;
                }
                final Dimension dim = new Dimension(cellWidth, cellHeight);
                //calc display
                if ((i == ROW_MIN - 1) && (j == COL_MIN - 1)) {
                    final int illegalCols = ILLEGAL_COL_MAX - ILLEGAL_COL_MIN + 1;
                    dim.width += illegalCols * cellWidth + gap * (illegalCols - 1);
                }
                layout[i][j].setBounds(new Rectangle(new Point(x, y), dim));
            }
        }
    }

    @Override
    public void addLayoutComponent(final Component comp, final Object constraints) {
        if (constraints == null) {
            throw new IllegalArgumentException("Constraint cannot be null.");
        }

        final RowColumnPosition rc = RowColumnPosition.fromString(constraints.toString());

        final int row = rc.getRow() - 1;
        final int col = rc.getCol() - 1;

        if (layout[row][col] == null) {
            layout[row][col] = comp;
        } else {
            throw new IllegalArgumentException(
                "Position (" + rc + ") is already taken.");
        }
    }

    @Override
    public float getLayoutAlignmentX(final Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(final Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(final Container target) {
    }

    /**
     * Interface which represents some source which can provide size.
     *
     * @author Boris
     * @version v1.0
     */
    @FunctionalInterface
    private static interface ISizeProvider {
        /**
         * Returns {@link Dimension} of the given {@link Component}.
         *
         * @param c given {@link Component}
         * @return dimension of the {@link Component}
         */
        Dimension getSize(Component c);
    }

    /**
     * Used as a constraint in the {@link CalculatorLayout}.<br>
     * Read about constraints of the {@link CalculatorLayout}.
     *
     * @author Boris
     * @version 1.0
     */
    private static class RowColumnPosition {

        /**
         * Row (y position)
         */
        private int row;
        /**
         * Column (x position)
         */
        private int col;

        /**
         * Creates a new {@link RowColumnPosition}.
         *
         * @param row desired row
         * @param col desired column
         */
        public RowColumnPosition(int row, int col) {
            checkArguments(row, col);
            this.row = row;
            this.col = col;
        }

        /**
         * Method which checks is given number of rows and columns are acceptable.
         *
         * @param row number of rows
         * @param col number of columns
         */
        private static void checkArguments(int row, int col) {

            checkArgumentInInterval(row, CalculatorLayout.ROW_MIN, CalculatorLayout.ROW_MAX);
            checkArgumentInInterval(col, CalculatorLayout.COL_MIN, CalculatorLayout.COL_MAX);

            //check illegal interval
            if (row == 1) {
                if (check(col, CalculatorLayout.ILLEGAL_COL_MIN,
                    CalculatorLayout.ILLEGAL_COL_MAX)) {
                    throw new IllegalArgumentException(
                        "Position (" + row + ", " + col + ") is inaccessible."
                    );
                }
            }
        }

        /**
         * Checks if the given argument is in interval [lowerBound, upperBound]
         *
         * @param arg argument to check
         * @param lowerBound lower bound
         * @param upperBound upper bound
         */
        private static void checkArgumentInInterval(int arg, int lowerBound, int upperBound) {
            if (!check(arg, lowerBound, upperBound)) {
                throw new IllegalArgumentException(
                    "Argument "
                        + arg
                        + " is out of legal interval ["
                        + lowerBound
                        + ", "
                        + upperBound
                        + "]."
                );
            }
        }

        /**
         * Returns true if argument is in [lowerBound, upperBound] interval
         *
         * @param arg argument to check
         * @param lowerBound lower bound
         * @param upperBound upper bound
         * @return <code>true</code> if argument is in that interval
         */
        private static boolean check(int arg, int lowerBound, int upperBound) {
            return (arg >= lowerBound && arg <= upperBound);
        }

        /**
         * Parses the given string to a valid {@link RowColumnPosition}.
         *
         * @param arg string to parse
         * @return {@link RowColumnPosition}
         */
        public static RowColumnPosition fromString(String arg) {
            int row;
            int col;
            try {
                String[] rc = arg.trim().split(",");
                row = Integer.parseInt(rc[0].trim());
                col = Integer.parseInt(rc[1].trim());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                    "Wrong argument format. Argument: \"" + arg + "\""
                );
            }
            return new RowColumnPosition(row, col);
        }

        /**
         * Returns row of this {@link RowColumnPosition}.
         *
         * @return the row
         */
        public int getRow() {
            return row;
        }

        /**
         * Returns column of this {@link RowColumnPosition}.
         *
         * @return the col
         */
        public int getCol() {
            return col;
        }

        @Override
        public String toString() {
            return Integer.toString(row) + "," + Integer.toString(col);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + col;
            result = prime * result + row;
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RowColumnPosition
                other = (RowColumnPosition) obj;
            if (col != other.col) {
                return false;
            }
            if (row != other.row) {
                return false;
            }
            return true;
        }
    }
}
