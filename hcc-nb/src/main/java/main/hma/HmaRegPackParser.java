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

import _0._3.rim.xsd.ebxml_regrep.tc.names.oasis.RegistryPackageType;
import javax.xml.namespace.QName;
import main.data.Metadata;
import main.data.MetadataNames;
import static main.data.MetadataNames.FOOTPRINT;
import main.data.Slots;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

/**
 * Maps registry packages in HMA GetRecords responses to {@link Metadata objects}.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public final class HmaRegPackParser {

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
                        XmlObject[] xpos = slot.selectPath("declare namespace gml='http://www.opengis.net/gml' .//gml:posList");
                        if (xpos.length > 0) {
                            XmlCursor xc2 = xpos[0].newCursor();
                            m.put(FOOTPRINT, xc2.getTextValue());
                            xc2.dispose();
                        }
                        break;
                    case SCENE_CENTER:
                        // TODO process scene center differently
                        break;
                    default:
                        // single valued slots (containing inner <rim:ValueList><rim:Value> tags)
                        xc.toFirstChild();
                        xc.toFirstChild();
                        m.put(mn, xc.getTextValue());
                }
            } else {
                System.err.printf("Unknown hma slot &s&n", slotName);
                // TODO introduce logging at low level or keep a flag and spit only one message at the end
            }
            xc.dispose();
        }
        // TODO process BrowseInformation extrinsic object for thumbnail
        // TODO process BrowseInformation extrinsic object for quicklook
        // TODO process ArchivingInformation extrinsic object for archiving center
        // TODO process AcquisitionPlatform extrinsic object for platform name
        // TODO process Classification ???
        System.out.println(m);
        return m;
    }
}
