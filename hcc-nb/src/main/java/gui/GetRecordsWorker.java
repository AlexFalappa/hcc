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
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import main.App;
import main.data.Metadata;
import main.hma.HmaRegPackParser;
import net.opengis.www.cat.csw._2_0_2.GetRecordsDocument;
import net.opengis.www.cat.csw._2_0_2.GetRecordsResponseDocument;
import net.opengis.www.cat.wrs._1_0.CatalogueStub;
import net.opengis.www.cat.wrs._1_0.ServiceExceptionReportFault;
import net.opengis.www.ows.ExceptionType;
import org.apache.xmlbeans.XmlObject;

/**
 * SwingWorker to make the GetRecords request.
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class GetRecordsWorker extends SwingWorker<Integer, String> {

    private final MainWindow mw;
    private final CatalogueStub stub;
    private final boolean isResults;
    private final BasicEventList<Metadata> results;

    public GetRecordsWorker(MainWindow mw, CatalogueStub stub, boolean isResults, BasicEventList<Metadata> resultList) {
        this.mw = mw;
        this.stub = stub;
        this.isResults = isResults;
        this.results = resultList;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        publish("Building request...");
        GetRecordsDocument req = mw.buildReq(isResults);
        App.dumpReq(req, isResults);
        publish("Sending request...");
        final GetRecordsResponseDocument resp = stub.getRecords(req);
        App.dumpResp(resp, isResults);
        int recs;
        if (isResults) {
            publish("Processing response...");
            recs = processResults(resp);
        } else {
            recs = processHits(resp);
        }
        publish("Done");
        return recs;
    }

    @Override
    protected void process(List<String> chunks) {
        mw.lMexs.setText(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        try {
            final Integer records = this.get();
            if (isResults) {
                mw.lMexs.setText(String.format("Retrieved %d records", records));
            } else {
                mw.showInfoDialog("Hits", String.format("Query will give %d records", records));
            }
        } catch (ExecutionException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof ServiceExceptionReportFault) {
                // decode error from remote service
                final ServiceExceptionReportFault serf = (ServiceExceptionReportFault) cause;
                final ExceptionType exc = serf.getFaultMessage().getExceptionReport().getExceptionArray(0);
                mw.showErrorDialog("Server error", String.format("Exception Report code %s:\n%s", exc.getExceptionCode(),
                        exc.getExceptionTextArray(0)));
            } else {
                mw.showErrorDialog("Unexpected error", "Could not perform request!", ex);
            }
            mw.lMexs.setText("No record retrieved");
        } catch (InterruptedException iex) {
            //ignored currently not supported
        } finally {
            mw.enableSearchButtons(true);
            mw.updGridWindow();
        }
    }

    int processHits(GetRecordsResponseDocument resp) {
        return resp.getGetRecordsResponse().getSearchResults().getNumberOfRecordsMatched().intValue();
    }

    int processResults(GetRecordsResponseDocument resp) {
        // extract registry packages via XPath
        XmlObject[] res = resp.selectPath("declare namespace rim='urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0' .//rim:RegistryPackage");
        // lock and clear the result list
        results.getReadWriteLock().writeLock().lock();
        results.clear();
        // process the registry packages trough a parser
        HmaRegPackParser regPackParser = new HmaRegPackParser();
        for (XmlObject xo : res) {
            Metadata m = regPackParser.parseXmlObj(xo);
            if (m != null) {
                results.add(m);
            }
        }
        // release the lock for update
        results.getReadWriteLock().writeLock().unlock();
        return results.size();
    }

}
