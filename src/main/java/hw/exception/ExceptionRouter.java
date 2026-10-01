package hw.exception;

public class ExceptionRouter {

    public ExceptionRoute route(Throwable e) {
        if (e instanceof TicketParseException) {
            return new ExceptionRoute(400, e.getMessage());
        } else if (e instanceof SpamCheckerException) {
            return new ExceptionRoute(500, e.getMessage());
        } else if (e instanceof ImageGeneratorNotFoundException) {
            return new ExceptionRoute(400, e.getMessage());
        } else if (e instanceof TicketPriceCalculationException) {
            return new ExceptionRoute(400, e.getMessage());
        } else if (e instanceof TicketValidationException) {
            return new ExceptionRoute(400, e.getMessage());
        } else if (e instanceof ImageGeneratorProcessingError) {
            return new ExceptionRoute(500, e.getMessage());
        } else if (e instanceof SpamCheckerSlurException) {
            return new ExceptionRoute(400, e.getMessage());
        } else {
            return new ExceptionRoute(500, e.getMessage());
        }
    }


}
