package net.falappa.wwind.posparser;

import gov.nasa.worldwind.geom.Position;

/**
 * A {@link PositionParser} for space separated Lat Lon strings.
 * <p>
 * Lat and lon should be double numbers
 *
 * @author Alessandro Falappa
 */
public class LatLonParser implements PositionParser {

    @Override
    public Position parseString(String text) {
        Position ret = null;
        // split text by space
        String[] numbers = text.split("\\s");
        // if there are exactly two components...
        if (numbers.length == 2) {
            try {
                // ... parse them as doubles...
                double lat = Double.valueOf(numbers[0]);
                double lon = Double.valueOf(numbers[1]);
                // ... return the position from those numbers
                return Position.fromDegrees(lat, lon);
            } catch (NumberFormatException nfe) {
                // do nothing will return null
            }
        }
        return ret;
    }

}
