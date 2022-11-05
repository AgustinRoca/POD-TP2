package ar.edu.itba.client.utils;

import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;

import static ar.edu.itba.client.utils.CSVUtils.CSV_EXTENSION;

public abstract class QueryWriter {
    private static final String QUERY_OUT_FILENAME = "query";

    protected CSVPrinter getPrinter(String path, int query) throws IOException  {
        Writer fileWriter = CSVUtils.getFileWriter(FileUtils.formatFilePath(
                path,
                QUERY_OUT_FILENAME + query,
                CSV_EXTENSION
        ));
        return CSVUtils.getWriter(fileWriter, this.getHeaders());
    }

    protected abstract String[] getHeaders();
}
