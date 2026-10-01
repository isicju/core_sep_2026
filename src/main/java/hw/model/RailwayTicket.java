package hw.model;

import hw.exception.TicketValidationException;

import java.time.LocalDate;
import java.util.Map;

public class RailwayTicket extends Ticket{

    public RailwayTicket(Map<String, String> form) {
        super(form);
    }

    @Override
    public void validate(){
        super.validate();
        int age = LocalDate.now().getYear() - getDob().getYear();
        if(age < 18 ){
            throw new TicketValidationException("For railway tickets age should be more than 21");
        }
        if(isHasLuggage() && getLuggage() > 5){
            throw new TicketValidationException("For railway tickets luggage should be less than 5");
        }
    }

}
