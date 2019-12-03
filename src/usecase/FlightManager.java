package usecase;


import material.maps.HashTableMapLP;

import java.util.ArrayList;
import java.util.List;

public class FlightManager {


    private HashTableMapLP<Flight, Flight> map;
    private HashTableMapLP<Flight, List<Passenger>> flightPassengerMap;

    private HashTableMapLP<String, List<Flight>> searchByDateMap;
    private HashTableMapLP<String, List<Flight>> searchByDestinationMap;
    private HashTableMapLP<Passenger, List<Flight>> searchByPassengerMap;


    public FlightManager(){
        this.map = new HashTableMapLP<>();
        this.flightPassengerMap = new HashTableMapLP<>();
        this.searchByDateMap = new HashTableMapLP<>();
        this.searchByDestinationMap = new HashTableMapLP<>();
        this.searchByPassengerMap = new HashTableMapLP<>();
    }

    public Flight addFlight(String company, int flightCode, int year, int month, int day) {
        Flight newFlight = new Flight(company, flightCode, year, month, day);
        //Validate key not appear in the map
        for(Flight k : this.map.keys()){
            if(k.equals(newFlight))
                throw new RuntimeException("The flight already exists.");
        }
        String dateKey = "" + year + month + day;
        List <Flight> flightByDateList;
        this.map.put(newFlight, newFlight);
        this.flightPassengerMap.put(newFlight, new ArrayList<>());
        if(this.searchByDateMap.get(dateKey) == null)
            this.searchByDateMap.put(dateKey, new ArrayList<>());
        if(this.searchByDateMap.get(dateKey).size()==0) {
            flightByDateList = new ArrayList<>();
            flightByDateList.add(newFlight);
            this.searchByDateMap.put(dateKey, flightByDateList);
        }else{
            flightByDateList = this.searchByDateMap.get(dateKey);
            flightByDateList.add(newFlight);
            this.searchByDateMap.remove(dateKey);
            this.searchByDateMap.put(dateKey, flightByDateList);
        }
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
            if((key.equals(updatedFlightInfo)) && (!key.equals(flightInMap))) {
                throw new RuntimeException("The new flight identifiers are already in use.");
            }
        }
        this.map.remove(flightInMap);
        Flight updatedFlightInfoCopy = new Flight(updatedFlightInfo);
        this.map.put(updatedFlightInfoCopy, updatedFlightInfoCopy);

        List<Passenger> passengersInFlight = this.flightPassengerMap.get(flightInMap);
        this.flightPassengerMap.remove(flightInMap);
        this.flightPassengerMap.put(updatedFlightInfoCopy, passengersInFlight);

        String oldDateKey = "" + year + month + day;
        String newDateKey = "" + updatedFlightInfo.getYear() + updatedFlightInfo.getMonth() + updatedFlightInfo.getDay();

        List<Flight> flightByDateList = new ArrayList<>(this.searchByDateMap.get(oldDateKey));
        this.searchByDateMap.remove(oldDateKey);
        flightByDateList.remove(flightInMap);
        flightByDateList.add(updatedFlightInfoCopy);
        this.searchByDateMap.put(newDateKey, flightByDateList);
    }

    public void addPassenger(String dni, String name, String surname, Flight flight) {
        Passenger newPassenger = new Passenger(dni, name, surname);
        List<Passenger> actualList = this.flightPassengerMap.get(flight);
        if(actualList == null)
            throw new RuntimeException("The flight doesn't exits.");
        if(actualList.size() >= flight.getCapacity() )
            throw new RuntimeException("This flight doesn't have capacity for more passengers.");
        if(!actualList.contains(newPassenger)) {
            actualList.add(newPassenger);
            this.flightPassengerMap.put(flight, actualList);
        }
    }

    public Iterable<Passenger> getPassengers(String company, int flightCode, int year, int month, int day) {
        Flight flightKey;
        try {
            flightKey = this.getFlight(company, flightCode, year, month, day);
        }catch (RuntimeException e){
            throw new RuntimeException("The flight doesn't exists.");
        }
        return this.flightPassengerMap.get(flightKey);

    }

    public Iterable<Flight> flightsByDate(int year, int month, int day) {
        String dateKey = "" + year + month + day;
        return this.searchByDateMap.get(dateKey) == null ? new ArrayList<>() : this.searchByDateMap.get(dateKey);
    }

    public Iterable<Flight> getFlightsByPassenger(Passenger passenger) {
        throw new RuntimeException("Not yet implemented.");
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        throw new RuntimeException("Not yet implemented.");
    }

}