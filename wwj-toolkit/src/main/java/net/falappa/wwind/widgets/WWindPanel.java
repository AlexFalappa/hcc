package net.falappa.wwind.widgets;

import com.telespazio.wwind.layers.StatusLayerExt;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.Earth.BMNGOneImage;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.util.measure.MeasureToolController;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import gov.nasa.worldwind.wms.WMSTiledImageLayer;
import net.falappa.prefs.PrefRestorable;
import net.falappa.wwind.layers.EditableMarkerLayer;
import net.falappa.wwind.layers.NoSuchShapeException;
import net.falappa.wwind.layers.ShapeSelectionSource;
import net.falappa.wwind.layers.SingleMarkerLayer;
import net.falappa.wwind.layers.SingleSurfShapeLayer;
import net.falappa.wwind.layers.SurfShapeLayer;
import net.falappa.wwind.layers.SurfShapesLayer;
import net.falappa.wwind.layers.WMSLayerFactory;
import net.falappa.wwind.posparser.LatLonDMSParser;
import net.falappa.wwind.posparser.LatLonParser;
import net.falappa.wwind.posparser.PositionParser;
import net.falappa.wwind.utils.WWindUtils;

/**
 * A base WorldWind panel with a bar containing a {@link FlyToPanel} and a {@link FlatRoundInLinePanel}.
 * <p>
 * Includes a configured {@link WorldWindowGLCanvas} with a base known layer definition (contained in an XML file). A customizable layer
 * definition made of WMS layers can be used specifying an external properties file.
 * <p>
 * A button in the toolbar can be shown to recal a {@link DynamicLayerSettingsDialog}.
 * <p>
 * Manages an Area of Interest using a {@link SingleSurfShapeLayer} (for polyline, polygon and circle) and a {@link SingleMarkerLayer} (for
 * point).
 * <p>
 * Manages a Map of {@link SurfShapesLayer} (indexed by name), <tt>SurfShapesLayer</tt>s can be added (if not already present) and removed,
 * events are fired on addition/removal.
 * <p>
 * Manages creation and modification of an editing shape (circle, polygon, polyline, point) that can afterwards be converted to an AOI.
 * Another editing shape can be controlled trough an editing toolbar.
 * <p>
 * It's also possible to add other WorldWind layers. Utility methods aids the placement in the layer stack.
 * <p>
 * Offers various fly to and eye positioning methods to animate going to a point or area on the Globe or instantly moving the camera..
 *
 * @author Alessandro Falappa
 */
public class WWindPanel extends javax.swing.JPanel implements PrefRestorable {

    /**
     * Types of supported area of interest.
     */
    public static enum AoiShapes {

        POLYGON, CIRCLE, POLYLINE, POINT
    }

    /**
     * Types of supported edit shapes.
     */
    public static enum EditModes {

        POLYGON, CIRCLE, POLYLINE, POINT
    }
    public static final String EVENT_SURF_LAYER_ADDED = "WWindPanel.SurfShapeLayerAdded";
    public static final String EVENT_SURF_LAYER_REMOVED = "WWindPanel.SurfShapeLayerRemoved";
    public static final String WWINDPANEL_BASECARTO_CONFIG_URL = "wwindpanel.basecarto.config.url";
    // preference node names
    private static final String PREFN_WWP = "view";
    private static final Logger logger = Logger.getLogger(WWindPanel.class.getName());
    private static final Color COLOR_EDIT = new Color(0, 200, 255, 200);
    private final LatLonGraticuleLayer lGraticule = new LatLonGraticuleLayer();
    private final StatusLayerExt lStatus = new StatusLayerExt();
    protected final SingleSurfShapeLayer aoi = new SingleSurfShapeLayer("Area of Interest");
    protected final SingleMarkerLayer moi = new SingleMarkerLayer("Marker of interest");
    private final HashMap<String, SurfShapeLayer> shapeLayers = new HashMap<>();
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private boolean editing = false;
    private EditModes editMode = EditModes.POLYGON;
    private MeasureTool mt = null;
    private EditableMarkerLayer eml = null;
    private EditButtonsPanel editBtnsPanel = null;
    private DynamicLayerSettingsDialog layerSettingsDialog = null;
    private JButton bLayerSettings = null;

    static {
        // set the WorldWind layers configuration file property
        StringBuilder sb = new StringBuilder("/");
        sb.append(WWindPanel.class.getName().replace('.', '/'));
        if (System.getProperty(WWINDPANEL_BASECARTO_CONFIG_URL) != null) {
            sb.append("Layers_minimal.xml");
        } else {
            sb.append("Layers_online.xml");
        }
        URL wwCfg = WWindPanel.class.getResource(sb.toString());
        logger.log(Level.CONFIG, "Setting WordlWind app config document to: {0}", wwCfg.toString());
        //NOTE: do not use WorldWind logger or WorldWind Configuration class before the following statement as it interferes with the initial bootstrap sequence
        System.setProperty("gov.nasa.worldwind.app.config.document", wwCfg.toString());
    }

