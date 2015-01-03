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
package gui.dialogs;

import com.l2fprod.common.swing.JTaskPaneGroup;
import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import com.l2fprod.common.swing.plaf.aqua.AquaLookAndFeelAddons;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import main.data.Metadata;
import main.data.MetadataNames;
import main.hma.HmaMetadataSeq;

/**
 * Modeless dialog for showing metadata detail of one product in a form like layout.
 * <p>
 * All product attributes are presented grouped by belonging extrinsic object in name value pairs.
 * <p>
 * Can "follow" selection on a {@link MetadataGridDialog}
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class MetadataDetailDialog extends javax.swing.JDialog {

    private MetadataGridDialog gridDialog = null;

    /**
     * Base constructor.
     *
     * @param parent parent frame
     */
    public MetadataDetailDialog(java.awt.Frame parent) {
        this(parent, null);
    }

    /**
     * Constructor to follow a {@link MetadataGridDialog}.
     *
     * @param parent parent frame
     * @param gDialog the grid dialog to follow. Maybe null.
     */
    public MetadataDetailDialog(java.awt.Frame parent, final MetadataGridDialog gDialog) {
        super(parent, false);
        // set addon for L2FProd JTaskPaneGroup Look&Feel
        try {
            LookAndFeelAddons.setAddon(AquaLookAndFeelAddons.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            // ignore will use default addons
        }
        initComponents();
        // attach a selection listener to the gridDialog if provided
        if (gDialog != null) {
            this.gridDialog = gDialog;
            gridDialog.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting() && !gridDialog.getListOfSelected().isEmpty()) {
                        setMetadata(gridDialog.getListOfSelected().get(0));
                    }
                }
            });
        }
    }

    /**
     * Sets the product to show.
     *
     * @param meta the product metadata or null to clear the dialog
     */
    public void setMetadata(Metadata meta) {
        tpgContainer.removeAll();
        if (meta != null) {
            addGroup("EO Product", buildTableModel(HmaMetadataSeq.formListEOProduct, meta));
            addGroup("EO Acquisition Platform", buildTableModel(HmaMetadataSeq.formListEOAcquisitionPlatform, meta));
            addGroup("EO Browse Information", buildTableModel(HmaMetadataSeq.formListEOBrowseInformation, meta));
            addGroup("EO Archiving Information", buildTableModel(HmaMetadataSeq.formListEOArchivingInformation, meta));
        }
        tpgContainer.revalidate();
        tpgContainer.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroller = new javax.swing.JScrollPane();
        tpgContainer = new com.l2fprod.common.swing.JTaskPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Metadata Detail");

        scroller.setViewportView(tpgContainer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addGroup(String title, TableModel tbModel) {
        if (tbModel.getRowCount() > 0) {
            JTable tbl = new JTable(tbModel);
            JTaskPaneGroup group = new JTaskPaneGroup();
            group.setTitle(title);
            group.setAnimated(false);
            group.setScrollOnExpand(true);
            group.add(tbl);
            tpgContainer.add(group);
        }
    }

    private TableModel buildTableModel(List<MetadataNames> mnList, Metadata meta) {
        DefaultTableModel dtm = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        for (MetadataNames mn : mnList) {
            if (meta.containsKey(mn)) {
                if (HmaMetadataSeq.longLabels.get(mn) == null) {
                    System.out.printf("Null name %s%n", mn.toString());
                }
                if (meta.get(mn) == null) {
                    System.out.printf("Null value %s%n", mn.toString());
                }
                dtm.addRow(new Object[]{HmaMetadataSeq.longLabels.get(mn), meta.get(mn)});
            }
        }
        return dtm;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scroller;
    private com.l2fprod.common.swing.JTaskPane tpgContainer;
    // End of variables declaration//GEN-END:variables
}