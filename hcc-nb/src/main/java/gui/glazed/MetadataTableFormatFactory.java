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
import main.data.Metadata;
import main.data.MetadataNames;
import static main.data.MetadataNames.ACQ_STATION;
import static main.data.MetadataNames.PARENT_IDENTIFIER;
import static main.data.MetadataNames.PRODUCT_IDENTIFIER;
import static main.data.MetadataNames.PRODUCT_TYPE;
import static main.data.MetadataNames.START_SENSING;
import static main.data.MetadataNames.STOP_SENSING;

/**
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class MetadataTableFormatFactory {

    public static final MetadataNames[] metaNamesOrder = new MetadataNames[]{
        PARENT_IDENTIFIER,
        PRODUCT_IDENTIFIER,
        PRODUCT_TYPE,
        START_SENSING,
        STOP_SENSING,
        ACQ_STATION
    // TODO add the others
    };

    public static MetadataTableFormat createTableFormat(Metadata sample) {
        ArrayList<MetadataNames> columnKeys = new ArrayList<>(sample.size());
        ArrayList<String> columnNames = new ArrayList<>(sample.size());
        for (MetadataNames mn : metaNamesOrder) {
            if (sample.containsKey(mn)) {
                columnKeys.add(mn);
                // TODO prepare an array of proper names
                columnNames.add(mn.toString());
            }
        }
        int colNums = columnKeys.size();
        String[] cn = new String[colNums];
        MetadataNames[] ck = new MetadataNames[colNums];
        return new MetadataTableFormat(colNums, columnNames.toArray(cn), columnKeys.toArray(ck));
    }
}
