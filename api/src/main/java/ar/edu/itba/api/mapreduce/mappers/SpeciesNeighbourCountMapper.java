package ar.edu.itba.api.mapreduce.mappers;

import ar.edu.itba.api.Tree;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class SpeciesNeighbourCountMapper implements Mapper<String, Tree, String, Long> {
    private final String species;

    public SpeciesNeighbourCountMapper(String species) {
        this.species = species;
    }

    @Override
    public void map(String key, Tree tree, Context<String, Long> context) {
        if(species.equals(tree.getSpecies())) {
            context.emit(tree.getNeighbourhood(), 1L);
        }
    }
}
