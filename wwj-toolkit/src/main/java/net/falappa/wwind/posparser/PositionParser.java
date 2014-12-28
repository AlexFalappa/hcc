package net.falappa.wwind.posparser;

import gov.nasa.worldwind.geom.Position;

/**
 * Capability to parse a position string.
 *
 * @author Alessandro Falappa
 */
public interface PositionParser {

    /**
     * Analyzes the given text and tries to convert it into a position.
     *
     * @param text the string to parse
     * @return the parsed position or null
     */
    Position parseString(String text);
}
