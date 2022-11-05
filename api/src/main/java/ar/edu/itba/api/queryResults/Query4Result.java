package ar.edu.itba.api.queryResults;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Query4Result implements Comparable<Query4Result>{
    private final String neighbourhood1;
    private final String neighbourhood2;

    public Query4Result(String neighbourhood1, String neighbourhood2) {
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

    public static List<Query4Result> listFrom(Set<String> set){
        List<Query4Result> list = new LinkedList<>();
        for(String s1: set){
            for (String s2: set){
                Query4Result result = new Query4Result(s1, s2);
                if (!list.contains(result) && !s1.equals(s2)){
                    list.add(result);
                }
            }
        }
        return list;
    }


    @Override
    public int compareTo(Query4Result o) {
        int r = neighbourhood1.compareTo(o.neighbourhood1);
        if (r == 0){
            return neighbourhood2.compareTo(o.neighbourhood2);
        } else {
            return r;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query4Result that = (Query4Result) o;
        return Objects.equals(neighbourhood1, that.neighbourhood1) &&
                Objects.equals(neighbourhood2, that.neighbourhood2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighbourhood1, neighbourhood2);
    }
}
