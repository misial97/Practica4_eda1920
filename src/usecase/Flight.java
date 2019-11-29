package usecase;

import material.maps.HashTableMapLP;

public class Flight {

    private int hours, minutes;
    private int year, month, day;
    private int flightCode, capacity, delay;
    private String company ,origin, destination;
    private HashTableMapLP<String, String> properties;

    public Flight(){
        this.properties = new HashTableMapLP<>();
    }

    public Flight(int hours, int minutes, int year, int month, int day, int flightCode, int capacity, int delay, String company, String origin, String destination) {
        this.hours = hours;
        this.minutes = minutes;
        this.year = year;
        this.month = month;
        this.day = day;
        this.flightCode = flightCode;
        this.capacity = capacity;
        this.delay = delay;
        this.company = company;
        this.origin = origin;
        this.destination = destination;
    }

    public void setTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return this.hours;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getFlightCode() {
        return this.flightCode;
    }

    public void setFlightCode(int flightCode){
        this.flightCode = flightCode;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setProperty(String attribute, String value) {
        this.properties.put(attribute, value);
    }

    public String getProperty(String attribute) {
        return this.properties.get(attribute);
    }

    public Iterable<String> getAllAttributes() {
        return this.properties.keys();
    }

}