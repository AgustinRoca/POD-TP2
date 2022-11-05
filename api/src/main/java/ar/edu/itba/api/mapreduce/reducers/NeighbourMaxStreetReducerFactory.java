package ar.edu.itba.api.mapreduce.reducers;

import com.hazelcast.map.impl.MapEntrySimple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.Map;

public class NeighbourMaxStreetReducerFactory implements ReducerFactory<String, Map<String, Long>, MapEntrySimple<String, Long>> {

    @Override
    public Reducer<Map<String, Long>, MapEntrySimple<String, Long>> newReducer(String key) {
        return new NeighbourMaxStreetReducerFactory.NeighbourMaxReducer();
    }

    private class NeighbourMaxReducer extends Reducer<Map<String, Long>, MapEntrySimple<String, Long>>{
        private MapEntrySimple<String, Long> max;

        @Override
        public void beginReduce() {
            max = new MapEntrySimple<>(null, 0L);
        }

        @Override
        public void reduce(Map<String, Long> map) {
            for(Map.Entry<String, Long> entry : map.entrySet()){
                if(entry.getValue() > max.getValue()){
                    max = new MapEntrySimple<>(entry.getKey(), entry.getValue());
                }
            }
        }

        @Override
        public MapEntrySimple<String, Long> finalizeReduce() {
            return max;
        }
    }
}