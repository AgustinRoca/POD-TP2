package ar.edu.itba.api;


import java.io.Serializable;

public class Tree implements Serializable {
    private final City city;
    private final String species;
    private final String neighbourhood;
    private final String street;
    private final double diameter;

    public Tree(City city, String species, String neighbourhood, String street, double diameter) {
        this.city = city;
        this.species = species;
        this.neighbourhood = neighbourhood;
        this.street = street;
        this.diameter = diameter;
    }

    public City getCity() {
        return city;
    }

    public String getSpecies() {
        return species;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getStreet() {
        return street;
    }

    public double getDiameter() {
        return diameter;
    }
}
