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
package net.falappa.prefs;

import java.util.prefs.Preferences;

/**
 * Capability of loading and storing a persistent state or settings from Java {@link Preferences} nodes.
 * <p>
 * Loading and storing should be relative to an external base node, this allows to organize settings/state hierarchically.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public interface PrefRestorable {

    /**
     * Load state/settings from Java Preferences API nodes.
     *
     * @param baseNode the root node under which to look for this class own node
     */
    void loadPrefs(Preferences baseNode);

    /**
     * Store state/settings to Java Preferences API nodes.
     *
     * @param baseNode the root node under which to store this class own node
     */
    void storePrefs(Preferences baseNode);
}
