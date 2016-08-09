package ca.cmpt213.courseplanner.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Offering {
    private static final int ENROLLMENT_CAPACITY = 4;
    private static final int ENROLLMENT_TOTAL = 5;

    private int semester;
    private String location;
    private String[] instructors;

    List<CourseComponent> courseComponents = new ArrayList<>();

    public Offering (int semester, String location, String[] instructors) {
        this.semester = semester;
        this.location = location;
        this.instructors = instructors;
        courseComponents = new ArrayList<>();
    }

    public void addComponent(int enrollmentCapacity, int enrollmentTotal, String componentCode) {
        courseComponents.add(new CourseComponent(enrollmentCapacity, enrollmentTotal, componentCode));
    }

    public List<CourseComponent> getCurrentCourseComponents() {
        List<CourseComponent> currentComponents = new ArrayList<>();
        if (courseComponents.size() > 0) {
            List<CourseComponent> allComponents = new ArrayList<>(courseComponents);
            String newCode = "";
            int newCapacity = 0;
            int newTotal = 0;
            while (allComponents.size() > 0) {
                newCode = allComponents.get(0).getComponentCode();
                newCapacity = allComponents.get(0).getEnrollmentCapacity();
                newTotal = allComponents.get(0).getEnrollmentTotal();
                allComponents.remove(0);
                int j = 0;
                while (j < allComponents.size()) {
                    if (newCode.equals(allComponents.get(j).getComponentCode())) {
                        newCapacity += allComponents.get(j).getEnrollmentCapacity();
                        newTotal += allComponents.get(j).getEnrollmentTotal();
                        allComponents.remove(j);
                        j--;
                    }
                    j++;
                }
                currentComponents.add(new CourseComponent(newCapacity, newTotal, newCode));
            }
            return currentComponents;
        }
        return currentComponents;
    }

    public String[] getInstructors() {
        return instructors;
    }

    public String getLocation() {
        return this.location;
    }

    public int getSemester() {
        return this.semester;
    }

    public int getYear() {
        int termCode = semester;
        int century;
        int year;
        termCode = termCode / 10;
        year = termCode % 100;
        termCode = termCode / 100;
        century = termCode % 10;
        if (century  == 0) {
            // 20th century
            year  = year + 1900;
        } else if (century == 1) {
            // 21st century
            year = year + 2000;
        }
        return year;
    }

    public String getSemesterTitle() {
        int semesterCode = semester % 10;
        if (semesterCode == 1) {
            return "Spring";
        } else if (semesterCode == 4) {
            return "Summer";
        } else if (semesterCode == 7) {
            return "Fall";
        }
        else return "";
    }

    public String getInstructorsString() {
        String result = "";
        for (String instructor : instructors) {
            if (instructor.equals("(null)")) {
                return result;
            }
            instructor = instructor.replace("\"", "");
            instructor = instructor.replace(",", "");
            result += instructor + ", ";
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }

    private String getComponentsString() {
        String result = "";
        for (CourseComponent component : courseComponents) {
            result += "\t\t" + component.toString() + "\n";
        }
        return result;
    }

    @Override
    public String toString() {
        return semester + " in " + getLocation() + " by " + getInstructorsString() + "\n" + getComponentsString();
    }

    public Boolean hasEquivalentComponent(String[] line) {
        for (CourseComponent component: courseComponents) {
            if (line[line.length - 1] == component.getComponentCode()
                && Integer.parseInt(line[ENROLLMENT_TOTAL]) == component.getEnrollmentTotal()
                && Integer.parseInt(line[ENROLLMENT_CAPACITY]) == component.getEnrollmentCapacity()) {
                return true;
            }
        }
        return false;
    }

}
