package ar.edu.itba.api.mapreduce.mappers;

import ar.edu.itba.api.Tree;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashMap;
import java.util.Map;

public class StreetCountMapper implements Mapper<String, Tree, String, Map<String, Long>> {
    @Override
    public void map(String key, Tree tree, Context<String, Map<String, Long>> context) {
        Map<String, Long> map = new HashMap<>();
        map.put(tree.getStreet(), 1L);
        context.emit(tree.getNeighbourhood(), map);
    }
}