    /**
     * Sets useful system properties for tweaking Java2D, Swing and JOGL behaviours to better fit WorldWind.
     * <p>
     * It's recommended to call this method as soon as possible in the main class.
     */
    public static void setupPropsForWWind() {
        // global JOGL properties
        System.setProperty("jogl.disable.opengles", "true");
        logger.log(Level.CONFIG, "Disabled lookup for OpenGL/ES");
        // indicate preferred JAXP XML parser to avoid problems in WorldWind when other parsers are available
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        // set other global properties depending on the OS
        if (Configuration.isMacOS()) {
            // offset the lower right resize grab handle (avoid overlapping)
            System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
            logger.log(Level.CONFIG, "Set Java2D and AWT hints for MacOS-X platform");
        } else if (Configuration.isWindowsOS()) {
            // prevents flashing during window resizing
            System.setProperty("sun.awt.noerasebackground", "true");
            // disable Java2D DirectDraw acceleration
            System.setProperty("sun.java2d.noddraw", "true");
            logger.log(Level.CONFIG, "Set Java2D and AWT hints for Windows platform");
        } else if (Configuration.isLinuxOS()) {
            // disable Java2D OpenGL acceleration
            System.setProperty("sun.java2d.opengl", "false");
            // prevents flashing during window resizing
            System.setProperty("sun.awt.noerasebackground", "true");
            logger.log(Level.CONFIG, "Set Java2D and AWT hints for Linux platform");
        }
    }

    /**
     * Default constructor.
     */
    public WWindPanel() {
        initComponents();
        setupWorldWind();
        flyToPanel.setWWindPanel(this);
        flyToPanel.setDelays(1500, 15000);
        flyToPanel.addParser(new LatLonDMSParser());
        flyToPanel.addParser(new LatLonParser());
        globeSwitcher.setWorldWindow(wwCanvas);
    }

    /**
     * Getter to the inner <code>WorldWindowGLCanvas</code>.
     *
     * @return the inner preconfigured <code>WorldWindowGLCanvas</code>
     */
    public WorldWindowGLCanvas getWWCanvas() {
        return wwCanvas;
    }

    /**
     * Specifies if the bar should be on the bottom of the panel.
     *
     * @param flag true to place the bar on the bottom
     */
    public void setBottomBar(boolean flag) {
        remove(pTop);
        add(pTop, java.awt.BorderLayout.PAGE_END);
    }

    /**
     * Getter for the current angle format used in the status bar and graticule.
     *
     * @return one of {@link Angle#ANGLE_FORMAT_DD},{@link Angle#ANGLE_FORMAT_DM} or {@link Angle#ANGLE_FORMAT_DMS} constants
     */
    public String getAngleFormat() {
        return lStatus.getAngleFormat();
    }

    /**
     * Setter for the angle format used in the status bar and graticule.
     *
     * @param angleFormat one of {@link Angle#ANGLE_FORMAT_DD},{@link Angle#ANGLE_FORMAT_DM} or {@link Angle#ANGLE_FORMAT_DMS} constants
     */
    public void setAngleFormat(String angleFormat) {
        lStatus.setAngleFormat(angleFormat);
        lGraticule.setAngleFormat(angleFormat);
    }

    /**
     * Getter for the edit shape color.
     *
     * @return the current color
     */
    public Color getEditColor() {
        mtInit();
        return mt.getLineColor();
    }

    /**
     * Setter for the edit shape color.
     *
     * @param editColor the new color
     */
    public void setEditColor(Color editColor) {
        mtInit();
        mt.setLineColor(editColor);
        eml.setColor(editColor);
    }

    /**
     * Tells if during edit shape creation the user can use a click-and-drag gesture.
     * <p>
     * When enabled the user can click, drag and then release to fix a control point position. Note: when click-and-drag is enabled the
     * first polyline and polygon segment must be drawn clicking and dragging, otherwise two starting point will be created.
     *
     * @return true if click-and-drag is currently enabled
     */
    public boolean isEditRubberbanding() {
        mtInit();
        return mt.getController().isUseRubberBand();
    }

    /**
     * Sets if during edit shape creation the user can use a click-and-drag gesture.
     *
     * @param flag true to enable click-and-drag
     */
    public void setEditRubberbanding(boolean flag) {
        mtInit();
        mt.getController().setUseRubberBand(flag);
    }

    /**
     * Getter for the current edit mode.
     * <p>
     * It is set to {@link EditModes#POLYGON} by default.
     *
     * @return one of the EditModes enumerated value
     */
    public EditModes getEditMode() {
        return editMode;
    }

    /**
     * Setter for the current edit mode.
     * <p>
     * The new mode takes effect at next start of editing.
     *
     * @param mode the new mode as EditModes enumerated value
     */
    public void setEditMode(EditModes mode) {
        if (mode != null) {
            editMode = mode;
        }
    }

    /**
     * Starts editing.
     * <p>
     * After this method is called mouse input is "captured" for editing. Call {@link #stopEditing()} to end an editing session. Previous
     * editing shapes are cleared. Edit mode changing does not take effect after editing has been started.
     */
    public void startEditing() {
        mtInit();
        editing = true;
        mt.clear();
        switch (editMode) {
            case CIRCLE:
                eml.setEditing(false);
                mt.setMeasureShapeType(MeasureTool.SHAPE_CIRCLE);
                mt.setArmed(true);
                break;
            case POINT:
                eml.setEditing(true);
                break;
            case POLYGON:
                eml.setEditing(false);
                mt.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
                mt.setArmed(true);
                break;
            case POLYLINE:
                eml.setEditing(false);
                mt.setMeasureShapeType(MeasureTool.SHAPE_PATH);
                mt.setArmed(true);
                break;
        }
    }

    /**
     * Tells if editing mode is currently active.
     *
     * @return true if editing, false otherwise
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * Ends editing.
     * <p>
     * After this method is called user input manipulates the view. The editing shape is not cleared.
     */
    public void stopEditing() {
        mt.setArmed(false);
        eml.setEditing(false);
        editing = false;
    }

