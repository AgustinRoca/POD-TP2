package ar.edu.itba.client.utils;

import ar.edu.itba.api.City;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CABACSVParser extends CSVParser {
    private static final Map<TreeHeaders, String> treeHeaders = new HashMap<>();
    private static final Map<CityHeaders, String> cityHeaders = new HashMap<>();
    static {
        {
            treeHeaders.put(TreeHeaders.CITY, "comuna");
            treeHeaders.put(TreeHeaders.STREET, "calle_nombre");
            treeHeaders.put(TreeHeaders.SCIENTIFIC_NAME, "nombre_cientifico");
            treeHeaders.put(TreeHeaders.DIAMETER, "diametro_altura_pecho");

            cityHeaders.put(CityHeaders.NAME, "nombre");
            cityHeaders.put(CityHeaders.POPULATION, "habitantes");
        }
    }

    @Override
    public void parseTrees(String filepath) throws IOException {
        super.parseTrees(filepath, City.CABA, treeHeaders);
    }

    @Override
    public void parseCities(String filepath) throws IOException {
        super.parseCities(filepath, cityHeaders);
    }
}
