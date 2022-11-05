package ar.edu.itba.api.queryResults;

import com.hazelcast.map.impl.MapEntrySimple;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Query2Result implements Comparable<Query2Result>{
    private final String neighbourhood;
    private final String street;
    private final long treesQty;

    public Query2Result(String neighbourhood, String street, long treesQty){
        this.neighbourhood = neighbourhood;
        this.street = street;
        this.treesQty = treesQty;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getStreet() {
        return street;
    }

    public long getTreesQty() {
        return treesQty;
    }

    public static List<Query2Result> listFrom(Map<String, MapEntrySimple<String, Long>> map){
        List<Query2Result> list = new LinkedList<>();
        for (String key: map.keySet()){
            list.add(new Query2Result(key, map.get(key).getKey(), map.get(key).getValue()));
        }
        return list;
    }


    @Override
    public int compareTo(Query2Result o) {
        return neighbourhood.compareTo(o.neighbourhood);
    }
}
