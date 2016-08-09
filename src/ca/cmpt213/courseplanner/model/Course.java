package ca.cmpt213.courseplanner.model;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Course {

    private static final int DEFAULT_SIZE = 8;
    private static final int SEMESTER = 0;
    private static final int LOCATION = 3;
    private static final int INSTRUCTOR = 6;

    private String subject;
    private String catalogueNumber;
    private List<Offering> offerings;

    public Course(String subject,  String catalogueNumber) {
        this.subject = subject;
        this.catalogueNumber = catalogueNumber;
        offerings = new ArrayList<>();
    }

    public void addOffering(int semester, String location, String[] instructors) {
        offerings.add(new Offering(semester, location, instructors));
    }

    public String getTitle() {
        return this.subject + " " + this.catalogueNumber;
    }

    public int getCourseNumber() {
        String catalogNumber = this.catalogueNumber.replace("X", "0");
        //I have no idea what this does, Brian Fraser recommended?
        return Integer.valueOf("0" + catalogNumber.replaceAll("(\\d*).*", "$1"));
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

    public List<Offering> getAllOfferings() {
        return offerings;
    }

    public Offering getOffering(String[] line) {
        String[] instructorsArray = getInstructors(line);
        boolean allInstructorsMatch;
        for (Offering offering: offerings) {
            allInstructorsMatch = true;
            if (offering.getSemester() == Integer.parseInt(line[SEMESTER])
                    && offering.getLocation().equals(line[LOCATION])) {
                if (instructorsArray.length == offering.getInstructors().length) {
                    for (int i = 0; i < instructorsArray.length; i++) {
                        if (offering.getInstructors()[i].equals(instructorsArray[i])) {
                            // Do nothing allinstructors remains true;
                        } else {
                            allInstructorsMatch = false;
                        }
                    }
                    if (allInstructorsMatch) {
                        return offering;
                    }
                }
            }
        }
        return null;
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

    public Boolean hasEquivalentOffering(String[] line) {
        String[] instructorsArray = getInstructors(line);
        boolean allInstructorsMatch;
        for (Offering offering: offerings) {
            allInstructorsMatch = true;
            if (offering.getSemester() == Integer.parseInt(line[SEMESTER])
                    && offering.getLocation().equals(line[LOCATION])) {
                if (instructorsArray.length == offering.getInstructors().length) {
                    for (int i = 0; i < instructorsArray.length; i++) {
                        if (offering.getInstructors()[i].equals(instructorsArray[i])) {
                            // Do nothing allinstructors remains true;
                        } else {
                            allInstructorsMatch = false;
                        }
                    }
                    if (allInstructorsMatch) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String[] getInstructors(String[] line) {
        if (line.length == DEFAULT_SIZE) {
            String[] instructorArray = {line[INSTRUCTOR]};
            return instructorArray;

        } else {
            int size = line.length - 7;
            String[] instructorArray = new String[size];
            for (int j = 0; j < size; j++) {
                instructorArray[j] = line[6 + j];
            }
            return instructorArray;
        }
    }
}
