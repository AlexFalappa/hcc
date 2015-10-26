package net.falappa.wwind.posparser;

import gov.nasa.worldwind.geom.LatLon;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.falappa.wwind.helpers.LabeledPosition;

/**
 * A {@link PositionParser} for space or comma separated Lat Lon in decimal degrees format and optional elevation in meters.
 * <p>
 * Lat and lon should be double numbers, elevation positive integer
 *
 * @author Alessandro Falappa
 */
public class LatLonParser implements PositionParser {

    private final Pattern pattern = Pattern.compile(
            "([+|-]?\\d{1,2}(?:\\.\\d*)?)\\s*°?" // signed latitude and optional degree character
            + "[\\s|,]+" // space or comma separators
            + "([+|-]?\\d{1,3}(?:\\.\\d*)?)\\s*°?" // signed longitude and optional degree character
            + "[\\s|,]*" // optional space or comma separators
            + "(\\d+)?" // optional integer elevation
    );

    @Override
    public LabeledPosition parseString(String text) {
        Matcher m = pattern.matcher(text);
        if (m.matches()) {
            try {
                if (m.groupCount() >= 2) {
                    double lat = Double.valueOf(m.group(1));
                    double lon = Double.valueOf(m.group(2));
                    double elev = DEFAULT_ELEVATION;
                    if (m.groupCount() > 2 && m.group(3) != null) {
                        elev = Double.valueOf(m.group(3));
                    }
                    return LabeledPosition.fromLatLonAutoLabel(LatLon.fromDegrees(lat, lon), elev);
                }
            } catch (NumberFormatException nfe) {
                // do nothing will return null
            }
        }
        return null;
    }

    @Override
    public String getFormatDescription() {
        return "Lat (deg) [,] Lon (deg) [ [,] Elev (m)]";
    }

}
