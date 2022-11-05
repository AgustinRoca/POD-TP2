package ar.edu.itba.client.utils;

import ar.edu.itba.api.queryResults.Query4Result;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.Collection;

public class Query4Writer extends QueryWriter {
    private static final String[] HEADERS = {"Barrio A", "Barrio B"};

    private final CSVPrinter printer;

    public Query4Writer(String outPath) throws IOException {
        this.printer = this.getPrinter(outPath, 4);
    }

    public void write(Collection<Query4Result> results) throws IOException {
        for (Query4Result result : results) {
            this.printer.printRecord(result.getNeighbourhood1(), result.getNeighbourhood2());
        }
        this.printer.flush();
    }

    protected final String[] getHeaders() {
        return HEADERS;
    }
}
