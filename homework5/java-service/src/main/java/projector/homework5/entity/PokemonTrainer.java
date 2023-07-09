package projector.homework5.entity;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@ToString
@Document(indexName = "trainers")
public class PokemonTrainer {

    @Id
    public String id;

    public String firstName;
    public String lastName;
    public String pokemon;

    public PokemonTrainer() {}

    public PokemonTrainer(String firstName, String lastName, String pokemon) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pokemon = pokemon;
    }

}
