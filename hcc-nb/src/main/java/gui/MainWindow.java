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
package gui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.Layer;
import gui.dialogs.AboutDialog;
import gui.dialogs.CatDefinitionDialog;
import gui.dialogs.MetadataDetailDialog;
import gui.dialogs.MetadataGridDialog;
import gui.dialogs.SettingsDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.App;
import main.data.CatalogueDefinition;
import main.data.Metadata;
import static main.data.MetadataNames.FOOTPRINT;
import static main.data.MetadataNames.PARENT_IDENTIFIER;
import static main.data.MetadataNames.PRODUCT_IDENTIFIER;
import main.hma.HmaGetRecordsBuilder;
import net.falappa.prefs.PrefRestorable;
import net.falappa.utils.GuiUtils;
import net.falappa.wwind.layers.NightDayLayer;
import net.falappa.wwind.layers.NoSuchShapeException;
import net.falappa.wwind.layers.SurfShapeLayer;
import net.falappa.wwind.layers.SurfShapesLayer;
import net.falappa.wwind.utils.WWindUtils;
import net.opengis.www.cat.csw._2_0_2.GetRecordsDocument;
import net.opengis.www.cat.wrs._1_0.CatalogueStub;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HCC main window.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class MainWindow extends javax.swing.JFrame implements PrefRestorable {

    private static final Color[] LAYER_COLORS = new Color[]{
        Color.ORANGE,
        Color.MAGENTA,
        Color.RED,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.WHITE,};
    // preference node names
    private static final String PREFN_WINDOW = "MainWindow";
    private static final String PREFN_CATALOGUES = "catalogues";
    // preference keys
    private static final String PREFK_CAT_COLLECTIONS = "collections";
    private static final String PREFK_CAT_TIMEOUT = "timeout";
    private static final String PREFK_CAT_SOAPV12 = "soapv12";
    private static final String PREFK_CAT_EDP = "edp";
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class.getName());
    private final DefaultComboBoxModel<CatalogueDefinition> dcmCatalogues = new DefaultComboBoxModel<>();
    private CatalogueStub stub = null;
    private BasicEventList<Metadata> results = new BasicEventList<>();
    private MetadataGridDialog gridDialog;
    private MetadataDetailDialog detailDialog;

    public MainWindow() {
        initComponents();
        try {
            stub = new CatalogueStub();
            pCollections.setStub(stub);
        } catch (AxisFault ex) {
            ex.printStackTrace(System.err);
        }
        wwindPane.setLayerSettingsButtonVisible(true);
        pNavigation.linkTo(wwindPane);
        loadPrefs(App.getAppPrefs());
        final NightDayLayer ndl = new NightDayLayer();
        ndl.setTime(new Date());
        ndl.setEnabled(false);
        wwindPane.addLayer(ndl);
        pTime.setNightDayLayer(ndl, wwindPane.getWWCanvas());
    }

    public CatalogueDefinition getCurrentCatalogue() {
        return (CatalogueDefinition) cbCatalogues.getSelectedItem();
    }

    public void execHits() {
        if (checkCanSubmit()) {
            pSearchButons.enableButtons(false);
            startWorker(false);
        }
    }

    public void execResults() {
        if (checkCanSubmit()) {
            pSearchButons.enableButtons(false);
            startWorker(true);
        }
    }

    public String getReqText() {
        if (checkCanSubmit()) {
            GetRecordsDocument req = buildReq(true);
            return req.xmlText(new XmlOptions().setSavePrettyPrint());
        }
        return null;
    }

    public void showErrorDialog(String title, String message) {
        showErrorDialog(title, message, null);
    }

    public void showErrorDialog(String title, String message, Exception ex) {
        StringBuilder sb = new StringBuilder(message);
        if (ex != null) {
            sb.append("\n\nException:\n").append(ex.getMessage());
        }
        JOptionPane.showMessageDialog(this, sb.toString(), title, JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pQueryParams = new javax.swing.JPanel();
        pCollections = new gui.panels.CollectionsPanel();
        pTime = new gui.panels.TimeWindowPanel();
        pGeo = new gui.panels.GeoAreaPanel();
        pSearchButons = new gui.panels.SearchButtonsPanel();
        pNavigation = new gui.panels.NavigationPanel();
        pToolBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbCatalogues = new javax.swing.JComboBox();
        bAddCat = new javax.swing.JButton();
        bDelCat = new javax.swing.JButton();
        lMexs = new javax.swing.JLabel();
        bEditCat = new javax.swing.JButton();
        bInfo = new javax.swing.JButton();
        bSettings = new javax.swing.JButton();
        wwindPane = new net.falappa.wwind.widgets.WWindPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HMA Catalogue Client");
        setPreferredSize(new java.awt.Dimension(1200, 800));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pCollections.setButtonsVisible(false);

        javax.swing.GroupLayout pQueryParamsLayout = new javax.swing.GroupLayout(pQueryParams);
        pQueryParams.setLayout(pQueryParamsLayout);
        pQueryParamsLayout.setHorizontalGroup(
            pQueryParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pCollections, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pSearchButons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pTime, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(pGeo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(pNavigation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pQueryParamsLayout.setVerticalGroup(
            pQueryParamsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pQueryParamsLayout.createSequentialGroup()
                .addComponent(pCollections, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(pTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pGeo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(pSearchButons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        getContentPane().add(pQueryParams, java.awt.BorderLayout.WEST);

        jLabel1.setText("Catalogue");

        cbCatalogues.setModel(dcmCatalogues);
        cbCatalogues.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbCataloguesItemStateChanged(evt);
            }
        });
        cbCatalogues.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cbCataloguesMouseEntered(evt);
            }
        });

        bAddCat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images_16x16/glyphicons_190_circle_plus.png"))); // NOI18N
        bAddCat.setToolTipText("Create catalogue definition");
        bAddCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddCatActionPerformed(evt);
            }
        });

        bDelCat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images_16x16/glyphicons_191_circle_minus.png"))); // NOI18N
        bDelCat.setToolTipText("Remove current catalogue definition");
        bDelCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDelCatActionPerformed(evt);
            }
        });

        lMexs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        bEditCat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images_16x16/glyphicons_150_edit.png"))); // NOI18N
        bEditCat.setToolTipText("Edit current catalogue definition");
        bEditCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEditCatActionPerformed(evt);
            }
        });

        bInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images_16x16/glyphicons_195_circle_info.png"))); // NOI18N
        bInfo.setToolTipText("About HCC");
        bInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInfoActionPerformed(evt);
            }
        });

        bSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images_16x16/glyphicons_137_cogwheels.png"))); // NOI18N
        bSettings.setToolTipText("HCC settings");
        bSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pToolBarLayout = new javax.swing.GroupLayout(pToolBar);
        pToolBar.setLayout(pToolBarLayout);
        pToolBarLayout.setHorizontalGroup(
            pToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pToolBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCatalogues, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAddCat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bDelCat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bEditCat)
                .addGap(5, 5, 5)
                .addComponent(lMexs, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bSettings)
                .addContainerGap())
        );

        pToolBarLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bInfo, bSettings});

        pToolBarLayout.setVerticalGroup(
            pToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pToolBarLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bSettings)
                    .addGroup(pToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lMexs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bAddCat)
                            .addComponent(cbCatalogues, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(bDelCat))
                        .addComponent(bEditCat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pToolBarLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bInfo, bSettings});

        getContentPane().add(pToolBar, java.awt.BorderLayout.PAGE_START);

        wwindPane.setBottomBar(true);
        getContentPane().add(wwindPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAddCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddCatActionPerformed
        CatDefinitionDialog d = new CatDefinitionDialog(this);
        d.setStub(stub);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
        if (d.isOkPressed()) {
            cbCatalogues.addItem(d.getDefinedCatalogue());
        }
    }//GEN-LAST:event_bAddCatActionPerformed

    private void bDelCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDelCatActionPerformed
        if (cbCatalogues.getSelectedIndex() >= 0) {
            dcmCatalogues.removeElementAt(cbCatalogues.getSelectedIndex());
        }
    }//GEN-LAST:event_bDelCatActionPerformed

    private void bInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInfoActionPerformed
        AboutDialog ad = new AboutDialog(this);
        ad.setLocationRelativeTo(this);
        ad.setVisible(true);
    }//GEN-LAST:event_bInfoActionPerformed

    private void bEditCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEditCatActionPerformed
        final int selIdx = cbCatalogues.getSelectedIndex();
        if (selIdx >= 0) {
            CatDefinitionDialog d = new CatDefinitionDialog(this, (CatalogueDefinition) cbCatalogues.getSelectedItem());
            d.setStub(stub);
            d.setLocationRelativeTo(this);
            d.setVisible(true);
            if (d.isOkPressed()) {
                dcmCatalogues.insertElementAt(d.getDefinedCatalogue(), selIdx + 1);
                dcmCatalogues.removeElementAt(selIdx);
            }
        }
    }//GEN-LAST:event_bEditCatActionPerformed

    private void cbCataloguesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbCataloguesMouseEntered
        cbCatalogues.setToolTipText(getCatalogueTooltip());
    }//GEN-LAST:event_cbCataloguesMouseEntered

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        storePrefs(App.getAppPrefs());
    }//GEN-LAST:event_formWindowClosing

    private void cbCataloguesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbCataloguesItemStateChanged
        if (cbCatalogues.getSelectedIndex() >= 0) {
            final CatalogueDefinition selCatDef = getCurrentCatalogue();
            // rewrite collection list
            pCollections.setCollections(selCatDef.getCollections());
            if (selCatDef.isSoapV12()) {
                //set soap 1.2 in stub
                stub._getServiceClient().getOptions().setSoapVersionURI(Constants.URI_SOAP12_ENV);
            } else {
                //set soap 1.1 in stub
                stub._getServiceClient().getOptions().setSoapVersionURI(Constants.URI_SOAP11_ENV);
            }
            //set timeout in stub
            Integer to = selCatDef.getTimeoutMillis();
            stub._getServiceClient().getOptions().setProperty(HTTPConstants.SO_TIMEOUT, to);
            stub._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, to);
            // set endpoint url in stub
            stub._getServiceClient().setTargetEPR(new EndpointReference(selCatDef.getEndpoint()));
        }
    }//GEN-LAST:event_cbCataloguesItemStateChanged

    private void bSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSettingsActionPerformed
        SettingsDialog sd = new SettingsDialog(this);
        sd.setLocationRelativeTo(this);
        sd.setVisible(true);
    }//GEN-LAST:event_bSettingsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddCat;
    private javax.swing.JButton bDelCat;
    private javax.swing.JButton bEditCat;
    private javax.swing.JButton bInfo;
    private javax.swing.JButton bSettings;
    private javax.swing.JComboBox cbCatalogues;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JLabel lMexs;
    private gui.panels.CollectionsPanel pCollections;
    private gui.panels.GeoAreaPanel pGeo;
    private gui.panels.NavigationPanel pNavigation;
    private javax.swing.JPanel pQueryParams;
    private gui.panels.SearchButtonsPanel pSearchButons;
    private gui.panels.TimeWindowPanel pTime;
    private javax.swing.JPanel pToolBar;
    public net.falappa.wwind.widgets.WWindPanel wwindPane;
    // End of variables declaration//GEN-END:variables

    private boolean checkCanSubmit() {
        if (getCurrentCatalogue() == null) {
            showErrorDialog("Submission error", "Select a catalogue first!");
            return false;
        }
        if (!pCollections.isCollectionSelected()) {
            showErrorDialog("Submission error", "At least one collection must be selected");
            return false;
        }
        return true;
    }

    GetRecordsDocument buildReq(boolean isResults) {
        HmaGetRecordsBuilder builder = new HmaGetRecordsBuilder();
        switch (pSearchButons.getDetail()) {
            case 0:
                //brief
                builder.setDetailBrief();
                break;
            case 1:
                //summary
                builder.setDetailSummary();
                break;
            case 2:
                //full
                builder.setDetailFull();
                break;
        }
        // request type
        if (isResults) {
            builder.setResults();
        } else {
            builder.setHits();
        }
        // max records and start position
        builder.setMaxRecords(pSearchButons.getMaxRecs());
        builder.setStartPosition(pSearchButons.getStartPos());
        //add collections
        String[] colls = pCollections.getSelectedCollections();
        if (colls != null) {
            if (colls.length == 1) {
                builder.addCollection(colls[0]);
            } else {
                builder.addCollections(colls);
            }
        }
        // add time constraints
        if (pTime.constraintsEnabled()) {
            switch (pTime.getOperator()) {
                case 0:
                    builder.addTemporalContained(pTime.getT1(), pTime.getT2());
                    break;
                case 1:
                    builder.addTemporalOverlaps(pTime.getT1(), pTime.getT2());
                    break;
                case 2:
                    builder.addTemporalAfter(pTime.getT1());
                    break;
                case 3:
                    builder.addTemporalBefore(pTime.getT1());
                    break;
            }
        }
        // add spatial constraints
        if (pGeo.constraintsEnabled()) {
            switch (pGeo.getPrimitive()) {
                case 0:
                    builder.addSpatialPolygon(pGeo.getOperator(), WWindUtils.latLonList2PosList(wwindPane.getAOICoordinates()));
                    break;
                case 1:
                    LatLon c = wwindPane.getAOICenter();
                    builder.addSpatialCircle(pGeo.getOperator(), c.latitude.degrees, c.longitude.degrees, wwindPane.getAOIRadius());
                    break;
                case 2:
                    builder.addSpatialPolyline(pGeo.getOperator(), WWindUtils.latLonList2PosList(wwindPane.getAOICoordinates()));
                    break;
                case 3:
                    c = wwindPane.getAOICenter();
                    builder.addSpatialPoint(pGeo.getOperator(), c.latitude.degrees, c.longitude.degrees);
                    break;
                case 4:
                    //TODO support lat lon range AOI
                    builder.addSpatialRange(pGeo.getOperator(), 0, 10, 0, 10);
                    break;
            }
        }
        final GetRecordsDocument request = builder.getRequest();
        return request;
    }

    void enableSearchButtons(boolean enabled) {
        pSearchButons.enableButtons(enabled);
    }

    private void startWorker(boolean isResults) {
        GetRecordsWorker grw = new GetRecordsWorker(this, stub, isResults, results);
        grw.execute();
    }

    private String getCatalogueTooltip() {
        CatalogueDefinition cat = getCurrentCatalogue();
        if (cat != null) {
            StringBuilder sb = new StringBuilder("<html>");
            sb.append("<b>").append(cat.getName()).append("</b><br>");
            sb.append("Endpoint: ").append(cat.getEndpoint()).append("<br><i>");
            sb.append(cat.isSoapV12() ? "SOAP v1.2" : "SOAP V1.1").append("</i></html>");
            return sb.toString();
        } else {
            return "No catalogue";
        }
    }

    @Override
    public void storePrefs(Preferences baseNode) {
        // save window state
        Preferences winPrefs = baseNode.node(PREFN_WINDOW);
        GuiUtils.storePrefsFrame(winPrefs, this);
        // save view settings
        wwindPane.storePrefs(baseNode);
        // save catalogue definitions
        Preferences pCatalogs = baseNode.node(PREFN_CATALOGUES);
        for (int i = 0; i < dcmCatalogues.getSize(); i++) {
            CatalogueDefinition catDef = dcmCatalogues.getElementAt(i);
            // create new pref child node with catalogue name
            Preferences catPref = pCatalogs.node(catDef.getName());
            // store endpoint, timeout and soap flag
            catPref.put(PREFK_CAT_EDP, catDef.getEndpoint());
            catPref.putBoolean(PREFK_CAT_SOAPV12, catDef.isSoapV12());
            catPref.putInt(PREFK_CAT_TIMEOUT, catDef.getTimeoutMillis());
            // store collections as space separated string
            StringBuilder sb = new StringBuilder();
            final int arrLen = catDef.getCollections().length;
            for (int j = 0; j < arrLen; j++) {
                sb.append(catDef.getCollections()[j]);
                if (j < arrLen - 1) {
                    sb.append(' ');
                }
            }
            catPref.put(PREFK_CAT_COLLECTIONS, sb.toString());
        }
        // store dialogs size/positions
        if (gridDialog != null) {
            gridDialog.storePrefs(baseNode);
        }
        if (detailDialog != null) {
            detailDialog.storePrefs(baseNode);
        }
    }

    @Override
    public void loadPrefs(Preferences baseNode) {
        try {
            // load window state
            Preferences winPrefs = baseNode.node(PREFN_WINDOW);
            GuiUtils.loadPrefsFrame(winPrefs, this);
            // load view settings
            wwindPane.loadPrefs(baseNode);
            // load catalogue definitions
            Preferences pCatalogs = baseNode.node(PREFN_CATALOGUES);
            final String[] nodes = pCatalogs.childrenNames();
            for (String nodeName : nodes) {
                Preferences catPref = pCatalogs.node(nodeName);
                CatalogueDefinition catDef = new CatalogueDefinition(nodeName, catPref.get(PREFK_CAT_EDP, "n/a"), catPref.getBoolean(
                        PREFK_CAT_SOAPV12,
                        false), catPref.getInt(PREFK_CAT_TIMEOUT, 20000));
                catDef.setCollections(catPref.get(PREFK_CAT_COLLECTIONS, "").split("\\s"));
                dcmCatalogues.addElement(catDef);
            }
        } catch (BackingStoreException ex) {
            // no prefs, do nothing
        }
    }

    void postResults() {
        // create the grid and detail dialogs if needed
        if (gridDialog == null) {
            gridDialog = new MetadataGridDialog(this);
            gridDialog.setDataList(results);
            detailDialog = new MetadataDetailDialog(this, gridDialog);
            // initial position in the bottom right part of the main window
            final Dimension dims = this.getSize();
            final Dimension gwDims = gridDialog.getSize();
            Point pt = this.getLocation();
            pt.translate(dims.width - gwDims.width, dims.height - gwDims.height);
            gridDialog.setLocation(pt);
            // override size/location from prefs
            gridDialog.loadPrefs(App.getAppPrefs());
            detailDialog.loadPrefs(App.getAppPrefs());
            // setup gridDialog listeners
            gridDialog.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        EventList<Metadata> selList = gridDialog.getListOfSelected();
                        if (!selList.isEmpty()) {
                            wwindPane.clearHighlights(false);
                            Metadata selMd = selList.get(0);
                            SurfShapeLayer ssl = wwindPane.getSurfShapeLayer(selMd.get(PARENT_IDENTIFIER));
                            try {
                                ssl.highlightShape(selMd.get(PRODUCT_IDENTIFIER), false);
                            } catch (NoSuchShapeException ex) {
                                //ignored should not verify
                            }
                            wwindPane.redraw();
                        }
                    }
                }
            });
            gridDialog.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        EventList<Metadata> selList = gridDialog.getListOfSelected();
                        if (!selList.isEmpty()) {
                            Metadata selMd = selList.get(0);
                            SurfShapeLayer ssl = wwindPane.getSurfShapeLayer(selMd.get(PARENT_IDENTIFIER));
                            try {
                                ssl.flyToShape(selMd.get(PRODUCT_IDENTIFIER));
                            } catch (NoSuchShapeException ex) {
                                //ignored should not verify
                            }
                        }
                    }
                }

            });
        }
        // remove previous surface shape layers
        wwindPane.removeAllSurfShapeLayers();
        HashMap<String, SurfShapesLayer> layerMap = new HashMap<>();
        // prepare and categorize the footprints
        ArrayList<String> prodIds = new ArrayList<>(results.size());
        for (Metadata md : results) {
            if (md.containsKey(PRODUCT_IDENTIFIER) && md.containsKey(PARENT_IDENTIFIER) && md.containsKey(FOOTPRINT)) {
                String pid = md.get(PRODUCT_IDENTIFIER);
                prodIds.add(pid);
                // get the collection layer or create one and add id to the wwindPane
                String collection = md.get(PARENT_IDENTIFIER);
                SurfShapesLayer ssl = layerMap.get(collection);
                if (ssl == null) {
                    ssl = new SurfShapesLayer(collection);
                    // choose next color from LAYER_COLORS palette
                    ssl.setColor(LAYER_COLORS[layerMap.size() % LAYER_COLORS.length]);
                    // listen for user clicks on map shapes
                    ssl.addShapeSelectionListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            String shpId = (String) evt.getNewValue();
                            String coll = ((Layer) evt.getSource()).getName();
                            gridDialog.selectRow(coll, shpId);
                        }
                    });
                    wwindPane.addSurfShapeLayer(ssl);
                    layerMap.put(collection, ssl);
                }
                // add a polygon to the layer
                ssl.addSurfPoly(WWindUtils.latLonOrdinates2LatLonList(md.getFootprintAsDoubles()), pid);
            }
        }
        for (Map.Entry<String, SurfShapesLayer> en : layerMap.entrySet()) {
            logger.info("Collection {} recs {}", en.getKey(), en.getValue().getNumShapes());
        }
        pNavigation.setProductIds(prodIds);
        wwindPane.redraw();
        // show the grid dialog if hidden
        if (!gridDialog.isVisible()) {
            gridDialog.setVisible(true);
        }
        // trigger the grid adjust
        gridDialog.updateFinished();
        // show the detail dialog if hidden
        if (!detailDialog.isVisible()) {
            detailDialog.setVisible(true);
        }
        // clear the detail dialog
        detailDialog.setMetadata(null);
    }
}
