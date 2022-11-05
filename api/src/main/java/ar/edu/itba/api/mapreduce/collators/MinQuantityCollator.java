package ar.edu.itba.api.mapreduce.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class MinQuantityCollator implements Collator<Map.Entry<String, Long>, Map<String, Long>> {
    private final long min;

    public MinQuantityCollator(long min) {
        this.min = min;
    }

    @Override
    public Map<String, Long> collate(Iterable<Map.Entry<String, Long>> iterable) {
        Map<String, Long> result = new HashMap<>();
        for (Map.Entry<String, Long> entry : iterable){
            if(entry.getValue() >= min){
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
