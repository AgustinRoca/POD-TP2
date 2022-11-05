package ar.edu.itba.api.queryResults;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Query1Result implements Comparable<Query1Result> {
    private final String neighbourhood;
    private final double treesPerPerson;

    public Query1Result(String neighbourhood, double treesPerPerson) {
        this.neighbourhood = neighbourhood;
        this.treesPerPerson = treesPerPerson;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public double getTreesPerPerson() {
        return treesPerPerson;
    }

    public static List<Query1Result> listFrom(Map<String, Double> map){
        List<Query1Result> list = new LinkedList<>();
        for (String neighbourhood: map.keySet()){
            list.add(new Query1Result(neighbourhood, map.get(neighbourhood)));
        }
        return list;
    }

    // Trees per person descending, then neighbourhood ascending
    @Override
    public int compareTo(Query1Result o) {
        long trees = (long) (treesPerPerson * 100);
        long otrees = (long) (o.treesPerPerson * 100);

        if(trees < otrees){
            return 1;
        } else if(trees == otrees) {
            return neighbourhood.compareTo(o.neighbourhood);
        } else {
            return -1;
        }
    }
}
