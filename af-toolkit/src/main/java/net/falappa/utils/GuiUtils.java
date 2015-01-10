/*
 * Copyright 2013 Alessandro Falappa.
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
package net.falappa.utils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Class of static utility methods for GUI purposes.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class GuiUtils {

    private static final String PREFK_LOC_X = "LocX";
    private static final String PREFK_LOC_Y = "LocY";
    private static final String PREFK_WIDTH = "Width";
    private static final String PREFK_HEIGHT = "Height";
    private static final String PREFK_STATE = "State";

    /**
     * Private constructor to prevent instantiation.
     */
    private GuiUtils() {
    }

    /**
     * Enable/disable a group of <tt>JComponent</tt>s widgets.
     *
     * @param flag the enable/disable flag
     * @param comps the components as variable arguments
     */
    public static void widgetsEnable(boolean flag, JComponent... comps) {
        for (JComponent cmp : comps) {
            cmp.setEnabled(flag);
        }
    }

    /**
     * Loads a {@link Component} size and location from the given {@link Preferences} node.
     * <p>
     * This method can load values written by {@link #storePrefsComponent(java.util.prefs.Preferences, javax.swing.JComponent)}.
     *
     * @param node the Preferences node
     * @param comp the component whose size and location must be set
     */
    public static void loadPrefsComponent(Preferences node, Component comp) {
        comp.setLocation(node.getInt(PREFK_LOC_X, comp.getX()), node.getInt(PREFK_LOC_Y, comp.getY()));
        comp.setSize(node.getInt(PREFK_WIDTH, comp.getWidth()), node.getInt(PREFK_HEIGHT, comp.getHeight()));
    }

    /**
     * Stores a {@link Component} size and location to the given {@link Preferences} node.
     *
     * @param node the Preferences node
     * @param comp the component whose size and location must be saved
     */
    public static void storePrefsComponent(Preferences node, Component comp) {
        node.putInt(PREFK_LOC_X, comp.getX());
        node.putInt(PREFK_LOC_Y, comp.getY());
        node.putInt(PREFK_WIDTH, comp.getWidth());
        node.putInt(PREFK_HEIGHT, comp.getHeight());
    }

    /**
     * Loads a {@link JFrame} size, location and state from the given {@link Preferences} node.
     * <p>
     * This method can load values written by {@link #storePrefsComponent(java.util.prefs.Preferences, javax.swing.JComponent)}.
     *
     * @param node the Preferences node
     * @param frame the frame whose size and location must be set
     */
    public static void loadPrefsFrame(Preferences node, JFrame frame) {
        loadPrefsComponent(node, frame);
        frame.setState(node.getInt(PREFK_STATE, frame.getExtendedState()));
    }

    /**
     * Stores a {@link JFrame} size, location and state to the given {@link Preferences} node.
     *
     * @param node the Preferences node
     * @param frame the frame whose size and location must be saved
     */
    public static void storePrefsFrame(Preferences node, JFrame frame) {
        storePrefsComponent(node, frame);
        node.putInt(PREFK_STATE, frame.getExtendedState());
    }

    /**
     * Converts a <tt>DefaultListModel</tt> into an <tt>ArrayList</tt> object.
     *
     * @param <T> the elements type
     * @param listModel a DefaultListModel to convert
     * @return an ArrayList containing the list model elements
     */
    public static <T> ArrayList<T> listModelAsList(DefaultListModel<T> listModel) {
        ArrayList<T> ret = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            ret.add(listModel.get(i));
        }
        return ret;
    }

    /**
     * Converts an <tt>ArrayList</tt> into a <tt>DefaultListModel</tt> object.
     *
     * @param <T> the elements type
     * @param list an ArrayList to convert
     * @return a DefaultListModel containing the list elements
     */
    public static <T> DefaultListModel<T> listAsListModel(ArrayList<T> list) {
        DefaultListModel<T> ret = new DefaultListModel<>();
        for (T elem : list) {
            ret.addElement(elem);
        }
        return ret;
    }

}
