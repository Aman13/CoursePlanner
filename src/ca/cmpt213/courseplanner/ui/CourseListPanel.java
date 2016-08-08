package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Course;
import ca.cmpt213.courseplanner.model.CoursePlannerObserver;
import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class CourseListPanel extends BasePanel {
    private static final String TITLE = "Course List";
    private JPanel courseListPanel;
    private JList<Course> list;
    private Course[] courseList;

    public CourseListPanel(Model model) {
        super(model, TITLE);
        registerAsObserver();
    }

    @Override
    public JPanel buildPanel() {
        courseListPanel = new JPanel();
        courseListPanel.setLayout(new BorderLayout());
        courseListPanel.add(makeListPane(), BorderLayout.CENTER);
        return courseListPanel;
    }

    private JScrollPane makeListPane() {
        if (courseList == null) {
            courseList = new Course[0];
        }
        list = new JList(courseList);
        list.setLayoutOrientation(list.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        list.setCellRenderer(new ListCellRenderer<Course>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Course> list, Course course, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, course, index, isSelected, cellHasFocus);
                renderer.setText(course.getTitle());
                return renderer;
            }
        });
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (list.isSelectionEmpty()) {
                    //do nothing
                } else {
                    int index = list.getSelectedIndex();
                    getModel().setCurrentCourse(courseList[index]);
                }
            }
        });
        JScrollPane listPane = new JScrollPane(list);
        listPane.setPreferredSize(new Dimension(100, 300));
        return listPane;
    }

    private void registerAsObserver() {
        getModel().addObserver(new CoursePlannerObserver() {
            @Override
            public void stateChanged() {
                courseList = getModel().getCurrentCourses();
                list.setListData(courseList);
            }
        });
    }


}
