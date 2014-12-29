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
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import gui.glazed.MetadataTableFormat;
import gui.glazed.MetadataTableFormatFactory;
import main.data.Metadata;
import main.data.MetadataNames;

/**
 * Window displaying a tabular view of queryed metadata.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class MetadataWindow extends javax.swing.JFrame {

    private BasicEventList<Metadata> dataList;
    private FilterList<Metadata> filterList;
    private SortedList<Metadata> sortedList;
    private AdvancedListSelectionModel<Metadata> selModel;

    public MetadataWindow() {
        initComponents();
    }

    public void setDataList(BasicEventList<Metadata> list) {
        dataList = list;
        sortedList = new SortedList<>(dataList);
        final MetadataTableFormat mtf = MetadataTableFormatFactory.createTableFormat(list.get(0));
        tblMetadata.setModel(GlazedListsSwing.eventTableModelWithThreadProxyList(sortedList, mtf));
        selModel = GlazedListsSwing.eventSelectionModel(sortedList);
        //TODO filtering trough a search textfield
        // filterList=new FilterList<>(sortedList, null);
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MetadataWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MetadataWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MetadataWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MetadataWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BasicEventList<Metadata> list = new BasicEventList<>();
                Metadata m = new Metadata();
                m.put(MetadataNames.PARENT_IDENTIFIER, "pippo");
                m.put(MetadataNames.PRODUCT_IDENTIFIER, "pi1");
                m.put(MetadataNames.ACQ_STATION, "roma");
                list.add(m);
                m = new Metadata();
                m.put(MetadataNames.PARENT_IDENTIFIER, "pluto");
                m.put(MetadataNames.PRODUCT_IDENTIFIER, "pl2");
                m.put(MetadataNames.ACQ_STATION, "latina");
                list.add(m);
                m = new Metadata();
                m.put(MetadataNames.PARENT_IDENTIFIER, "pluto");
                m.put(MetadataNames.PRODUCT_IDENTIFIER, "pl1");
                m.put(MetadataNames.ACQ_STATION, "rieti");
                list.add(m);
                m = new Metadata();
                m.put(MetadataNames.PARENT_IDENTIFIER, "pippo");
                m.put(MetadataNames.PRODUCT_IDENTIFIER, "pi2");
                m.put(MetadataNames.ACQ_STATION, "viterbo");
                list.add(m);
                final MetadataWindow metadataWindow = new MetadataWindow();
                metadataWindow.setDataList(list);
                metadataWindow.setVisible(true);
            }
        });
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

        setTitle("Metadata");

        scroller.setViewportView(tblMetadata);

        getContentPane().add(scroller, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable tblMetadata;
    // End of variables declaration//GEN-END:variables
}
