package ar.edu.itba.api.mapreduce.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class ThousandTreesCollator implements Collator<Map.Entry<String, Long>, Map<String, Long>> {

    @Override
    public Map<String, Long> collate(Iterable<Map.Entry<String, Long>> iterable) {
        Map<String, Long> map = new HashMap<>();
        for (Map.Entry<String, Long> entry : iterable){
            map.put(entry.getKey(), entry.getValue() - entry.getValue() % 1000);
        }
        return map;
    }
}
