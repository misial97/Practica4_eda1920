package usecase;

import material.maps.HashTableMapLP;
import java.util.ArrayList;
import java.util.List;
/**
 * Realizado por: Miguel Sierra Alonso
 *
 * */
public class FlightManager {

    private HashTableMapLP<Flight, Flight> map;
    private HashTableMapLP<Flight, List<Passenger>> flightPassengerMap;

    private HashTableMapLP<String, List<Flight>> searchByDateMap;
    private HashTableMapLP<String, List<Flight>> searchByDestinationMap;
    private HashTableMapLP<Passenger, List<Flight>> searchByPassengerMap;

    private List<String> destinations;

    public FlightManager(){
        this.map = new HashTableMapLP<>();
        this.flightPassengerMap = new HashTableMapLP<>();
        this.searchByDateMap = new HashTableMapLP<>();
        this.searchByDestinationMap = new HashTableMapLP<>();
        this.searchByPassengerMap = new HashTableMapLP<>();
        this.destinations = new ArrayList<>();
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
        if(this.searchByDateMap.get(dateKey) == null) {
            this.searchByDateMap.put(dateKey, new ArrayList<>());
        }
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
        return new Flight(newFlight);
    }

    public Flight getFlight(String company, int flightCode, int year, int month, int day) {
        Flight flightInMap = this.map.get(new Flight(company, flightCode, year, month, day));
        if(flightInMap == null)
            throw new RuntimeException("Flight not found.");
        return new Flight(flightInMap);
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

        //byPassenger
        updateFlightByPassengerMap(flightInMap, updatedFlightInfoCopy);

        //byDate
        String oldDateKey = "" + year + month + day;
        String newDateKey = "" + updatedFlightInfo.getYear() + updatedFlightInfo.getMonth() + updatedFlightInfo.getDay();
        updateFlightByDateMap(flightInMap, updatedFlightInfoCopy, oldDateKey, newDateKey);

        //byDestination
        String oldDestinationKey = flightInMap.getDestination() + "/_/" + year + month + day;
        String newDestinationKey = updatedFlightInfo.getDestination() + "/_/" + updatedFlightInfo.getYear() +
                updatedFlightInfo.getMonth() + updatedFlightInfo.getDay();

        updateFlightByDestinationMap(updatedFlightInfo, flightInMap, updatedFlightInfoCopy, oldDestinationKey, newDestinationKey);

        if((updatedFlightInfo.getDestination() != null) && (!updatedFlightInfo.getDestination().equals("")) &&
                (!this.destinations.contains(updatedFlightInfo.getDestination())))
            this.destinations.add(updatedFlightInfo.getDestination());
    }

    private void updateFlightByPassengerMap(Flight flightInMap, Flight updatedFlightInfoCopy) {
        List<Passenger> passengersInFlight = this.flightPassengerMap.get(flightInMap);
        this.flightPassengerMap.remove(flightInMap);
        this.flightPassengerMap.put(updatedFlightInfoCopy, passengersInFlight);

        for(List<Flight> list :this.searchByPassengerMap.values()){
            if(list.contains(flightInMap)){
                list.remove(flightInMap);
                list.add(updatedFlightInfoCopy);
            }
        }
    }

    private void updateFlightByDateMap(Flight flightInMap, Flight updatedFlightInfoCopy, String oldDateKey, String newDateKey) {
        List<Flight> flightByDateList = new ArrayList<>(this.searchByDateMap.get(oldDateKey));
        this.searchByDateMap.remove(oldDateKey);
        flightByDateList.remove(flightInMap);
        flightByDateList.add(updatedFlightInfoCopy);
        this.searchByDateMap.put(newDateKey, flightByDateList);
    }

    private void updateFlightByDestinationMap(Flight updatedFlightInfo, Flight flightInMap, Flight updatedFlightInfoCopy, String oldDestinationKey, String newDestinationKey) {
        List<Flight> flightList = this.searchByDestinationMap.get(oldDestinationKey);
        this.searchByDestinationMap.remove(oldDestinationKey);
        if(flightList != null){
            if(((updatedFlightInfo.getDestination() != null) || (flightInMap.getDestination() != null)) &&
                    (!updatedFlightInfo.getDestination().equals(flightInMap.getDestination())))
            {
                flightList.remove(flightInMap);
                flightList.add(updatedFlightInfoCopy);
                this.searchByDestinationMap.put(newDestinationKey, flightList);
            }else{
                flightList.add(updatedFlightInfoCopy);
                this.searchByDestinationMap.put(newDestinationKey,flightList);
            }
        }else{
            flightList = this.searchByDestinationMap.get(newDestinationKey);
            if(flightList != null){
                flightList.add(updatedFlightInfoCopy);
                //this.searchByDestinationMap.remove(newDestinationKey);
                this.searchByDestinationMap.put(newDestinationKey, flightList);
            }else {
                flightList = new ArrayList<>();
                flightList.add(updatedFlightInfoCopy);
                this.searchByDestinationMap.put(newDestinationKey, flightList);
            }
        }
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
        List<Flight> flightByPassengerList;
        if(this.searchByPassengerMap.get(newPassenger) == null)
            this.searchByPassengerMap.put(newPassenger, new ArrayList<>());
        if(this.searchByPassengerMap.get(newPassenger).size()==0) {
            flightByPassengerList = new ArrayList<>();
            flightByPassengerList.add(new Flight(flight));
            this.searchByPassengerMap.put(newPassenger, flightByPassengerList);
        }else{
            flightByPassengerList = this.searchByPassengerMap.get(newPassenger);
            flightByPassengerList.add(new Flight(flight));
            this.searchByPassengerMap.remove(newPassenger);
            this.searchByPassengerMap.put(newPassenger, flightByPassengerList);
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
        return this.searchByPassengerMap.get(passenger) == null ? new ArrayList<>() : this.searchByPassengerMap.get(passenger);
    }

    public Iterable<Flight> getFlightsByDestination(String destination, int year, int month, int day) {
        String key = destination + "/_/" + year + month + day;
        if(!this.destinations.contains(destination))
            throw new RuntimeException("The destination doesn't exists.");
        return this.searchByDestinationMap.get(key) == null ? new ArrayList<>() : this.searchByDestinationMap.get(key);
    }

    /*private List<String> listOfDestinationsInMap() {
        List<String> keysInMap = new ArrayList<>();
        for (String keyInMap : this.searchByDestinationMap.keys())
            keysInMap.add(keyInMap.split("/_/")[0]);

        return keysInMap;
    }*/

}
