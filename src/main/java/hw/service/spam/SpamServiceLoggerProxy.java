package hw.service.spam;

import hw.model.spam.SpamCheckResults;

public class SpamServiceLoggerProxy implements SpamService {

    private SpamService spamService;

    public SpamServiceLoggerProxy(SpamService spamService) {
        this.spamService = spamService;
    }

    @Override
    public SpamCheckResults verifySpam(String input) {
        long before = System.currentTimeMillis();
        SpamCheckResults results = spamService.verifySpam(input);
        System.out.println("Spam verification time: " + (System.currentTimeMillis() - before) / 1000f + " seconds");
        return results;
    }
}
