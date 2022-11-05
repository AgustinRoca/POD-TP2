package ar.edu.itba.api.mapreduce.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class StringCountCombinerFactory implements CombinerFactory<String, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(String s) {
        return new StringCountCombiner();
    }

    private class StringCountCombiner extends Combiner<Long, Long> {
        private long sum = 0;

        @Override
        public void combine(Long num) {
            sum += num;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = 0;
        }
    }
}
