package ar.edu.itba.api.queryResults;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Query3Result implements Comparable<Query3Result>{
    private final String species;
    private final double diameterAverage;

    public Query3Result(String species, double diameterAverage) {
        this.species = species;
        this.diameterAverage = diameterAverage;
    }

    public String getSpecies() {
        return species;
    }

    public double getDiameterAverage() {
        return diameterAverage;
    }

    public static List<Query3Result> listFrom(Map<String, Double> map){
        List<Query3Result> list = new LinkedList<>();
        for (String species: map.keySet()){
            list.add(new Query3Result(species, map.get(species)));
        }
        return list;
    }

    @Override
    public int compareTo(Query3Result o) {
        long d = (long) (diameterAverage * 100);
        long od = (long) (o.diameterAverage * 100);

        if(d < od){
            return 1;
        } else if(d == od) {
            return species.compareTo(o.species);
        } else {
            return -1;
        }
    }
}
