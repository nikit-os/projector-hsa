package projector.homework5.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import projector.homework5.entity.PokemonTrainer;

public interface CustomerElasticRepo extends ElasticsearchRepository<PokemonTrainer, String> {
}
