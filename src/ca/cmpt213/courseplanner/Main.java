package ca.cmpt213.courseplanner;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Model model = new Model();
        model.getData();
    }
}
