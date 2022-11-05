package ar.edu.itba.api.mapreduce.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class AverageReducerFactory implements ReducerFactory<String, Double, Double> {
    @Override
    public Reducer<Double, Double> newReducer(String key) {
        return new AverageReducerFactory.AverageReducer();
    }

    private class AverageReducer extends Reducer<Double, Double> {
        private long diameterSum;
        private long totalTrees;

        @Override
        public void beginReduce() {
            diameterSum = 0;
            totalTrees = 0;
        }

        @Override
        public void reduce(Double diameter) {
            diameterSum += diameter;
            totalTrees++;
        }

        @Override
        public Double finalizeReduce() {
            return (double) diameterSum / totalTrees;
        }
    }
}
