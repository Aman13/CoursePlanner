package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Course;
import ca.cmpt213.courseplanner.model.DepartmentObserver;
import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import java.awt.*;

public class CourseListPanel extends BasePanel {
    private static final String TITLE = "Course List";
    private JPanel courseListPanel;
    private JList<Course> list;
    private Course[] courseList;
    private final int CELL_WIDTH = 80;
    private final int SCROLL_PANE_WIDTH = 210;
    private final int SCROLL_PANE_HEIGHT = 300;

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
        list.setFixedCellWidth(CELL_WIDTH);
        DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        defaultRenderer.setLayout(new GridLayout(1, 2));
        list.setCellRenderer(new ListCellRenderer<Course>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Course> list, Course course, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, course, index, isSelected, cellHasFocus);
                renderer.setText(course.getTitle());
                return renderer;
            }
        });
        list.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                if (list.isSelectionEmpty()) {
                    //do nothing
                } else {
                    int index = list.getSelectedIndex();
                    getModel().setCurrentCourse(courseList[index]);
                }
            }
        });
        JScrollPane listPane = new JScrollPane(list);
        listPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        return listPane;
    }

    private void registerAsObserver() {
        getModel().addDepartmentObserver(new DepartmentObserver() {
            @Override
            public void stateChanged() {
                courseList = getModel().getCurrentCourses();
                list.setListData(courseList);
            }
        });
    }


}
