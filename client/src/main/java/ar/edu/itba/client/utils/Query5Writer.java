package ar.edu.itba.client.utils;

import ar.edu.itba.api.queryResults.Query5Result;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.Collection;

public class Query5Writer extends QueryWriter {
    private static final String[] HEADERS = {"Grupo", "Barrio A", "Barrio B"};

    private final CSVPrinter printer;

    public Query5Writer(String outPath) throws IOException {
        this.printer = this.getPrinter(outPath, 5);
    }

    public void write(Collection<Query5Result> results) throws IOException {
        for (Query5Result result : results) {
            this.printer.printRecord(result.getThousandOfTrees(), result.getNeighbourhood1(), result.getNeighbourhood2());
        }
        this.printer.flush();
    }

    protected final String[] getHeaders() {
        return HEADERS;
    }
}
