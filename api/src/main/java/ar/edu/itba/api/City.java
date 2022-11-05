package ar.edu.itba.api;

import java.io.Serializable;

public enum City implements Serializable {
    CABA,
    VANCOUVER;

    public City from(String city){
        return City.valueOf(city.toUpperCase());
    }
}
