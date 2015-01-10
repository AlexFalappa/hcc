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

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import main.data.MetadataNames;
import main.data.Slots;
import net.opengis.www.cat.csw._2_0_2.GetRecordsDocument;
import net.opengis.www.cat.csw._2_0_2.QueryType;
import net.opengis.www.cat.csw._2_0_2.ResultType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder for HMA GetRecords XML requests.
 * <p>
 * Uses an internal XML template representing a GetRecords RESULTS request with full detail, max 100 records starting from 1.
 * <p>
 * Follows the <i>Builder</i> Design pattern.
 * <p>
 * Typical usage is as follows:
 * <pre>
 * HmaGetRecordsBuilder builder=new HmaGetRecordsBuilder();
 * builder.setResults();
 * builder.setMaxRecords(100);
 * builder.setDetailFull();
 * builder.addCollection("Collection1");
 * builder.addTemporalOverlaps(date1, date2);
 * GetRecordsDocument req=builder.getRequest();
 * </pre>
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class HmaGetRecordsBuilder {

    private static final String NS_OGC = "http://www.opengis.net/ogc";
    private static final String NS_GML = "http://www.opengis.net/gml";
    private static final Logger logger = LoggerFactory.getLogger(HmaGetRecordsBuilder.class.getName());

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private GetRecordsDocument reqDoc;
    private QueryType query;

    public HmaGetRecordsBuilder() {
        internalInit();
    }

    public void setHits() {
        reqDoc.getGetRecords().setResultType(ResultType.HITS);
        logger.trace("Set request type to HITS");
    }

    public void setResults() {
        reqDoc.getGetRecords().setResultType(ResultType.RESULTS);
        logger.trace("Set request type to RESULTS");
    }

    public void setStartPosition(int pos) {
        reqDoc.getGetRecords().setStartPosition(BigInteger.valueOf(pos));
        logger.trace("Set initial record to {}", pos);
    }

    public void setMaxRecords(int max) {
        reqDoc.getGetRecords().setMaxRecords(BigInteger.valueOf(max));
        logger.trace("Set max records to {}", max);
    }

    public void setDetailFull() {
        query.getElementSetName().setStringValue("full");
        logger.trace("Set requested detail to FULL");
    }

    public void setDetailSummary() {
        query.getElementSetName().setStringValue("summary");
        logger.trace("Set requested detail to SUMMARY");
    }

    public void setDetailBrief() {
        query.getElementSetName().setStringValue("brief");
        logger.trace("Set requested detail to BRIEF");
    }

    public void addCollection(String collection) {
        final XmlCursor xc = getGlobalAndCur();
        addParentIdXMLBlock(xc, collection);
        xc.dispose();
    }

    public void addCollections(String[] collections) {
        XmlCursor xc = getGlobalAndCur();
        xc.beginElement("Or", NS_OGC);
        xc.toEndToken();
        for (String c : collections) {
            addParentIdXMLBlock(xc, c);
        }
        xc.dispose();
    }

    public void addTemporalContained(Date t1, Date t2) {
        XmlCursor xc = getGlobalAndCur();
        xc.beginElement("PropertyIsGreaterThanOrEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.START_SENSING));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(df.format(t1));
        xc.toNextToken();
        xc.toNextToken();
        xc.beginElement("PropertyIsLessThanOrEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.STOP_SENSING));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(df.format(t2));
        xc.toNextToken();
        xc.toNextToken();
        xc.dispose();
        logger.trace("Added temporal containement clause between {} and {}", t1, t2);
    }

    public void addTemporalOverlaps(Date t1, Date t2) {
        XmlCursor xc = getGlobalAndCur();
        xc.beginElement("PropertyIsGreaterThanOrEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.STOP_SENSING));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(df.format(t1));
        xc.toNextToken();
        xc.toNextToken();
        xc.beginElement("PropertyIsLessThanOrEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.START_SENSING));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(df.format(t2));
        xc.toNextToken();
        xc.toNextToken();
        xc.dispose();
        logger.trace("Added temporal overlap clause between {} and {}", t1, t2);
    }

    public void addTemporalAfter(Date t1) {
        XmlCursor xc = getGlobalAndCur();
        xc.beginElement("PropertyIsGreaterThanOrEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.START_SENSING));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(df.format(t1));
        xc.toNextToken();
        xc.toNextToken();
        xc.dispose();
        logger.trace("Added temporal clause successive to {}", t1);
    }

    public void addTemporalBefore(Date t1) {
        XmlCursor xc = getGlobalAndCur();
        xc.beginElement("PropertyIsLessThanOrEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.STOP_SENSING));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(df.format(t1));
        xc.toNextToken();
        xc.toNextToken();
        xc.dispose();
        logger.trace("Added temporal clause preceding {}", t1);
    }

    private void insertSpatialOperator(int operator, XmlCursor xc) {
        //    0="Overlaps", 1="Contains", 2="Intersect", 3="Is contained"
        String op = "-";
        switch (operator) {
            case 0:
                op = "Overlaps";
                break;
            case 1:
                op = "Contains";
                break;
            case 2:
                op = "Intersects";
                break;
            case 3:
                op = "Within";
                break;
            default:
                throw new AssertionError("unknown spatial operator");
        }
        xc.beginElement(op, NS_OGC);
        xc.toEndToken();
        logger.trace("Added spatial operator {}", op);
    }

    public void addSpatialPoint(int operator, double lat, double lon) {
        XmlCursor xc = getGlobalAndCur();
        insertSpatialOperator(operator, xc);
        insertPointBlock(xc, lat, lon);
        xc.dispose();
        logger.trace("Added spatial constraint with Point geometry [{};{}]", lat, lon);
    }

    public void addSpatialPolygon(int operator, String coords) {
        XmlCursor xc = getGlobalAndCur();
        insertSpatialOperator(operator, xc);
        insertPolygonBlock(xc, coords);
        xc.dispose();
        logger.trace("Added spatial constraint with Polygon geometry [{}]", coords);
    }

    public void addSpatialPolyline(int operator, String coords) {
        XmlCursor xc = getGlobalAndCur();
        insertSpatialOperator(operator, xc);
        insertLinestringBlock(xc, coords);
        xc.dispose();
        logger.trace("Added spatial constraint with Polyline geometry [{}]", coords);
    }

    public void addSpatialRange(int operator, double minlat, double maxlat, double minlon, double maxlon) {
        XmlCursor xc = getGlobalAndCur();
        insertSpatialOperator(operator, xc);
        insertEnvelopeBlock(xc, minlat, minlon, maxlat, maxlon);
        xc.dispose();
        logger.trace("Added spatial constraint with lat-lon range [{};{}]-[{};{}]", minlat, maxlat, minlon, maxlon);
    }

    public void addSpatialCircle(int operator, double lat, double lon, double radius) {
        XmlCursor xc = getGlobalAndCur();
        insertSpatialOperator(operator, xc);
        insertCircleBlock(xc, lat, lon, radius);
        xc.dispose();
        logger.trace("Added spatial constraint with circle center [{};{}] radius {}", lat, lon, radius);
    }

    public GetRecordsDocument getRequest() {
        logger.trace("Returning built request");
        return reqDoc;
    }

    private XmlCursor getGlobalAndCur() {
        XmlCursor xc = query.getConstraint().getFilter().newCursor();
        xc.toFirstChild();
        xc.toEndToken();
        return xc;
    }

    private void internalInit() {
        try {
            reqDoc = GetRecordsDocument.Factory.parse(getClass().getResourceAsStream("templates/getrecords-template.xml"));
            query = (QueryType) reqDoc.getGetRecords().getAbstractQuery();
            logger.trace("HmaGetRecordsBuilder initialized");
        } catch (XmlException | IOException ex) {
            // ignored should always works
        }
    }

    private void addParentIdXMLBlock(XmlCursor xc, String parentId) {
        xc.beginElement("PropertyIsEqualTo", NS_OGC);
        xc.toEndToken();
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.PARENT_IDENTIFIER));
        xc.toNextToken();
        xc.beginElement("Literal", NS_OGC);
        xc.insertChars(parentId);
        xc.toNextToken();
        xc.toNextToken();
        logger.trace("Added parent identifier clause for {}", parentId);
    }

    private void insertPolygonBlock(XmlCursor xc, String coords) {
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.FOOTPRINT));
        xc.toNextToken();
        xc.beginElement("Polygon", NS_GML);
        xc.insertAttributeWithValue("srsName", "urn:ogc:def:crs:EPSG:6.3:4326");
        xc.toEndToken();
        xc.beginElement("exterior", NS_GML);
        xc.toEndToken();
        xc.beginElement("LinearRing", NS_GML);
        xc.toEndToken();
        xc.beginElement("posList", NS_GML);
        xc.insertChars(coords);
        xc.toNextToken();
        xc.toNextToken();
        xc.toNextToken();
        xc.toNextToken();
    }

    private void insertLinestringBlock(XmlCursor xc, String coords) {
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.FOOTPRINT));
        xc.toNextToken();
        xc.beginElement("LineString", NS_GML);
        xc.insertAttributeWithValue("srsName", "urn:ogc:def:crs:EPSG:6.3:4326");
        xc.toEndToken();
        xc.beginElement("posList", NS_GML);
        xc.insertChars(coords);
        xc.toNextToken();
        xc.toNextToken();
    }

    private void insertEnvelopeBlock(XmlCursor xc, double minlat, double minlon, double maxlat, double maxlon) {
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.FOOTPRINT));
        xc.toNextToken();
        xc.beginElement("Envelope", NS_GML);
        xc.insertAttributeWithValue("srsName", "urn:ogc:def:crs:EPSG:6.3:4326");
        xc.toEndToken();
        xc.beginElement("lowerCorner", NS_GML);
        xc.insertChars(String.format("%f %f", minlat, minlon));
        xc.toNextToken();
        xc.beginElement("upperCorner", NS_GML);
        xc.insertChars(String.format(Locale.ENGLISH, "%f %f", maxlat, maxlon));
        xc.toNextToken();
    }

    private void insertPointBlock(XmlCursor xc, double lat, double lon) {
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.FOOTPRINT));
        xc.toNextToken();
        xc.beginElement("Point", NS_GML);
        xc.insertAttributeWithValue("srsName", "urn:ogc:def:crs:EPSG:6.3:4326");
        xc.toEndToken();
        xc.beginElement("pos", NS_GML);
        xc.insertChars(String.format(Locale.ENGLISH, "%f %f", lat, lon));
        xc.toNextToken();
    }

    private void insertCircleBlock(XmlCursor xc, double centerLat, double centerLon, double radius) {
        xc.beginElement("PropertyName", NS_OGC);
        xc.insertChars(Slots.meta2req.get(MetadataNames.FOOTPRINT));
        xc.toNextToken();
        xc.beginElement("CircleByCenterPoint", NS_GML);
        xc.insertAttributeWithValue("numArc", "1");
        xc.insertAttributeWithValue("srsName", "urn:ogc:def:crs:EPSG:6.3:4326");
        xc.toEndToken();
        xc.beginElement("pos", NS_GML);
        xc.insertChars(String.format(Locale.ENGLISH, "%f %f", centerLat, centerLon));
        xc.toNextToken();
        xc.beginElement("radius", NS_GML);
        xc.insertAttributeWithValue("uom", "m");
        xc.insertChars(String.valueOf(radius));
        xc.toNextToken();
    }
}
