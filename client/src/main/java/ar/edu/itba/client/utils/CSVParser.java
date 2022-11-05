package ar.edu.itba.client.utils;

import ar.edu.itba.api.City;
import ar.edu.itba.api.Tree;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public abstract class CSVParser {
    private final Collection<Tree> trees = new LinkedList<>();
    private final Map<String, Long> population = new HashMap<>();

    public Collection<Tree> getTrees() {
        return this.trees;
    }

    public Map<String, Long> getPopulation() {
        return this.population;
    }

    protected void parseTrees(String filepath, City city, Map<TreeHeaders, String> headersMap) throws IOException {
        this.parse(filepath, record -> {
            this.trees.add(new Tree(
                city,
                record.get(headersMap.get(TreeHeaders.SCIENTIFIC_NAME)),
                record.get(headersMap.get(TreeHeaders.CITY)),
                record.get(headersMap.get(TreeHeaders.STREET)),
                Double.parseDouble(record.get(headersMap.get(TreeHeaders.DIAMETER)))
            ));
        });
    }

    protected void parseCities(String filepath, Map<CityHeaders, String> headersMap) throws IOException {
        this.parse(filepath, record -> {
            this.population.put(
                record.get(headersMap.get(CityHeaders.NAME)),
                Long.parseLong(record.get(headersMap.get(CityHeaders.POPULATION)))
            );
        });
    }

    private void parse(String filepath, Consumer<Map<String, String>> recordConsumer) throws IOException {
        try (CSVReaderHeaderAware reader = CSVUtils.getReader(CSVUtils.getFileReader(filepath))) {
            Map<String, String> record;
            while ((record = reader.readMap()) != null) {
                recordConsumer.accept(record);
            }
        } catch (IOException | CsvValidationException e) {
            throw new IOException("Error while trying to read CSV file from: " + filepath + ". File not found, not enough permissions or format error");
        }
    }

    public abstract void parseTrees(String filepath) throws IOException;

    public abstract void parseCities(String filepath) throws IOException;

    protected enum TreeHeaders {
        CITY, STREET, SCIENTIFIC_NAME, DIAMETER
    }

    protected enum CityHeaders {
        NAME, POPULATION
    }
}
