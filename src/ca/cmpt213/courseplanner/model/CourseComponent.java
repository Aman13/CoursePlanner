package ca.cmpt213.courseplanner.model;

/**
 * Course component contains the enrollment,
 * and component code. Used by the offering.
 * @author Aram & Aman
 */
public class CourseComponent {

    private String componentCode;
    private int enrollmentCapacity;
    private int enrollmentTotal;

    public CourseComponent (int enrollmentCapacity, int enrollmentTotal, String componentCode) {
        this.componentCode = componentCode;
        this.enrollmentCapacity = enrollmentCapacity;
        this.enrollmentTotal = enrollmentTotal;
    }

    public String getComponentCode() {
        return this.componentCode;
    }

    public int getEnrollmentCapacity() {
        return this.enrollmentCapacity;
    }

    public int getEnrollmentTotal() {
        return this.enrollmentTotal;
    }

    @Override
    public String toString() {
        return "Type=" + componentCode + ", Enrollment=" + enrollmentTotal + "/" + enrollmentCapacity;
    }
}
