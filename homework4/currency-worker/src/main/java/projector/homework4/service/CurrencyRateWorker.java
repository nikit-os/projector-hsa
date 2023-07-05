package projector.homework4.service;

import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyRateWorker {

    @Autowired
    private CurrencyRateFetcher currencyRateFetcher;

    @Autowired
    private CurrencyRateSender currencyRateSender;

    @Job(name = "currency_rate_job")
    public void fetchAndSend() {
        var currRate = currencyRateFetcher.fetch();
        currencyRateSender.send(currRate);
    }
}
