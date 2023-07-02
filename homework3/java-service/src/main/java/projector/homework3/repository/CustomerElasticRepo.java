package projector.homework3.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import projector.homework3.entity.PokemonTrainer;

public interface CustomerElasticRepo extends ElasticsearchRepository<PokemonTrainer, String> {
}
