package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.CoursePlannerObserver;
import ca.cmpt213.courseplanner.model.Model;
import ca.cmpt213.courseplanner.model.Offering;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CourseOfferingsPanel extends BasePanel {
    private static final String TITLE = "Course Offerings By Semester";
    private JPanel courseOfferingPanel;
    private java.util.List<Offering> offeringList;
    private final int COLUMNS = 3;

    public CourseOfferingsPanel(Model model) {
        super(model, TITLE);
        offeringList = new ArrayList<>();
        registerAsObserver();
    }

    @Override
    public JPanel buildPanel() {
        courseOfferingPanel = new JPanel();
        courseOfferingPanel.setLayout(new GridBagLayout());
        offeringList = getModel().getCurrentOfferings();
        if (offeringList.size() > 0) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < Math.ceil(offeringList.size()/3) + 1; y++) {
                    GridBagConstraints constraint = makeGridBagConstraints(x, y);
                    getGridLabels(x, y);
                }
            }
        } else {
            JLabel noOfferings = new JLabel("No offerings available, please choose a course.");
            courseOfferingPanel.add(noOfferings);
        }
        return courseOfferingPanel;
    }

    private void getGridLabels(int x, int y) {
        if (x == 0 || y == 0) {
            //corner
            String title = "";
            if (x == 1) {
                title = "Spring";
            } else if (x == 2) {
                title = "Summer";
            } else if (x == 3) {
                title = "Fall";
            } else if (y != 0) {
                int year = getEarliestYear() + y - 1;
                title = title + year;
            }
            JLabel label = new JLabel(title);
            courseOfferingPanel.add(label);
            label.setBackground(Color.white);
        }
    }

    private int getEarliestYear() {
        return offeringList.get(0).calculateYear();
    }

    private int getLatestYear() {
        return offeringList.get(offeringList.size() - 1).calculateYear();
    }


    private GridBagConstraints makeGridBagConstraints(int x, int y) {
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = x;
        constraint.gridy = y;
        if (x == 0 && y == 0) {
            //corner
            constraint.gridheight = 50;
            constraint.gridwidth = 50;
        } else if (x > 0 && y == 0) {
            //spring, summer, fall
            constraint.gridheight = 50;
        } else if (x == 0 && y > 0) {
            //year labels
            constraint.gridwidth = 50;
        }
        return constraint;
    }

    private void registerAsObserver() {
        getModel().addObserver(new CoursePlannerObserver() {
            @Override
            public void stateChanged() {
                offeringList = getModel().getCurrentOfferings();
                //
            }
        });
    }



}
