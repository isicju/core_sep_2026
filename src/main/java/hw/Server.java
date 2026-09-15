package hw;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/generate", Server::handleGenerate);
        server.createContext("/", Server::handleStatic);

        server.setExecutor(null);
        server.start();

        System.out.println("Server started: http://localhost:8080");
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
            Map<String, String> form = parseForm(body);
            String type = form.get("type");
            String name = form.get("name");
            String surname = form.get("surname");
            String dob = form.get("dob");
            String comment = form.get("comment");
            String hasLuggage = form.get("has_luggage");
            String luggage = form.get("luggage");
            String fileType = form.get("file_type");

            RequestProcessor requestProcessor = new RequestProcessor();

            byte[] responseFileBytes = requestProcessor.processRequest(name,
                    surname,
                    dob,
                    comment,
                    hasLuggage,
                    luggage,
                    type,
                    fileType);

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
            sendText(exchange, 500, e.getMessage());
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