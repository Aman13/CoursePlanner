package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import java.awt.*;

public class CourseOfferingsPanel extends BasePanel {
    private static final String TITLE = "Course Offerings By Semester";
    private JPanel courseOfferingPanel;

    public CourseOfferingsPanel(Model model) {
        super(model, TITLE);
    }

    @Override
    public JPanel buildPanel() {
        courseOfferingPanel = new JPanel();
        return courseOfferingPanel;
    }
}
