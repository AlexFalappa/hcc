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

import java.util.EnumMap;
import java.util.HashMap;

/**
 * Utility class to support conversion of metadata names to HMA requests and responses slot names.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class Slots {

    // private to prevent instantiation
    private Slots() {
    }

    /**
     * Mapping of responses slot names to metadata names
     */
    public static final HashMap<String, MetadataNames> resp2meta = new HashMap<>();
    /**
     * Mapping of metadata names to responses slot names
     */
    public static final EnumMap<MetadataNames, String> meta2resp = new EnumMap<>(MetadataNames.class);
    /**
     * Mapping of metadata names to requests slot names
     */
    public static final EnumMap<MetadataNames, String> meta2req = new EnumMap<>(MetadataNames.class);
    /**
     * Mapping of requests slot names to metadata names
     */
    public static final HashMap<String, MetadataNames> req2meta = new HashMap<>();

    static {
        // EOProduct extrinsic object slots
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::parentIdentifier", MetadataNames.PARENT_IDENTIFIER);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::productType", MetadataNames.PRODUCT_TYPE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::multiExtentOf", MetadataNames.FOOTPRINT);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::beginPosition", MetadataNames.START_SENSING);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::endPosition", MetadataNames.STOP_SENSING);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::orbitNumber", MetadataNames.ORBIT_NUMBER);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::lastOrbitNumber", MetadataNames.LAST_ORBIT_NUMBER);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::orbitDirection", MetadataNames.ORBIT_DIRECTION);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionStation", MetadataNames.ACQ_STATION);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::polarisationChannels", MetadataNames.POLARISN_CHANNELS);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::polarisationMode", MetadataNames.POLARISN_MODE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::incidenceAngle", MetadataNames.INCID_ANGLE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::alongTrackIncidenceAngle", MetadataNames.INCID_ANGLE_ALONG);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acrossTrackIncidenceAngle", MetadataNames.INCID_ANGLE_ACROSS);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::illuminationAzimuthAngle", MetadataNames.ILLUM_ANGLE_AZIM);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::illuminationElevationAngle", MetadataNames.ILLUM_ANGLE_ELEV);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::minimumIncidenceAngle", MetadataNames.MIN_INCID_ANGLE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::maximumIncidenceAngle", MetadataNames.MAX_INCID_ANGLE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::status", MetadataNames.STATUS);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::centerOf", MetadataNames.SCENE_CENTER);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionDate", MetadataNames.ACQ_DATE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionType", MetadataNames.ACQ_TYPE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionSubType", MetadataNames.ACQ_SUBTYPE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::ascendingNodeLongitude", MetadataNames.ASC_NODE_LON);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::wrsLongitudeGrid", MetadataNames.WRS_LON);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::wrsLatitudeGrid", MetadataNames.WRS_LAT);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::antennaLookDirection", MetadataNames.LOOK_SIDE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::imageQualityDegradation", MetadataNames.IMG_DEGRADATION);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::cloudCoverPercentage", MetadataNames.CLOUD_COVER);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::snowCoverPercentage", MetadataNames.SNOW_COVER);
        setReq(MetadataNames.PARENT_IDENTIFIER,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::parentIdentifier\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.PRODUCT_TYPE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::productType\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.FOOTPRINT,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::multiExtentOf\"]/wrs:ValueList/wrs:AnyValue[1]");
        setReq(MetadataNames.START_SENSING,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::beginPosition\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.STOP_SENSING,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::endPosition\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ORBIT_NUMBER,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::orbitNumber\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.LAST_ORBIT_NUMBER,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::lastOrbitNumber\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ORBIT_DIRECTION,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::orbitDirection\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ACQ_STATION,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionStation\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.POLARISN_CHANNELS,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::polarisationChannels\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.POLARISN_MODE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::polarisationMode\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.INCID_ANGLE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::incidenceAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.INCID_ANGLE_ALONG,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::alongTrackIncidenceAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.INCID_ANGLE_ACROSS,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acrossTrackIncidenceAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ILLUM_ANGLE_AZIM,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::illuminationAzimuthAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ILLUM_ANGLE_ELEV,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::illuminationElevationAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.MIN_INCID_ANGLE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::minimumIncidenceAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.MAX_INCID_ANGLE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::maximumIncidenceAngle\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.STATUS,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::status\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SCENE_CENTER,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::centerOf\"]/wrs:ValueList/wrs:AnyValue[1]");
        setReq(MetadataNames.ACQ_DATE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionDate\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ACQ_TYPE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionType\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ACQ_SUBTYPE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::acquisitionSubType\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ASC_NODE_LON,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::ascendingNodeLongitude\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.WRS_LON,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::wrsLongitudeGrid\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.WRS_LAT,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::wrsLatitudeGrid\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.LOOK_SIDE,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::antennaLookDirection\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.IMG_DEGRADATION,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::imageQualityDegradation\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.CLOUD_COVER,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::cloudCoverPercentage\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SNOW_COVER,
                "/rim:ExtrinsicObject/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::snowCoverPercentage\"]/rim:ValueList/rim:Value[1]");
        // EOAcquisitionPlatform extrinsic object slots
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::instrumentShortName", MetadataNames.INST_NAME);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::sensorOperationalMode", MetadataNames.SENS_OP_MODE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::platformSerialIdentifier", MetadataNames.SAT_SERIAL);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::sensorType", MetadataNames.SENS_TYPE);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::sensorResolution", MetadataNames.SENS_RESOLUTION);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::swathIdentifier", MetadataNames.SENS_SWATH);
        setReq(MetadataNames.SAT_NAME,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Name/rim:LocalizedString/@value");
        setReq(MetadataNames.INST_NAME,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::instrumentShortName\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SENS_OP_MODE,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::sensorOperationalMode\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SAT_SERIAL,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::platformSerialIdentifier\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SENS_TYPE,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::sensorType\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SENS_RESOLUTION,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::sensorResolution\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.SENS_SWATH,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOAcquisitionPlatform\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::swathIdentifier\"]/rim:ValueList/rim:Value[1]");
        // EOArchivingInformation extrinsic object slots
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::archivingIdentifier", MetadataNames.ARCH_ID);
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::archivingDate", MetadataNames.ARCH_DATE);
        setReq(MetadataNames.ARCH_CENTER,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOArchivingInformation\"]/rim:Name/rim:LocalizedString/@value");
        setReq(MetadataNames.ARCH_ID,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOArchivingInformation\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::archivingIdentifier\"]/rim:ValueList/rim:Value[1]");
        setReq(MetadataNames.ARCH_DATE,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOArchivingInformation\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::archivingDate\"]/rim:ValueList/rim:Value[1]");
        // EOBrowseInformation extrinsic object slots (managed differently)
        setResp("urn:ogc:def:slot:OGC-CSW-ebRIM-EO::fileName", MetadataNames.BRWSE_FILE_NAME);
        setReq(MetadataNames.BRWSE_FILE_NAME,
                "/rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOBrowseInformation\"]/rim:Slot[@name=\"urn:ogc:def:slot:OGC-CSW-ebRIM-EO::fileName\"]/rim:ValueList/rim:Value[1]");
        /*
         *
         * note that the following HMA attributes are not in a <Slot> tag (no resp2meta mapping)
         * /rim:ExtrinsicObject[@objectType=\"urn:x-ogc:specification:csw-ebrim:ObjectType:EO:EOAcquisitionPlatform\"]/rim:Name/rim:LocalizedString/@value
         * /rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOArchivingInformation\"]/rim:Name/rim:LocalizedString/@value
         * /rim:ExtrinsicObject[@objectType=\"urn:ogc:def:objectType:OGC-CSW-ebRIM-EO::EOBrowseInformation\"]/rim:Name/rim:LocalizedString/@value
         */
    }

    private static void setResp(String hma, MetadataNames meta) {
        resp2meta.put(hma, meta);
        meta2resp.put(meta, hma);
    }

    private static void setReq(MetadataNames meta, String hma) {
        req2meta.put(hma, meta);
        meta2req.put(meta, hma);
    }
}
