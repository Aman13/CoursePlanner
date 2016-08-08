package ca.cmpt213.courseplanner.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

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

    public String[] getInstructors() {
        return instructors;
    }

    public String getLocation() {
        return this.location;
    }

    public int getSemester() {
        return this.semester;
    }

    private String getInstructorsString() {
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
