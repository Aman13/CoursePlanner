package ca.cmpt213.courseplanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model {
    File courseDataFile;
    String line;
    private List<Course> courseList = new ArrayList<>();

    public Model () {

    }

    public void getData() throws FileNotFoundException{
        try {
            courseDataFile = new File("data/course_data_2016.csv");
            Scanner scanner = new Scanner(courseDataFile);
            while (scanner.hasNextLine());
            line = scanner.nextLine();
            String[] courseData = line.split(",");
            for (String entry : courseData) {
                System.out.println(entry);
            }
        } catch(Exception e) {
            System.out.println("problems");
        }

    }


    void dumpModel() throws FileNotFoundException {
//        while (scanner.hasNextLine()) {
//            line = scanner.nextLine();
//            String[] courseData = line.split(",");
//            int semester = Integer.parseInt(courseData[0]);
//            String Subject = courseData[1];
//
//            System.out.printf()
//        }
    }
}
