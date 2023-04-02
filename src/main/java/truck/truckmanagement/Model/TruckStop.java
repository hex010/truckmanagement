package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class TruckStop extends Checkpoint {
    private String reason;
    private LocalDate stopStartDate;
    private LocalDate stopEndDate;
    @ManyToOne
    private Destination destination;

    public TruckStop(String country, String city, String address, String reason, LocalDate stopStartDate, LocalDate stopEndDate, Destination destination) {
        super(country, city, address);
        this.reason = reason;
        this.stopStartDate = stopStartDate;
        this.stopEndDate = stopEndDate;
        this.destination = destination;
    }

    @Override
    public String toString(){
        return stopStartDate + " - " + stopEndDate + " " + getCity();
    }
}
