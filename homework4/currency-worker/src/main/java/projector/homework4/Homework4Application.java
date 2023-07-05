package projector.homework4;

import jakarta.annotation.PostConstruct;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import projector.homework4.properties.CoinApiProperties;
import projector.homework4.service.CurrencyRateFetcher;
import projector.homework4.service.CurrencyRateWorker;

import java.time.Duration;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Homework4Application {

	@Autowired
	private JobScheduler jobScheduler;

	@Autowired
	private CurrencyRateWorker currencyRateWorker;

	public static void main(String[] args) {
		SpringApplication.run(Homework4Application.class, args);
	}

	@PostConstruct
	public void scheduleJob() {
		jobScheduler.scheduleRecurrently(Duration.ofHours(1), () -> currencyRateWorker.fetchAndSend());
	}
}
