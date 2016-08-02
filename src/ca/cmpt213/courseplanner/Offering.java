package ca.cmpt213.courseplanner;

import java.util.ArrayList;
import java.util.List;

public class Offering {

    private int semester;
    private String location;
    private String[] instructors;

    List<CourseComponent> courseComponents = new ArrayList<>();

    public Offering (int semester, String location, String[] instructors) {
        this.semester = semester;
        this.location = location;
        this. instructors = instructors;
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

}
