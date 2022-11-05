package ar.edu.itba.client.utils;

import ar.edu.itba.api.queryResults.Query2Result;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.Collection;

public class Query2Writer extends QueryWriter {
    private static final String[] HEADERS = {"BARRIO", "CALLE_CON_MAS_ARBOLES", "ARBOLES"};

    private final CSVPrinter printer;

    public Query2Writer(String outPath) throws IOException {
        this.printer = this.getPrinter(outPath, 2);
    }

    public void write(Collection<Query2Result> results) throws IOException {
        for (Query2Result result : results) {
            this.printer.printRecord(result.getNeighbourhood(), result.getStreet(), result.getTreesQty());
        }
        this.printer.flush();
    }

    protected final String[] getHeaders() {
        return HEADERS;
    }
}
