package usecase;

import material.maps.Entry;
import material.maps.HashTableMapLP;

/**
 * Realizado por: Miguel Sierra Alonso
 *
 * */
public class Flight {

    private int hours, minutes;
    private int year, month, day;
    private int flightCode, capacity, delay;
    private String company ,origin, destination;
    private HashTableMapLP<String, String> properties;

    public Flight(){
        this.properties = new HashTableMapLP<>();
    }

    public Flight(Flight f){
        if(f!=null){
            this.hours = f.getHours();
            this.minutes = f.getMinutes();
            this.year = f.getYear();
            this.month = f.getMonth();
            this.day = f.getDay();
            this.flightCode = f.getFlightCode();
            this.capacity = f.getCapacity();
            this.company = f.getCompany();
            this.delay = f.getDelay();
            this.origin = f.getOrigin();
            this.destination = f.getDestination();
            this.properties = new HashTableMapLP<>();
            for(Entry<String, String> e: f.properties.entries()){
                this.properties.put(e.getKey(),e.getValue());
            }
        }
    }

    public Flight(String company, int flightCode, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.flightCode = flightCode;
        this.company = company;
        this.properties = new HashTableMapLP<>();
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

    public String toString(){
        String fecha = this.day + "-" + this.month + "-" + this.year ;
        String companyAndFlightCode = "\t" + this.company + this.flightCode;
        String delayString = "";
        String hourAndMinutes = "";
        String origin = "";
        String destination = "";

        //Validations
        if (this.delay > 0)
            delayString = "\t" + "DELAYED (" + this.delay + "min)";
        if((this.hours > 0) || (this.minutes > 0))
            hourAndMinutes = "\t" + this.hours + ":" + this.minutes;
        if((this.origin != null) && (!this.origin.equals("")))
            origin = "\t" + this.origin;
        if((this.destination != null) && (!this.destination.equals("")))
            destination = "\t" + this.destination;

        return fecha + companyAndFlightCode + hourAndMinutes + origin + destination  + delayString;
    }

    @Override
    public int hashCode(){
        int result  = 13;
        result  = 31 * result  + (this.company != null ? company.hashCode() : 0);
        result = 31 * result  + this.flightCode;
        result = 31 * result  + this.day;
        result = 31 * result  + this.month;
        result = 31 * result  + this.year;
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if((obj == null) || (!(obj instanceof Flight)))
            return false;
        Flight objFlight = (Flight) obj;

        return objFlight.hashCode() == this.hashCode();
    }
}