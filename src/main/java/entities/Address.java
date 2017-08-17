package entities;

import java.security.PublicKey;

/**
 * Created by sergeybp on 20.07.17.
 */
public class Address {

    public String zip;

    public String country;

    public String city;

    public String district;

    public String street;

    public int id;

    public Address(){}

    public Address(String zip, String country, String city, String district, String street) {
        this.zip = zip;
        this.country = country;
        this.city = city;
        this.district = district;
        this.street = street;
    }

    public String toString() {
        String res = "";
        res += zip + "   " + country + "   " + city + "\n";
        res += district + "  |  " + street + "\n";
        return res;

    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
