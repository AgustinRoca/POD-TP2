package ar.edu.itba.client.utils;

import ar.edu.itba.api.City;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VancouverCSVParser extends CSVParser {
    private static final Map<TreeHeaders, String> treeHeaders = new HashMap<>();
    private static final Map<CityHeaders, String> cityHeaders = new HashMap<>();
    static {
        {
            treeHeaders.put(TreeHeaders.CITY, "NEIGHBOURHOOD_NAME");
            treeHeaders.put(TreeHeaders.STREET, "STD_STREET");
            treeHeaders.put(TreeHeaders.SCIENTIFIC_NAME, "COMMON_NAME");
            treeHeaders.put(TreeHeaders.DIAMETER, "DIAMETER");

            cityHeaders.put(CityHeaders.NAME, "nombre");
            cityHeaders.put(CityHeaders.POPULATION, "habitantes");
        }
    }

    @Override
    public void parseTrees(String filepath) throws IOException {
        super.parseTrees(filepath, City.VANCOUVER, treeHeaders);
    }

    @Override
    public void parseCities(String filepath) throws IOException {
        super.parseCities(filepath, cityHeaders);
    }
}
