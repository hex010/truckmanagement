package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity

public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String model;
    private int maxSpeed;
    private int mileage;
    private LocalDate dateOfManufacture;
    private String color;
    private String fuelType;
    private String transmissionType;

    public Transport(String model, int maxSpeed, int mileage, LocalDate dateOfManufacture, String color, String fuelType, String transmissionType) {
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.mileage = mileage;
        this.dateOfManufacture = dateOfManufacture;
        this.color = color;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
    }

    public String getIdModelDateOfManuFacture(){
        return "#" + id + " " + model + " " + dateOfManufacture.toString();
    }

    @Override
    public String toString(){
        return "#" + id + " " + model + " " + dateOfManufacture.toString();
    }
}
