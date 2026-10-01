package hw.service.spam;

import hw.model.spam.SpamCheckResults;

public interface SpamService {

    public SpamCheckResults verifySpam(String input);

}
