package net.falappa.wwind.posparser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.falappa.wwind.helpers.LabeledPosition;

/**
 * An abstract base {@link PositionParser} implementation referencing a database table.
 * <p>
 * Subclasses should implement the {@link #execQuery(java.lang.String)} method and use the protected {@code PreparedStatement pstmt}. The
 * {@link #dispose()} method should be called to free database resources (statement and connection).
 * <p>
 * This base class can be used to implement a <a href="https://en.wikipedia.org/wiki/Gazetteer">Gazetteer</a>. Free gazetteer data can be
 * taken for example from <a href="http://www.geonames.org">GeoNames</a>.
 *
 * @author Alessandro Falappa
 */
public abstract class AbstractDbParser implements PositionParser {

    protected final String jdbcUrl;
    protected final String searchStatement;
    protected final int minimumChars;
    protected final PreparedStatement pstmt;
    protected final Connection conn;

    /**
     * Constructs and initializes the parser based on the specified parameters.
     *
     * @param jdbcUrl JDBC connection URL
     * @param searchStatement lookup query string
     * @param minimumChars minimum characters to effectively query the database
     * @throws SQLException
     */
    public AbstractDbParser(String jdbcUrl, String searchStatement, int minimumChars) throws SQLException {
        this.jdbcUrl = jdbcUrl;
        this.searchStatement = searchStatement;
        this.minimumChars = minimumChars;
        this.conn = DriverManager.getConnection(jdbcUrl);
        this.pstmt = conn.prepareStatement(searchStatement);
    }

    /**
     * Constructs and initializes the parser based on the specified parameters and 3 minimum characters.
     *
     * @param jdbcUrl
     * @param searchStatement
     * @throws SQLException
     */
    public AbstractDbParser(String jdbcUrl, String searchStatement) throws SQLException {
        this(jdbcUrl, searchStatement, 3);
    }

    @Override
    public LabeledPosition parseString(String text) {
        LabeledPosition ret = null;
        // executes query for at least a defined minimum set of characters
        if (text.length() >= minimumChars) {
            ret = execQuery(text);
        }
        return ret;
    }

    /**
     * Performs the search query.
     * <p>
     * Should not raise exceptions.
     *
     * @param text the search string from the {@link PositionParser#parseString(java.lang.String)} method
     * @return the position found or null for no match
     */
    protected abstract LabeledPosition execQuery(String text);

    @Override
    public abstract String getFormatDescription();

    /**
     * Getter for the minimum chars property.
     *
     * @return
     */
    public int getMinimumChars() {
        return minimumChars;
    }

    /**
     * Getter for the database JDBC URL.
     *
     * @return
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     * Getter for the search query string.
     *
     * @return
     */
    public String getSearchStatement() {
        return searchStatement;
    }

    /**
     * Disposes the database resources (db connection and statement).
     *
     * @throws SQLException
     */
    public void dispose() throws SQLException {
        // close statement and db
        pstmt.close();
        conn.close();
    }

}
