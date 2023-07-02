package projector.homework3.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import projector.homework3.entity.Customer;

public interface CustomerMongoRepo extends MongoRepository<Customer, String> {

}
