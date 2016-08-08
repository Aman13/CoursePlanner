package ca.cmpt213.courseplanner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.sort;

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

//    private String line;

    private HashMap<String, Department> departments;

    private List<Course> courseList;
    private List<String[]> lineList;

    public Model () {
        courseList = new ArrayList<>();
        lineList = new ArrayList<>();
        try {
            getData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parseFile();
    }

    private void parseFile() {
        departments = new HashMap<>();
        for (String[] line: lineList) {
            Course currentCourse = new Course(line[SUBJECT], line[CATALOGUE_NUMBER]);

            //First Entry
            if (departments.size() == 0) {
                Department newDepartment = new Department(currentCourse.getSubject());
                departments.put(newDepartment.getName(), newDepartment);
                //Add offering
                addOffering(line, currentCourse);
                //Add course component
                Offering currentOffering = currentCourse.getOffering(currentCourse.getOfferingsSize() - 1);
                currentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
                        Integer.parseInt(line[ENROLLMENT_TOTAL]),
                        line[line.length - 1]);
                //Add Course to Department
                newDepartment.addCourse(currentCourse);

            } else if (departments.containsKey(currentCourse.getSubject())) {
                //Department exists add course to existing department
                Department department = departments.get(currentCourse.getSubject());

                if (department.hasCourse(currentCourse)) {
                    //Course exists add new offering or section
                    Course equivalentCourse = department.getCourse(currentCourse);

                    //Check if offering exists
                    if (equivalentCourse.hasEquivalentOffering(line)) {
                        //offering exists
                        Offering equivalentOffering = equivalentCourse.getOffering(line);
                        //Add component to existing offering
                        equivalentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
                                Integer.parseInt(line[ENROLLMENT_TOTAL]),
                                line[line.length - 1]);
                    } else {
                        //Offering doesn't exist add new offering AND course component
                        //Add offering
                        addOffering(line, equivalentCourse);
                        //Add course component
                        Offering currentOffering = currentCourse.getOffering(currentCourse.getOfferingsSize() - 1);
                        currentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
                                Integer.parseInt(line[ENROLLMENT_TOTAL]),
                                line[line.length - 1]);
                    }
                } else {
                    //Add new course to same department
                    //Add offering
                    addOffering(line, currentCourse);
                    //Add section
                    Offering currentOffering = currentCourse.getOffering(currentCourse.getOfferingsSize() - 1);
                    currentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
                            Integer.parseInt(line[ENROLLMENT_TOTAL]),
                            line[line.length - 1]);
                    //Add Course to Department
                    department.addCourse(currentCourse);
                }
            } else {
                //Department does not exist for this course
                Department newDepartment = new Department(currentCourse.getSubject());
                departments.put(newDepartment.getName(), newDepartment);
                addOffering(line, currentCourse);
                Offering currentOffering = currentCourse.getOffering(currentCourse.getOfferingsSize() - 1);
                currentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]), Integer.parseInt(line[ENROLLMENT_TOTAL]), line[line.length - 1]);
            }

        }
    }

    private void addOffering(String[] line, Course currentCourse) {
        if (line.length == DEFAULT_SIZE) {
            String[] instructorArray = {line[INSTRUCTOR]};
            currentCourse.addOffering(
                    Integer.parseInt(line[SEMESTER]),
                    line[LOCATION],
                    instructorArray);
        } else {
            int size = line.length - 7;
            String[] instructorArray = new String[size];
            for (int j = 0; j < size; j++) {
                instructorArray[j] = line[6 + j];
            }
            currentCourse.addOffering(
                    Integer.parseInt(line[SEMESTER]),
                    line[LOCATION],
                    instructorArray);
        }

    }

    public void getData() throws FileNotFoundException{
        try {
            Scanner scanner = new Scanner(new File(COURSE_DATA_FILE));
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //System.out.println(line);
                String[] courseData = line.split(",");
                lineList.add(courseData);
            }
            scanner.close();
        } catch(Exception e) {
            System.out.println("problems");
        }
//        buildCourseObjects();
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
        Comparator<Course> courseSorter = (course1, course2) -> {
            int subjectComparison = course1.getSubject().compareTo(course2.getSubject());
            if (subjectComparison != 0) {
                return subjectComparison;
            } else {
                return course1.getCatalogueNumber().compareTo(course2.getCatalogueNumber());
            }
        };
        sort(courseList, courseSorter);
    }

    public void printSubject() {
        for (Course course : courseList) {
            System.out.println(course.getSubject());
        }
    }

    public void dumpModel() {
//        sortCoursesInAlphabeticalOrder(courseList);
//        for (Course course : courseList) {
//            System.out.printf("%s", course.toString());
//        }
        for (Department department : departments.values()) {
            Course[] courses = department.getAllCourses();
            for (Course course : courses) {
                System.out.println(course.toString());
            }
        }
    }
}
