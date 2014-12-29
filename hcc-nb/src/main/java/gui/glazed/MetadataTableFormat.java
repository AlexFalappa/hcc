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

import ca.odell.glazedlists.gui.TableFormat;
import main.data.Metadata;
import main.data.MetadataNames;

/**
 * Glazed lists {@link TableFormat} for {@link Metadata} objects.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class MetadataTableFormat implements TableFormat<Metadata> {

    private final int columns;
    private final String[] columnNames;
    private final MetadataNames[] columnKeys;

    MetadataTableFormat(int columns, String[] columnNames, MetadataNames[] columnKeys) {
        this.columns = columns;
        this.columnNames = columnNames;
        this.columnKeys = columnKeys;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getColumnValue(Metadata baseObject, int column) {
        return baseObject.get(columnKeys[column]);
    }

}
