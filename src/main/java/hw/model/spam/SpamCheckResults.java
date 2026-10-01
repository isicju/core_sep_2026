package hw.model.spam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SpamCheckResults {
    private double spamProbability;
    private String shortSpamDescription;
}