    /**
     * Tells if an editing shape has been defined.
     *
     * @return true if shape present
     */
    public boolean hasEditShape() {
        switch (editMode) {
            case POINT:
                return eml.isPositionSet();
            case CIRCLE:
                return mt != null && !mt.getPositions().isEmpty();
            default:
                return mt != null && !mt.isArmed() && !mt.getPositions().isEmpty();
        }
    }

    /**
     * Clears a previous editing shape.
     * <p>
     * Can be called also while editing.
     */
    public void editShapeClear() {
        mt.clear();
        eml.clear();
    }

    /**
     * Starts editing in {@code POLYGON} mode from a polygon shape.
     * <p>
     * Clears previous editing shapes.
     *
     * @param locations the locations (that will get copied and closed by duplicating the first as last).
     */
    public void startEditingFromPoly(Iterable<? extends LatLon> locations) {
        mtInit();
        mt.clear();
        editMode = EditModes.POLYGON;
        eml.setEditing(false);
        editing = true;
        // create a copy of the given locations duplicating the first point at the end
        ArrayList<LatLon> coords = new ArrayList<>();
        for (LatLon location : locations) {
            coords.add(location);
        }
        coords.add(coords.get(coords.size() - 1));
        // build up a polygon from the coordinates
        mt.setMeasureShape(new SurfacePolygon(coords));
        mt.setFillColor(new Color(255, 255, 255, 63));
        mt.setLineColor(COLOR_EDIT);
    }

    /**
     * Starts editing in {@code CIRCLE} mode from the given center and radius.
     * <p>
     * Clears previous editing shapes.
     *
     * @param center the circle center
     * @param radius the circle radius in meters
     */
    public void startEditingFromCircle(LatLon center, double radius) {
        mtInit();
        mt.clear();
        editMode = EditModes.CIRCLE;
        eml.setEditing(false);
        editing = true;
        // create a copy of the given surface polygon duplicating the first point at the end
        mt.setMeasureShape(new SurfaceCircle(center, radius));
        mt.setFillColor(new Color(255, 255, 255, 63));
        mt.setLineColor(COLOR_EDIT);
    }

    /**
     * Returns the current edit shape radius.
     * <p>
     * Applicable only when edit mode is {@link EditModes#CIRCLE}.
     *
     * @return the radius or null if not applicable
     */
    public Double getEditShapeRadius() {
        Double ret = null;
        if (hasEditShape() && mt.getMeasureShapeType().equals(MeasureTool.SHAPE_CIRCLE)) {
            ret = mt.getWidth() / 2.d;
        }
        return ret;
    }

    /**
     * Returns the current edit shape coordinates.
     * <p>
     * When edit mode is {@link EditModes#CIRCLE} gives the circle center coordinates. In case of {@link EditModes#POLYGON} the first
     * coordinate is repeated.
     *
     * @return the coordinates or null if not applicable
     */
    public List<LatLon> getEditShapeLocations() {
        ArrayList<LatLon> ret = null;
        if (hasEditShape()) {
            ret = new ArrayList<>();
            if (eml.isPositionSet()) {
                ret.add(eml.getPosition());
            } else {
                switch (mt.getMeasureShapeType()) {
                    case MeasureTool.SHAPE_CIRCLE:
                        ret.add(mt.getCenterPosition());
                        break;
                    case MeasureTool.SHAPE_PATH:
                    case MeasureTool.SHAPE_POLYGON:
                        ret.addAll(mt.getPositions());
                        break;
                }
            }
        }
        return ret;
    }

    /**
     * Transfers the editing shape to the Area of Interest or Marker of Interest layers.
     * <p>
     * Implicitly ends editing.
     */
    public void editShapeToAOI() {
        stopEditing();
        if (!hasEditShape()) {
            return;
        }
        if (eml.isPositionSet()) {
            aoi.clear();
            moi.setPosition(eml.getPosition());
        } else {
            moi.clear();
            switch (mt.getMeasureShapeType()) {
                case MeasureTool.SHAPE_CIRCLE:
                    aoi.setSurfCircle(mt.getCenterPosition(), mt.getWidth() / 2);
                    break;
                case MeasureTool.SHAPE_PATH:
                    // note: coords list must be copied!!
                    aoi.setSurfLine(new ArrayList<>(mt.getPositions()));
                    break;
                case MeasureTool.SHAPE_POLYGON:
                    // note: coords list must be copied!!
                    aoi.setSurfPoly(new ArrayList<>(mt.getPositions()));
                    break;
            }
        }
    }

    /**
     * Adds a listener which gets notified of all editing actions.
     *
     * @param listener a PropertyChangeListener object receiving all editing notifications
     */
    public void addEditListener(PropertyChangeListener listener) {
        mtInit();
        mt.addPropertyChangeListener(listener);
        eml.addPropertyChangeListener(listener);
    }

    /**
     * Removes a previously added editing action listener.
     *
     * @param listener a PropertyChangeListener object previously added
     */
    public void removeEditListener(PropertyChangeListener listener) {
        mtInit();
        mt.removePropertyChangeListener(listener);
        eml.removePropertyChangeListener(listener);
    }

    /**
     * Adds a listener which gets notified of actions on managed {@link SurfShapesLayer} objects.
     *
     * @param listener a PropertyChangeListener object
     */
    public void addSurfShapeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a previously added {@link SurfShapesLayer} listener.
     *
     * @param listener a PropertyChangeListener object previously added
     */
    public void removeSurfShapeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Tells if the FlyToPanel is shown.
     *
     * @return true if fly to functions are enabled
     */
    public boolean isFlyToVisible() {
        return flyToPanel != null && flyToPanel.isVisible();
    }

