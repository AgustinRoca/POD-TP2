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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class StreetCountMapperTest {
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
        treesCollection.add(new Tree(City.CABA,"sp2","n2","st3",17.0));
        treesCollection.add(new Tree(City.CABA,"sp3","n1","st1",11.0));
        treesCollection.add(new Tree(City.CABA,"sp2","n3","st1",27.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n1","st1",22.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n4","st2",24.0));
        treesCollection.add(new Tree(City.CABA,"sp3","n1","st2",15.0));
        treesCollection.add(new Tree(City.CABA,"sp3","n5","st3",21.0));
        treesCollection.add(new Tree(City.CABA,"sp2","n3","st2",19.0));
        treesCollection.add(new Tree(City.CABA,"sp1","n2","st2",29.0));
    }

    @After
    public void shutdown() {
        hazelcast.shutdown();
        hazelcastClient.shutdown();
    }

    @Test
    public void getTotalQtyOfTreesByStreetOfEachNeighbourhood() throws ExecutionException, InterruptedException {
        iTreeList.addAll(this.treesCollection);
        final KeyValueSource<String, Tree> source = KeyValueSource.fromList(iTreeList);
        final Job<String, Tree> job = jobTracker.newJob(source);
        ICompletableFuture<Map<String, List<Map<String,Long>>>> result = job.mapper(new StreetCountMapper()).submit();
        Map<String, List<Map<String,Long>>> ans = result.get();
        //TEST NEIGHBOUR 1
        int n1St1 = 0;
        int n1St2 = 0;
        for(Map<String,Long> sts : ans.get("n1")) {
            for (String st : sts.keySet()) {
                if(st.equals("st1")) {
                    n1St1++;
                } else if(st.equals("st2")) {
                    n1St2++;
                }
            }
        }
        Assert.assertEquals(n1St1, 3);
        Assert.assertEquals(n1St2, 1);

        //TEST NEIGHBOUR 2
        int n2St2 = 0;
        int n2St3 = 0;
        for(Map<String,Long> sts : ans.get("n2")) {
            for (String st : sts.keySet()) {
                if(st.equals("st2")) {
                    n2St2++;
                } else if(st.equals("st3")) {
                    n2St3++;
                }
            }
        }
        Assert.assertEquals(n2St2, 1);
        Assert.assertEquals(n2St3, 1);

        //TEST NEIGHBOUR 3
        int n3St1 = 0;
        int n3St2 = 0;
        for(Map<String,Long> sts : ans.get("n3")) {
            for (String st : sts.keySet()) {
                if(st.equals("st1")) {
                    n3St1++;
                } else if(st.equals("st2")) {
                    n3St2++;
                }
            }
        }
        Assert.assertEquals(n3St1, 1);
        Assert.assertEquals(n3St2, 1);

        //TEST NEIGHBOUR 4
        int n4St2 = 0;
        for(Map<String,Long> sts : ans.get("n4")) {
            for (String st : sts.keySet()) {
                if(st.equals("st2")) {
                    n4St2++;
                }
            }
        }
        Assert.assertEquals(n4St2, 1);
        //TEST NEIGHBOUR 5
        int n5St3 = 0;
        for(Map<String,Long> sts : ans.get("n5")) {
            for (String st : sts.keySet()) {
                if(st.equals("st3")) {
                    n5St3++;
                }
            }
        }
        Assert.assertEquals(n5St3, 1);
    }
}
