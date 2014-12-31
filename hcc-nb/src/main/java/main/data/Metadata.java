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
import static main.data.MetadataNames.FOOTPRINT;
import static main.data.MetadataNames.PARENT_IDENTIFIER;
import static main.data.MetadataNames.PRODUCT_IDENTIFIER;
import static main.data.MetadataNames.SCENE_CENTER;

/**
 * A domain object representing HMA metadata of an EO product.
 * <p>
 * It's actually an {@link EnumMap} with {@link MetadataNames} as keys.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class Metadata extends EnumMap<MetadataNames, String> implements Comparable<Metadata> {

    // footprint ordinates cache
    private transient double[] fpOrdinates = null;
    // scene center ordinates cache
    private transient double[] fpCenter = null;

    public Metadata() {
        super(MetadataNames.class);
    }

    /**
     * Returns the footprint as an array of coordinates (lat lon pairs).
     * <p>
     * The string to double conversion from the footprint attribute is performed once then cached.
     *
     * @return the footprint ordinates or null if invalid footprint or no footprint present
     */
    public double[] getFootprintAsDoubles() {
        if (fpOrdinates == null && containsKey(FOOTPRINT)) {
            // parse and cache the ordinates
            String[] coords = get(FOOTPRINT).split("\\s+");
            try {
                fpOrdinates = new double[coords.length];
                for (int i = 0; i < coords.length; i++) {
                    fpOrdinates[i] = Double.valueOf(coords[i]);
                }
            } catch (NumberFormatException nfe) {
                // remove invalid footprint content
                remove(FOOTPRINT);
                fpOrdinates = null;
            }
        }
        return fpOrdinates;
    }

    /**
     * Returns the scene center as an array of coordinates (lat lon pair).
     * <p>
     * The string to double conversion from the scene center attribute is performed once then cached.
     *
     * @return the scene center ordinates or null if invalid center or no center present
     */
    public double[] getSceneCenterAsDoubles() {
        if (fpCenter == null && containsKey(SCENE_CENTER)) {
            // parse and cache the ordinates
            String[] coords = get(SCENE_CENTER).split("\\s+");
            try {
                fpCenter = new double[coords.length];
                for (int i = 0; i < coords.length; i++) {
                    fpCenter[i] = Double.valueOf(coords[i]);
                }
            } catch (NumberFormatException nfe) {
                // remove invalid footprint content
                remove(SCENE_CENTER);
                fpCenter = null;
            }
        }
        return fpCenter;
    }

    @Override
    public int compareTo(Metadata o) {
        if (o == null) {
            throw new NullPointerException("Can't compare to null");
        }
        // compare collections if possible
        int collCompare = 0;
        if (containsKey(PARENT_IDENTIFIER) && o.containsKey(PARENT_IDENTIFIER)) {
            collCompare = get(PARENT_IDENTIFIER).compareTo(o.get(PARENT_IDENTIFIER));
        }
        // if no collection or equal collections compare product ids
        if (collCompare == 0) {
            if (containsKey(PRODUCT_IDENTIFIER) && o.containsKey(PRODUCT_IDENTIFIER)) {
                return get(PRODUCT_IDENTIFIER).compareTo(o.get(PRODUCT_IDENTIFIER));
            }
        } else {
            return collCompare;
        }
        // failing all return "equal"
        return 0;
    }

}
