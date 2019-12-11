package usecase;

/**
 * Realizado por: Miguel Sierra Alonso
 *
 * */
public class Passenger {

    private String dni;
    private String name;
    private String surname;

    public Passenger(){

    }

    public Passenger(String dni, String name, String surname) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
    }

    public String getDNI() {
        return dni;
    }

    public void setDNI(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString(){
        return this.dni + " " + this.name + " " + this.surname;
    }

    @Override
    public int hashCode(){
        int result  = 13;
        result  = 31 * result  + (this.dni != null ? dni.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if((obj == null) || (!(obj instanceof Passenger)))
            return false;
        Passenger objPassenger = (Passenger) obj;

        return objPassenger.hashCode() == this.hashCode();
    }
}