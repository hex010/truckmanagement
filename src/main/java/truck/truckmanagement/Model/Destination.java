package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    private DestinationPoint destinationPoint;
    @ManyToOne
    private Transport transport;
    @ManyToOne
    private Freight freight;
    @ManyToOne
    private User driver;
    @ManyToOne
    private User manager;
    @OneToMany (mappedBy = "destination")
    private List<TruckStop> truckStops;

    public Destination(LocalDate startDate, DestinationPoint destinationPoint, Transport transport, Freight freight, User driver, User manager) {
        this.startDate = startDate;
        this.destinationPoint = destinationPoint;
        this.transport = transport;
        this.freight = freight;
        this.driver = driver;
        this.manager = manager;
    }

    @Override
    public String toString(){
        return driver.getFirstname() + " (vair.) | "+ manager.getFirstname() + " (vadyb.) | "+destinationPoint.toString()+ " | "+transport.getModel() + " | "+ freight.toString();
    }
}
