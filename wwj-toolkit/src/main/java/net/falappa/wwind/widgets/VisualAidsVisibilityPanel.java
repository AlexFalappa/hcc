package net.falappa.wwind.widgets;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JCheckBox;
import net.falappa.prefs.PrefRestorable;
import net.falappa.wwind.utils.ToggleVisibilityAction;

/**
 * A panel listing and controlling the visibility of the default visual aids in a {@link WWindPanel} (mini world map, compass, scale bar,
 * navigation controls and Lat Lon graticule).
 * <p>
 * Layers are listed in the order they are added.
 *
 * @author Alessandro Falappa
 */
public class VisualAidsVisibilityPanel extends javax.swing.JPanel implements PrefRestorable {

    private static final String PREFNODE_VISUALAIDS = "visual-aids";

    /**
     * Default constructor.
     */
    public VisualAidsVisibilityPanel() {
        initComponents();
    }

    /**
     * Attach this component to the given {@link WWindPanel}.
     *
     * @param wwp the WWindPanel to attach to
     */
    public void linkTo(WWindPanel wwp) {
        final WorldWindowGLCanvas wwCanvas = wwp.getWWCanvas();
        LayerList layers = wwCanvas.getModel().getLayers();
        Layer layer = layers.getLayerByName("Scale bar");
        link(chScale, wwCanvas, layer);
        layer = layers.getLayerByName("Compass");
        link(chCompass, wwCanvas, layer);
        layer = layers.getLayerByName("World Map");
        link(chMiniMap, wwCanvas, layer);
        layer = layers.getLayerByName("View Controls");
        link(chViewContrl, wwCanvas, layer);
        layer = layers.getLayerByName("Lat-Lon Graticule");
        link(chGraticule, wwCanvas, layer);
    }

    @Override
    public void storePrefs(Preferences prefs) {
        // create a subnode for view settings
        Preferences vnode = prefs.node(PREFNODE_VISUALAIDS);
        // store layer enablement
        putChbInPrefs(chCompass, vnode);
        putChbInPrefs(chGraticule, vnode);
        putChbInPrefs(chMiniMap, vnode);
        putChbInPrefs(chScale, vnode);
        putChbInPrefs(chViewContrl, vnode);
    }

    @Override
    public void loadPrefs(Preferences prefs) {
        // get view settings subnode
        Preferences vnode = prefs.node(PREFNODE_VISUALAIDS);
        // load layer enablement
        getChbFromPrefs(chCompass, vnode, false);
        getChbFromPrefs(chGraticule, vnode, false);
        getChbFromPrefs(chMiniMap, vnode, false);
        getChbFromPrefs(chScale, vnode, true);
        getChbFromPrefs(chViewContrl, vnode, true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chCompass = new javax.swing.JCheckBox();
        chViewContrl = new javax.swing.JCheckBox();
        chScale = new javax.swing.JCheckBox();
        chGraticule = new javax.swing.JCheckBox();
        chMiniMap = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Visual Aids"));

        chCompass.setText("Compass");

        chViewContrl.setText("View Controls");

        chScale.setText("Scale");

        chGraticule.setText("Graticule");

        chMiniMap.setText("World Map");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chCompass)
                    .addComponent(chScale)
                    .addComponent(chViewContrl)
                    .addComponent(chGraticule)
                    .addComponent(chMiniMap))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(chMiniMap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chCompass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chScale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chViewContrl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chGraticule)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chCompass;
    private javax.swing.JCheckBox chGraticule;
    private javax.swing.JCheckBox chMiniMap;
    private javax.swing.JCheckBox chScale;
    private javax.swing.JCheckBox chViewContrl;
    // End of variables declaration//GEN-END:variables

    private void link(JCheckBox jcb, WorldWindow wwd, Layer layer) {
        if (layer != null) {
            jcb.setSelected(layer.isEnabled());
            jcb.setAction(new ToggleVisibilityAction(layer, wwd));
        } else {
            jcb.setEnabled(false);
        }
    }

    private void putChbInPrefs(JCheckBox chb, Preferences vnode) {
        vnode.putBoolean(chb.getText(), chb.isSelected());
    }

    private void getChbFromPrefs(JCheckBox chb, Preferences vnode, boolean flag) {
        chb.setSelected(vnode.getBoolean(chb.getText(), flag));
        // force action firing
        final Action act = chb.getAction();
        if (act != null) {
            act.actionPerformed(new ActionEvent(chb, 1, "initial"));
        }
    }
}
