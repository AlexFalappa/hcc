package net.falappa.wwind.gazetteer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.store.fs.FileUtils;
import org.h2.tools.Backup;
import org.h2.tools.DeleteDbFiles;

/**
 *
 * @author Alessandro Falappa
 */
public class DbCreator {

    static final Logger logger = Logger.getLogger(DbCreator.class.getName());
    static final Set<String> featClasses = new HashSet<>();
    static final Set<String> featCodes = new HashSet<>(200);
    static final Map<String, Integer> codesPriority = new HashMap<>(250);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        final String fName = "/home/afalappa/Scaricati/geonames-2015-10-13/allCountries.txt";
        final String fName = "/home/afalappa/Sviluppo/NetBeansProjects/gazetteer/src/main/data/IT.txt";
        final String dirName = "temp";
//        final String dbName = "gztr-world";
        final String dbName = "gztr-it";
        final String dbName2 = "loc-it";
        final String zName = "gztr.zip";
        final String zPath = "/home/afalappa/" + dirName + "/" + zName;

        initFeatClasses(featClasses);
        initFeatCodes(featCodes);
        initPriorities("/home/afalappa/Sviluppo/NetBeansProjects/gazetteer/src/main/data/codes-ordered.txt");

        // delete all files in this directory
        System.out.println("Removing previous db files");
        FileUtils.deleteRecursive("~/" + dirName, false);

//        try (Connection conn = DriverManager.getConnection("jdbc:h2:split:22:~/" + dName + "/" + dbName)) {
//        try (Connection conn = DriverManager.getConnection("jdbc:h2:split:22:~/" + dName + "/" + dbName)) {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/" + dirName + "/" + dbName);
                Connection conn2 = DriverManager.getConnection("jdbc:h2:~/" + dirName + "/" + dbName2)) {
            tableCreate(conn);
            tableCreateFeatCodes(conn);
            tableCreate(conn2);
            load(fName, conn, conn2);
            loadFeatCodes("/home/afalappa/Sviluppo/NetBeansProjects/gazetteer/src/main/data/featureCodes_en.txt", conn);
            indexAndCompact(conn);
            indexAndCompact(conn2);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "DB error", ex);
        }

