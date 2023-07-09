package projector.homework5.controller;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projector.homework5.entity.Customer;
import projector.homework5.repository.CustomerMongoRepo;

@RestController
@RequestMapping("/mongodb")
public class MongoDbController {

    private Faker faker;
    private CustomerMongoRepo customerRepo;

    @Autowired
    public MongoDbController(Faker faker, CustomerMongoRepo customerRepo) {
        this.faker = faker;
        this.customerRepo = customerRepo;
    }

    @GetMapping("/test")
    public String testRoute() {
        return "everything is fine";
    }

    @PostMapping("/customer")
    public Customer createCustomer() {
        var customer = new Customer(faker.name().firstName(), faker.name().lastName());
        return customerRepo.save(customer);
    }

}
