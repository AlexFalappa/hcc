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
package gui.glazed;

import java.util.ArrayList;
import java.util.EnumMap;
import main.data.Metadata;
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
 * Factory to build {@link MetadataTableFormat} objects.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class MetadataTableFormatFactory {

    /**
     * Default order of metadata attributes, roughly by importance.
     */
    public static final MetadataNames[] metaNamesOrder = new MetadataNames[]{
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
    };

    /**
     * Label strings for metadata attributes.
     */
    public static final EnumMap<MetadataNames, String> metaNamesLabels = new EnumMap<>(MetadataNames.class);

    static {
        metaNamesLabels.put(PARENT_IDENTIFIER, "Parent Id");
        metaNamesLabels.put(PRODUCT_IDENTIFIER, "Product Id");
        metaNamesLabels.put(PRODUCT_TYPE, "Prod. Type");
        metaNamesLabels.put(START_SENSING, "Sens. Start");
        metaNamesLabels.put(STOP_SENSING, "Sens. Stop");
        metaNamesLabels.put(ACQ_STATION, "Acq. Station");
        metaNamesLabels.put(ACQ_DATE, "Acq. Date");
        metaNamesLabels.put(ACQ_TYPE, "Acq. Type");
        metaNamesLabels.put(ACQ_SUBTYPE, "Acq. Subtype");
        metaNamesLabels.put(ARCHIVE_PATH, "Arch. Path");
        metaNamesLabels.put(ARCH_CENTER, "Arch. Center");
        metaNamesLabels.put(ARCH_DATE, "Arch. Date");
        metaNamesLabels.put(ARCH_ID, "Arch. Id");
        metaNamesLabels.put(STATUS, "Status");
        metaNamesLabels.put(ORBIT_NUMBER, "Orb. Num.");
        metaNamesLabels.put(LAST_ORBIT_NUMBER, "Last Orb.");
        metaNamesLabels.put(ORBIT_DIRECTION, "Orb. Dir.");
        metaNamesLabels.put(START_DOWNLINK, "Dlink Start");
        metaNamesLabels.put(STOP_DOWNLINK, "Dlink Stop");
        metaNamesLabels.put(MISSION_NAME, "Mission");
        metaNamesLabels.put(SAT_NAME, "Satellite");
        metaNamesLabels.put(SAT_SERIAL, "Sat. Ser.");
        metaNamesLabels.put(INST_NAME, "Instrument");
        metaNamesLabels.put(SENS_TYPE, "Sensor Type");
        metaNamesLabels.put(SENS_OP_MODE, "Sens. Op. Mode");
        metaNamesLabels.put(SENS_RESOLUTION, "Sens. Res.");
        metaNamesLabels.put(SENS_SWATH, "Sens. Swath");
        metaNamesLabels.put(PROC_LEVEL, "Proc. Level");
        metaNamesLabels.put(ASC_NODE_LON, "ANX Lon.");
        metaNamesLabels.put(WRS_LON, "WRS Lon.");
        metaNamesLabels.put(WRS_LAT, "WRS Lat.");
        metaNamesLabels.put(POLARISN_CHANNELS, "Polarz. Chans.");
        metaNamesLabels.put(POLARISN_MODE, "Polarz. Mode");
        metaNamesLabels.put(LOOK_SIDE, "Look Side");
        metaNamesLabels.put(INCID_ANGLE, "Incid. Angle");
        metaNamesLabels.put(MIN_INCID_ANGLE, "Min Incid. Angle");
        metaNamesLabels.put(MAX_INCID_ANGLE, "Max Incid. Angle");
        metaNamesLabels.put(INCID_ANGLE_VAR, "Incid. Angle Var.");
        metaNamesLabels.put(INCID_ANGLE_ACROSS, "Acrs.Incid. Angle");
        metaNamesLabels.put(INCID_ANGLE_ALONG, "Alon.Incid. Angle");
        metaNamesLabels.put(ILLUM_ANGLE_AZIM, "Illum. Angle Azim.");
        metaNamesLabels.put(ILLUM_ANGLE_ELEV, "Illum. Angle Elev.");
        metaNamesLabels.put(DOPPLER_FREQ, "Doppler Freq.");
        metaNamesLabels.put(URL_THUMB, "Thumbnail URL");
        metaNamesLabels.put(URL_QLOOK, "Quicklook URL");
        metaNamesLabels.put(CLOUD_COVER, "Cloud Cover");
        metaNamesLabels.put(SNOW_COVER, "Snow Cover");
    }

    /**
     * Builds a {@link MetadataTableFormat} from the given {@link Metadata} sample.
     * <p>
     * The built object uses the default order and the metadata attributes labels.
     *
     * @param sample the sample object
     * @return a new {@link MetadataTableFormat} object
     */
    public static MetadataTableFormat createTableFormat(Metadata sample) {
        ArrayList<MetadataNames> columnKeys = new ArrayList<>(sample.size());
        ArrayList<String> columnNames = new ArrayList<>(sample.size());
        for (MetadataNames mn : metaNamesOrder) {
            if (sample.containsKey(mn)) {
                columnKeys.add(mn);
                columnNames.add(metaNamesLabels.get(mn));
            }
        }
        int colNums = columnKeys.size();
        String[] cn = new String[colNums];
        MetadataNames[] ck = new MetadataNames[colNums];
        return new MetadataTableFormat(colNums, columnNames.toArray(cn), columnKeys.toArray(ck));
    }
}
