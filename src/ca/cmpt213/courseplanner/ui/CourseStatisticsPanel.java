package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import java.awt.*;

public class CourseStatisticsPanel extends BasePanel {
    private static final String TITLE = "Statistics";
    private JPanel courseStatisticsPanel;
    public CourseStatisticsPanel(Model model) {
        super(model, TITLE);
        restrictResizing();
    }

    @Override
    public JPanel buildPanel() {
        courseStatisticsPanel = new JPanel();
        courseStatisticsPanel.setLayout(new BoxLayout(courseStatisticsPanel, BoxLayout.PAGE_AXIS));
        return courseStatisticsPanel;
    }
}