//        try {
//            backup(zPath, "~/" + dName);
//        } catch (SQLException ex) {
//            logger.log(Level.SEVERE, "Backup error", ex);
//        }
//        try (Connection conn = DriverManager.getConnection("jdbc:h2:split:22:~/" + dName + "/" + dbName)) {
//        try (Connection conn = DriverManager.getConnection("jdbc:h2:zip:~/" + dName + "/" + zName + "!/" + dbName)) {
//        try (Connection conn = DriverManager.getConnection("jdbc:h2:split:22:zip:~/" + dName + "/" + zName + "!/" + dbName)) {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/" + dirName + "/" + dbName);
                Connection conn2 = DriverManager.getConnection("jdbc:h2:~/" + dirName + "/" + dbName2)) {
            query(conn);
            query(conn2);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "DB error", ex);
        }
    }

    private static void backup(final String zPath, String dir) throws SQLException {
        System.out.println("Performing compressed backup");
        Backup.execute(zPath, dir, "", false);
        DeleteDbFiles.execute(dir, null, false);
    }

    private static void query(final Connection conn) throws SQLException {
        System.out.println("Querying locations");
        try (PreparedStatement pstmt = conn.prepareStatement(
                "select rank,name,latitude,longitude "
                + "from locations "
                + "where name like 'Roma%' "
                + "order by rank")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.format("%d [%g; %g]\t%s%n", rs.getLong(1), rs.getDouble(3), rs.getDouble(4), rs.getString(2));
                }
            }
        }
    }

    private static void query2(final Connection conn) throws SQLException {
        System.out.println("Querying with feature codes");
        try (PreparedStatement pstmt = conn.prepareStatement(
                "select name,short_desc "
                + "from locations join feature_codes on class=feature_class and subclass=feature_code"
                + "where subclass like 'CNL%' "
                + "order by rank,name")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.format("%s\t%s%n", rs.getString(2), rs.getString(1));
                }
            }
        }
    }

    private static void indexAndCompact(final Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating indexes");
            stmt.execute("create index idx_name on locations(name)");
            stmt.execute("create index idx_class on locations(class)");
            stmt.execute("create index idx_subclass on locations(subclass)");
            System.out.println("Compacting");
            stmt.execute("shutdown defrag");
        }
    }

    private static void load(final String fName, final Connection conn, final Connection conn2) {
        System.out.format("Inserting from %s%n", fName);
//            try (Statement stmt = conn.createStatement()) {
//                try (ResultSet rs = stmt.executeQuery(
//                        "select geonameid,name,latitude,longitude "
//                        + "from CSVREAD("
//                        + "'/home/afalappa/Scaricati/geonames-2015-10-13/allCountries.txt',"
//                        + "'GEONAMEID\tNAME\tASCIINAME\tALTERNATENAMES\tLATITUDE\tLONGITUDE\tFEATURE CLASS\tFEATURE CODE\tCOUNTRY CODE\tCC2\tADMIN1 CODE\tADMIN2 CODE\tADMIN3 CODE\tADMIN4 CODE\tPOPULATION\tELEVATION\tDEM\tTIMEZONE\tMODIFICATION DATE',"
//                        + "'UTF-8',"
//                        + "chr(9))")) {
//                    while (rs.next()) {
//                        try {
//                            int id = Integer.parseInt(rs.getString(1));
//                            double lat = Double.parseDouble(rs.getString(3));
//                            double lon = Double.parseDouble(rs.getString(4));
//                        } catch (Exception ex) {
//                            System.out.format("%s [%s; %s]\t%s%n", rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(2));
//                        }
//                    }
//                }
//            }
        try (PreparedStatement pstmt = conn.prepareStatement(
                "insert into locations(id,class,subclass,name,latitude,longitude,rank) values(?,?,?,?,?,?,?)");
                PreparedStatement pstmt2 = conn2.prepareStatement(
                        "insert into locations(id,class,subclass,name,latitude,longitude,rank) values(?,?,?,?,?,?,?)")) {
            try (BufferedReader bfr = new BufferedReader(new FileReader(new File(fName)))) {
                String line;
                int count = 1;
                while ((line = bfr.readLine()) != null) {
                    String[] fields = line.split("\t");
                    try {
                        long id = Long.parseLong(fields[0]);
                        double lat = Double.parseDouble(fields[4]);
                        double lon = Double.parseDouble(fields[5]);
                        final String featClass = fields[6];
                        final String featCode = fields[7];
                        final String name = fields[2];
                        int prio = -1;
                        if (codesPriority.containsKey(featCode)) {
                            prio = codesPriority.get(featCode);
                        } else {
                            System.out.println("Missing feat code rank for " + featCode);
                        }
                        pstmt.setLong(1, id);
                        pstmt.setString(2, featClass);
                        pstmt.setString(3, featCode);
                        pstmt.setString(4, name);
                        pstmt.setDouble(5, lat);
                        pstmt.setDouble(6, lon);
                        pstmt.setInt(7, prio);
                        pstmt.execute();
                        if (featClasses.contains(featClass) || featCodes.contains(featCode)) {
                            pstmt2.setLong(1, id);
                            pstmt2.setString(2, featClass);
                            pstmt2.setString(3, featCode);
                            pstmt2.setString(4, name);
                            pstmt2.setDouble(5, lat);
                            pstmt2.setDouble(6, lon);
                            pstmt2.setInt(7, prio);
                            pstmt2.execute();
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println(line);
                    }
                    if ((++count) % 100000 == 0) {
                        System.out.println(count);
                    }
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "IO problem", ex);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "DB error", ex);
        }
    }

    private static void loadFeatCodes(final String fName, final Connection conn) {
        System.out.format("Inserting from %s%n", fName);
        try (PreparedStatement pstmt = conn.prepareStatement(
                "insert into feature_codes(feature_class,feature_code,short_desc,long_desc,rank) values(?,?,?,?,?)")) {
            try (BufferedReader bfr = new BufferedReader(new FileReader(new File(fName)))) {
                String line;
                int count = 1;
                while ((line = bfr.readLine()) != null) {
                    String[] fields = line.split("\t");
                    pstmt.setString(1, fields[0]);
                    final String code = fields[1];
                    pstmt.setString(2, code);
                    pstmt.setString(3, fields[2]);
                    if (fields.length < 4) {
                        pstmt.setString(4, null);
                    } else {
                        pstmt.setString(4, fields[3]);
                    }
                    int prio = -1;
                    if (codesPriority.containsKey(code)) {
                        prio = codesPriority.get(code);
                    }
                    pstmt.setInt(5, prio);
                    pstmt.execute();
                    if ((++count) % 100 == 0) {
                        System.out.println(count);
                    }
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "IO problem", ex);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "DB error", ex);
        }
    }

    private static void tableCreate(final Connection conn) throws SQLException {
        System.out.println("Creating locations table");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "create table locations("
                    + "id bigint primary key,"
                    + "class varchar(1),"
                    + "subclass varchar(10),"
                    + "name varchar(200) not null,"
                    + "rank int not null,"
                    + "latitude number,"
                    + "longitude number"
                    + ")");
        }
    }

    private static void tableCreateFeatCodes(final Connection conn) throws SQLException {
        System.out.println("Creating feature codes table");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "create table feature_codes("
                    + "feature_class varchar(1),"
                    + "feature_code varchar(10),"
                    + "short_desc varchar(50),"
                    + "long_desc varchar(255),"
                    + "rank int not null,"
                    + "primary key (feature_class,feature_code)"
                    + ")");
        }
    }

    private static void initFeatClasses(Set<String> set) {
        set.add("A");
        set.add("H");
        set.add("P");
        set.add("T");
    }

    private static void initFeatCodes(Set<String> set) {
        set.add("CONT");
        set.add("MILB");
        set.add("NVB");
        set.add("PRK");
        set.add("PRT");
        set.add("AIRB");
        set.add("AIRF");
        set.add("AIRH");
        set.add("AIRP");
        set.add("CTRS");
        set.add("DAM");
        set.add("FCL");
        set.add("FT");
        set.add("HSP");
        set.add("INSM");
        set.add("ITTR");
        set.add("MAR");
        set.add("RSTN");
    }

    private static void initPriorities(String fName) {
        System.out.format("Priorities from %s%n", fName);
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File(fName)))) {
            String line;
            int prio = 1;
            while ((line = bfr.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length > 1) {
                    codesPriority.put(fields[1], prio++);
                } else {
                    System.out.print("Bad line: ");
                    System.out.println(line);
                }
            }
            System.out.println("Total priorities " + prio);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO problem", ex);
        }
    }

}
