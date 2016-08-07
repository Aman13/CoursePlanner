package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import java.awt.*;

public class CourseListPanel extends BasePanel {
    private static final String TITLE = "Course List";
    private JPanel courseListPanel;

    public CourseListPanel(Model model) {
        super(model, TITLE);
    }

    @Override
    public JPanel buildPanel() {
        courseListPanel = new JPanel();
        courseListPanel.setLayout(new BorderLayout());
        return courseListPanel;
    }
}
