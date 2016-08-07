package ca.cmpt213.courseplanner.model;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Course {
    private String subject;
    private String catalogueNumber;
    private List<Offering> offerings = new ArrayList<>();

    public Course(String subject,  String catalogueNumber) {
        this.subject = subject;
        this.catalogueNumber = catalogueNumber;
        offerings = new ArrayList<>();
    }

    public void addOffering(int semester, String location, String[] instructors) {
        offerings.add(new Offering(semester, location, instructors));
    }

    private static void sortOfferingsBySemester(List<Offering> offeringList) {
        Comparator<Offering> offeringSorter = new Comparator<Offering>() {
            @Override
            public int compare(Offering offer1, Offering offer2) {
                return offer1.getSemester() - offer2.getSemester();
            }
        };
        java.util.Collections.sort(offeringList, offeringSorter);
    }

    private String getOfferingsString() {
        sortOfferingsBySemester(offerings);
        String result = "";
        for (Offering offering : offerings) {
            result += "\t" + offering.toString() + "\n";
        }
        return result;
    }

    @Override
    public String toString() {
        return subject + " " + catalogueNumber + "\n" + getOfferingsString();
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
