package ca.cmpt213.courseplanner.model;

import java.util.HashMap;

public class Department {
    private String name;
    private HashMap<String, Course> courseList;

    public Department(String name) {
        this.name = name;
        courseList = new HashMap<String, Course>();
    }

    public void addCourse(Course course) {
        courseList.put(course.getTitle(), course);
    }

    public boolean hasCourse(Course course) {
        return courseList.containsValue(course);
    }

    public Course getCourse(Course course) {
        return courseList.get(course.getTitle());
    }

    public String getName() {
        return this.name;
    }

    public Course[] getAllCourses() {
        Course[] array = new Course[courseList.size()];
        courseList.values().toArray(array);
        return array;
    }

}
