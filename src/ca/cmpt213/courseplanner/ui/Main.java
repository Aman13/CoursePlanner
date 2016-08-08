package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.model.Model;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        model.dumpModel();
        //Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.add(new CourseFilterPanel(model));
        leftPanel.add(new CourseListPanel(model));

        //Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.add(new CourseOfferingsPanel(model));

        //Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.add(new CourseStatisticsPanel(model));
        rightPanel.add(new CourseDetailsPanel(model));

        //Main Frame
        JFrame mainFrame = new JFrame("Course Planner");
        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(leftPanel, BorderLayout.WEST);
        mainFrame.add(centerPanel, BorderLayout.CENTER);
        mainFrame.add(rightPanel, BorderLayout.EAST);


        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
