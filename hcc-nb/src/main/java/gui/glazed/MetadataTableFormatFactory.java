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
import static main.data.MetadataNames.PRODUCT_IDENTIFIER;
import static main.hma.HmaMetadataSeq.gridListBrief;
import static main.hma.HmaMetadataSeq.shortLabels;

/**
 * Factory to build {@link MetadataTableFormat} objects.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class MetadataTableFormatFactory {

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
        for (MetadataNames mn : gridListBrief) {
            if (sample.containsKey(mn)) {
                columnKeys.add(mn);
                columnNames.add(shortLabels.get(mn));
            }
        }
        int colNums = columnKeys.size();
        String[] cn = new String[colNums];
        MetadataNames[] ck = new MetadataNames[colNums];
        return new MetadataTableFormat(colNums, columnNames.toArray(cn), columnKeys.toArray(ck));
    }

    /**
     * Builds a minimal {@link MetadataTableFormat} with only product identifier column.
     *
     * @return a new {@link MetadataTableFormat} object
     */
    public static MetadataTableFormat createMinimalTableFormat() {
        return new MetadataTableFormat(1, new String[]{shortLabels.get(PRODUCT_IDENTIFIER)}, new MetadataNames[]{PRODUCT_IDENTIFIER});
    }
}
