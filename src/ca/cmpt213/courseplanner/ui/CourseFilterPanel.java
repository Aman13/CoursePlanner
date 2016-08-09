package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Course;
import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseFilterPanel extends BasePanel {
    private static String TITLE = "Course List Filter";
    private JPanel courseFilterPanel;

    protected JComboBox departmentBox;
    protected JCheckBox gradCheckBox;
    protected JCheckBox underGradCheckBox;

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
        courseFilterPanel.add(makeCheckBox(), BorderLayout.CENTER);
        courseFilterPanel.add(makeUpdateBtn(), BorderLayout.SOUTH);
        return courseFilterPanel;
    }

    private Component makeUpdateBtn() {
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
        JButton updateBtn = new JButton("Update Course List");
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(updateBtn);

        updateBtn.addActionListener(event -> {
            String option = (String) departmentBox.getSelectedItem();
            getModel().setCurrentDepartment(option,
                    underGradCheckBox.isSelected(),
                    gradCheckBox.isSelected());
        });
        return btnPanel;
    }

    private Component makeCheckBox() {
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.PAGE_AXIS));
        underGradCheckBox = new JCheckBox("Include Grad Courses");
        gradCheckBox = new JCheckBox("Include Undergrad Courses");
        checkBoxPanel.add(underGradCheckBox);
        checkBoxPanel.add(gradCheckBox);
        return checkBoxPanel;
    }
}
