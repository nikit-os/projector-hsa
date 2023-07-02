package projector.homework3.controller;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projector.homework3.entity.PokemonTrainer;
import projector.homework3.repository.CustomerElasticRepo;

@RestController
@RequestMapping("/elastic")
public class ElasticController {

    private Faker faker;
    private CustomerElasticRepo customerRepo;

    @Autowired
    public ElasticController(Faker faker, CustomerElasticRepo customerRepo) {
        this.faker = faker;
        this.customerRepo = customerRepo;
    }

    @PostMapping("/trainer")
    public PokemonTrainer createPokemonTrainer() {
        var trainer = new PokemonTrainer(faker.name().firstName(), faker.name().lastName(), faker.pokemon().name());
        return customerRepo.save(trainer);
    }

}
