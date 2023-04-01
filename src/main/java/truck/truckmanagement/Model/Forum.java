package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity

public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    @ManyToOne
    private User user;

    public Forum(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    @Override
    public String toString(){
        return title;
    }
}
