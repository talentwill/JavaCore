package core2.chapter03.homework;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class StudentInfo {

    public static void setDefaultLocale() {
        // if current locale is not china, set as china.
        if (!Locale.getDefault().equals(Locale.CHINA)) {
            Locale.setDefault(Locale.CHINA);
//            Locale.setDefault(new Locale("zh", "CN"));
        }
    }

    public static void main(String[] args) {
        setDefaultLocale();

        printUniversityAndName(new Locale("zh", "CN"));
        printUniversityAndName(new Locale("en", "NZ"));
        printUniversityAndName(new Locale("en", "US"));
    }

    private static void printUniversityAndName(Locale locale) {
        System.out.println("Locale = " + locale.getDisplayName());

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("student", locale);
            String university = bundle.getString("university");
            System.out.println("University is " + university);
        } catch (MissingResourceException e) {
            System.out.println(e.getMessage());
        }

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("student", locale);
            String name = bundle.getString("name");
            System.out.println("Name is " + name);
        } catch (MissingResourceException e) {
            System.out.println(e.getMessage());
        }
    }
}

