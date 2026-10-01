package hw;

import hw.model.Ticket;
import hw.model.TicketFactory;
import hw.service.TickerValidator;
import hw.service.TicketCalculator;
import hw.service.image.ImageGenerator;
import hw.service.image.ImageGeneratorLookup;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class RequestProcessor {

    private TicketFactory ticketFactory;
    private TickerValidator tickerValidator;
    private TicketCalculator ticketCalculator;
    private ImageGeneratorLookup imageGenerator;

   public byte[] processRequest(Map<String,String> form) {
       Ticket ticket = ticketFactory.createTicket(form);
       tickerValidator.validate(ticket);

       int cost = ticketCalculator.calculatePrice(ticket);
       ImageGenerator generator = imageGenerator.lookup(ticket.getImageOutputType());
       return generator.generateImage(ticket, cost);
    }

}
