package net.falappa.wwind.widgets;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import net.falappa.wwind.helpers.LabeledPosition;
import net.falappa.wwind.layers.SingleMarkerLayer;
import net.falappa.wwind.posparser.PositionParser;

/**
 * A panel allowing to enter a location and moving the view to the entered location.
 * <p>
 * The component also allows to go to the previous, next locations. The component enables itself after setting the controlled
 * {@link WWindPanel}.
 *
 * @author Alessandro Falappa
 */
public class FlyToPanel extends javax.swing.JPanel {

    private WWindPanel wp;
    private final SingleMarkerLayer flyPosLayer = new SingleMarkerLayer("FlyToMarker");
    private int showDelay = 0;
    private int hideDelay = 0;
    private Timer showTimer;
    private Timer hideTimer;
    private LabeledPosition curLoc = null;
    private final LinkedList<LabeledPosition> prevLocs = new LinkedList<>();
    private final LinkedList<LabeledPosition> nextLocs = new LinkedList<>();
    private final ArrayList<PositionParser> parsers = new ArrayList<>();
    private final StringBuilder sbTooltip = new StringBuilder();

    /**
     * Default constructor.
     * <p>
     * The panel is initially disabled, must be linked to a WWindPanel to work.
     */
    public FlyToPanel() {
        initComponents();
        initTooltipMessage();
    }

