package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass

public class Checkpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String country;
    private String city;
    private String address;

    public Checkpoint(String country, String city, String address) {
        this.country = country;
        this.city = city;
        this.address = address;
    }

    public String getIdCountryCityAddress(){
        return "#"+ id + " " + country + " " + city + " " + address;
    }

    @Override
    public String toString(){
        return country+" ("+city+")";
    }
}
