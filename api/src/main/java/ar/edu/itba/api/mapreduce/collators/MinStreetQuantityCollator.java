package ar.edu.itba.api.mapreduce.collators;

import com.hazelcast.map.impl.MapEntrySimple;
import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class MinStreetQuantityCollator implements Collator<Map.Entry<String, MapEntrySimple<String, Long>>, Map<String, MapEntrySimple<String, Long>>> {
    private final long min;

    public MinStreetQuantityCollator(long min) {
        this.min = min;
    }

    @Override
    public Map<String, MapEntrySimple<String, Long>> collate(Iterable<Map.Entry<String, MapEntrySimple<String, Long>>> iterable) {
        Map<String, MapEntrySimple<String, Long>> result = new HashMap<>();
        for (Map.Entry<String, MapEntrySimple<String, Long>> entry : iterable){
            if(entry.getValue().getValue() >= min){
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
