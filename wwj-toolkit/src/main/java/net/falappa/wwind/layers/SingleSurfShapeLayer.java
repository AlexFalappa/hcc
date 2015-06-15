package net.falappa.wwind.layers;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfaceSector;
import gov.nasa.worldwind.render.SurfaceShape;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import net.falappa.prefs.PrefRestorable;
import net.falappa.wwind.utils.WWindUtils;

/**
 * A layer containing a single surface shape.
 * <p>
 * The layer is not pickable and does not support user interaction; can be altered programmatically only. Setting a surface shape (one of
 * polygon, circle, sector, polyline) removes the previous one.
 * <p>
 * Recommended for containing areas of interest in spatial queries.
 *
 * @author Alessandro Falappa
 */
public class SingleSurfShapeLayer extends RenderableLayer implements PrefRestorable {

    private static final float NORM_INSIDE_OPACITY = 0.1f;
    private final BasicShapeAttributes attr = new BasicShapeAttributes();
    private Renderable current;

    /**
     * Initializing constructor.
     *
     * @param name the name of this layer
     */
    public SingleSurfShapeLayer(String name) {
        // properties of layer
        setName(name);
        setEnabled(true);
        setPickEnabled(false);
        // painting attributes for footprints
        attr.setOutlineMaterial(Material.RED);
        attr.setOutlineWidth(2);
        attr.setInteriorMaterial(new Material(Color.red.brighter().brighter()));
        attr.setInteriorOpacity(NORM_INSIDE_OPACITY);
    }

    /**
     * Getter for the shape color.
     *
     * @return the current color
     */
    public Color getColor() {
        return attr.getOutlineMaterial().getDiffuse();
    }

    /**
     * Setter for the shape color.
     * <p>
     * Both the outline and fill colors are set. The fill color is a brighter version of the given color.
     *
     * @param col the new color
     */
    public void setColor(Color col) {
        attr.setOutlineMaterial(new Material(col));
        attr.setInteriorMaterial(new Material(col.brighter().brighter()));
    }

    /**
     * Getter for the shape fill color
     *
     * @return the current color
     */
    public Color getColorInterior() {
        return attr.getInteriorMaterial().getDiffuse();
    }

    /**
     * Setter for the shape fill color.
     *
     * @param col the new color
     */
    public void setColorInterior(Color col) {
        attr.setInteriorMaterial(new Material(col));
    }

    /**
     * Getter for the shape outline color
     *
     * @return the current color
     */
    public Color getColorExterior() {
        return attr.getOutlineMaterial().getDiffuse();
    }

    /**
     * Setter for the shape outline color.
     *
     * @param col the new color
     */
    public void setColorExterior(Color col) {
        attr.setOutlineMaterial(new Material(col));
    }

    @Override
    public void setOpacity(double opacity) {
        super.setOpacity(opacity);
        attr.setOutlineOpacity(opacity);
        attr.setInteriorOpacity(NORM_INSIDE_OPACITY * opacity);
    }

    /**
     * Return the current surface shape.
     * <p>
     * Maybe null if no shape has ever been set.
     *
     * @return the current shape
     */
    public Renderable getShape() {
        return current;
    }

    /**
     * Changes the current shape to a circle.
     *
     * @param center the circle center
     * @param radius the circle radius in meters
     */
    public void setSurfCircle(LatLon center, double radius) {
        removeOldAoI();
        current = new SurfaceCircle(attr, center, radius);
        addRenderable(current);
    }

    /**
     * Changes the current shape to a geodetic polygon.
     *
     * @param coords the polygon corner coordiinates
     */
    public void setSurfPoly(Iterable<? extends LatLon> coords) {
        removeOldAoI();
        current = new SurfacePolygon(attr, coords);
        addRenderable(current);
    }

    /**
     * Changes the current shape to a geodetic polygon with holes.
     * <p>
     * Each boundary is a list of LatLon points. Should have at least two boundaries.
     *
     * @param boundaries a list of poligon boundaries, first is outer then one or more inner (holes)
     */
    public void setSurfPoly(List<List<LatLon>> boundaries) {
        if (boundaries.isEmpty()) {
            return;
        }
        removeOldAoI();
        SurfacePolygon poly = new SurfacePolygon(attr, boundaries.get(0));
        for (int i = 1; i < boundaries.size(); i++) {
            poly.addInnerBoundary(boundaries.get(i));
        }
        current = poly;
        addRenderable(current);
    }

    /**
     * Changes the current shape to a Lat Lon range expressed as minimum/maximum latitudes/longitudes.
     *
     * @param minlat minimum latitude
     * @param minlon minimum longitude
     * @param maxlat maximum latitude
     * @param maxlon maximum longitude
     */
    public void setSurfSect(double minlat, double minlon, double maxlat, double maxlon) {
        removeOldAoI();
        SurfaceSector shape = new SurfaceSector(attr, Sector.fromDegrees(minlat, maxlat, minlon, maxlon));
        shape.setPathType(AVKey.LINEAR);
        current = shape;
        addRenderable(current);
    }

    /**
     * Changes the current shape to a Lat Lon range expressed as a <tt>Sector</tt> object.
     *
     * @param sec the Sector
     */
    public void setSurfSect(Sector sec) {
        removeOldAoI();
        SurfaceSector shape = new SurfaceSector(attr, sec);
        shape.setPathType(AVKey.LINEAR);
        current = shape;
        addRenderable(current);
    }

    /**
     * Changes the current shape to a polyline.
     *
     * @param coords the polyline corners
     */
    public void setSurfLine(Iterable<? extends Position> coords) {
        removeOldAoI();
        Path shape = new Path(coords);
        shape.setAttributes(attr);
        shape.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        shape.setPathType(AVKey.GREAT_CIRCLE);
        shape.setFollowTerrain(true);
        shape.setTerrainConformance(40);
        current = shape;
        addRenderable(current);
    }

    /**
     * Tells if a shape exists.
     *
     * @return true if the shape of this layer has been set
     */
    public boolean hasShape() {
        return current != null;
    }

    /**
     * Removes the current shape if exists.
     */
    public void clear() {
        removeAllRenderables();
        current = null;
    }

    private void removeOldAoI() {
        if (current != null) {
            removeRenderable(current);
            current = null;
        }
    }

    /**
     * Animate a given map bringing the current shape into view.
     * <p>
     * Does nothing if no sape has been set.
     *
     * @param wwd the <tt>WorldWindow</tt> to animate
     */
    public void flyToAOI(WorldWindow wwd) {
        if (!hasShape()) {
            return;
        }
        if (current instanceof SurfaceShape) {
            SurfaceShape shape = (SurfaceShape) current;
            WWindUtils.flyToObjects(wwd, Arrays.asList(shape));
        } else {
            Path shape = (Path) current;
            WWindUtils.flyToObjects(wwd, Arrays.asList(shape));
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

}
