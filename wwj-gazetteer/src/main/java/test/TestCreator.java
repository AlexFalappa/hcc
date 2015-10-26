package test;

import java.sql.SQLException;
import net.falappa.wwind.posparser.H2DbParser;

/**
 *
 * @author Alessandro Falappa
 */
public class TestCreator {

    public static void main(String[] args) throws SQLException {
        H2DbParser lp = new H2DbParser(System.getProperty("user.home") + "/temp/gztr-it.mv.db");
//        LocationPosParser lp = new LocationPosParser(System.getProperty("user.home") + "/temp/loc-it");
        String term = "zzz";
        System.out.printf("Position of '%s': %s%n", term, String.valueOf(lp.parseString(term)));
        term = "aa";
        System.out.printf("Position of '%s': %s%n", term, String.valueOf(lp.parseString(term)));
        term = "Sassa";
        System.out.printf("Position of '%s': %s%n", term, String.valueOf(lp.parseString(term)));
        term = "Cagl";
        System.out.printf("Position of '%s': %s%n", term, String.valueOf(lp.parseString(term)));
        term = "Roma ";
        System.out.printf("Position of '%s': %s%n", term, String.valueOf(lp.parseString(term)));
        term = "Roma";
        System.out.printf("Position of '%s': %s%n", term, String.valueOf(lp.parseString(term)));
        System.out.println("--------");
        term = "Sassa";
        System.out.println("Search of " + term);
//        lp.search(term);
        System.out.println("--------");
        term = "Cagl";
        System.out.println("Search of " + term);
//        lp.search(term);
        System.out.println("--------");
        term = "Roma ";
        System.out.println("Search of " + term);
//        lp.search(term);
        System.out.println("--------");
        term = "Roma";
        System.out.println("Search of " + term);
//        lp.search(term);
        lp.dispose();
    }
}
