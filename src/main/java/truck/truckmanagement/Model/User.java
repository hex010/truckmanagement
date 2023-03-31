package truck.truckmanagement.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import truck.truckmanagement.Enum.Role_enum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Role_enum role;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private int phoneNumber;
    private LocalDate birthday;

    public User(Role_enum role, String login, String password, String firstname, String lastname, String email, int phoneNumber, LocalDate birthday) {
        this.role = role;
        this.login = login;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public String getIdFirstnameLastNameDate(){
        return "#"+id+" "+firstname + " " + lastname + " "+ birthday.toString();
    }

    public String toString(){
        String roleFormat = role.name();
        roleFormat = roleFormat.substring(0,1).toUpperCase() + roleFormat.substring(1).toLowerCase();
        return roleFormat + " " + firstname + " " + lastname;
    }
}
