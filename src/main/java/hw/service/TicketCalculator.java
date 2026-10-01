package hw.service;

import hw.exception.TicketPriceCalculationException;
import hw.model.Ticket;
import hw.model.TicketType;

import static hw.model.TicketType.AVIA;
import static hw.model.TicketType.RAILWAY;

public class TicketCalculator {

   public int calculatePrice(Ticket ticket) {
        if (ticket.getTicketType() == AVIA) {
            return 1000 + (ticket.isHasLuggage() ? ticket.getLuggage() * 500 : 0);
        } else if (ticket.getTicketType() == RAILWAY) {
            int paidLuggageCount = (ticket.getLuggage() <= 2) ? 0 : ticket.getLuggage() - 2;

            return 750 + (ticket.isHasLuggage() ? paidLuggageCount * 300 : 0);
        }
        throw new TicketPriceCalculationException("Invalid ticket type: " + ticket.getTicketType());
    }
}
