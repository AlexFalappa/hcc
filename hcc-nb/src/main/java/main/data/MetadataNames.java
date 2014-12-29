/*
 * Copyright 2014 Alessandro Falappa <alex.falappa@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main.data;

/**
 * Enumeration of names of standard EO product metadata.
 * <p>
 * The strings refers to the metadata names in HMA catalogue spec.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public enum MetadataNames {

    // NOTE: most frequent attributes should come first!
    PRODUCT_IDENTIFIER("prodIdentifier"),
    PARENT_IDENTIFIER("parentIdentifier"),
    FOOTPRINT("footprint"),
    START_SENSING("startSensingTime"),
    STOP_SENSING("stopSensingTime"),
    PRODUCT_TYPE("productType"),
    ORBIT_NUMBER("orbitNumber"),
    ACQ_STATION("acquisitionStation"),
    ACQ_DATE("acquisitionDate"),
    ARCHIVE_PATH("archivePath"),
    ARCH_CENTER("archivingCenter"),
    ARCH_DATE("archivingDate"),
    STATUS("productStatus"),
    ORBIT_DIRECTION("orbitDirection"),
    START_DOWNLINK("startDownlinkTime"),
    STOP_DOWNLINK("stopDownlinkTime"),
    MISSION_NAME("missionName"),
    SAT_NAME("platformShortName"),
    INST_NAME("instrumentShortName"),
    SAT_SERIAL("platformSerialIdentifier"),
    SENS_TYPE("sensorType"),
    SENS_OP_MODE("sensorOperationalMode"),
    SENS_RESOLUTION("sensorResolution"),
    SCENE_CENTER("sceneCenter"),
    ARCH_ID("archivingIdentifier"),
    PROC_LEVEL("processingLevel"),
    SENS_SWATH("sensorSwathIdentifier"),
    ASC_NODE_LON("ascendingNodeLongitude"),
    WRS_LON("wrsLongitudeGrid"),
    WRS_LAT("wrsLatitudeGrid"),
    POLARISN_CHANNELS("polarisationChannels"),
    POLARISN_MODE("polarisationMode"),
    LOOK_SIDE("antennaLookDirection"),
    INCID_ANGLE("incidenceAngle"),
    MIN_INCID_ANGLE("minimumIncidenceAngle"),
    MAX_INCID_ANGLE("maximumIncidenceAngle"),
    INCID_ANGLE_VAR("incindenceAngleVariation"),
    DOPPLER_FREQ("dopplerFrequency");

    private final String attribute;

    private MetadataNames(final String n) {
        attribute = n;
    }

    @Override
    public String toString() {
        return attribute;
    }

    /**
     * Returns the enum for the given HMA attribute name.
     * <p>
     * Throws {@code IllegalArgumentException} if attribute name is null or invalid.
     *
     * @param attrName the attribute name
     * @return the corresponding enum object
     */
    public static MetadataNames valueForAttribute(String attrName) {
        if (attrName == null) {
            throw new IllegalArgumentException("Null MetadataNames enum value!");
        }
        for (MetadataNames v : values()) {
            if (attrName.equalsIgnoreCase(v.attribute)) {
                return v;
            }
        }
        throw new IllegalArgumentException(String.format("No MetadataNames enum value for %s", attrName));
    }
}
