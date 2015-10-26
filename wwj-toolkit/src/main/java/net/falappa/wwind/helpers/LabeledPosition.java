package net.falappa.wwind.helpers;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;

/**
 * Represents a geographical position with an associated label text.
 *
 * @author Alessandro Falappa
 */
public class LabeledPosition extends Position {

    private String label = null;

    public LabeledPosition(String label, Angle latitude, Angle longitude, double elevation) {
        super(latitude, longitude, elevation);
        this.label = label;
    }

    public LabeledPosition(String label, LatLon latLon, double elevation) {
        super(latLon, elevation);
        this.label = label;
    }

    public LabeledPosition(String label, Position pos) {
        this(label, pos.latitude, pos.longitude, pos.elevation);
    }

    public LabeledPosition(Angle latitude, Angle longitude, double elevation) {
        this(null, latitude, longitude, elevation);
    }

    public LabeledPosition(LatLon latLon, double elevation) {
        this(null, latLon, elevation);
    }

    public LabeledPosition(Position pos) {
        this(null, pos.latitude, pos.longitude, pos.elevation);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static LabeledPosition fromDegrees(String label, double latitude, double longitude, double elevation) {
        return new LabeledPosition(label, Angle.fromDegrees(latitude), Angle.fromDegrees(longitude), elevation);
    }

    public static LabeledPosition fromDegrees(String label, double latitude, double longitude) {
        return new LabeledPosition(label, Angle.fromDegrees(latitude), Angle.fromDegrees(longitude), 0);
    }

    public static Position fromRadians(String label, double latitude, double longitude, double elevation) {
        return new LabeledPosition(label, Angle.fromRadians(latitude), Angle.fromRadians(longitude), elevation);
    }

    public static LabeledPosition fromLatLon(String label, LatLon latLon, double elevation) {
        return new LabeledPosition(label, latLon, elevation);
    }

    public static LabeledPosition fromLatLon(String label, LatLon latLon) {
        return new LabeledPosition(label, latLon, 0);
    }

    public static LabeledPosition fromPosition(String label, Position pos) {
        return new LabeledPosition(label, pos);
    }

    public static LabeledPosition fromLatLonAutoLabel(LatLon latLon, double elevation) {
        LabeledPosition lp = new LabeledPosition(latLon, elevation);
        lp.setLabel(lp.toString());
        return lp;
    }

    public static LabeledPosition fromLatLonAutoLabel(LatLon latLon) {
        LabeledPosition lp = new LabeledPosition(latLon, 0);
        lp.setLabel(lp.toString());
        return lp;
    }

    public static LabeledPosition fromPositionAutoLabel(Position pos) {
        LabeledPosition lp = new LabeledPosition(pos);
        lp.setLabel(lp.toString());
        return lp;
    }

}
