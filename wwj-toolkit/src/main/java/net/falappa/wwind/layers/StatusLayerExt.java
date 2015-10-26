package com.telespazio.wwind.layers;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwindx.examples.util.StatusLayer;

/**
 * Extended version of {@link StatusLayer} allowing display of angular coordinates in degrees-minutes-seconds format.
 *
 * @author Alessandro Falappa
 */
public class StatusLayerExt extends StatusLayer {

    private String angleFormat = Angle.ANGLE_FORMAT_DD;

    public String getAngleFormat() {
        return angleFormat;
    }

    public void setAngleFormat(String angleFormat) {
        if (angleFormat == null) {
            String message = Logging.getMessage("nullValue.StringIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        switch (angleFormat) {
            case Angle.ANGLE_FORMAT_DD:
            case Angle.ANGLE_FORMAT_DM:
            case Angle.ANGLE_FORMAT_DMS:
                this.angleFormat = angleFormat;
                break;
            default:
                String message = Logging.getMessage("generic.EnumNotFound", angleFormat);
                Logging.logger().severe(message);
                throw new IllegalArgumentException(message);
        }
    }

    @Override
    protected String makeAngleDescription(String label, Angle angle, int places) {
        switch (angleFormat) {
            case Angle.ANGLE_FORMAT_DM:
                return String.format("%s %s", label, angle.toDMString());
            case Angle.ANGLE_FORMAT_DMS:
                return String.format("%s %s", label, angle.toDMSString());
            case Angle.ANGLE_FORMAT_DD:
            default:
                return String.format("%s %s", label, angle.toDecimalDegreesString(places));
        }
    }

}
