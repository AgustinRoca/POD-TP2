package ar.edu.itba.api.mapreduce.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.Map;

public class StreetCountCombinerFactory implements CombinerFactory<String, Map<String, Long>, Map<String, Long>> {
    @Override
    public Combiner<Map<String, Long>, Map<String, Long>> newCombiner(String s) {
        return new StreetCountCombinerFactory.StreetCountCombiner();
    }

    private class StreetCountCombiner extends Combiner<Map<String, Long>, Map<String, Long>> {
        private Map<String, Long> map = new HashMap<>();

        @Override
        public void combine(Map<String, Long> m) {
            for (String key : m.keySet()){
                map.put(key, map.getOrDefault(key, 0L) + m.get(key));
            }
        }

        @Override
        public Map<String, Long> finalizeChunk() {
            return map;
        }

        @Override
        public void reset() {
            map = new HashMap<>();
        }
    }
}
