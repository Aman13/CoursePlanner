package ca.cmpt213.courseplanner.model;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

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
    public static final File OUTPUT = new File("data/output_dump.txt");

    private HashMap<String, Department> departments;

    private List<String[]> lineList;

    private List<CoursePlannerObserver> observers;
    private String departmentSelected;
    private Course[] currentCourses;
    private Course courseSelected;
    private List<Offering> currentOfferings;
    private Offering offeringSelected;

    public Model () {
        lineList = new ArrayList<>();
        try {
            getData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        buildDepartments();
        observers = new ArrayList<>();
        currentOfferings = new ArrayList<>();
        departmentSelected = "";
    }

    public void setCurrentDepartment(String department, Boolean underGradCheck, Boolean gradCheck) {
        departmentSelected = department;
        if (underGradCheck || gradCheck) {
            currentCourses = filterCourses(departments.get(department).getAllCourses(), underGradCheck, gradCheck);
            notifyObservers();
        }
    }

    private Course[] filterCourses(Course[] allCourses, Boolean underGrad, Boolean grad) {
      ArrayList<Course> filteredCourseList = new ArrayList<>();
        for (Course course: allCourses) {
            if (underGrad && grad) {
                filteredCourseList.add(course);
            } else if (underGrad && course.getCourseNumber() < 500 ){
                filteredCourseList.add(course);
            } else if (grad && course.getCourseNumber() >= 500) {
                filteredCourseList.add(course);
            }
        }
        return filteredCourseList.toArray(new Course[filteredCourseList.size()]);
    };

    public void setCurrentCourse(Course course) {
        courseSelected = course;
        currentOfferings = course.getAllOfferings();
        notifyObservers();
    }

    public void setCurrentOffering(Offering offering) {
        offeringSelected = offering;
        notifyObservers();
    }

    public String getDepartmentSelected() {
        return departmentSelected;
    }

    public Course[] getCurrentCourses() {
        sortCurrentCourses(currentCourses);
        return currentCourses;
    }

    public Course getCourseSelected() {
        return courseSelected;
    }

    public List<Offering> getCurrentOfferings() {
        return currentOfferings;
    }

    public Offering getOfferingSelected() {
        return offeringSelected;
    }

    private static void sortCurrentCourses(Course[] courses) {
        Comparator<Course> courseSorter = new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                return course1.getTitle().compareTo(course2.getTitle());
            }
        };
        Arrays.sort(courses, courseSorter);
    }


    private void buildDepartments() {
        departments = new HashMap<>();
        for (String[] line: lineList) {
            Course currentCourse = new Course(line[SUBJECT], line[CATALOGUE_NUMBER]);

            //First Entry
            if (departments.size() == 0) {
                Department newDepartment = new Department(currentCourse.getSubject());
                departments.put(newDepartment.getName(), newDepartment);
                addOffering(line, currentCourse);
                Offering currentOffering = currentCourse.getOffering(currentCourse.getOfferingsSize() - 1);
                currentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
                        Integer.parseInt(line[ENROLLMENT_TOTAL]),
                        line[line.length - 1]);
                newDepartment.addCourse(currentCourse);

            // Check if Department exists add course to existing department
            } else if (departments.containsKey(currentCourse.getSubject())) {
                Department department = departments.get(currentCourse.getSubject());

                // Check if Course exists (cmpt 322w)
                if (department.hasCourse(currentCourse)) {
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
                        Offering currentOffering = equivalentCourse.getOffering(equivalentCourse.getOfferingsSize() - 1);
                        currentOffering.addComponent(Integer.parseInt(line[ENROLLMENT_CAPACITY]),
                                Integer.parseInt(line[ENROLLMENT_TOTAL]),
                                line[line.length - 1]);
                    }
                } else {
                    //Add new course to same department
                    addOffering(line, currentCourse);
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
                newDepartment.addCourse(currentCourse);
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
    }

    public String[] getDepartmentNames() {
        String[] departmentNames = new String [departments.size()];
        departments.keySet().toArray(departmentNames);
        Arrays.sort(departmentNames);
        return departmentNames;
    }


    public void dumpModel() {
//        for (Department department : departments.values()) {
//            Course[] courses = department.getAllCourses();
//            for (Course course : courses) {
//                System.out.println(course.toString());
//            }
//        }
        try {
            PrintWriter writer = new PrintWriter(OUTPUT);
            for (Department department : departments.values()) {
                Course[] courses = department.getAllCourses();
                for (Course course : courses) {
                    writer.printf(course.toString());
                }
            }
            writer.close();
            System.out.println("Course info written to output file: " + OUTPUT.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addObserver(CoursePlannerObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (CoursePlannerObserver observer : observers) {
            observer.stateChanged();
        }
    }

}