    /**
     * Shows/hides the FlyToPanel.
     * <p>
     * FlyToPanel is shown by default.
     *
     * @param flag true to show the FlyToPanel
     */
    public void setFlyToVisible(boolean flag) {
        flyToPanel.setVisible(flag);
        pTop.revalidate();
    }

    /**
     * Adds a {@link PositionParser} to the FlyToPanel.
     * <p>
     * By default the FlyToPanel has a {@link LatLonParser} and a {@link LatLonDMSParser}.
     *
     * @param parser a {@link PositionParser} object
     */
    public void addFlyToPositionParser(PositionParser parser) {
        flyToPanel.addParser(parser);
    }

    /**
     * Tells if editing toolbar is shown.
     *
     * @return true if editing functions enabled
     */
    public boolean isEditBarVisible() {
        return editBtnsPanel != null && editBtnsPanel.isVisible();
    }

    /**
     * Shows/hides the editing toolbar.
     * <p>
     * Editing toolbar is hidden by default.
     *
     * @param flag true to show editing toolbar
     */
    public void setEditBarVisible(boolean flag) {
        // lazyly construct and place on the toolbar the EditButtonsPanel component
        if (editBtnsPanel == null) {
            editBtnsPanel = new EditButtonsPanel();
            editBtnsPanel.setWorldWindow(wwCanvas);
            pTop.add(editBtnsPanel, 2);
            pTop.add(Box.createHorizontalStrut(12), 3);
        }
        editBtnsPanel.setVisible(flag);
        pTop.revalidate();
    }

