package hw;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import hw.exception.ExceptionRoute;
import hw.exception.ExceptionRouter;
import hw.model.ImageOutputType;
import hw.model.TicketFactory;
import hw.service.TickerValidator;
import hw.service.TicketCalculator;
import hw.service.image.ImageGeneratorLookup;
import hw.service.image.PdfGenerator;
import hw.service.image.PngGenerator;
import hw.service.spam.DeepSeekSpamService;
import hw.service.spam.SpamService;
import hw.service.spam.SpamServiceLoggerProxy;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Server {

    static RequestProcessor requestProcessor;
    static ExceptionRouter router;

    public static void main(String[] args) throws Exception {
        log.info("Staring application..");

        String spamApiKey = System.getenv().get("KEY");
        String port = System.getenv().get("PORT") == null ? "8080" : System.getenv().get("PORT");
        Integer appPort = Integer.parseInt(port);
        if (spamApiKey == null) {
            System.err.println("Missing environment variable: KEY");
            System.exit(1);
        }

        TicketFactory ticketFactory = new TicketFactory();
        SpamService service = new SpamServiceLoggerProxy(new DeepSeekSpamService(spamApiKey));
        TickerValidator tickerValidator = new TickerValidator(service);
        TicketCalculator ticketCalculator = new TicketCalculator();

        ImageGeneratorLookup imageGeneratorFactory = new ImageGeneratorLookup(Map.of(
                ImageOutputType.PDF, new PdfGenerator(),
                ImageOutputType.PNG, new PngGenerator()
        ));

        requestProcessor = new RequestProcessor(ticketFactory, tickerValidator, ticketCalculator, imageGeneratorFactory);

        router = new ExceptionRouter();

        HttpServer server = HttpServer.create(new InetSocketAddress(appPort), 0);

        server.createContext("/api/generate", Server::handleGenerate);
        server.createContext("/", Server::handleStatic);

        server.setExecutor(null);
        server.start();

        log.info("Server started: http://:" + InetAddress.getLocalHost().getHostAddress() + ":" + appPort + "/");
    }

    private static void handleGenerate(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed");
            return;
        }

        try {
            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );
            log.info("received new request: {}", body);

            Map<String, String> form = parseForm(body);

            String fileType = form.get("file_type");

            byte[] responseFileBytes = requestProcessor.processRequest(form);

            String fileName = "result." + fileType;
            String contentType = "pdf".equals(fileType)
                    ? "application/pdf"
                    : "image/png";

            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", contentType);
            headers.set(
                    "Content-Disposition",
                    "attachment; filename=\"" + fileName + "\""
            );
            headers.set("Content-Length", String.valueOf(responseFileBytes.length));

            exchange.sendResponseHeaders(200, responseFileBytes.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(responseFileBytes);
            }

        } catch (Exception e) {
            log.error("exception occurred: {}", e.getMessage());
            ExceptionRoute route = router.route(e);
            sendText(exchange, route.getCode(), route.getMessage());
        }
    }

    private static void handleStatic(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed");
            return;
        }

        String path = exchange.getRequestURI().getPath();

        if (!"/".equals(path) && !"/index.html".equals(path)) {
            sendText(exchange, 404, "Not Found");
            return;
        }

        try (InputStream input = Server.class.getClassLoader()
                .getResourceAsStream("index.html")) {

            if (input == null) {
                sendText(exchange, 404, "index.html not found in resources");
                return;
            }

            byte[] content = input.readAllBytes();

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    "text/html; charset=UTF-8"
            );

            exchange.sendResponseHeaders(200, content.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(content);
            }
        }
    }

    private static Map<String, String> parseForm(String body) {
        Map<String, String> result = new HashMap<>();

        if (body == null || body.isEmpty()) {
            return result;
        }

        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);

            String key = URLDecoder.decode(
                    parts[0],
                    StandardCharsets.UTF_8
            );

            String value = parts.length > 1
                    ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                    : "";

            result.put(key, value);
        }

        return result;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static void sendText(
            HttpExchange exchange,
            int status,
            String text
    ) throws IOException {

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain; charset=UTF-8"
        );

        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}