package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.CourseObserver;
import ca.cmpt213.courseplanner.model.DepartmentObserver;
import ca.cmpt213.courseplanner.model.Model;
import ca.cmpt213.courseplanner.model.Offering;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CourseOfferingsPanel extends BasePanel {
    private static final int COLUMNS = 4;

    private static final String TITLE = "Course Offerings By Semester";

    private JPanel courseOfferingPanel;
    private List<Offering> offeringList;

    public CourseOfferingsPanel(Model model) {
        super(model, TITLE);
        offeringList = new ArrayList<>();
        registerAsObserver();
    }

    @Override
    public JPanel buildPanel() {
        courseOfferingPanel = new JPanel();
        courseOfferingPanel.setLayout(new BorderLayout());
        return courseOfferingPanel;
//        courseOfferingPanel.setLayout(new GridBagLayout());
//        offeringList = getModel().getCurrentOfferings();
//        if (offeringList.size() > 0) {
//            for (int x = 0; x < 4; x++) {
//                for (int y = 0; y < Math.ceil(offeringList.size()/3) + 1; y++) {
//                    GridBagConstraints constraint = makeGridBagConstraints(x, y);
//                    getGridLabels(x, y);
//                }
//            }
//        } else {
//            JLabel noOfferings = new JLabel("No offerings available, please choose a course.");
//            courseOfferingPanel.add(noOfferings);
//        }
//        return courseOfferingPanel;
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
        return offeringList.get(0).getYear();
    }

    private int getLatestYear() {
        return offeringList.get(offeringList.size() - 1).getYear();
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
        getModel().addCourseObserver(new CourseObserver() {
            @Override
            public void stateChanged() {
                offeringList = getModel().getCurrentOfferings();
                updatePanel();
            }
        });
    }

    private void updatePanel() {
        int earliestYear = getEarliestYear();
        int latestYear = getLatestYear();
        int rangeYear = latestYear - earliestYear + 1;
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        courseOfferingPanel.setLayout(gridBag);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;

        //Add Label for Semester
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        courseOfferingPanel.add(makeSemesterLabel("Spring"), gridBagConstraints);
        gridBagConstraints.gridx = 2;
        courseOfferingPanel.add(makeSemesterLabel("Summer"), gridBagConstraints);
        gridBagConstraints.gridx = 3;
        courseOfferingPanel.add(makeSemesterLabel("Fall"), gridBagConstraints);

        //Loop to build offerings
        for (int row = 0; row < rangeYear; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                gridBagConstraints.weightx = 0;
                gridBagConstraints.weighty = 0;
                gridBagConstraints.gridx = col;
                gridBagConstraints.gridy = row;
                if (row != 0 && col == 0) {
                    //Make year label
                    courseOfferingPanel.add(makeYearLabel(earliestYear + row), gridBagConstraints);
                } else if (row > 0 && col > 0) {
                    gridBagConstraints.weightx = 1;
                    gridBagConstraints.weighty = 1;
                    courseOfferingPanel.add(makeOfferingsButton(row, col), gridBagConstraints);
                }
            }
        }

        courseOfferingPanel.repaint();
    }

    private Component makeOfferingsButton(int row, int col) {
        JPanel offeringButtonPanel = new JPanel();
        offeringButtonPanel.setLayout(new BoxLayout(offeringButtonPanel, BoxLayout.PAGE_AXIS));
        int currentYear = getEarliestYear() + row - 1;
        String semester = semesterConverter(col);
        //Check if offering is same year && same term
        for (Offering offering : offeringList) {
            if (offering.getYear() == currentYear && offering.getSemesterTitle().equals(semester)) {
                //Matching year and semester
                JButton offeringButton = new JButton(getModel().getCourseSelected().getTitle() + " " + offering.getLocation());
                offeringButton.addActionListener(e -> {
                    System.out.println(getModel().getCourseSelected().getTitle() + " " + offering.getLocation());
                });
            }
        }
        offeringButtonPanel.add(offeringButtonPanel);
        return offeringButtonPanel;
    }

    private String semesterConverter(int col) {
        if (col == 1) {
            return "Spring";
        } else if (col == 2) {
            return "Summer";
        } else if (col == 3) {
            return "Fall";
        }
        return "";
    }

    private Component makeYearLabel(int year) {
        JPanel yearLabelPanel = new JPanel();
        yearLabelPanel.add(new JLabel(String.valueOf(year - 1)));
        return yearLabelPanel;
    }

    private Component makeSemesterLabel(String semester) {
        JPanel semesterLabelPanel = new JPanel();
        semesterLabelPanel.add(new JLabel(semester));
        return semesterLabelPanel;
    }

}