    private void initTimers() {
        if (showDelay > 0) {
            showTimer = new Timer(showDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flyPosLayer.setPosition(curLoc, curLoc.getLabel());
                    wp.redraw();
                }
            });
            showTimer.setRepeats(false);
        }
        if (hideDelay > 0) {
            hideTimer = new Timer(hideDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flyPosLayer.clear();
                    wp.redraw();
                }
            });
            hideTimer.setRepeats(false);
        }
    }

    /**
     * Initializing constructor.
     *
     * @param wp the WWindPanel to link to
     */
    public FlyToPanel(WWindPanel wp) {
        initComponents();
        initTooltipMessage();
        setWWindPanel(wp);
    }

    /**
     * Getter for the linked WWindPanel.
     *
     * @return the current WWindPanel
     */
    public WWindPanel getWWindPanel() {
        return wp;
    }

    /**
     * Setter for the linked WWindPanel.
     *
     * @param wp the new WWindPanel
     */
    public final void setWWindPanel(WWindPanel wp) {
        if (wp == null) {
            throw new NullPointerException("Null WWindPanel");
        }
        this.wp = wp;
        // set up marker layer
        flyPosLayer.setColor(Color.orange);
        flyPosLayer.setOpacity(0.5);
        wp.addLayer(flyPosLayer);
        // enable widgets
        jLabel1.setEnabled(true);
        txLocation.setEnabled(true);
        bGo.setEnabled(true);
        // make tooltips heavyweight by default
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }

    /**
     * Set the position marker visual attributes.
     *
     * @param col marker color
     * @param opacity marker opacity
     */
    public void setMarkerLayerAttributes(Color col, double opacity) {
        flyPosLayer.setColor(col);
        flyPosLayer.setOpacity(opacity);
    }

    /**
     * Activates a delayed showing/hiding of the position marker.
     *
     * @param showMillis the showing delay in milliseconds, zero or negative to disable delayed showing.
     * @param hideMillis the hiding delay in milliseconds, must be greater than showMillis if both specified, zero or negative to disable
     * delayed hiding.
     */
    public void setDelays(int showMillis, int hideMillis) {
        if (hideMillis > 0 && hideMillis < showMillis) {
            throw new IllegalArgumentException("hide delay must be greater than show delay");
        }
        showDelay = showMillis;
        hideDelay = hideMillis;
        initTimers();
    }

    /**
     * Adds a <tt>PositionParser</tt> to the list of parsers used to interpret location strings.
     *
     * @param parser an object implementing the {@link PositionParser} interface
     */
    public void addParser(PositionParser parser) {
        if (parser != null) {
        parsers.add(parser);
            sbTooltip.append("<li>").append(parser.getFormatDescription());
            txLocation.setToolTipText(sbTooltip.toString());
        }
    }

    /**
     * Removes all position parsers.
     */
    public void removeAllParsers() {
        parsers.clear();
        initTooltipMessage();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        bGo = new javax.swing.JButton();
        txLocation = new javax.swing.JTextField();
        bPrevLoc = new javax.swing.JButton();
        bNextLoc = new javax.swing.JButton();

        jLabel1.setText("Fly to");
        jLabel1.setEnabled(false);

        bGo.setText("Go");
        bGo.setEnabled(false);
        bGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGoActionPerformed(evt);
            }
        });

        txLocation.setColumns(10);
        txLocation.setEnabled(false);
        txLocation.setMaximumSize(new java.awt.Dimension(200, 30));
        txLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txLocationActionPerformed(evt);
            }
        });

        bPrevLoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/falappa/wwind/widgets/img/glyphicons_224_chevron-left.png"))); // NOI18N
        bPrevLoc.setToolTipText("Previous location");
        bPrevLoc.setEnabled(false);
        bPrevLoc.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bPrevLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrevLocActionPerformed(evt);
            }
        });

        bNextLoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/falappa/wwind/widgets/img/glyphicons_223_chevron-right.png"))); // NOI18N
        bNextLoc.setToolTipText("Next location");
        bNextLoc.setEnabled(false);
        bNextLoc.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bNextLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNextLocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txLocation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bGo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bPrevLoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bNextLoc)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bNextLoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(txLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(bPrevLoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bGo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initTooltipMessage() {
        txLocation.setToolTipText(null);
        sbTooltip.setLength(0);
        sbTooltip.append("<html><p>").append("Supported formats").append(":<ul>");
    }
    private void enablePrevNextButtons() {
        bPrevLoc.setEnabled(!prevLocs.isEmpty());
        bNextLoc.setEnabled(!nextLocs.isEmpty());
    }

    private LabeledPosition parseLocation(String text) {
        // try all the parsers, return the position parsed by the first found
        for (PositionParser pars : parsers) {
            LabeledPosition pos = pars.parseString(text);
            if (pos != null) {
                return pos;
            }
        }
        return null;
    }

    private void goToPos(LabeledPosition pos) {
        // store position
        curLoc = pos;
        // (delayed) show of marker
        if (showDelay > 0) {
            showTimer.start();
        } else {
            flyPosLayer.setPosition(pos, pos.getLabel());
        }
        // delayed hiding of marker
        if (hideDelay > 0) {
            hideTimer.start();
        }
        // pan to position
        wp.flyToPoint(pos);
    }

    private void bGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGoActionPerformed
        LabeledPosition pos = parseLocation(txLocation.getText());
        if (pos != null) {
            // store position in previous locations
            if (curLoc != null) {
                prevLocs.addFirst(curLoc);
            }
            goToPos(pos);
            enablePrevNextButtons();
        }
    }//GEN-LAST:event_bGoActionPerformed

    private void bPrevLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrevLocActionPerformed
        // current to next and previous location to current
        final LabeledPosition prevPos = prevLocs.removeFirst();
        nextLocs.addFirst(curLoc);
        goToPos(prevPos);
        txLocation.setText(prevPos.getLabel());
        enablePrevNextButtons();
    }//GEN-LAST:event_bPrevLocActionPerformed

    private void bNextLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNextLocActionPerformed
        // current to prev and next location to current
        final LabeledPosition nextLoc = nextLocs.removeFirst();
        prevLocs.addFirst(curLoc);
        goToPos(nextLoc);
        txLocation.setText(nextLoc.getLabel());
        enablePrevNextButtons();
    }//GEN-LAST:event_bNextLocActionPerformed

    private void txLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txLocationActionPerformed
        bGoActionPerformed(null);
    }//GEN-LAST:event_txLocationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bGo;
    private javax.swing.JButton bNextLoc;
    private javax.swing.JButton bPrevLoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txLocation;
    // End of variables declaration//GEN-END:variables
}
