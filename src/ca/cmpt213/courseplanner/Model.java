package ca.cmpt213.courseplanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Model {

    private static final int DEFAULT_SIZE = 8;
    private static final int SEMESTER = 0;
    private static final int SUBJECT = 1;
    private static final int CATALOGUE_NUMBER = 2;
    private static final int LOCATION = 3;
    private static final int ENROLLMENT_CAPACITY = 4;
    private static final int ENROLLMENT_TOTAL = 5;
    private static final int INSTRUCTOR = 6;

    public static final String COURSE_DATA_FILE = "data/course_data_2016.csv";
    File courseDataFile;
    String line;
    private List<Course> courseList = new ArrayList<>();
    private List<String[]> lineList = new ArrayList<>();

    public Model () {
        courseList = new ArrayList<>();
        lineList = new ArrayList<>();
    }

    public void getData() throws FileNotFoundException{
        try {
            Scanner scanner = new Scanner(new File(COURSE_DATA_FILE));
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                //System.out.println(line);
                String[] courseData = line.split(",");
                lineList.add(courseData);
//                int semester = Integer.parseInt(courseData[0]);
//                int enrollmentCapacity = Integer.parseInt(courseData[4]);
//                int enrollmentTotal = Integer.parseInt(courseData[5]);
//                if (courseData.length == DEFAULT_SIZE) {
//                    String[] instructorArray = {courseData[6]};
//                    courseList.add(new Course(
//                            semester,
//                            courseData[1],
//                            courseData[2],
//                            courseData[3],
//                            enrollmentCapacity,
//                            enrollmentTotal,
//                            instructorArray,
//                            courseData[7]));
//                } else {
//                    int size = courseData.length - 7;
//                    String[] instructorArray = new String[size];
//                    for (int i = 0; i < size; i++) {
//                        instructorArray[i] = courseData[6 + i];
//                    }
//                    courseList.add(new Course(
//                            semester,
//                            courseData[1],
//                            courseData[2],
//                            courseData[3],
//                            enrollmentCapacity,
//                            enrollmentTotal,
//                            instructorArray,
//                            courseData[7]));
//                }
            }
            scanner.close();
        } catch(Exception e) {
            System.out.println("problems");
        }
        buildCourseObjects();
    }

    private void buildCourseObjects() {
        while (lineList.size() > 0) {
            //Utility
            List<String[]> equivalentCourse = new ArrayList<>();
            // Gather all same course
            String[] currentLine = lineList.get(0);
            Course currentCourse = new Course(currentLine[SUBJECT], currentLine[CATALOGUE_NUMBER]);
            courseList.add(currentCourse);
            lineList.remove(0);
            equivalentCourse.add(currentLine);
            //Search for course
            int i = 0;
            while (i < lineList.size()) {
//            for (int i = 0; i < lineList.size(); i ++) {
                String[] targetLine = lineList.get(i);
                if (currentLine[SUBJECT].equals(targetLine[SUBJECT])
                        && currentLine[CATALOGUE_NUMBER].equals(targetLine[CATALOGUE_NUMBER])) {
                    equivalentCourse.add(targetLine);
                    lineList.remove(i);
                    i--;
                }
                i++;
            }
            //Create Course
            while (equivalentCourse.size() > 0) {
                String[] currentOffering = equivalentCourse.get(0);

                if (currentOffering.length == DEFAULT_SIZE) {
                    String[] instructorArray = {currentOffering[INSTRUCTOR]};
                    currentCourse.addOffering(
                            Integer.parseInt(currentOffering[SEMESTER]),
                            currentOffering[LOCATION],
                            instructorArray);
                } else {
                    int size = currentOffering.length - 7;
                    String[] instructorArray = new String[size];
                    for (int j = 0; j < size; j++) {
                        instructorArray[j] = currentOffering[6 + j];
                    }
                    currentCourse.addOffering(
                            Integer.parseInt(currentOffering[SEMESTER]),
                            currentOffering[LOCATION],
                            instructorArray);
                }
                equivalentCourse.remove(0);
                Offering thisOffering = currentCourse.getOffering(currentCourse.getOfferingsSize()-1);
                thisOffering.addComponent(Integer.parseInt(currentOffering[ENROLLMENT_CAPACITY]),
                        Integer.parseInt(currentOffering[ENROLLMENT_TOTAL]),
                        currentOffering[currentOffering.length - 1]);
                int j = 0;
                while (j < equivalentCourse.size()) {
                    String[] targetOffering = equivalentCourse.get(j);
                    if (currentOffering[SEMESTER].equals(targetOffering[SEMESTER])
                            && currentOffering[LOCATION].equals(targetOffering[LOCATION])
                            && currentOffering[INSTRUCTOR].equals(targetOffering[INSTRUCTOR])){
                        thisOffering.addComponent(Integer.parseInt(targetOffering[ENROLLMENT_CAPACITY]),
                                Integer.parseInt(targetOffering[ENROLLMENT_TOTAL]),
                                targetOffering[targetOffering.length - 1]);
                        equivalentCourse.remove(j);
                        j--;
                    }
                    j++;
                }
            }

        }
    }

    private static void sortCoursesInAlphabeticalOrder(List<Course> courseList) {
        Comparator<Course> courseSorter = new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                int subjectComparison = course1.getSubject().compareTo(course2.getSubject());
                if (subjectComparison != 0) {
                    return subjectComparison;
                } else {
                    return course1.getCatalogueNumber().compareTo(course2.getCatalogueNumber());
                }
            }
        };
        java.util.Collections.sort(courseList, courseSorter);
    }


    public void dumpModel() {
        sortCoursesInAlphabeticalOrder(courseList);
        for (Course course : courseList) {
            System.out.printf("%s", course.toString());
        }
    }
}