    /**
     * Shows/hides the layer settings button.
     * <p>
     * Layer settings button is hidden by default.
     *
     * @param flag true to show the button
     */
    public void setLayerSettingsButtonVisible(boolean flag) {
        // lazily construct the dialog
        if (layerSettingsDialog == null) {
            layerSettingsDialog = new DynamicLayerSettingsDialog((Frame) SwingUtilities.getAncestorOfClass(javax.swing.JFrame.class, this));
            layerSettingsDialog.linkTo(this);
            layerSettingsDialog.pack();
            layerSettingsDialog.setLocationRelativeTo(this);
        }
        // lazily construct the button and place it on the toolbar
        if (bLayerSettings == null) {
            bLayerSettings = new JButton();
            bLayerSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/net/falappa/wwind/widgets/img/glyphicons_114_list.png"))); // NOI18N
            bLayerSettings.setToolTipText("Layer settings");
            bLayerSettings.setMargin(new java.awt.Insets(0, 0, 0, 0));
            bLayerSettings.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    layerSettingsDialog.setVisible(true);
                }
            });
            pTop.add(Box.createHorizontalStrut(6), pTop.getComponentCount());
            pTop.add(bLayerSettings, pTop.getComponentCount());
        }
        bLayerSettings.setVisible(flag);
        pTop.revalidate();
    }

    /**
     * Tells if the layer settings button is shown.
     *
     * @return true if editing functions enabled
     */
    public boolean isLayerSettingsButtonVisible() {
        return bLayerSettings != null && bLayerSettings.isVisible();
    }

    /**
     * Adds a generic WorldWind layer to the map.
     * <p>
     * The layer is added on top of the other layers.
     *
     * @param layer the layer to add
     */
    public void addLayer(Layer layer) {
        wwCanvas.getModel().getLayers().add(layer);
    }

    /**
     * Removes a generic WorldWind layer from the map.
     *
     * @param layer the layer to remove
     */
    public void removeLayer(Layer layer) {
        wwCanvas.getModel().getLayers().remove(layer);
    }

    /**
     * Adds a SurfShapeLayer to the map and to the map of managed layers.
     * <p>
     * The layer is added if not already present in the map. The layer is linked to the map for event processing (e.g. selection)
     *
     * @param slayer the layer to add
     */
    public void addSurfShapeLayer(SurfShapeLayer slayer) {
        final String layerName = slayer.getName();
        if (!shapeLayers.containsKey(layerName)) {
            //link layer for selection processing
            if (slayer instanceof ShapeSelectionSource) {
                ((ShapeSelectionSource) slayer).linkTo(wwCanvas);
            }
            shapeLayers.put(layerName, slayer);
            wwCanvas.getModel().getLayers().add(slayer);
            changeSupport.firePropertyChange(EVENT_SURF_LAYER_ADDED, null, slayer);
        }
    }

    /**
     * Checks if a named SurfShapeLayer is present in the managed set.
     *
     * @param name the name of the layer
     * @return true if a layer is present, false otherwise
     */
    public boolean hasSurfShapeLayer(String name) {
        return shapeLayers.containsKey(name);
    }

    /**
     * Getter for a named SurfShapeLayer.
     *
     * @param name the name of the layer
     * @return the layer object reference or null if not present
     */
    public SurfShapeLayer getSurfShapeLayer(String name) {
        return shapeLayers.get(name);
    }

    /**
     * Removes the named SurfShapeLayer from the map.
     * <p>
     * The layer content is left untouched. Before removal the layer is correctly detached from the WorldWind map.
     *
     * @param name the name of the layer
     */
    public void removeSurfShapeLayer(String name) {
        SurfShapeLayer removedLayer = shapeLayers.remove(name);
        if (removedLayer != null) {
            // detach the layer listeners from the WorldWindow
            if (removedLayer instanceof ShapeSelectionSource) {
                ((ShapeSelectionSource) removedLayer).detach();
            }
            // remove the layer from the map layer list
            wwCanvas.getModel().getLayers().remove(removedLayer);
            changeSupport.firePropertyChange(EVENT_SURF_LAYER_REMOVED, removedLayer, null);
        }
    }

    /**
     * Getter for all the registered SurfShapeLayers.
     *
     * @return a reference to the layers map values (as a Collection object) see {@link Map#values()}.
     */
    public Collection<SurfShapeLayer> getAllSurfShapeLayers() {
        return shapeLayers.values();
    }

    /**
     * Removes all SurfaceShapeLayer layers from the map.
     */
    public void removeAllSurfShapeLayers() {
        for (Iterator<Map.Entry<String, SurfShapeLayer>> it = shapeLayers.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, SurfShapeLayer> entry = it.next();
            entry.getValue().dispose();
            wwCanvas.getModel().getLayers().remove(entry.getValue());
            changeSupport.firePropertyChange(EVENT_SURF_LAYER_REMOVED, entry.getValue(), null);
            it.remove();
        }
    }

    /**
     * Toggles the visibility on the map of the given named SurfaceShapeLayer.
     * <p>
     * The layer is still present on the map.
     *
     * @param name the layer name
     * @param flag true if visible false otherwise
     */
    public void setSurfShapeLayerVisible(String name, boolean flag) {
        if (shapeLayers.containsKey(name)) {
            shapeLayers.get(name).setEnabled(flag);
        }
    }

    /**
     * Tells if an Area Of Interest has been set.
     *
     * @return true if an area of interest exists
     */
    public boolean hasAOI() {
        return moi.isPositionSet() || aoi.hasShape();
    }

    /**
     * Getter for the current Area Of Interest shape type.
     *
     * @return an {@link AoiShapes} enumerated value or null if no area of interest
     */
    public AoiShapes getAOIType() {
        if (moi.isPositionSet()) {
            return AoiShapes.POINT;
        } else {
            Renderable shape = aoi.getShape();
            if (shape instanceof SurfacePolygon) {
                return AoiShapes.POLYGON;
            } else if (shape instanceof SurfaceCircle) {
                return AoiShapes.CIRCLE;
            } else if (shape instanceof Path) {
                return AoiShapes.POLYLINE;
            }
        }
        return null;
    }

    /**
     * Accesses the current Area Of Interest coordinates.
     * <p>
     * Can be called when the AOI type is either {@link AoiShapes#POLYGON} or {@link AoiShapes#POLYGON}.
     *
     * @return an iterable on the coordinates or null if not applicable
     */
    public Iterable<? extends LatLon> getAOICoordinates() {
        Renderable shape = aoi.getShape();
        if (shape instanceof SurfacePolygon) {
            SurfacePolygon poly = (SurfacePolygon) shape;
            return poly.getOuterBoundary();
        }
        if (shape instanceof Path) {
            Path path = (Path) shape;
            return path.getPositions();
        }
        return null;
    }

    /**
     * Accesses the current Area Of Interest center/position.
     * <p>
     * Can be called when the AOI type is either {@link AoiShapes#CIRCLE} or {@link AoiShapes#POINT}.
     *
     * @return the center/positon as a LatLon object or null if not applicable
     */
    public LatLon getAOICenter() {
        if (moi.isPositionSet()) {
            return moi.getPosition();
        } else {
            Renderable shape = aoi.getShape();
            if (shape instanceof SurfaceCircle) {
                SurfaceCircle circle = (SurfaceCircle) shape;
                return circle.getCenter();
            }
        }
        return null;
    }

    /**
     * Accesses the current Area Of Interest radius.
     * <p>
     * Can be called when the AOI type is {@link AoiShapes#CIRCLE}.
     *
     * @return the radius in meters or null if not applicable
     */
    public Double getAOIRadius() {
        Renderable shape = aoi.getShape();
        if (shape instanceof SurfaceCircle) {
            SurfaceCircle circle = (SurfaceCircle) shape;
            return circle.getRadius();
        }
        return null;
    }

    /**
     * Clears the current Area Of Interest.
     */
    public void clearAOI() {
        aoi.clear();
        moi.clear();
    }

    /**
     * Hides/shows the current Area Of Interest.
     *
     * @param flag true to show, false to hide
     */
    public void setAOIVisible(boolean flag) {
        aoi.setEnabled(flag);
        moi.setEnabled(flag);
    }

    /**
     * Tells if the current Area Of Interest is visible.
     *
     * @return true if visible
     */
    public boolean isAOIVisible() {
        return aoi.isEnabled();
    }

    /**
     * Defines a circular Area Of Interest.
     *
     * @param center circle center as Position object
     * @param radius circle radius in meters
     */
    public void setAOICircle(Position center, double radius) {
        aoi.setSurfCircle(center, radius);
        moi.clear();
    }

    /**
     * Defines a point Area Of Interest.
     *
     * @param pos location as Position object
     */
    public void setAOIPoint(Position pos) {
        moi.setPosition(pos);
        aoi.clear();
    }

    /**
     * Defines a labeled point Area Of Interest.
     *
     * @param pos location as Position object
     * @param text the label text
     */
    public void setAOIPoint(Position pos, String text) {
        moi.setPosition(pos, text);
        aoi.clear();
    }

    /**
     * Defines a geodetic poligonal Area Of Interest.
     *
     * @param points polygon points as Iterable of Position objects
     */
    public void setAOIPolygon(Iterable<? extends LatLon> points) {
        ArrayList<LatLon> pointsCopy = new ArrayList<>();
        for (LatLon p : points) {
            pointsCopy.add(p);
        }
        aoi.setSurfPoly(pointsCopy);
        moi.clear();
    }

    /**
     * Defines a geodetic multi segment Area Of Interest.
     *
     * @param points polyline points as Iterable of Position objects
     */
    public void setAOIPolyline(Iterable<? extends LatLon> points) {
        ArrayList<Position> pointsCopy = new ArrayList<>();
        for (LatLon p : points) {
            pointsCopy.add(new Position(p, 0));
        }
        aoi.setSurfLine(pointsCopy);
        moi.clear();
    }

    /**
     * Setter for Area Of Interest Color.
     *
     * @param col the new color
     */
    public void setAOIColor(Color col) {
        aoi.setColor(col);
        moi.setColor(col);
    }

    /**
     * Getter for Area Of Interest Color.
     *
     * @return the current color
     */
    public Color getAOIColor() {
        return aoi.getColor();
    }

    /**
     * Setter for Area Of Interest outline color (border).
     *
     * @param col the new color
     */
    public void setAOIOutlineColor(Color col) {
        aoi.setColorExterior(col);
        moi.setColor(col);
    }

    /**
     * Getter for Area Of Interest outline color.
     *
     * @return the current color
     */
    public Color getAOIOutlineColor() {
        return aoi.getColorExterior();
    }

    /**
     * Setter for Area Of Interest fill color (inner).
     *
     * @param col the new color
     */
    public void setAOIFillColor(Color col) {
        aoi.setColorInterior(col);
    }

    /**
     * Getter for Area Of Interest outline color.
     *
     * @return the current color
     */
    public Color getAOIFillColor() {
        return aoi.getColorInterior();
    }

    /**
     * Insert the layer into the layer list just before the placenames.
     *
     * @param layer
     */
    public void insertBeforePlacenames(Layer layer) {
        int pos = 0;
        LayerList layers = wwCanvas.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof PlaceNameLayer) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos, layer);
    }

    /**
     * Insert the layer into the layer list just before the Lat Lon graticule.
     *
     * @param layer
     */
    public void insertBeforeGraticule(Layer layer) {
        int pos = 0;
        LayerList layers = wwCanvas.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof LatLonGraticuleLayer) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos, layer);
    }

    /**
     * Insert the layer into the layer list just after the Blue Marble Next Generation Image layer.
     *
     * @param layer
     */
    public void insertAfterBMNGImage(Layer layer) {
        int pos = 0;
        LayerList layers = wwCanvas.getModel().getLayers();
        for (Layer l : layers) {
            if (l instanceof BMNGOneImage) {
                pos = layers.indexOf(l);
            }
        }
        layers.add(pos + 1, layer);
    }

    /**
     * Asks for an asynchronous redraw of the map.
     */
    public void redraw() {
        wwCanvas.redraw();
    }

    /**
     * Instantly moves the point of view of the map as looking to the given lat lon coordinates from the given altitude.
     *
     * @param position the coordinates and elevation of the eye
     */
    public void eyeToPoint(Position position) {
        wwCanvas.getView().setEyePosition(position);
        wwCanvas.redraw();
    }

    /**
     * Instantly moves the point of view of the map to the given altitude.
     *
     * @param altitude the desired altitude in meters
     */
    public void eyeToAltitude(double altitude) {
        if (wwCanvas.getView() instanceof BasicOrbitView) {
            BasicOrbitView oview = (BasicOrbitView) wwCanvas.getView();
            oview.setZoom(altitude);
        } else if (wwCanvas.getView() instanceof FlatOrbitView) {
            FlatOrbitView fview = (FlatOrbitView) wwCanvas.getView();
            fview.setZoom(altitude);
        }
        wwCanvas.redraw();
    }

    /**
     * Instantly zooms in or out according to the given factor.
     * <p>
     * Recalculates the current eye altitude according to the formula:
     * <tt>current_altitude + current_altitude * zFact</tt>
     *
     * @param zFact the zoom factor, positive values zoom out while negative ones zoom in
     */
    public void eyeZoomAltitude(double zFact) {
        if (wwCanvas.getView() instanceof BasicOrbitView) {
            BasicOrbitView oview = (BasicOrbitView) wwCanvas.getView();
            final double currZoom = oview.getZoom();
            oview.setZoom(currZoom + currZoom * zFact);
        } else if (wwCanvas.getView() instanceof FlatOrbitView) {
            FlatOrbitView fview = (FlatOrbitView) wwCanvas.getView();
            final double currZoom = fview.getZoom();
            fview.setZoom(currZoom + currZoom * zFact);
        }
        wwCanvas.redraw();
    }

    /**
     * Animates the map, in a sort of fligth, to bring the given position into view.
     *
     * @param position the coordinates and elevation to fly to
     */
    public void flyToPoint(Position position) {
        WWindUtils.flyToPoint(wwCanvas, position);
    }

    /**
     * Animates the map, in a sort of fligth, to bring the given area into view.
     *
     * @param sector the lat lon range sector to fly to
     */
    public void flyToSector(Sector sector) {
        WWindUtils.flyToSector(wwCanvas, sector);
    }

    /**
     * Animates the map, in a sort of fligth, to bring the current area of interest into view.
     * <p>
     * Does nothing if there's no AOI defined.
     */
    public void flyToAOI() {
        if (hasAOI()) {
            if (getAOIType() == AoiShapes.POINT) {
                moi.flyToCurrPos(wwCanvas);
            } else {
                aoi.flyToAOI(wwCanvas);
            }
        }
    }

    /**
     * Animates the map, in a sort of fligth, to bring the current edit shape into view.
     * <p>
     * Does nothing if there's no edit shape defined.
     */
    public void flyToEditShape() {
        if (hasEditShape()) {
            switch (editMode) {
                case POLYLINE:
                    WWindUtils.flyToObjects(wwCanvas, Arrays.asList(mt.getLine()));
                    break;
                case POINT:
                    Position pos = new Position(eml.getPosition(), 1_000_000);
                    WWindUtils.flyToPoint(wwCanvas, pos);
                    break;
                default:
                    WWindUtils.flyToObjects(wwCanvas, Arrays.asList(mt.getSurfaceShape()));
                    break;
            }
        }
    }

    /**
     * Animates the map, in a sort of fligth, to bring the given WorldWind objects into view.
     * <p>
     * Delegates to {@link WWindUtils#flyToObjects(gov.nasa.worldwind.WorldWindow, java.lang.Iterable)}.
     *
     * @param itrs a collection of objects
     */
    public void flyToObjects(Iterable<?> itrs) {
        WWindUtils.flyToObjects(wwCanvas, itrs);
    }

    /**
     * Clears highlightd shapes from every registered surface shape layer.
     *
     * @param fireEvent whether to fire de-selection events or not
     */
    public void clearHighlights(boolean fireEvent) {
        for (SurfShapeLayer sl : shapeLayers.values()) {
            sl.clearHighlight(fireEvent);
        }
    }

    /**
     * Highlights the shape with the given id in one of the registered shape layers.
     *
     * @param id the shape identifier
     * @param fireEvent true to fire selection events
     * @throws NoSuchShapeException
     */
    public void highlightShape(String id, boolean fireEvent) throws NoSuchShapeException {
        for (SurfShapeLayer sl : shapeLayers.values()) {
            sl.highlightShape(id, fireEvent);
        }
    }

    @Override
    public void loadPrefs(Preferences baseNode) {
        // store layer settings
        Preferences viewPrefs = baseNode.node(PREFN_WWP);
        if (layerSettingsDialog != null) {
            layerSettingsDialog.loadPrefs(viewPrefs);
        }
        // TODO load projection
        // TODO delegate to FlyToPanel
        // TODO delegate to layer too??
    }

    @Override
    public void storePrefs(Preferences baseNode) {
        // store layer settings
        Preferences viewPrefs = baseNode.node(PREFN_WWP);
        if (layerSettingsDialog != null) {
            layerSettingsDialog.storePrefs(viewPrefs);
        }
        // TODO store projection
        // TODO delegate to FlyToPanel
        // TODO delegate to layer too??
    }

    /**
     * Debug method to print the current layer list (with class names).
     */
    public void dumpLayerList() {
        int pos = 1;
        LayerList layers = wwCanvas.getModel().getLayers();
        StringBuilder sb = new StringBuilder("Current layer list:\n");
        for (Layer l : layers) {
            sb.append(String.format("%2d: %s [%s]\n", pos++, l.getName(), l.getClass().getName()));
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Layers", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Toggles a crosshair mouse cursor over the map.
     *
     * @param flag true if cursor should be changed to crosshair false to revert it back to arrow
     */
    public void setCrosshair(boolean flag) {
        if (flag) {
            ((Component) wwCanvas).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            ((Component) wwCanvas).setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Retrieve a screenshot of World Wind current view
     *
     * @return Burred image instance containing current view graphic details.
     * @throws java.awt.AWTException if graphical libraries are not properly installed/working as expected.
     */
    public BufferedImage getScreenshot() throws AWTException {
        // Get the Widget's Location and Size
        Component world = this.wwCanvas;
        java.awt.Point p = world.getLocationOnScreen();
        int width = world.getWidth();
        int height = world.getHeight();
        BufferedImage screencapture = null;
        // Sanity Check
        if (height > 0 && width > 0) {
            // Create the Buffer
            screencapture = new java.awt.Robot().createScreenCapture(new java.awt.Rectangle(p.x, p.y, width, height));
        }
        return screencapture;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pTop = new javax.swing.JPanel();
        flyToPanel = new net.falappa.wwind.widgets.FlyToPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        globeSwitcher = new net.falappa.wwind.widgets.FlatRoundInLinePanel();
        wwCanvas = new gov.nasa.worldwind.awt.WorldWindowGLCanvas();

        setLayout(new java.awt.BorderLayout());

        pTop.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 2, 3, 2));
        pTop.setLayout(new javax.swing.BoxLayout(pTop, javax.swing.BoxLayout.LINE_AXIS));
        pTop.add(flyToPanel);
        pTop.add(filler1);
        pTop.add(globeSwitcher);

        add(pTop, java.awt.BorderLayout.PAGE_START);
        add(wwCanvas, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private net.falappa.wwind.widgets.FlyToPanel flyToPanel;
    private net.falappa.wwind.widgets.FlatRoundInLinePanel globeSwitcher;
    private javax.swing.JPanel pTop;
    private gov.nasa.worldwind.awt.WorldWindowGLCanvas wwCanvas;
    // End of variables declaration//GEN-END:variables

    private void setupWorldWind() {
        BasicModel model = new BasicModel();
        wwCanvas.setModel(model);
        wwCanvas.setSize(400, 300);
        // Register a rendering exception listener
        wwCanvas.addRenderingExceptionListener(new RenderingExceptionListener() {
            @Override
            public void exceptionThrown(Throwable t) {
                if (t instanceof WWAbsentRequirementException) {
                    StringBuilder message = new StringBuilder("Computer does not meet minimum graphics requirements.\n");
                    message.append("Please install up-to-date graphics driver and try again.\n");
                    message.append("Reason: ").append(t.getMessage());
                    message.append("\nThis program will end when you press OK.");
                    JOptionPane.showMessageDialog(null, message.toString(), "Unable to Start Program", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                } else {
                    JOptionPane.showMessageDialog(null, "WorldWind library rendering problem!\n" + t.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    t.printStackTrace(System.err);
                }
            }
        });
        LayerList layers = model.getLayers();
        // (optionally) setup the base cartography WMS layer stack
        setupBaseCarto();
        // add the graticule layer (not visible)
        lGraticule.setAngleFormat(Angle.ANGLE_FORMAT_DD);
        lGraticule.setEnabled(false);
        layers.add(lGraticule);
        // add the area of interest layer
        layers.add(aoi);
        // add the marker of interest layer
        layers.add(moi);
        // add the StatusLayer
        lStatus.setEventSource(wwCanvas);
        lStatus.setDefaultFont(UIManager.getFont("Label.font"));
        lStatus.setAngleFormat(Angle.ANGLE_FORMAT_DD);
        layers.add(lStatus);
        // add a view controls layer and register a controller for it.
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        layers.add(viewControlsLayer);
        wwCanvas.addSelectListener(new ViewControlsSelectListener(wwCanvas, viewControlsLayer));
    }

    private void setupBaseCarto() {
        String cartoConfigUrl = System.getProperty(WWINDPANEL_BASECARTO_CONFIG_URL);
        if (cartoConfigUrl == null) {
            logger.log(Level.CONFIG, "No base cartography configuration url specified");
            return;
        }
        logger.log(Level.CONFIG, "Loading base cartography from {0}", cartoConfigUrl);
        InputStream is = null;
        if (cartoConfigUrl.startsWith("file:")) {
            try {
                File f = new File(new URI(cartoConfigUrl));
                if (f.isFile() && f.canRead()) {
                    try {
                        is = new FileInputStream(f);
                    } catch (FileNotFoundException ex) {
                        logger.log(Level.WARNING, ex.getMessage());
                    }
                }
            } catch (URISyntaxException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        } else if (cartoConfigUrl.startsWith("classpath:")) {
            is = WWindPanel.class.getResourceAsStream(cartoConfigUrl.replace("classpath:", ""));
        } else {
            logger.log(Level.WARNING, "Unknown or malofrmed URL {0}", cartoConfigUrl);
        }
        if (is == null) {
            logger.log(Level.SEVERE, "Could not find base cartography configuration url {0}", cartoConfigUrl);
            return;
        }
        try {
            Properties prop = new Properties();
            prop.load(is);
            int numLayers = Integer.parseInt(prop.getProperty("num-layers", "0"));
            logger.log(Level.CONFIG, "Read {0} configured layers (top to bottom):", numLayers);
            for (int i = numLayers; i > 0; i--) {
                // skip iteration if display name with currend index does not exists
                if (!prop.containsKey(String.format("%d.display-name", i))) {
                    continue;
                }
                String displayName = prop.getProperty(String.format("%d.display-name", i));
                String getCapUrl = prop.getProperty(String.format("%d.getcapabilities-url", i));
                String getMapUrl = prop.getProperty(String.format("%d.getmap-url", i));
                String layerNames = prop.getProperty(String.format("%d.server-layer-names", i));
                String cacheFolder = prop.getProperty(String.format("%d.data-cache-folder", i));
                int numLevels = Integer.parseInt(prop.getProperty(String.format("%d.num-levels", i), "1"));
                boolean onLoad = Boolean.parseBoolean(prop.getProperty(String.format("%d.actuate-onLoad", i), "true"));
                logger.log(Level.CONFIG, " {0}: {1} {2} - {3}",
                        new Object[]{i, onLoad ? "[ON ]" : "[OFF]", displayName, getMapUrl});
                WMSTiledImageLayer wmsl = WMSLayerFactory.createWMSLayer(displayName, getCapUrl, getMapUrl, layerNames, cacheFolder,
                        numLevels);
                wmsl.setEnabled(onLoad);
                insertAfterBMNGImage(wmsl);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not load base cartography configuration from {0}: {1}", new Object[]{cartoConfigUrl, ex
                .getMessage()});
        }
    }

    // lazily construct the MeasureTool and the EditableMarkerLayer
    private void mtInit() {
        if (mt == null) {
            // create and setup the measure tool
            mt = new MeasureTool(wwCanvas);
            // set some attributes of the measure tool
            final MeasureToolController mtController = new MeasureToolController();
            mtController.setUseRubberBand(false);
            mt.setController(mtController);
            mt.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
            mt.setShowAnnotation(false);
            mt.setFollowTerrain(true);
            mt.setFillColor(new Color(255, 255, 255, 63));
            mt.setLineColor(COLOR_EDIT);
            mt.getControlPointsAttributes().setBackgroundColor(new Color(0, 200, 255, 200));
            mt.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(MeasureTool.EVENT_ARMED)) {
                        setCrosshair(mt.isArmed());
                    }
                }
            });
            // create the EditableMarkerLayer for marker editing
            eml = new EditableMarkerLayer(wwCanvas, "EditableMarkerLayer");
            eml.setColor(COLOR_EDIT);
            eml.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(EditableMarkerLayer.EVENT_EDITING_START)) {
                        setCrosshair(true);
                    }
                    if (evt.getPropertyName().equals(EditableMarkerLayer.EVENT_POSITION_SET)) {
                        setCrosshair(false);
                        eml.setEditing(false);
                    }
                }
            });
            wwCanvas.getModel().getLayers().add(eml);
        }
    }
}
