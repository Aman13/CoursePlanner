package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CourseDetailsPanel extends BasePanel {
    private static final String TITLE = "Details of Course Offering";
    private JPanel courseDetailsPanel;
    private Course courseSelected;
    private Offering offeringSelected;
    private java.util.List<CourseComponent> currentComponents;
    private JPanel infoBox;
    private JPanel componentInfoBox;

    public CourseDetailsPanel(Model model) {
        super(model, TITLE);
        currentComponents = new ArrayList<>();
        registerAsObserver();
    }

    @Override
    public JPanel buildPanel() {
        courseDetailsPanel = new JPanel();
        courseDetailsPanel.setLayout(new BorderLayout());
        JPanel detailsBoxPanel = new JPanel();
        detailsBoxPanel.setLayout(new BoxLayout(detailsBoxPanel, BoxLayout.PAGE_AXIS));
        makeOfferingInfoBox();
        detailsBoxPanel.add(infoBox);
//        courseDetailsPanel.add(infoBox, BorderLayout.NORTH);
        if (offeringSelected != null) {
            makeComponentInfoBox();
            detailsBoxPanel.add(componentInfoBox);
//            courseDetailsPanel.add(componentInfoBox, BorderLayout.NORTH);
        }
        courseDetailsPanel.add(detailsBoxPanel, BorderLayout.NORTH);
        courseDetailsPanel.setPreferredSize(new Dimension(300,300));
        return courseDetailsPanel;
    }

    private void updateCourseDetailsPanel() {
        courseDetailsPanel.removeAll();
        updateOfferingInfoBox();
        courseDetailsPanel.setLayout(new BorderLayout());
        JPanel detailsBoxPanel = new JPanel();
        detailsBoxPanel.setLayout(new BoxLayout(detailsBoxPanel, BoxLayout.PAGE_AXIS));
        detailsBoxPanel.add(infoBox);
//        courseDetailsPanel.add(infoBox, BorderLayout.NORTH);
        if (offeringSelected != null) {
            updateComponentInfoBox();
            detailsBoxPanel.add(componentInfoBox);
//            courseDetailsPanel.add(componentInfoBox, BorderLayout.NORTH);
        }
        courseDetailsPanel.add(detailsBoxPanel, BorderLayout.NORTH);
        courseDetailsPanel.setPreferredSize(new Dimension(300, 300));
        courseDetailsPanel.revalidate();
        courseDetailsPanel.repaint();
    }

    private void updateOfferingInfoBox() {
        infoBox.removeAll();
        makeOfferingInfoBox();
    }

    private void updateComponentInfoBox() {
        if (componentInfoBox != null) {
            componentInfoBox.removeAll();
        }
        makeComponentInfoBox();
    }

    private void makeOfferingInfoBox() {
        courseSelected = getModel().getCourseSelected();
        offeringSelected = getModel().getOfferingSelected();
        currentComponents = getModel().getCurrentComponents();

        infoBox = new JPanel();
        infoBox.setLayout(new GridBagLayout());
        JLabel[] infoLabels = new JLabel[4];

        if (offeringSelected == null) {
            GridBagConstraints constraint = new GridBagConstraints();
            String[] labelTitleArray = {"Course Name: ", "Semester: ", "Location: ", "Instructors: "};
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 2; x++) {
                    constraint.fill = GridBagConstraints.BOTH;
                    constraint.gridx = x;
                    constraint.gridy = y;
                    if (x ==  0) {
                        constraint.weightx = 0;
                        constraint.weighty = 0;
                        infoBox.add(makeTitleLabel(labelTitleArray[y]), constraint);
                    } else {
                        constraint.weightx = 1;
                        constraint.weighty = 1;
                        infoLabels[y] = makeInfoLabel(" ");
                        JPanel infoPanel = makeInfoPanel();
                        infoPanel.add(infoLabels[y]);
                        infoBox.add(infoPanel, constraint);
                    }
                }
            }
        } else {
            infoBox.removeAll();
            infoBox.setLayout(new GridBagLayout());
            System.out.println("updating info box " + offeringSelected.getSemesterTitle());
            GridBagConstraints constraint = new GridBagConstraints();
            String[] titleLabelArray = {"Course Name: ", "Semester: ", "Location: ", "Instructors: "};
            String[] infoLabelArray = {courseSelected.getTitle(), "" + offeringSelected.getSemester(),
                    offeringSelected.getLocation(), offeringSelected.getInstructorsString()};
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 2; x++) {
                    constraint.fill = GridBagConstraints.BOTH;
                    constraint.gridx = x;
                    constraint.gridy = y;
                    if (x ==  0) {
                        constraint.weightx = 0;
                        constraint.weighty = 0;
                        infoBox.add(makeTitleLabel(titleLabelArray[y]), constraint);
                    } else {
                        constraint.weightx = 1;
                        constraint.weighty = 1;
                        JPanel infoPanel = makeInfoPanel();
                        infoPanel.add(makeInfoLabel(infoLabelArray[y]));
                        infoBox.add(infoPanel, constraint);
                    }
                }
            }
        }
    }

    private void makeComponentInfoBox() {
        courseSelected = getModel().getCourseSelected();
        offeringSelected = getModel().getOfferingSelected();
        currentComponents = getModel().getCurrentComponents();

        componentInfoBox = new JPanel();
        componentInfoBox.setLayout(new GridBagLayout());

        if (currentComponents.size() > 0) {
            GridBagConstraints constraint = new GridBagConstraints();
            constraint.fill = GridBagConstraints.HORIZONTAL;
            constraint.weighty = 1;
            constraint.weightx = 1;

            //Section Type
            constraint.gridx = 0;
            constraint.gridy = 0;
            constraint.anchor = GridBagConstraints.WEST;
            JLabel sectionTitle = makeTitleLabel("Section Type: ");
            componentInfoBox.add(sectionTitle, constraint);
            for (int y = 0; y < currentComponents.size(); y++) {
                constraint.gridy = y + 1;

                JLabel section = makeTitleLabel(currentComponents.get(y).getComponentCode());
                componentInfoBox.add(section, constraint);
            }

            //Enrollment total/capacity
            constraint.gridx = 1;
            constraint.gridy = 0;
            constraint.anchor = GridBagConstraints.EAST;
            JLabel enrollTitle = makeTitleLabel("Enrollment (filled/cap): ");
            enrollTitle.setPreferredSize(new Dimension(150, 20));
            componentInfoBox.add(enrollTitle, constraint);
            for (int y = 0; y < currentComponents.size(); y++){
                constraint.gridy = y + 1;
                JLabel enrollment = makeTitleLabel(currentComponents.get(y).getEnrollmentTotal() + "/" +
                                                    currentComponents.get(y).getEnrollmentCapacity());
                enrollment.setPreferredSize(new Dimension(150, 10));
                componentInfoBox.add(enrollment, constraint);
            }
        }
    }

    private JLabel makeTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title);
        return titleLabel;
    }

    private JLabel makeInfoLabel(String data) {
        JLabel infoLabel = new JLabel(data);
        infoLabel.setForeground(Color.black);
//        JPanel infoPanel = new JPanel();
//        infoPanel.setBackground(Color.white);
//        infoPanel.add(infoLabel);
        return infoLabel;
    }

    private JPanel makeInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.white);
        return infoPanel;
    }

    private void registerAsObserver() {
        getModel().addCourseObserver(new CourseObserver() {
            @Override
            public void stateChanged() {
                updateCourseDetailsPanel();
            }
        });
    }


}
