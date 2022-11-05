package ar.edu.itba.api.mapreduce.reducers;

import ar.edu.itba.api.City;
import ar.edu.itba.api.Tree;
import ar.edu.itba.api.mapreduce.mappers.SpeciesDiameterMapper;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AverageReducersFactoryTest {

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
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",13.0));
        treesCollection.add(new Tree(City.CABA,"sp2","n2","st1",17.0));
        treesCollection.add(new Tree(City.CABA,"sp3","n1","st1",11.0));
        treesCollection.add(new Tree(City.CABA,"sp2","n3","st1",27.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",22.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n4","st1",24.0));
        treesCollection.add(new Tree(City.CABA,"sp3","n1","st1",15.0));
        treesCollection.add(new Tree(City.CABA,"sp3","n5","st1",21.0));
        treesCollection.add(new Tree(City.CABA,"sp2","n3","st1",19.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n2","st1",29.0));
    }

    @After
    public void shutdown() {
        hazelcast.shutdown();
        hazelcastClient.shutdown();
    }

    @Test
    public void getAllDiametersByTreesSpecies() throws ExecutionException, InterruptedException {
        iTreeList.addAll(this.treesCollection);
        final KeyValueSource<String, Tree> source = KeyValueSource.fromList(iTreeList);
        final Job<String, Tree> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, Double>> result = job.mapper(new SpeciesDiameterMapper()).reducer(new AverageReducerFactory()).submit();
        Map<String,Double> ans = result.get();
        Assert.assertEquals(ans.get("sp1"), 22.0);
        Assert.assertEquals(ans.get("sp2"), 21.0);
        Assert.assertEquals(ans.get("sp3"), 15.666666666666666);
    }
}
