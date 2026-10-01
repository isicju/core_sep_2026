package hw.model;

import hw.exception.TicketParseException;

import java.util.Map;

public class TicketFactory {
    public Ticket createTicket(Map<String, String> form) {
        if ("AVIA".equals(form.get("type"))) {
            return new AviaTicket(form);
        } else if ("RAILWAY".equals(form.get("type"))) {
            return new RailwayTicket(form);
        }
        throw new TicketParseException("Ticket type must be AVIA or RAILWAY");
    }
}
