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
    private static final int HEIGHT = 800;
    private static final int WIDTH = 500;

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
        clearAndResetPanel();
        return courseOfferingPanel;
    }

    private int getEarliestYear() {
        return offeringList.get(0).getYear();
    }

    private int getLatestYear() {
        return offeringList.get(offeringList.size() - 1).getYear();
    }

    private void registerAsObserver() {
        getModel().addCourseObserver(new CourseObserver() {
            @Override
            public void stateChanged() {
                offeringList = getModel().getCurrentOfferings();
                clearAndResetPanel();
                updatePanel();
            }
        });
    }

    private void clearAndResetPanel() {
        courseOfferingPanel.removeAll();
        courseOfferingPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        courseOfferingPanel.setLayout(new BorderLayout());
        courseOfferingPanel.setBackground(Color.WHITE);
        courseOfferingPanel.repaint();
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
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        //Add Label for Semester
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        courseOfferingPanel.add(makeSemesterLabel("Spring"), gridBagConstraints);
        gridBagConstraints.gridx = 2;
        courseOfferingPanel.add(makeSemesterLabel("Summer"), gridBagConstraints);
        gridBagConstraints.gridx = 3;
        courseOfferingPanel.add(makeSemesterLabel("Fall"), gridBagConstraints);
        System.out.println("earliest year: " + earliestYear);
        System.out.println("latestYear: " + latestYear);
        System.out.println("Range: " + rangeYear);
        //Loop to build offerings
        for (int row = 0; row < rangeYear + 1; row++) {
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
        courseOfferingPanel.revalidate();
        courseOfferingPanel.repaint();
    }

    private Component makeOfferingsButton(int row, int col) {
        JPanel offeringsButtonPanel = new JPanel();
        offeringsButtonPanel.setLayout(new BoxLayout(offeringsButtonPanel, BoxLayout.PAGE_AXIS));
        int currentYear = getEarliestYear() + row - 1;
        String semester = semesterConverter(col);
        //Check if offering is same year && same term
        for (Offering offering : offeringList) {
            if (offering.getYear() == currentYear && offering.getSemesterTitle().equals(semester)) {
                //Matching year and semester
                JButton offeringButton = new JButton(getModel().getCourseSelected().getTitle() + " " + offering.getLocation());
                offeringButton.addActionListener(e -> {
                    System.out.println(getModel().getCourseSelected().getTitle() + " " + offering.getLocation() + " " + offering.getYear());
                });
                JPanel singleOfferingButtonPanel = new JPanel();
                singleOfferingButtonPanel.setLayout(new BorderLayout());
                singleOfferingButtonPanel.add(offeringButton, BorderLayout.NORTH);
                offeringsButtonPanel.add(singleOfferingButtonPanel);
            }
        }
        offeringsButtonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return offeringsButtonPanel;
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
        yearLabelPanel.setLayout(new BoxLayout(yearLabelPanel, BoxLayout.LINE_AXIS));
        yearLabelPanel.add(new JLabel(String.valueOf(year - 1)));
//        yearLabelPanel.add(Box.createHorizontalGlue());
        yearLabelPanel.setBackground(Color.WHITE);
        return yearLabelPanel;
    }

    private Component makeSemesterLabel(String semester) {
        JPanel semesterLabelPanel = new JPanel();
        semesterLabelPanel.add(new JLabel(semester));
        semesterLabelPanel.setBackground(Color.WHITE);
        return semesterLabelPanel;
    }

}
