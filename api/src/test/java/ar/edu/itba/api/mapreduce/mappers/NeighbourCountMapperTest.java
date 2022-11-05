package ar.edu.itba.api.mapreduce.mappers;

import ar.edu.itba.api.City;
import ar.edu.itba.api.Tree;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class NeighbourCountMapperTest {

    private Collection<Tree> treesCollection = new ArrayList<Tree>();
    private HazelcastInstance hazelcast;
    private HazelcastInstance hazelcastClient;
    private JobTracker jobTracker;
    private IList<Tree> iTreeList;


    @Before
    public void initialize() {
        Config serverConfig = new Config();

        hazelcast = Hazelcast.newHazelcastInstance(serverConfig);
        hazelcastClient = HazelcastClient.newHazelcastClient();
        jobTracker = hazelcastClient.getJobTracker("trees-per-person");
        iTreeList = hazelcastClient.getList("tree-list");
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n2","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n3","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n4","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n5","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n3","st1",20.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n2","st1",20.0));
    }

    @After
    public void shutdown() {
        hazelcast.shutdown();
        hazelcastClient.shutdown();
    }

    @Test
    public void getTotalTreesQtyByNeighbour() throws ExecutionException, InterruptedException {
        iTreeList.addAll(this.treesCollection);
        final KeyValueSource<String, Tree> source = KeyValueSource.fromList(iTreeList);
        final Job<String, Tree> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, List<Long>>> result = job.mapper(new NeighbourCountMapper()).submit();
        Map<String, List<Long>> ans = result.get();
        Assert.assertEquals(ans.get("n1").size(), 4);
        Assert.assertEquals(ans.get("n2").size(), 2);
        Assert.assertEquals(ans.get("n3").size(), 2);
        Assert.assertEquals(ans.get("n4").size(), 1);
        Assert.assertEquals(ans.get("n5").size(), 1);
    }
}