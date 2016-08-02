package ca.cmpt213.courseplanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

    }

    public void getData() throws FileNotFoundException{
        try {
            Scanner scanner = new Scanner(new File(COURSE_DATA_FILE));
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                System.out.println(line);
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
            List<String[]> equivalentOffering = new ArrayList<>();
            List<String[]> equivalentComponent = new ArrayList<>();
            // Gather all same course
            String[] currentLine = lineList.get(0);
            Course currentCourse = new Course(currentLine[SUBJECT], currentLine[CATALOGUE_NUMBER]);
            lineList.remove(0);
            equivalentCourse.add(currentLine);
            //Search for course
            for (int i = 0; i < lineList.size(); i ++) {
                String[] targetLine = lineList.get(i);
                if (currentLine[1] == targetLine[1] && currentLine[2] == targetLine[2]) {
                    equivalentCourse.add(targetLine);
                }
            }
            //Create Course
            while (equivalentCourse.size() > 0) {
                String[] currentOffering = equivalentCourse.get(0);

                if (currentOffering.length == DEFAULT_SIZE) {
                    String[] instructorArray = {currentOffering[INSTRUCTOR]};
                    currentCourse.addOfferingt(
                            Integer.parseInt(currentOffering[SEMESTER]),
                            currentOffering[LOCATION],
                            instructorArray);
                } else {
                    int size = currentOffering.length - 7;
                    String[] instructorArray = new String[size];
                    for (int j = 0; j < size; j++) {
                        instructorArray[j] = currentLine[6 + j];
                    }
                    currentCourse.addOfferingt(
                            Integer.parseInt(currentOffering[SEMESTER]),
                            currentOffering[LOCATION],
                            instructorArray);
                }
                equivalentCourse.remove(0);
                Offering thisOffering = currentCourse.getOffering(currentCourse.getOfferingsSize()-1);
                thisOffering.addComponent(Integer.parseInt(currentOffering[ENROLLMENT_CAPACITY]),
                        Integer.parseInt(currentOffering[ENROLLMENT_TOTAL]),
                        currentOffering[currentOffering.length - 1]);

                for (int i = 0; i < equivalentCourse.size(); i++) {
                    String[] targetOffering = equivalentCourse.get(i);
                    if (currentOffering[ENROLLMENT_CAPACITY].equals(targetOffering[ENROLLMENT_CAPACITY])
                            && currentOffering[ENROLLMENT_TOTAL].equals(targetOffering[ENROLLMENT_TOTAL])
                            && currentOffering[currentOffering.length - 1].equals(targetOffering[targetOffering.length - 1])) {
                        thisOffering.addComponent(Integer.parseInt(targetOffering[ENROLLMENT_CAPACITY]),
                                Integer.parseInt(targetOffering[ENROLLMENT_TOTAL]),
                                targetOffering[targetOffering.length - 1]);
                    }
                }
            }

        }
    }

//    private void buildCourseObjects() {
//        while (lineList.size() > 0) {
//            String[] currentLine = lineList.get(0);
//            int semester = Integer.parseInt(currentLine[SEMESTER]);
//            if (currentLine.length == DEFAULT_SIZE) {
//                String[] instructorArray = {currentLine[INSTRUCTOR]};
//                courseList.add(new Course(
//                        semester,
//                        currentLine[SUBJECT],
//                        currentLine[CATALOGUE_NUMBER],
//                        currentLine[LOCATION],
//                        instructorArray));
//            } else {
//                int size = currentLine.length - 7;
//                String[] instructorArray = new String[size];
//                for (int j = 0; j < size; j++) {
//                    instructorArray[j] = currentLine[6 + j];
//                }
//                courseList.add(new Course(
//                        semester,
//                        currentLine[SUBJECT],
//                        currentLine[CATALOGUE_NUMBER],
//                        currentLine[LOCATION],
//                        instructorArray));
//            }
//            lineList.remove(0);
//            Course currentCourse = courseList.get(courseList.size() - 1);
//            for (int i = 0; i < lineList.size(); i++ ) {
//                String[] line = lineList.get(i);
//                if (currentCourse.getSemester() == Integer.parseInt(line[SEMESTER])
//                && currentCourse.getSubject().equals(line[SUBJECT])
//                && currentCourse.getCatalogueNumber().equals(line[CATALOGUE_NUMBER])
//                && currentCourse.getInstructors()[0].equals(line[INSTRUCTOR])) {
//                    currentCourse.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
//                            Integer.parseInt(line[ENROLLMENT_TOTAL]),
//                            line[line.length - 1]);
//                    lineList.remove(i);
//                }
//            }
//        }
//    }


    void dumpModel() {
        for (Course course : courseList) {
            course.toString();
        }
    }
}
