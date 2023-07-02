package projector.homework3.entity;

import lombok.ToString;
import org.springframework.data.annotation.Id;

@ToString
public class Customer {

    @Id
    public String id;

    public String firstName;
    public String lastName;

    public Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
