package ar.edu.itba.client.utils;

import ar.edu.itba.api.queryResults.Query3Result;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

public class Query3Writer extends QueryWriter {
    private static final String[] HEADERS = {"NOMBRE_CIENTIFICO", "PROMEDIO_DIAMETRO"};

    private final CSVPrinter printer;

    public Query3Writer(String outPath) throws IOException {
        this.printer = this.getPrinter(outPath, 3);
    }

    public void write(Collection<Query3Result> results) throws IOException {
        for (Query3Result result : results) {
            this.printer.printRecord(result.getSpecies(), String.format(Locale.US, "%.2f", result.getDiameterAverage()));
        }
        this.printer.flush();
    }

    protected final String[] getHeaders() {
        return HEADERS;
    }
}
