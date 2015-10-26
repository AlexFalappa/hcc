package net.falappa.wwind.posparser;

import net.falappa.wwind.helpers.LabeledPosition;

/**
 * Capability to parse a position string.
 *
 * @author Alessandro Falappa
 */
public interface PositionParser {

    /**
     * Default elevation (in meters) that should be assumed if not included in the position string.
     */
    final double DEFAULT_ELEVATION = 50000;

    /**
     * Analyzes the given text and tries to convert it into a position.
     *
     * @param text the string to parse
     * @return the parsed position or null
     */
    LabeledPosition parseString(String text);

    /**
     * Provides a textual sample of the parsed position format.
     *
     * @return the parsed format sample string
     */
    String getFormatDescription();
}
