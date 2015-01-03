/*
 * Copyright 2015 Alessandro Falappa <alex.falappa@gmail.com>.
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
package main.hma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import main.data.MetadataNames;
import static main.data.MetadataNames.ACQ_DATE;
import static main.data.MetadataNames.ACQ_STATION;
import static main.data.MetadataNames.ACQ_SUBTYPE;
import static main.data.MetadataNames.ACQ_TYPE;
import static main.data.MetadataNames.ARCHIVE_PATH;
import static main.data.MetadataNames.ARCH_CENTER;
import static main.data.MetadataNames.ARCH_DATE;
import static main.data.MetadataNames.ARCH_ID;
import static main.data.MetadataNames.ASC_NODE_LON;
import static main.data.MetadataNames.CLOUD_COVER;
import static main.data.MetadataNames.DOPPLER_FREQ;
import static main.data.MetadataNames.FOOTPRINT;
import static main.data.MetadataNames.ILLUM_ANGLE_AZIM;
import static main.data.MetadataNames.ILLUM_ANGLE_ELEV;
import static main.data.MetadataNames.IMG_DEGRADATION;
import static main.data.MetadataNames.INCID_ANGLE;
import static main.data.MetadataNames.INCID_ANGLE_ACROSS;
import static main.data.MetadataNames.INCID_ANGLE_ALONG;
import static main.data.MetadataNames.INCID_ANGLE_VAR;
import static main.data.MetadataNames.INST_NAME;
import static main.data.MetadataNames.LAST_ORBIT_NUMBER;
import static main.data.MetadataNames.LOOK_SIDE;
import static main.data.MetadataNames.MAX_INCID_ANGLE;
import static main.data.MetadataNames.MIN_INCID_ANGLE;
import static main.data.MetadataNames.MISSION_NAME;
import static main.data.MetadataNames.ORBIT_DIRECTION;
import static main.data.MetadataNames.ORBIT_NUMBER;
import static main.data.MetadataNames.PARENT_IDENTIFIER;
import static main.data.MetadataNames.POLARISN_CHANNELS;
import static main.data.MetadataNames.POLARISN_MODE;
import static main.data.MetadataNames.PROC_LEVEL;
import static main.data.MetadataNames.PRODUCT_IDENTIFIER;
import static main.data.MetadataNames.PRODUCT_TYPE;
import static main.data.MetadataNames.SAT_NAME;
import static main.data.MetadataNames.SAT_SERIAL;
import static main.data.MetadataNames.SCENE_CENTER;
import static main.data.MetadataNames.SENS_OP_MODE;
import static main.data.MetadataNames.SENS_RESOLUTION;
import static main.data.MetadataNames.SENS_SWATH;
import static main.data.MetadataNames.SENS_TYPE;
import static main.data.MetadataNames.SNOW_COVER;
import static main.data.MetadataNames.START_DOWNLINK;
import static main.data.MetadataNames.START_SENSING;
import static main.data.MetadataNames.STATUS;
import static main.data.MetadataNames.STOP_DOWNLINK;
import static main.data.MetadataNames.STOP_SENSING;
import static main.data.MetadataNames.URL_QLOOK;
import static main.data.MetadataNames.URL_THUMB;
import static main.data.MetadataNames.WRS_LAT;
import static main.data.MetadataNames.WRS_LON;

/**
 * Utility class containing preferred sequences and label strings to use when displaying metadata.
 * <p>
 * Static utility class.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class HmaMetadataSeq {

    // private to prevent instantiation.
    public HmaMetadataSeq() {
    }

    /**
     * Sequence of the most important metadata attributes targeted at display of several products in a grid.
     */
    public static final List<MetadataNames> gridListBrief;

    /**
     * Sequence of all metadata attributes, roughly ordered by importance, targeted at display of several products in a grid.
     */
    public static final List<MetadataNames> gridListFull;

    /**
     * Sequence of metadata attributes, grouped by Extrinsic Object, targeted at display of one product in a form.
     */
    public static final List<MetadataNames> formList;

    /**
     * Sequence of EOProduct extrinsic object metadata attributes.
     */
    public static final List<MetadataNames> formListEOProduct;

    /**
     * Sequence of EOAcquisitionPlatform extrinsic object metadata attributes.
     */
    public static final List<MetadataNames> formListEOAcquisitionPlatform;

    /**
     * Sequence of EOArchivingInformation extrinsic object metadata attributes.
     */
    public static final List<MetadataNames> formListEOArchivingInformation;

    /**
     * Sequence of EOBrowseInformation extrinsic object metadata attributes.
     */
    public static final List<MetadataNames> formListEOBrowseInformation;

    /**
     * Short label strings for metadata attributes.
     * <p>
     * Enumeration map indexed by metadata name
     */
    public static final EnumMap<MetadataNames, String> shortLabels = new EnumMap<>(MetadataNames.class);

    /**
     * Long label strings for metadata attributes.
     * <p>
     * Enumeration map indexed by metadata name
     */
    public static final EnumMap<MetadataNames, String> longLabels = new EnumMap<>(MetadataNames.class);

    static {
        shortLabels.put(PARENT_IDENTIFIER, "Parent Id");
        shortLabels.put(PRODUCT_IDENTIFIER, "Product Id");
        shortLabels.put(PRODUCT_TYPE, "Prod. Type");
        shortLabels.put(START_SENSING, "Sens. Start");
        shortLabels.put(STOP_SENSING, "Sens. Stop");
        shortLabels.put(ACQ_STATION, "Acq. Station");
        shortLabels.put(ACQ_DATE, "Acq. Date");
        shortLabels.put(ACQ_TYPE, "Acq. Type");
        shortLabels.put(ACQ_SUBTYPE, "Acq. Subtype");
        shortLabels.put(ARCHIVE_PATH, "Arch. Path");
        shortLabels.put(ARCH_CENTER, "Arch. Center");
        shortLabels.put(ARCH_DATE, "Arch. Date");
        shortLabels.put(ARCH_ID, "Arch. Id");
        shortLabels.put(STATUS, "Status");
        shortLabels.put(ORBIT_NUMBER, "Orb. Num.");
        shortLabels.put(LAST_ORBIT_NUMBER, "Last Orb.");
        shortLabels.put(ORBIT_DIRECTION, "Orb. Dir.");
        shortLabels.put(START_DOWNLINK, "Dlink Start");
        shortLabels.put(STOP_DOWNLINK, "Dlink Stop");
        shortLabels.put(MISSION_NAME, "Mission");
        shortLabels.put(SAT_NAME, "Satellite");
        shortLabels.put(SAT_SERIAL, "Sat. Ser.");
        shortLabels.put(INST_NAME, "Instrument");
        shortLabels.put(SENS_TYPE, "Sensor Type");
        shortLabels.put(SENS_OP_MODE, "Sens. Op. Mode");
        shortLabels.put(SENS_RESOLUTION, "Sens. Res.");
        shortLabels.put(SENS_SWATH, "Sens. Swath");
        shortLabels.put(PROC_LEVEL, "Proc. Level");
        shortLabels.put(ASC_NODE_LON, "ANX Lon.");
        shortLabels.put(WRS_LON, "WRS Lon.");
        shortLabels.put(WRS_LAT, "WRS Lat.");
        shortLabels.put(POLARISN_CHANNELS, "Polarz. Chans.");
        shortLabels.put(POLARISN_MODE, "Polarz. Mode");
        shortLabels.put(LOOK_SIDE, "Look Side");
        shortLabels.put(INCID_ANGLE, "Incid. Angle");
        shortLabels.put(MIN_INCID_ANGLE, "Min Incid. Angle");
        shortLabels.put(MAX_INCID_ANGLE, "Max Incid. Angle");
        shortLabels.put(INCID_ANGLE_VAR, "Incid. Angle Var.");
        shortLabels.put(INCID_ANGLE_ACROSS, "Acr. Incid. Angle");
        shortLabels.put(INCID_ANGLE_ALONG, "Aln. Incid. Angle");
        shortLabels.put(ILLUM_ANGLE_AZIM, "Illum. Angle Azim.");
        shortLabels.put(ILLUM_ANGLE_ELEV, "Illum. Angle Elev.");
        shortLabels.put(DOPPLER_FREQ, "Doppler Freq.");
        shortLabels.put(URL_THUMB, "Thumbnail URL");
        shortLabels.put(URL_QLOOK, "Quicklook URL");
        shortLabels.put(CLOUD_COVER, "Cloud Cover");
        shortLabels.put(SNOW_COVER, "Snow Cover");
        shortLabels.put(FOOTPRINT, "Footprint");
        shortLabels.put(SCENE_CENTER, "Scn. Center");
        shortLabels.put(MISSION_NAME, "Mission");
        // ----------------------------
        longLabels.put(PARENT_IDENTIFIER, "Parent Identifier");
        longLabels.put(PRODUCT_IDENTIFIER, "Product Identifier");
        longLabels.put(PRODUCT_TYPE, "Product Type");
        longLabels.put(START_SENSING, "Sensing Start");
        longLabels.put(STOP_SENSING, "Sensing Stop");
        longLabels.put(ACQ_STATION, "Acquisition Station");
        longLabels.put(ACQ_DATE, "Acquisition Date");
        longLabels.put(ACQ_TYPE, "Acquisition Type");
        longLabels.put(ACQ_SUBTYPE, "Acquisition Subtype");
        longLabels.put(ARCHIVE_PATH, "Archiving Path");
        longLabels.put(ARCH_CENTER, "Archiving Center");
        longLabels.put(ARCH_DATE, "Archiving Date");
        longLabels.put(ARCH_ID, "Archiving Identifier");
        longLabels.put(STATUS, "Status");
        longLabels.put(ORBIT_NUMBER, "Orbit Number");
        longLabels.put(LAST_ORBIT_NUMBER, "Last Orbit");
        longLabels.put(ORBIT_DIRECTION, "Orbit Direction");
        longLabels.put(START_DOWNLINK, "Downlink Start");
        longLabels.put(STOP_DOWNLINK, "Downlink Stop");
        longLabels.put(MISSION_NAME, "Mission");
        longLabels.put(SAT_NAME, "Satellite");
        longLabels.put(SAT_SERIAL, "Satellite Serial");
        longLabels.put(INST_NAME, "Instrument Name");
        longLabels.put(SENS_TYPE, "Sensor Type");
        longLabels.put(SENS_OP_MODE, "Sensor Operational Mode");
        longLabels.put(SENS_RESOLUTION, "Sensor Resolution");
        longLabels.put(SENS_SWATH, "Sensor Swath");
        longLabels.put(PROC_LEVEL, "Processing Level");
        longLabels.put(ASC_NODE_LON, "ANX Longitude");
        longLabels.put(WRS_LON, "WRS Longitude");
        longLabels.put(WRS_LAT, "WRS Latitude");
        longLabels.put(POLARISN_CHANNELS, "Polarization Channels");
        longLabels.put(POLARISN_MODE, "Polarization Mode");
        longLabels.put(LOOK_SIDE, "Look Side");
        longLabels.put(INCID_ANGLE, "Incidence Angle");
        longLabels.put(MIN_INCID_ANGLE, "Min Incidence Angle");
        longLabels.put(MAX_INCID_ANGLE, "Max Incidence Angle");
        longLabels.put(INCID_ANGLE_VAR, "Incidence Angle Variation");
        longLabels.put(INCID_ANGLE_ACROSS, "Across Incidence Angle");
        longLabels.put(INCID_ANGLE_ALONG, "Along Incidence Angle");
        longLabels.put(ILLUM_ANGLE_AZIM, "Illumination Angle Azimuth");
        longLabels.put(ILLUM_ANGLE_ELEV, "Illumination Angle Elevation");
        longLabels.put(DOPPLER_FREQ, "Doppler Frequency");
        longLabels.put(URL_THUMB, "Thumbnail URL");
        longLabels.put(URL_QLOOK, "Quicklook URL");
        longLabels.put(CLOUD_COVER, "Cloud Coverage %");
        longLabels.put(SNOW_COVER, "Snow Coverage %");
        longLabels.put(FOOTPRINT, "Footprint");
        longLabels.put(SCENE_CENTER, "Scene Center");
        longLabels.put(MISSION_NAME, "Mission Name");
        //--------------------
        gridListFull = Collections.unmodifiableList(Arrays.asList(new MetadataNames[]{
            PARENT_IDENTIFIER,
            PRODUCT_IDENTIFIER,
            PRODUCT_TYPE,
            START_SENSING,
            STOP_SENSING,
            ACQ_STATION,
            ACQ_DATE,
            ORBIT_NUMBER,
            LAST_ORBIT_NUMBER,
            ORBIT_DIRECTION,
            MISSION_NAME,
            SAT_NAME,
            SAT_SERIAL,
            INST_NAME,
            SENS_TYPE,
            SENS_OP_MODE,
            SENS_RESOLUTION,
            ARCH_CENTER,
            ARCH_DATE,
            ARCHIVE_PATH,
            URL_THUMB,
            URL_QLOOK,
            STATUS,
            START_DOWNLINK,
            STOP_DOWNLINK,
            ARCH_ID,
            ACQ_TYPE,
            ACQ_SUBTYPE,
            PROC_LEVEL,
            SENS_SWATH,
            ASC_NODE_LON,
            WRS_LON,
            WRS_LAT,
            CLOUD_COVER,
            SNOW_COVER,
            POLARISN_CHANNELS,
            POLARISN_MODE,
            LOOK_SIDE,
            INCID_ANGLE,
            INCID_ANGLE_ALONG,
            INCID_ANGLE_ACROSS,
            MIN_INCID_ANGLE,
            MAX_INCID_ANGLE,
            INCID_ANGLE_VAR,
            ILLUM_ANGLE_AZIM,
            ILLUM_ANGLE_ELEV,
            DOPPLER_FREQ,
            IMG_DEGRADATION
        }));
        //--------------------
        gridListBrief = Collections.unmodifiableList(Arrays.asList(new MetadataNames[]{
            PARENT_IDENTIFIER,
            PRODUCT_IDENTIFIER,
            PRODUCT_TYPE,
            START_SENSING,
            STOP_SENSING,
            ACQ_STATION,
            ACQ_DATE,
            ORBIT_NUMBER,
            INST_NAME,
            SENS_OP_MODE,
            ARCH_CENTER,
            STATUS
        }));
        //--------------------
        formListEOProduct = Collections.unmodifiableList(Arrays.asList(new MetadataNames[]{
            PRODUCT_IDENTIFIER,
            PARENT_IDENTIFIER,
            PRODUCT_TYPE,
            STATUS,
            START_SENSING,
            STOP_SENSING,
            START_DOWNLINK,
            STOP_DOWNLINK,
            ORBIT_NUMBER,
            LAST_ORBIT_NUMBER,
            ORBIT_DIRECTION,
            ACQ_STATION,
            ACQ_DATE,
            ACQ_TYPE,
            ACQ_SUBTYPE,
            CLOUD_COVER,
            SNOW_COVER,
            POLARISN_CHANNELS,
            POLARISN_MODE,
            ILLUM_ANGLE_AZIM,
            ILLUM_ANGLE_ELEV,
            INCID_ANGLE,
            INCID_ANGLE_ALONG,
            INCID_ANGLE_ACROSS,
            MIN_INCID_ANGLE,
            MAX_INCID_ANGLE,
            INCID_ANGLE_VAR,
            LOOK_SIDE,
            IMG_DEGRADATION,
            PROC_LEVEL,
            DOPPLER_FREQ,
            ASC_NODE_LON,
            WRS_LON,
            WRS_LAT,
            FOOTPRINT,
            SCENE_CENTER
        }));
        //-----------------
        formListEOAcquisitionPlatform = Collections.unmodifiableList(Arrays.asList(new MetadataNames[]{
            SAT_NAME,
            SAT_SERIAL,
            INST_NAME,
            SENS_TYPE,
            SENS_OP_MODE,
            SENS_SWATH,
            SENS_RESOLUTION
        }));
        formListEOArchivingInformation = Collections.unmodifiableList(Arrays.asList(new MetadataNames[]{
            //EOArchivingInformation extrinsic object
            ARCH_CENTER,
            ARCH_DATE,
            ARCHIVE_PATH,
            ARCH_ID
        }));
        //-----------------
        formListEOBrowseInformation = Collections.unmodifiableList(Arrays.asList(new MetadataNames[]{
            URL_THUMB,
            URL_QLOOK
        }));
        //-----------------
        ArrayList<MetadataNames> list = new ArrayList<>(formListEOProduct);
        list.addAll(formListEOAcquisitionPlatform);
        list.addAll(formListEOBrowseInformation);
        list.addAll(formListEOArchivingInformation);
        formList = Collections.unmodifiableList(list);
    }

}
