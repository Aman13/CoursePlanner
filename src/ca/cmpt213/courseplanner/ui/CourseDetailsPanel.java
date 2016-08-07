package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import java.awt.*;

public class CourseDetailsPanel extends BasePanel {
    private static final String TITLE = "Details of Course Offering";
    private JPanel courseDetailsPanel;
    public CourseDetailsPanel(Model model) {
        super(model, TITLE);
    }

    @Override
    public JPanel buildPanel() {
        courseDetailsPanel = new JPanel();
        courseDetailsPanel.setLayout(new BorderLayout());
        return courseDetailsPanel;
    }
}
