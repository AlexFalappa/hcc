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
package main.hma;

import _0._3.rim.xsd.ebxml_regrep.tc.names.oasis.ExtrinsicObjectType;
import _0._3.rim.xsd.ebxml_regrep.tc.names.oasis.RegistryPackageType;
import javax.xml.namespace.QName;
import main.data.Metadata;
import main.data.MetadataNames;
import static main.data.MetadataNames.ARCH_CENTER;
import static main.data.MetadataNames.FOOTPRINT;
import static main.data.MetadataNames.SAT_NAME;
import static main.data.MetadataNames.SCENE_CENTER;
import static main.data.MetadataNames.URL_QLOOK;
import static main.data.MetadataNames.URL_THUMB;
import main.data.Slots;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps registry packages in HMA GetRecords responses to {@link Metadata objects}.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class HmaRegPackParser {

    private static final Logger logger = LoggerFactory.getLogger(HmaRegPackParser.class.getName());

    public Metadata parseXmlObj(XmlObject xobj) {
        if (xobj instanceof RegistryPackageType) {
            RegistryPackageType regPack = (RegistryPackageType) xobj;
            return parseRegistryPackage(regPack);
        }
        return null;
    }

    public Metadata parseRegistryPackage(RegistryPackageType regPack) {
        Metadata m = new Metadata();
        XmlCursor xc;
        // extract product id
        m.put(MetadataNames.PRODUCT_IDENTIFIER, regPack.getId());
        logger.trace("Parsing registry package for {}", regPack.getId());
        // process slots
        XmlObject[] slots = regPack.selectPath("declare namespace rim='urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0' .//rim:Slot");
        for (XmlObject slot : slots) {
            xc = slot.newCursor();
            final String slotName = xc.getAttributeText(new QName("name"));
            if (Slots.resp2meta.containsKey(slotName)) {
                final MetadataNames mn = Slots.resp2meta.get(slotName);
                switch (mn) {
                    case FOOTPRINT:
                        // extract footprint coordinates
                        XmlObject[] xposlist = slot.selectPath("declare namespace gml='http://www.opengis.net/gml' .//gml:posList");
                        if (xposlist.length > 0) {
                            XmlCursor xc2 = xposlist[0].newCursor();
                            m.put(FOOTPRINT, xc2.getTextValue());
                            xc2.dispose();
                        }
                        break;
                    case SCENE_CENTER:
                        // extract center coordinates
                        XmlObject[] xpos = slot.selectPath("declare namespace gml='http://www.opengis.net/gml' .//gml:pos");
                        if (xpos.length > 0) {
                            XmlCursor xc3 = xpos[0].newCursor();
                            m.put(SCENE_CENTER, xc3.getTextValue());
                            xc3.dispose();
                        }
                        break;
                    default:
                        // all other single valued slots (containing inner <rim:ValueList><rim:Value> tags)
                        xc.toFirstChild();
                        xc.toFirstChild();
                        m.put(mn, xc.getTextValue());
                }
            } else {
                logger.trace("Unknown hma slot {}", slotName);
            }
            xc.dispose();
        }
        // process <rim:Name> tags
        XmlObject[] extObjs = regPack.selectPath(
                "declare namespace rim='urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0' .//rim:ExtrinsicObject");
        for (XmlObject extObj : extObjs) {
            if (extObj instanceof ExtrinsicObjectType) {
                ExtrinsicObjectType xExtObj = (ExtrinsicObjectType) extObj;
                if (xExtObj.getObjectType().contains("EOArchivingInformation")) {
                    // archiving center
                    String center = xExtObj.getName().getLocalizedStringArray(0).getValue();
                    m.put(ARCH_CENTER, center);
                } else if (xExtObj.getObjectType().contains("EOAcquisitionPlatform")) {
                    // satellite name
                    String satName = xExtObj.getName().getLocalizedStringArray(0).getValue();
                    m.put(SAT_NAME, satName);
                } else if (xExtObj.getObjectType().contains("EOBrowseInformation")) {
                    // quicklook or thumbnail urls
                    XmlCursor xc2 = xExtObj.newCursor();
                    xc2.toChild(new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Slot"));
                    xc2.toFirstChild();
                    xc2.toFirstChild();
                    String url = xc2.getTextValue();
                    xc2.dispose();
                    String brwsType = xExtObj.getName().getLocalizedStringArray(0).getValue();
                    switch (brwsType) {
                        case "THUMBNAIL":
                            m.put(URL_THUMB, url);
                            break;
                        case "QUICKLOOK":
                            m.put(URL_QLOOK, url);
                            break;
                    }
                }
            }
        }
        // TODO process Classification ???
        return m;
    }
}
