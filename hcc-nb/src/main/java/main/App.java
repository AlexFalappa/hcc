/*
 * Copyright 2014 Alessandro Falappa <alex.falappa@gmail.com> <alex.falappa@gmail.com>.
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
package main;

import gui.MainWindow;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.falappa.utils.LogUtils;
import net.falappa.wwind.widgets.WWindPanel;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application entry point.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class.getName());
    public static MainWindow frame;
    private static final XmlOptions dumpXopts = new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(2).setSaveNamespacesFirst();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    // preference node names
    public static final String PREFN_APP = "alexfalappa.hcc-nb";
    // preference keys
    public static final String PREFK_LAFCLASS = "LAF-classname";
    public static final String PREFK_DUMP_REQS_FLAG = "dump-requests";
    public static final String PREFK_DUMP_REQS_DIR = "dump-requests-dir";
    public static final String PREFK_DUMP_RESPS_FLAG = "dump-responses";
    public static final String PREFK_DUMP_RESPS_DIR = "dump-responses-dir";
    private static final Preferences prefs = Preferences.userRoot().node(PREFN_APP);

    /**
     * Main method.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // setup logging and log startup messages
        LogUtils.silenceJUL();
        logger.info("HCC started");
        logger.info("Version {} build {}", Version.NUMBER, Build.NUMBER);
        logger.debug("Running on {} {}", System.getProperty("java.runtime.name"), System.getProperty("java.runtime.version"));
        logger.debug("Java VM {} {} v. {}", System.getProperty("java.vm.vendor"), System.getProperty("java.vm.name"), System.getProperty(
                "java.vm.version"));
        // setup properties for WWind
        logger.debug("Setting up properties for WorldWind");
        WWindPanel.setupPropsForWWind();
        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setPrefLAF();
                frame = new MainWindow();
                frame.setVisible(true);
            }
        });
    }

    private static void setPrefLAF() {
        try {
            logger.debug("Restoring previous look and feel: {}", prefs.get(PREFK_LAFCLASS, "None"));
            UIManager.setLookAndFeel(prefs.get(PREFK_LAFCLASS, UIManager.getSystemLookAndFeelClassName()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            //ignored will go with default look and feel
        }
    }

    public static synchronized void dumpReq(XmlObject req, boolean isResults) {
        if (prefs.getBoolean(PREFK_DUMP_REQS_FLAG, false)) {
            try {
                File dumpFile = new File(genDumpFilePath(true, isResults));
                logger.debug("Dumping request to: {}", dumpFile.getAbsolutePath());
                req.save(dumpFile, dumpXopts);
                logger.debug("Request dumped");
            } catch (IOException ex) {
                // TODO log exception to logger
                System.err.println(ex.getMessage());
            }
        }
    }

    public static synchronized void dumpResp(XmlObject req, boolean isResults) {
        if (prefs.getBoolean(PREFK_DUMP_RESPS_FLAG, false)) {
            try {
                File dumpFile = new File(genDumpFilePath(false, isResults));
                logger.debug("Dumping response to: {}", dumpFile.getAbsolutePath());
                req.save(dumpFile, dumpXopts);
                logger.debug("Response dumped");
            } catch (IOException ex) {
                // TODO log exception to logger
                System.err.println(ex.getMessage());
            }
        }
    }

    private static String genDumpFilePath(boolean isReq, boolean isResults) {
        StringBuilder sb = new StringBuilder();
        if (isReq) {
            sb.append(prefs.get(PREFK_DUMP_REQS_DIR, System.getProperty("user.home")));
        } else {
            sb.append(prefs.get(PREFK_DUMP_RESPS_DIR, System.getProperty("user.home")));
        }
        sb.append(System.getProperty("file.separator"));
        sb.append(sdf.format(new Date()));
        sb.append('-').append(frame.getCurrentCatalogue().getName());
        if (isResults) {
            sb.append("-results");
        } else {
            sb.append("-hits");
        }
        if (isReq) {
            sb.append("-req");
        } else {
            sb.append("-resp");
        }
        sb.append(".xml");
        return sb.toString();
    }
}
