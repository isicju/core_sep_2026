package hw.service;

import hw.exception.SpamCheckerException;
import hw.exception.SpamCheckerSlurException;
import hw.exception.TicketValidationException;
import hw.model.Ticket;
import hw.model.spam.SpamCheckResults;
import hw.service.spam.SpamService;

public class TickerValidator {

    private SpamService service;

    public TickerValidator(SpamService service) {
        this.service = service;
    }

    public void validate(Ticket ticket) {
        ticket.validate();
        try {
            SpamCheckResults spamCheckResult = service.verifySpam(ticket.getComment());
            if (spamCheckResult.getSpamProbability() > 0.1) {
                throw new SpamCheckerSlurException("Spam check failed " + spamCheckResult.getShortSpamDescription());
            }
        } catch (Exception e) {
            throw new SpamCheckerException("failed to run spam check: " + e.getMessage() + "\n" + ticket.getComment());
        }
    }

}
