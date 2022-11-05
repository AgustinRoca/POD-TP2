package ar.edu.itba.client.queries;

import ar.edu.itba.api.Tree;
import ar.edu.itba.api.queryResults.Query4Result;
import ar.edu.itba.api.mapreduce.collators.MinQuantityCollator;
import ar.edu.itba.api.mapreduce.combiners.StringCountCombinerFactory;
import ar.edu.itba.api.mapreduce.mappers.SpeciesNeighbourCountMapper;
import ar.edu.itba.api.mapreduce.reducers.NeighbourCountReducerFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query4 {
    private final ICompletableFuture<Map<String, Long>> result;

    public Query4 (HazelcastInstance hz, String species, long min) {
        JobTracker jobTracker = hz.getJobTracker("pair-neigh-of-at-least-species");
        final IList<Tree> trees = hz.getList("tree-list");
        final KeyValueSource<String, Tree> source = KeyValueSource.fromList(trees);

        final Job<String, Tree> job = jobTracker.newJob(source);
        result = job
                .mapper(new SpeciesNeighbourCountMapper(species))
                .combiner(new StringCountCombinerFactory())
                .reducer(new NeighbourCountReducerFactory())
                .submit(new MinQuantityCollator(min));

    }

    public List<Query4Result> getResult() throws ExecutionException, InterruptedException {
        // Wait and retrieve the result
        Map<String, Long> ans = result.get();

        List<Query4Result> list = Query4Result.listFrom(ans.keySet());
        Collections.sort(list);
        return list;
    }
}
