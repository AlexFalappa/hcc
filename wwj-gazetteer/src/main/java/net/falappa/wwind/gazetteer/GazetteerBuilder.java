package net.falappa.wwind.gazetteer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.falappa.wwind.posparser.H2DbParser;

/**
 *
 * @author Alessandro Falappa
 */
public final class GazetteerBuilder {

    private static final Logger logger = Logger.getLogger(GazetteerBuilder.class.getName());

    // prevent instantiation
    private GazetteerBuilder() {
    }

    /**
     * Prepares an {@link H2DbParser} using the {@code loc-it} database in the classpath.
     * <p>
     * The database file is first extracted into a temporary directory.
     *
     * @return the parser or null in case of creation problems
     */
    public static H2DbParser buildItaGazetteer() {
        try {
            String gazItaDb = "loc-it";
            String gazItaDbFile = gazItaDb.concat(".mv.db");
            InputStream is = GazetteerBuilder.class.getResourceAsStream("/net/falappa/wwind/gazetteer/" + gazItaDbFile);
            if (is != null) {
                // extract the db file from the jar and put it in a temporary directory
                Path tempDir = Files.createTempDirectory("wwind-gazetteer");
                Path dbPath = tempDir.resolve(gazItaDbFile);
                Files.copy(is, dbPath, StandardCopyOption.REPLACE_EXISTING);
                // use it for a new parser
                return new H2DbParser(tempDir.toString() + "/" + gazItaDb);
            }
        } catch (SQLException | IOException ex) {
            logger.log(Level.SEVERE, "Could not create italian gazetteer");
            logger.throwing("GazetteerBuilder", "buildItaGazetteer", ex);
        }
        return null;
    }
}
