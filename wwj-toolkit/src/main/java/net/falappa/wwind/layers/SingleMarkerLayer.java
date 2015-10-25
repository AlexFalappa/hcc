package net.falappa.wwind.layers;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.render.Annotation;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.AnnotationRenderer;
import gov.nasa.worldwind.render.BasicAnnotationRenderer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import gov.nasa.worldwind.render.markers.MarkerRenderer;
import gov.nasa.worldwind.terrain.SectorGeometryList;
import net.falappa.prefs.PrefRestorable;
import net.falappa.wwind.utils.WWindUtils;

/**
 * Layer containing a single spherical marker, optionaly labeled.
 * <p>
 * The optional label appears above the marker and has a white text on a semi-transparent black background.
 * <p>
 * The layer is not pickable and is meant to be altered programmatically only (does not offer support for user interaction). Some parts of
 * the implementation has been taken from WorldWind MarkerLayer class.
 *
 * @author Alessandro Falappa
 */
public class SingleMarkerLayer extends AbstractLayer implements PrefRestorable {

    private final MarkerRenderer mrkrRenderer = new MarkerRenderer();
    private final MarkerAttributes mrkrAttr = new BasicMarkerAttributes();
    private final ArrayList<Marker> mrkrs = new ArrayList<>(1);
    private final AnnotationRenderer annotRenderer = new BasicAnnotationRenderer();
    private final AnnotationAttributes annotAttr = new AnnotationAttributes();
    private final ArrayList<Annotation> annots = new ArrayList<>(1);

    /**
     * Initializing constructor.
     *
     * @param name the name of this layer
     */
    public SingleMarkerLayer(String name) {
        // properties of layer
        setName(name);
        setPickEnabled(false);
        // keep markers on top of terrain
        mrkrRenderer.setOverrideMarkerElevation(true);
        // painting attributes for markers
        mrkrAttr.setShapeType(BasicMarkerShape.SPHERE);
        mrkrAttr.setMaterial(Material.RED);
        mrkrAttr.setMarkerPixels(8);
        mrkrAttr.setMinMarkerSize(3);
        // painting attribute for annotation
        annotAttr.setDrawOffset(new Point(0, 12));
        annotAttr.setFrameShape(AVKey.SHAPE_RECTANGLE);
        annotAttr.setCornerRadius(2);
        annotAttr.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT);
        annotAttr.setLeader(AVKey.SHAPE_NONE);
        annotAttr.setBackgroundColor(new Color(0f, 0f, 0f, .35f));
        annotAttr.setBorderWidth(0);
        annotAttr.setInsets(new Insets(3, 6, 3, 6));
        annotAttr.setTextColor(Color.WHITE);
        annotAttr.setFont(UIManager.getFont("Label.font"));

    }

    /**
     * Getter for the marker color.
     *
     * @return the current color
     */
    public Color getColor() {
        return mrkrAttr.getMaterial().getDiffuse();
    }

    /**
     * Setter for the marker color.
     *
     * @param col the new color
     */
    public void setColor(Color col) {
        mrkrAttr.setMaterial(new Material(col));
    }

    @Override
    public void setOpacity(double opacity) {
        super.setOpacity(opacity);
        mrkrAttr.setOpacity(opacity);
    }

    /**
     * Getter for the label text color.
     *
     * @return the current color
     */
    public Color getLabelTextColor() {
        return annotAttr.getTextColor();
    }

    /**
     * Setter for the label text color.
     *
     * @param col the new color
     */
    public void setLabelTextColor(Color col) {
        annotAttr.setTextColor(col);
    }

    /**
     * Getter for the label background color.
     *
     * @return the current color
     */
    public Color getLabelBackgroundColor() {
        return annotAttr.getBackgroundColor();
    }

    /**
     * Setter for the label background color.
     *
     * @param col the new color
     */
    public void setLabelBackgroundColor(Color col) {
        annotAttr.setBackgroundColor(col);
    }

    /**
     * Setter for the only marker position.
     * <p>
     * Implicitly clears the label text and hides the label
     *
     * @param coords the new position
     */
    public void setPosition(Position coords) {
        if (mrkrs.size() == 1) {
            mrkrs.get(0).setPosition(coords);
        } else {
            Marker marker = new BasicMarker(coords, mrkrAttr);
            mrkrs.add(marker);
        }
        annots.clear();
    }

    /**
     * Setter for the labeled marker position.
     *
     * @param coords the new position
     * @param label the label text
     */
    public void setPosition(Position coords, String label) {
        if (mrkrs.size() == 1) {
            mrkrs.get(0).setPosition(coords);
        } else {
            Marker marker = new BasicMarker(coords, mrkrAttr);
            mrkrs.add(marker);
        }
        setLabel(label);
    }

    /**
     * Getter for the marker position.
     *
     * @return the current position or null if no position has ever been set (no marker)
     */
    public Position getPosition() {
        if (isPositionSet()) {
            return mrkrs.get(0).getPosition();
        }
        return null;
    }

    /**
     * Getter for the current marker label.
     *
     * @return the label text or null if no label has been set.
     */
    public String getLabel() {
        if (annots.size() == 1) {
            return annots.get(0).getText();
        }
        return null;
    }

    /**
     * Setter for the label text.
     * <p>
     * Does nothing if no position has been set.
     *
     * @param label the label text. Clears the label if null.
     */
    public void setLabel(String label) {
        Position currPos = getPosition();
        if (currPos == null) {
            return;
        }
        if (label == null) {
            annots.clear();
        } else if (annots.size() == 1) {
            annots.get(0).setText(label);
        } else {
            Annotation ann = new GlobeAnnotation(label, currPos, annotAttr);
            annots.add(ann);
        }
    }

    /**
     * Clear the current label and marker position (hides the marker).
     */
    public void clear() {
        mrkrs.clear();
        annots.clear();
    }

    /**
     * Tells if the marker has ever been set.
     *
     * @return true if the marker has been positioned, false otherwise
     */
    public boolean isPositionSet() {
        return !mrkrs.isEmpty();
    }

    /**
     * Animate a given map bringing the current marker into view.
     * <p>
     * Does nothing if no position has been set.
     *
     * @param wwd the <tt>WorldWindow</tt> to animate
     */
    public void flyToCurrPos(WorldWindow wwd) {
        if (isPositionSet()) {
            final Position pos = new Position(mrkrs.get(0).getPosition(), 100e3);
            WWindUtils.flyToPoint(wwd, pos);
        }
    }

    @Override
    public void loadPrefs(Preferences baseNode) {
        // TODO caricamento da preferences
    }

    @Override
    public void storePrefs(Preferences baseNode) {
        // TODO memorizzazione preferences
    }

    @Override
    protected void doRender(DrawContext dc) {
        draw(dc, null);
    }

    @Override
    protected void doPick(DrawContext dc, Point pickPoint) {
        draw(dc, pickPoint);
    }

    // taken from WorldWind MarkerLayer
    protected void draw(DrawContext dc, Point pickPoint) {
        if (mrkrs.isEmpty()) {
            return;
        }
        if (dc.getVisibleSector() == null) {
            return;
        }
        SectorGeometryList geos = dc.getSurfaceGeometry();
        if (geos == null) {
            return;
        }
        mrkrRenderer.render(dc, mrkrs);
        annotRenderer.render(dc, annots, this);
    }
}
