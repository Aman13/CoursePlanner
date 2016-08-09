package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.*;

import javax.swing.*;
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

    public CourseDetailsPanel(Model model) {
        super(model, TITLE);
        currentComponents = new ArrayList<>();
        registerAsObserver();
    }

    @Override
    public JPanel buildPanel() {
        courseDetailsPanel = new JPanel();
        courseDetailsPanel.setLayout(new BorderLayout());
        makeInfoBox();
        courseDetailsPanel.add(infoBox, BorderLayout.CENTER);
        return courseDetailsPanel;
    }

    private void updateInfoBox() {
        infoBox.removeAll();
        makeInfoBox();
    }

    private void makeInfoBox() {
        courseSelected = getModel().getCourseSelected();
        offeringSelected = getModel().getOfferingSelected();
        currentComponents = getModel().getCurrentComponents();
        infoBox = new JPanel();
        infoBox.setLayout(new GridLayout(4, 2));
        if (offeringSelected == null) {
            //Course Name
            infoBox.add(makeTitleLabel("Course Name: "));
            infoBox.add(makeInfoLabel(" "));
            //Semester
            infoBox.add(makeTitleLabel("Semester: "));
            infoBox.add(makeInfoLabel(" "));
            //Location
            infoBox.add(makeTitleLabel("Location: "));
            infoBox.add(makeInfoLabel(" "));
            //Instructors
            infoBox.add(makeTitleLabel("Instructors: "));
            infoBox.add(makeInfoLabel(" "));
        } else {
            infoBox.removeAll();
            System.out.println("updating info box " + courseSelected.getTitle());
            //Course Name
            infoBox.add(makeTitleLabel("Course Name: "));
            infoBox.add(makeTitleLabel(courseSelected.getTitle()));
            //infoBox.add(makeInfoLabel(courseSelected.getTitle()));
            //Semester
            infoBox.add(makeTitleLabel("Semester: "));
            infoBox.add(makeInfoLabel("" + offeringSelected.getSemester()));
            //Location
            infoBox.add(makeTitleLabel("Location: "));
            infoBox.add(makeInfoLabel(offeringSelected.getLocation()));
            //Instructors
            infoBox.add(makeTitleLabel("Instructors: "));
            infoBox.add(makeInfoLabel(offeringSelected.getInstructorsString()));
        }
    }

    private JLabel makeTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title);
        return titleLabel;
    }

    private JPanel makeInfoLabel(String data) {
        JLabel infoLabel = new JLabel(data);
        infoLabel.setForeground(Color.black);
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.white);
        infoPanel.add(infoLabel);
        return infoPanel;
    }

    private void registerAsObserver() {
        getModel().addCourseObserver(new CourseObserver() {
            @Override
            public void stateChanged() {
                updateInfoBox();
            }
        });
    }


}
