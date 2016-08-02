package ca.cmpt213.courseplanner;


import java.util.ArrayList;
import java.util.List;

public class Course {
    private String subject;
    private String catalogueNumber;
    private List<Offering> offerings = new ArrayList<>();

    public Course(String subject,  String catalogueNumber) {
        this.subject = subject;
        this.catalogueNumber = catalogueNumber;
    }

    public void addOfferingt(int semester, String location, String[] instructors) {
        offerings.add(new Offering(semester, location, instructors));
    }

    @Override
    public String toString() {
        return "";
    }

    public Offering getOffering(int index) {
        return this.offerings.get(index);
    }

    public String getSubject() {
        return this.subject;
    }

    public String getCatalogueNumber() {
        return this.catalogueNumber;
    }

    public int getOfferingsSize() {
        return offerings.size();
    }
}
