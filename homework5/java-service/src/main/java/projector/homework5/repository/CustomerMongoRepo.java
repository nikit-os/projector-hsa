package projector.homework5.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import projector.homework5.entity.Customer;

public interface CustomerMongoRepo extends MongoRepository<Customer, String> {

}
