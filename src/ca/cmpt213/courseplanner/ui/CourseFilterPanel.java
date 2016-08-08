package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Course;
import ca.cmpt213.courseplanner.model.Model;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseFilterPanel extends BasePanel {
    private static String TITLE = "Course List Filter";
    private JPanel courseFilterPanel;

    protected JComboBox departmentBox;

    public CourseFilterPanel(Model model) {
        super(model, TITLE);
        restrictResizing();
    }

    private Component makeDropDown() {
        JPanel dropDown = new JPanel();
        String[] departmentStrings = getModel().getDepartmentNames();
        departmentBox = new JComboBox(departmentStrings);
        dropDown.setLayout(new BoxLayout(dropDown, BoxLayout.LINE_AXIS));
        dropDown.add(new JLabel("Departments"));
        dropDown.add(departmentBox);
        return dropDown;
    }

    @Override
    public JPanel buildPanel() {
        courseFilterPanel = new JPanel();
        courseFilterPanel.setLayout(new BorderLayout());
        courseFilterPanel.add(makeDropDown(), BorderLayout.NORTH);
        courseFilterPanel.add(makeUpdateBtn(), BorderLayout.SOUTH);
        return courseFilterPanel;
    }

    private Component makeUpdateBtn() {
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
        JButton updateBtn = new JButton("Update Course List");
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(updateBtn);

        updateBtn.addActionListener(e -> {
            String option = (String) departmentBox.getSelectedItem();
            getModel().setCurrentDepartment(option);
            Course[] displayCourses = getModel().getCurrentCourses();
            for (Course course : displayCourses) {
                System.out.println(course.getTitle());
            }
        });
        return btnPanel;
    }
}
