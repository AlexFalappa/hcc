/*
 * Copyright 2014 Alessandro Falappa.
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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import main.App;

/**
 * HCC settings dialog.
 *
 * @author Alessandro Falappa
 */
public class SettingsDialog extends javax.swing.JDialog {

    private final BiMap<String, String> lafClassToNames = HashBiMap.create(6);
    private final DefaultComboBoxModel dcbm = new DefaultComboBoxModel();

    public SettingsDialog(java.awt.Frame parent) {
        super(parent, true);
        // add to the combobox and the map all platform installed L&Fs
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            lafClassToNames.put(info.getClassName(), info.getName());
            dcbm.addElement(info.getName());
        }
        // add to the combobox and the map third party L&Fs
        lafClassToNames.put("com.jgoodies.looks.plastic.PlasticLookAndFeel", "JGoodies Plastic");
        dcbm.addElement("JGoodies Plastic");
        lafClassToNames.put("com.jgoodies.looks.plastic.PlasticXPLookAndFeel", "JGoodies Plastic XP");
        dcbm.addElement("JGoodies Plastic XP");
        // build widgets
        initComponents();
        // select the current look and feel
        cbLF.setSelectedItem(lafClassToNames.get(UIManager.getLookAndFeel().getClass().getName()));
        // load dump flags and dirs from prefs
        Preferences prefs = Preferences.userRoot().node(App.PREFN_APP);
        chDumpReqs.setSelected(prefs.getBoolean(App.PREFK_DUMP_REQS_FLAG, false));
        dsfReqsDir.setDir(new File(prefs.get(App.PREFK_DUMP_REQS_DIR, System.getProperty("user.home"))));
        chDumpRsps.setSelected(prefs.getBoolean(App.PREFK_DUMP_RESPS_FLAG, false));
        dsfRspsDir.setDir(new File(prefs.get(App.PREFK_DUMP_RESPS_DIR, System.getProperty("user.home"))));
    }

    /** This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPane = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbLF = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        bCancel = new javax.swing.JButton();
        bOk = new javax.swing.JButton();
        chDumpReqs = new javax.swing.JCheckBox();
        chDumpRsps = new javax.swing.JCheckBox();
        lDreqs = new javax.swing.JLabel();
        lDrsps = new javax.swing.JLabel();
        dsfRspsDir = new net.falappa.swing.text.DirSelectorField();
        dsfReqsDir = new net.falappa.swing.text.DirSelectorField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HCC Settings");

        jLabel2.setText("Look & Feel");

        cbLF.setModel(dcbm);

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()-1f));
        jLabel3.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.acceleratorSelectionBackground"));
        jLabel3.setText("Requires restart of the application");

        bCancel.setText("Cancel");
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });

        bOk.setText("OK");
        bOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });

        chDumpReqs.setText("Dump requests");
        chDumpReqs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chDumpReqsItemStateChanged(evt);
            }
        });

        chDumpRsps.setText("Dump responses");
        chDumpRsps.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chDumpRspsItemStateChanged(evt);
            }
        });

        lDreqs.setText("to");
        lDreqs.setEnabled(false);

        lDrsps.setText("to");
        lDrsps.setEnabled(false);

        dsfRspsDir.setEnabled(false);

        dsfReqsDir.setEnabled(false);

        javax.swing.GroupLayout contentPaneLayout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancel))
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbLF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(chDumpReqs)
                    .addComponent(chDumpRsps)
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(lDrsps)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dsfRspsDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(lDreqs)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dsfReqsDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        contentPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bCancel, bOk});

        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbLF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chDumpReqs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lDreqs)
                    .addComponent(dsfReqsDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chDumpRsps)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lDrsps)
                    .addComponent(dsfRspsDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(contentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bCancel)
                    .addComponent(bOk))
                .addContainerGap())
        );

        getContentPane().add(contentPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
        // get preferences API node
        Preferences prefs = Preferences.userRoot().node(App.PREFN_APP);
        // store selected LAF class name
        prefs.put(App.PREFK_LAFCLASS, lafClassToNames.inverse().get(cbLF.getSelectedItem().toString()));
        // store dump flags and dirs
        prefs.putBoolean(App.PREFK_DUMP_REQS_FLAG, chDumpReqs.isSelected());
        prefs.put(App.PREFK_DUMP_REQS_DIR, dsfReqsDir.getDir().getAbsolutePath());
        prefs.putBoolean(App.PREFK_DUMP_RESPS_FLAG, chDumpRsps.isSelected());
        prefs.put(App.PREFK_DUMP_RESPS_DIR, dsfRspsDir.getDir().getAbsolutePath());
        setVisible(false);
    }//GEN-LAST:event_bOkActionPerformed

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_bCancelActionPerformed

    private void chDumpReqsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chDumpReqsItemStateChanged
        boolean flag = chDumpReqs.isSelected();
        lDreqs.setEnabled(flag);
        dsfReqsDir.setEnabled(flag);
    }//GEN-LAST:event_chDumpReqsItemStateChanged

    private void chDumpRspsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chDumpRspsItemStateChanged
        boolean flag = chDumpRsps.isSelected();
        lDrsps.setEnabled(flag);
        dsfRspsDir.setEnabled(flag);
    }//GEN-LAST:event_chDumpRspsItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bOk;
    private javax.swing.JComboBox cbLF;
    private javax.swing.JCheckBox chDumpReqs;
    private javax.swing.JCheckBox chDumpRsps;
    private javax.swing.JPanel contentPane;
    private net.falappa.swing.text.DirSelectorField dsfReqsDir;
    private net.falappa.swing.text.DirSelectorField dsfRspsDir;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lDreqs;
    private javax.swing.JLabel lDrsps;
    // End of variables declaration//GEN-END:variables
}
