package hw.service.image;

import hw.exception.ImageGeneratorProcessingError;
import hw.model.Ticket;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeFormatter;

public class PdfGenerator implements ImageGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final float MARGIN = 50f;
    private static final float LINE_HEIGHT = 22f;

    @Override
    public byte[] generateImage(Ticket ticket, int cost) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A5);
            document.addPage(page);

            PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font regular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            float width = page.getMediaBox().getWidth();
            float height = page.getMediaBox().getHeight();
            float y = height - MARGIN;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                // Border
                cs.setLineWidth(1.5f);
                cs.addRect(20, 20, width - 40, height - 40);
                cs.stroke();

                // Title
                y = writeLine(cs, bold, 20, MARGIN, y, "TICKET") - 10;

                // Separator
                cs.moveTo(MARGIN, y + 8);
                cs.lineTo(width - MARGIN, y + 8);
                cs.stroke();
                y -= 6;

                // Fields
                y = field(cs, bold, regular, y, "Name", ticket.getName());
                y = field(cs, bold, regular, y, "Surname", ticket.getSurname());
                y = field(cs, bold, regular, y, "Date of birth",
                        ticket.getDob() != null ? ticket.getDob().format(DATE_FORMAT) : "-");
                y = field(cs, bold, regular, y, "Ticket type",
                        ticket.getTicketType() != null ? String.valueOf(ticket.getTicketType()) : "-");

                String luggage = ticket.isHasLuggage()
                        ? "Yes (" + ticket.getLuggage() + ")"
                        : "No";
                y = field(cs, bold, regular, y, "Luggage", luggage);

                if (ticket.getComment() != null && !ticket.getComment().isBlank()) {
                    y = field(cs, bold, regular, y, "Comment", ticket.getComment());
                }

                // Cost
                y -= 10;
                writeLine(cs, bold, 16, MARGIN, y, String.format("Total cost: %d", cost));
            }

            document.save(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new ImageGeneratorProcessingError("Failed to generate PDF for ticket" + e.getMessage());
        }
    }

    private float field(PDPageContentStream cs, PDType1Font labelFont, PDType1Font valueFont,
                        float y, String label, String value) throws IOException {
        cs.beginText();
        cs.setFont(labelFont, 12);
        cs.newLineAtOffset(MARGIN, y);
        cs.showText(label + ": ");
        cs.endText();

        cs.beginText();
        cs.setFont(valueFont, 12);
        cs.newLineAtOffset(MARGIN + 100, y);
        cs.showText(sanitize(value));
        cs.endText();

        return y - LINE_HEIGHT;
    }

    private float writeLine(PDPageContentStream cs, PDType1Font font, float size,
                            float x, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, y);
        cs.showText(sanitize(text));
        cs.endText();
        return y - LINE_HEIGHT;
    }

    /** Standard 14 fonts only support WinAnsi; drop control chars and replace unsupported ones. */
    private String sanitize(String text) {
        if (text == null) {
            return "-";
        }
        return text.replaceAll("[\\r\\n\\t]+", " ")
                .replaceAll("[^\\x20-\\x7E\\u00A0-\\u00FF]", "?");
    }
}