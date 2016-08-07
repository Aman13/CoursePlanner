package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import java.awt.*;

public class CourseFilterPanel extends BasePanel {
    private static final long serialVersionUID = 1L;
    private static String TITLE = "Course List Filter";
    private JPanel courseFilterPanel;

    public CourseFilterPanel(Model model) {
        super(model, TITLE);
        restrictResizing();
    }

    @Override
    public JPanel buildPanel() {
        JPanel courseFilterPanel = new JPanel();
        courseFilterPanel.setLayout(new BorderLayout());

        return courseFilterPanel;
    }
}
