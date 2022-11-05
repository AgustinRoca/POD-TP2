package ar.edu.itba.api.mapreduce.mappers;

import ar.edu.itba.api.Tree;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class SpeciesDiameterMapper implements Mapper<String, Tree, String, Double> {
    @Override
    public void map(String key, Tree tree, Context<String, Double> context) {
        context.emit(tree.getSpecies(), tree.getDiameter());
    }
}
