package ca.cmpt213.courseplanner.ui;

import ca.cmpt213.courseplanner.BarGraph.BarGraphIcon;
import ca.cmpt213.courseplanner.BarGraph.BarGraphModel;
import ca.cmpt213.courseplanner.model.CoursePlannerObserver;
import ca.cmpt213.courseplanner.model.Model;
import ca.cmpt213.courseplanner.model.Offering;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CourseStatisticsPanel extends BasePanel {
    private static final String TITLE = "Statistics";
    private JPanel courseStatisticsPanel;
    private java.util.List<Offering> offeringList;
    private BarGraphModel semesterOfferingsModel;
    private BarGraphModel campusOfferingsModel;
    private BarGraphIcon semesterOfferingsIcon;
    private BarGraphIcon campusOfferingsIcon;

    private final int SPRING_INDEX = 0;
    private final int SUMMER_INDEX = 1;
    private final int FALL_INDEX = 2;

    private final int BBY_INDEX = 0;
    private final int SRY_INDEX = 1;
    private final int VAN_INDEX = 2;
    private final int OTHER_INDEX = 3;

    private final int GRAPH_SIZE = 200;

    public CourseStatisticsPanel(Model model) {
        super(model, TITLE);
        offeringList = new ArrayList<>();
        restrictResizing();
        registerAsObserver();
    }

    @Override
    public JPanel buildPanel() {
        courseStatisticsPanel = new JPanel();
        courseStatisticsPanel.setLayout(new BoxLayout(courseStatisticsPanel, BoxLayout.PAGE_AXIS));
        offeringList = getModel().getCurrentOfferings();
        setupSemesterOfferingsGraph();
        setupCampusOfferingsGraph();
        semesterOfferingsIcon = makeBarGraphIcon(semesterOfferingsModel);
        campusOfferingsIcon = makeBarGraphIcon(campusOfferingsModel);
        courseStatisticsPanel.add(makeGraphPanel("Semester Offerings:", semesterOfferingsModel, semesterOfferingsIcon));
        courseStatisticsPanel.add(makeGraphPanel("Campus Offerings:", campusOfferingsModel, campusOfferingsIcon));
        return courseStatisticsPanel;
    }

    private JPanel makeGraphPanel(String title, BarGraphModel model, BarGraphIcon icon) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel titleLabel = makeTitle(title);
        panel.add(titleLabel, BorderLayout.NORTH);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(icon);
        panel.add(iconLabel, BorderLayout.CENTER);
        return panel;
    }

    private BarGraphIcon makeBarGraphIcon(BarGraphModel model) {
        BarGraphIcon icon = new BarGraphIcon(model, GRAPH_SIZE, GRAPH_SIZE);
        return icon;
    }

    private JLabel makeTitle(String title) {
        JLabel titleLabel = new JLabel(title);
        return titleLabel;
    }

    private BarGraphModel setupBlankGraph() {
        int[] data = new int[0];
        String[] titles = {""};
        BarGraphModel blankModel = new BarGraphModel(data, titles);
        return blankModel;
    }

    private void setupSemesterOfferingsGraph() {
        if (offeringList.size() > 0) {
            int[] semesterData = new int[3];
            String[] semesterTitles = {"Spring", "Summer", "Fall"};
            for (Offering offering : offeringList) {
                if (offering.getSemester() % 10 == 1) {
                    //spring
                    semesterData[SPRING_INDEX]++;
                } else if (offering.getSemester() % 10 == 4) {
                    //summer
                    semesterData[SUMMER_INDEX]++;
                } else {
                    // fall
                    semesterData[FALL_INDEX]++;
                }
            }
            semesterOfferingsModel = new BarGraphModel(semesterData, semesterTitles);
        } else {
            int[] data = new int[0];
            String[] titles = new String[0];
            semesterOfferingsModel = new BarGraphModel(data, titles);
        }
    }

    private void setupCampusOfferingsGraph() {
        if (offeringList.size() > 0) {
            int[] campusData = new int[4];
            String[] campusTitles = {"Bby", "Sry", "Van", "Other"};
            for (Offering offering : offeringList) {
                if (offering.getLocation().equals("BURNABY")) {
                    campusData[BBY_INDEX]++;
                } else if (offering.getLocation().equals("SURREY")) {
                    campusData[SRY_INDEX]++;
                } else if (offering.getLocation().equals("HRBRCNTR")) {
                    campusData[VAN_INDEX]++;
                } else {
                    campusData[OTHER_INDEX]++;
                }
            }
            campusOfferingsModel = new BarGraphModel(campusData, campusTitles);
        } else {
            int[] data = new int[0];
            String[] titles = new String[0];
            campusOfferingsModel = new BarGraphModel(data, titles);
        }
    }

    private void registerAsObserver() {
        getModel().addObserver(new CoursePlannerObserver() {
            @Override
            public void stateChanged() {
                offeringList = getModel().getCurrentOfferings();
                setupSemesterOfferingsGraph();
                setupCampusOfferingsGraph();
            }
        });
    }
}
