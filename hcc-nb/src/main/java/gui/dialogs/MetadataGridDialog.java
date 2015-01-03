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
package gui.dialogs;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import gui.glazed.MetadataTableFormat;
import gui.glazed.MetadataTableFormatFactory;
import java.awt.event.MouseListener;
import java.util.Collections;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import main.data.Metadata;
import main.data.MetadataNames;
import net.falappa.swing.table.TableColumnAdjuster;

/**
 * Modeless dialog displaying a tabular view of some of the queryed metadata.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class MetadataGridDialog extends javax.swing.JDialog {

    private EventList<Metadata> dataList;
//    private FilterList<Metadata> filterList;
    private SortedList<Metadata> sortedList;
    private AdvancedListSelectionModel<Metadata> selModel;
    private final TableColumnAdjuster adjuster;
    private AdvancedTableModel<Metadata> tblModel;

    /**
     * Constructor.
     *
     * @param parent parent frame
     */
    public MetadataGridDialog(java.awt.Frame parent) {
        super(parent, false);
        initComponents();
        adjuster = new TableColumnAdjuster(tblMetadata);
    }

    /**
     * Set the metadata list to show.
     * <p>
     * Can be called several times to update the grid.
     *
     * @param list the new {@link EventList} to show
     */
    public void setDataList(EventList<Metadata> list) {
        if (list.isEmpty()) {
            return;
        }
        dataList = list;
        sortedList = new SortedList<>(dataList);
        //TODO filtering trough a search textfield
        // filterList=new FilterList<>(sortedList, null);
        selModel = GlazedListsSwing.eventSelectionModel(sortedList);
        selModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //TODO higlight selected item on map
        tblMetadata.setSelectionModel(selModel);
        final MetadataTableFormat mtf = MetadataTableFormatFactory.createTableFormat(dataList.get(0));
        tblModel = GlazedListsSwing.eventTableModelWithThreadProxyList(sortedList, mtf);
        tblMetadata.setModel(tblModel);
    }

    /**
     * Adds a selection listener to the grid.
     *
     * @param selListener the listener
     */
    public void addListSelectionListener(ListSelectionListener selListener) {
        selModel.addListSelectionListener(selListener);
    }

    /**
     * Removes a selection listener from the grid.
     *
     * @param selListener the listener
     */
    public void removeListSelectionListener(ListSelectionListener selListener) {
        selModel.removeListSelectionListener(selListener);
    }

    /**
     * Getter for the selected objects list.
     * <p>
     * Currently selection is limited to one item only.
     *
     * @return an {@link EventList} with selected items
     */
    public EventList<Metadata> getListOfSelected() {
        return selModel.getSelected();
    }

    /**
     * Triggers grid adapting to a new list and column width re-adjusting.
     */
    public void updateFinished() {
        if (!dataList.isEmpty()) {
            final MetadataTableFormat mtf = MetadataTableFormatFactory.createTableFormat(dataList.get(0));
            tblModel.setTableFormat(mtf);
            adjuster.adjustColumns();
        }
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        tblMetadata.addMouseListener(l);
    }

    @Override
    public synchronized void removeMouseListener(MouseListener l) {
        tblMetadata.removeMouseListener(l);
    }

    /**
     * Finds and select the table row of the product with the given collection and product identifier.
     *
     * @param coll the collection
     * @param prodId the product identifier
     */
    public void selectRow(String coll, String prodId) {
        Metadata m = new Metadata();
        m.put(MetadataNames.PRODUCT_IDENTIFIER, prodId);
        m.put(MetadataNames.PARENT_IDENTIFIER, coll);
        int idx = Collections.binarySearch(sortedList, m);
        if (idx >= 0) {
            tblMetadata.changeSelection(idx, 0, false, false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroller = new javax.swing.JScrollPane();
        tblMetadata = new javax.swing.JTable();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 4), new java.awt.Dimension(0, 4), new java.awt.Dimension(32767, 4));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 4), new java.awt.Dimension(0, 4), new java.awt.Dimension(32767, 4));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Metadata Attributes");
        setPreferredSize(new java.awt.Dimension(800, 200));

        tblMetadata.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scroller.setViewportView(tblMetadata);

        getContentPane().add(scroller, java.awt.BorderLayout.CENTER);
        getContentPane().add(filler1, java.awt.BorderLayout.LINE_END);
        getContentPane().add(filler2, java.awt.BorderLayout.LINE_START);
        getContentPane().add(filler3, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(filler4, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable tblMetadata;
    // End of variables declaration//GEN-END:variables
}
