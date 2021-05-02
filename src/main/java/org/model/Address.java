package org.model;

import java.util.Objects;

public class Address {


    private String country;
    private String region;
    private String town;
    private String street;

    public Address(){
        this.country = "";
        this.region = "";
        this.town = "";
        this.street = "";
    }

    public Address(String country, String region, String town, String street){
        this.country = country;
        this.region = region;
        this.town = town;
        this.street = street;
    }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getTown() { return town; }

    public void setTown(String town) { this.town = town; }

    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    @Override
    public boolean equals(Object o){
        if(o instanceof Address){
            Address comp = (Address) o;
            return Objects.equals(this.country,comp.getCountry()) && Objects.equals(this.region,comp.getRegion()) && Objects.equals(this.town, comp.town) && Objects.equals(this.street, comp.getStreet());
        }
        return false;
    }

    @Override
    public String toString(){
        return (this.country + " " + this.region + " " + this.town + " " + this.town);
    }
}
