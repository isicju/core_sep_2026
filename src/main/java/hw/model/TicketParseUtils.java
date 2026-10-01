package hw.model;

import hw.exception.TicketParseException;

import java.time.LocalDate;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

public class TicketParseUtils {
    public static TicketType parseTicketType(String type, String error) {
        try{
            return TicketType.valueOf(type);
        }catch (Exception e) {
            throw new TicketParseException(error);
        }
    }

    public static ImageOutputType parseImageType(String type, String error) {
        try{
            return ImageOutputType.valueOf(type);
        }catch (Exception e) {
            throw new TicketParseException(error);
        }
    }

    public static LocalDate parseDate(String date, String error) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new TicketParseException(error);
        }
    }

    public static boolean parseBoolean(String rawString, String error) {
        if (!isBlank(rawString) && List.of("1", "0").contains(rawString)) {
            return "1".equals(rawString);
        }
        throw new TicketParseException(error);
    }


    public static int parseInt(String rawString, String error) {
        try{
            return  Integer.parseInt(rawString);
        }catch (Exception e) {
            throw new TicketParseException(error);
        }
    }
}
