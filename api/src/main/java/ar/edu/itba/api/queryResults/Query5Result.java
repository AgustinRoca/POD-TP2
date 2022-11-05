package ar.edu.itba.api.queryResults;

import java.util.*;

public class Query5Result implements Comparable<Query5Result>{
    private final String neighbourhood1;
    private final String neighbourhood2;
    private final long thousandOfTrees;

    public Query5Result(String neighbourhood1, String neighbourhood2, long thousandOfTrees) {
        this.thousandOfTrees = thousandOfTrees;
        if (neighbourhood1.compareTo(neighbourhood2) < 0) {
            this.neighbourhood1 = neighbourhood1;
            this.neighbourhood2 = neighbourhood2;
        } else {
            this.neighbourhood1 = neighbourhood2;
            this.neighbourhood2 = neighbourhood1;
        }
    }

    public String getNeighbourhood1() {
        return neighbourhood1;
    }

    public String getNeighbourhood2() {
        return neighbourhood2;
    }

    public long getThousandOfTrees() {
        return thousandOfTrees;
    }

    public static List<Query5Result> listFrom(Map<String, Long> map){
        Map<Long, List<String>> mapByThousands = new TreeMap<>(Comparator.reverseOrder()); // Sorted by thousands
        for (String neighbourhood : map.keySet()){
            if (!mapByThousands.containsKey(map.get(neighbourhood))) {
                mapByThousands.put(map.get(neighbourhood), new LinkedList<>());
            }
            mapByThousands.get(map.get(neighbourhood)).add(neighbourhood);
        }

        List<Query5Result> list = new LinkedList<>();
        for(Long thousands : mapByThousands.keySet()){
            for (String n1 : mapByThousands.get(thousands)){
                for (String n2 : mapByThousands.get(thousands)){
                    Query5Result result = new Query5Result(n1, n2, thousands);
                    if(!list.contains(result) && !n1.equals(n2)){
                        list.add(result);
                    }
                }
            }
        }

        return list;
    }


    @Override
    public int compareTo(Query5Result o) {
        int r1 = Long.compare(o.thousandOfTrees, thousandOfTrees);
        if(r1 == 0) {
            int r2 = neighbourhood1.compareTo(o.neighbourhood1);
            if (r2 == 0) {
                return neighbourhood2.compareTo(o.neighbourhood2);
            } else {
                return r2;
            }
        } else{
            return r1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query5Result result = (Query5Result) o;
        return thousandOfTrees == result.thousandOfTrees &&
                Objects.equals(neighbourhood1, result.neighbourhood1) &&
                Objects.equals(neighbourhood2, result.neighbourhood2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighbourhood1, neighbourhood2, thousandOfTrees);
    }
}
