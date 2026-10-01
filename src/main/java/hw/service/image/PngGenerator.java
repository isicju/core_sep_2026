package hw.service.image;

import hw.model.Ticket;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

public class PngGenerator implements ImageGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int MARGIN = 40;
    private static final int LINE_HEIGHT = 32;
    private static final int VALUE_X = 190;

    @Override
    public byte[] generateImage(Ticket ticket, int cost) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // Border
            g.setColor(new Color(40, 40, 40));
            g.setStroke(new BasicStroke(3f));
            g.drawRoundRect(10, 10, WIDTH - 20, HEIGHT - 20, 20, 20);

            // Title
            int y = MARGIN + 20;
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            g.drawString("TICKET", MARGIN, y);

            // Separator
            y += 15;
            g.setStroke(new BasicStroke(1.5f));
            g.drawLine(MARGIN, y, WIDTH - MARGIN, y);
            y += LINE_HEIGHT;

            // Fields
            y = field(g, y, "Name", ticket.getName());
            y = field(g, y, "Surname", ticket.getSurname());
            y = field(g, y, "Date of birth",
                    ticket.getDob() != null ? ticket.getDob().format(DATE_FORMAT) : "-");
            y = field(g, y, "Ticket type",
                    ticket.getTicketType() != null ? String.valueOf(ticket.getTicketType()) : "-");
            y = field(g, y, "Luggage",
                    ticket.isHasLuggage() ? "Yes (" + ticket.getLuggage() + ")" : "No");

            if (ticket.getComment() != null && !ticket.getComment().isBlank()) {
                y = field(g, y, "Comment", ticket.getComment());
            }

            // Cost
            g.setFont(new Font("SansSerif", Font.BOLD, 22));
            g.drawString("Total cost: " + cost, MARGIN, HEIGHT - MARGIN);

            g.dispose();
            ImageIO.write(image, "png", out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate PNG for ticket", e);
        } finally {
            g.dispose();
        }
    }

    private int field(Graphics2D g, int y, String label, String value) {
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString(label + ":", MARGIN, y);

        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g.drawString(truncate(value, g, WIDTH - VALUE_X - MARGIN), VALUE_X, y);

        return y + LINE_HEIGHT;
    }

    /** Cuts text with "..." so it doesn't run past the right edge of the ticket. */
    private String truncate(String text, Graphics2D g, int maxWidth) {
        if (text == null) {
            return "-";
        }
        var fm = g.getFontMetrics();
        if (fm.stringWidth(text) <= maxWidth) {
            return text;
        }
        String ellipsis = "...";
        int end = text.length();
        while (end > 0 && fm.stringWidth(text.substring(0, end) + ellipsis) > maxWidth) {
            end--;
        }
        return text.substring(0, end) + ellipsis;
    }
}