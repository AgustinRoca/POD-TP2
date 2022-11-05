package ar.edu.itba.client.queries;

import ar.edu.itba.api.Tree;
import ar.edu.itba.api.queryResults.Query5Result;
import ar.edu.itba.api.mapreduce.collators.ThousandTreesCollator;
import ar.edu.itba.api.mapreduce.combiners.StringCountCombinerFactory;
import ar.edu.itba.api.mapreduce.mappers.NeighbourCountMapper;
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

public class Query5 {
    private final ICompletableFuture<Map<String, Long>> result;

    public Query5 (HazelcastInstance hz) {
        JobTracker jobTracker = hz.getJobTracker("pair-neigh-with-same-thousand-trees");
        final IList<Tree> trees = hz.getList("tree-list");
        final KeyValueSource<String, Tree> source = KeyValueSource.fromList(trees);

        final Job<String, Tree> job = jobTracker.newJob(source);
        result = job
                .mapper(new NeighbourCountMapper())
                .combiner(new StringCountCombinerFactory())
                .reducer(new NeighbourCountReducerFactory())
                .submit(new ThousandTreesCollator());
    }

    public List<Query5Result> getResult() throws ExecutionException, InterruptedException {
        // Wait and retrieve the result
        Map<String, Long> ans = result.get();

        List<Query5Result> list = Query5Result.listFrom(ans);
        Collections.sort(list);
        return list;
    }
}
