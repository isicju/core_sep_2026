package hw.service.image;

import hw.model.Ticket;

public interface ImageGenerator {
    byte[] generateImage(Ticket ticket, int cost);
}
