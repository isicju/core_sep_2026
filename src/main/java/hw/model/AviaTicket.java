package hw.model;

import hw.exception.TicketValidationException;

import java.time.LocalDate;
import java.util.Map;

public class AviaTicket extends Ticket{

    public AviaTicket(Map<String, String> form) {
        super(form);
    }

    @Override
    public void validate(){
        super.validate();
        int age = LocalDate.now().getYear() - getDob().getYear();
        if(age < 21 ){
            throw new TicketValidationException("For avia tickets age should be more than 21");
        }
        if(isHasLuggage() && getLuggage() > 3){
            throw new TicketValidationException("For avia tickets luggage should be less than 3");
        }
    }

}
