package usecase;


import material.maps.HashTableMapLP;

public class FlightManager {

    private HashTableMapLP<Flight, Flight> map;

    public FlightManager(){
        this.map = new HashTableMapLP<>();
    }

    public Flight addFlight(String company, int flightCode, int year, int month, int day) {
        Flight newFlight = new Flight(company, flightCode, year, month, day);
        //Validate key not appear in the map
        for(Flight k : this.map.keys()){
            if(k.equals(newFlight))
                throw new RuntimeException("The flight already exists.");
        }
        this.map.put(newFlight, newFlight);
        return newFlight;
    }

    public Flight getFlight(String company, int flightCode, int year, int month, int day) {
        Flight flighInMap = this.map.get(new Flight(company, flightCode, year, month, day));
        if(flighInMap == null)
            throw new RuntimeException("Flight not found.");
        Flight copy = new Flight(flighInMap);
        return copy;
    }


    public void updateFlight(String company, int flightCode, int year, int month, int day, Flight updatedFlightInfo) {
        Flight flightInMap;
        try{
            flightInMap = this.getFlight(company, flightCode, year, month, day);
        }catch (RuntimeException e){
            throw new RuntimeException("The flight doesn't exists and can't be updated.");
        }
        for(Flight key : this.map.keys()){
            if(key.equals(updatedFlightInfo)) {
                throw new RuntimeException("The new flight identifiers are already in use.");
            }
        }
        this.map.remove(flightInMap);
        this.map.put(updatedFlightInfo, updatedFlightInfo);
    }

    public void addPassenger(String dni, String name, String surname, Flight flight) {
        throw new RuntimeException("Not yet implemented.");
    }


    public Iterable<Passenger> getPassengers(String company, int flightCode, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> flightsByDate(int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> getFlightsByPassenger(Passenger passenger) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
    }

}