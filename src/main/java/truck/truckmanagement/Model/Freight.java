package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity

public class Freight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String header;
    private String description;
    private double weight;
    private int quantity;
    private String provider;

    public Freight(String header, String description, double weight, int quantity, String provider) {
        this.header = header;
        this.description = description;
        this.weight = weight;
        this.quantity = quantity;
        this.provider = provider;
    }

    public String getIdHeaderQuantityWeightProvider(){
        return "#"+ id + " "+ header + " " + quantity + " vnt. po " + weight + " kg. " + provider;
    }

    @Override
    public String toString(){
        return header;
    }
}
