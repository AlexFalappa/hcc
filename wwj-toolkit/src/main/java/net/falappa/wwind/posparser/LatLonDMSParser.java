package net.falappa.wwind.posparser;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import net.falappa.wwind.helpers.LabeledPosition;

/**
 * A {@link PositionParser} for comma separated Lat Lon in degrees minutes seconds format and optional elevation in meters.
 * <p>
 * English (N S E W) letters for cardinal directions are supported.
 *
 * @author Alessandro Falappa
 */
public class LatLonDMSParser implements PositionParser {

    @Override
    public LabeledPosition parseString(String text) {
        LabeledPosition ret = null;
        // split text by space
        String[] parts = text.split(",");
        switch (parts.length) {
            case 2:
                // if there are exactly two components...
                try {
                    // ... parse them as angles in DMS...
                    Angle lat = Angle.fromDMS(parts[0].trim());
                    Angle lon = Angle.fromDMS(parts[1].trim());
                    // ... return the position from those numbers
                    ret = LabeledPosition.fromLatLonAutoLabel(new LatLon(lat, lon), DEFAULT_ELEVATION);
                } catch (Exception e) {
                    // do nothing will return null
                }
                break;
            case 3:
                // if there are exactly three components...
                try {
                    // ... parse them as angles in DMS...
                    Angle lat = Angle.fromDMS(parts[0].trim());
                    Angle lon = Angle.fromDMS(parts[1].trim());
                    double elev = Double.valueOf(parts[2].trim());
                    // ... return the position from those numbers
                    ret = LabeledPosition.fromLatLonAutoLabel(new LatLon(lat, lon), elev);
                } catch (Exception e) {
                    // do nothing will return null
                }
                break;
        }
        return ret;
    }

    @Override
    public String getFormatDescription() {
        return "Lat (deg min sec) , Lon (deg min sec) [, Elev (m)]";
    }

}
