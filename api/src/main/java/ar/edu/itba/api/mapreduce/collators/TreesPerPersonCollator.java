package ar.edu.itba.api.mapreduce.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class TreesPerPersonCollator implements Collator<Map.Entry<String, Long>, Map<String, Double>> {
    private final Map<String, Long> populations;

    public TreesPerPersonCollator(Map<String, Long> populations) {
        this.populations = populations;
    }

    @Override
    public Map<String, Double> collate(Iterable<Map.Entry<String, Long>> iterable) {
        Map<String, Double> treesPerPerson = new HashMap<>();
        for(Map.Entry<String, Long> entry: iterable){
            treesPerPerson.put(entry.getKey(), (double) entry.getValue() / populations.get(entry.getKey()));
        }
        return treesPerPerson;
    }
}
