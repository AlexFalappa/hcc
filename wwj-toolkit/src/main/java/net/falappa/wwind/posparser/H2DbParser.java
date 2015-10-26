package net.falappa.wwind.posparser;

import gov.nasa.worldwind.geom.Position;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.falappa.wwind.helpers.LabeledPosition;
import static net.falappa.wwind.posparser.PositionParser.DEFAULT_ELEVATION;

/**
 * A {@link PositionParser} referencing an external gazetteer database.
 * <p>
 * The database should be an H2 database containing a LOCATIONS table with the following columns:
 * <ul>
 * <li>ID BIGINT PRIMARY KEY
 * <li>CLASS VARCHAR(1)
 * <li>SUBCLASS VARCHAR(10)
 * <li>NAME VARCHAR(200) NOT NULL
 * <li>RANK INT NOT NULL
 * <li>LATITUDE NUMBER
 * <li>LONGITUDE NUMBER
 * </ul>
 * <p>
 * The H2 database file must exist, the database is opened read only and lookup is case insensitive.
 *
 * @author Alessandro Falappa
 */
public class H2DbParser extends AbstractDbParser {

    /**
     * Builds a new gazetter db parser from the given database file and minimum characters.
     * <p>
     * <b>NOTE:</b> the file must be a real file on filesystem, it must not be contained in a jar.
     *
     * @param gazDbPath the H2 database file path. The H2 connection string is built as
     * {@code jdbc:h2:<gazDbPath>;IFEXISTS=TRUE;ACCESS_MODE_DATA=r}. File extensions "{@code .h2.db}" or "{@code .mv.db}" must not be
     * present.
     * @param minimumChars minimum numbers of chars to effectively query the database
     * @throws java.sql.SQLException in case of problems opening the database
     */
    public H2DbParser(String gazDbPath, int minimumChars) throws SQLException {
        super(String.format("jdbc:h2:%s;IFEXISTS=TRUE;ACCESS_MODE_DATA=r;TRACE_LEVEL_FILE=0;TRACE_LEVEL_SYSTEM_OUT=0", gazDbPath),
                "select name,latitude,longitude from locations where lower(name) like ? || '%' order by rank,name limit 1",
                minimumChars);
    }

    /**
     * Builds a new gazetter db parser from the given database file.
     * <p>
     * <b>NOTE:</b> the file must be a real file on filesystem, it must not be contained in a jar.
     *
     * @param gazDbPath the H2 database file path. The H2 connection string is built as
     * {@code jdbc:h2:<gazDbPath>;IFEXISTS=TRUE;ACCESS_MODE_DATA=r}. File extensions "{@code .h2.db}" or "{@code .mv.db}" must not be
     * present.
     * @throws java.sql.SQLException in case of problems opening the database
     */
    public H2DbParser(String gazDbPath) throws SQLException {
        this(gazDbPath, 3);
    }

    @Override
    protected LabeledPosition execQuery(String text) {
        try {
            pstmt.setString(1, text.toLowerCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                // return match
                if (rs.next()) {
                    return LabeledPosition.fromDegrees(rs.getString(1), rs.getDouble(2), rs.getDouble(3), DEFAULT_ELEVATION);
                }
            }
        } catch (SQLException ex) {
            // ignore will return null (no match found)
        }
        return null;
    }

    @Override
    public String getFormatDescription() {
        return "City, mountain, lake, etc.";
    }

}
