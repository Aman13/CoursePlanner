package ca.cmpt213.courseplanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Course {
    private int semester;
    private int enrollmentCapacity;
    private int enrollment;

    private String subject;
    private String catalogNumber;
    private String location;
    private String componentCode;

    private String[] instructors;

    public Course(int semester,
                  String subject,
                  String catalogNumber,
                  String location,
                  int enrollmentCapacity,
                  int enrollment,
                  String[] instructors,
                  String componentCode) {
        this.semester = semester;
        this.subject = subject;
        this.catalogNumber = catalogNumber;
        this.location = location;
        this.enrollmentCapacity = enrollmentCapacity;
        this.enrollment = enrollment;
        this.instructors = instructors;
        this.componentCode = componentCode;
    }
}
