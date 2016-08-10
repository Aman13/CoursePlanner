package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This class is the abstract base class for all JPanels
 * @author Aram & Aman
 */
public abstract class BasePanel extends JPanel {
    private static final int BORDER_PADDING = 2;
    private Model model;
    private String title;

    public BasePanel(Model model, String title) {
        this.model = model;
        this.title = title;
        JPanel info = buildPanel();
        info.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setLayout(new BorderLayout());
        add(buildTitle(), BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
        setBorder(new EmptyBorder(BORDER_PADDING,
                BORDER_PADDING,
                BORDER_PADDING,
                BORDER_PADDING));
    }

    private Component buildTitle() {
        JLabel title = new JLabel(this.title);
        title.setForeground(Color.BLUE);
        return title;
    }

    public abstract JPanel buildPanel();

    protected void restrictResizing() {
        Dimension prefSize = this.getPreferredSize();
        Dimension currentSize = new Dimension(Integer.MAX_VALUE, (int)prefSize.getHeight());
        this.setMaximumSize(currentSize);
    }

    protected Model getModel() {
        return this.model;
    }

}
