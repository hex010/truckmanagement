package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity

public class DestinationPoint extends Checkpoint{
    private String additionalInformation;

    public DestinationPoint(String country, String city, String address, String additionalInformation) {
        super(country, city, address);
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString(){
        return getCountry()+" ("+getCity()+")";
    }
}
