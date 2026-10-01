package hw.model;


import hw.exception.TicketValidationException;
import io.micrometer.common.util.StringUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

import static hw.model.TicketParseUtils.*;

@Data
public class Ticket {

    private String name;
    private String surname;
    private LocalDate dob;
    private String comment;
    private boolean hasLuggage;
    private int luggage;
    private ImageOutputType imageOutputType;
    private TicketType ticketType;

    public Ticket(Map<String, String> form) {
        this.name = form.get("name");
        this.surname = form.get("surname");
        this.dob = parseDate(form.get("dob"), "please keep format YYYY-MM-DD");
        this.comment = form.get("comment");
        this.hasLuggage = parseBoolean(form.get("has_luggage"), " has_luggage has to be 1 or 0");
        this.luggage = parseInt(form.get("luggage"), "luggage has to be not null integer");
        this.ticketType = parseTicketType(form.get("type"), " type can be only AVIA or RAILWAY");
        this.imageOutputType = parseImageType(form.get("file_type"), " file image type can be only PDF or PNG");
    }

    public void validate(){
        if(StringUtils.isEmpty(name)){
            throw new TicketValidationException("name can't be empty!");
        }
        if(StringUtils.isEmpty(surname)){
            throw new TicketValidationException("surname can't be empty!");
        }
        if(hasLuggage && luggage <= 0){
            throw new TicketValidationException("has luggage but has 0 or less value!");
        }
    }

}