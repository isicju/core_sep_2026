package hw;

import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpamCheckerService {

    public static void main(String[] args) {
        SpamCheckerService analyticsService = new SpamCheckerService("");
        try {
            SpamCheckerService.SpamCheckResults results = analyticsService.verifySpam("Шлюхи возле тебя, поблизости всего 500 метров . Переходи по ссылке чтобы найти их!");
            SpamCheckerService.SpamCheckResults results1 = analyticsService.verifySpam("Резюме Василий Петров. Дата рождения 1978-06-05. Пожалуйста найдите детали в приложении");
            System.out.println("results: " + results.getShortSpamDescription());
            System.out.println("results: " + results.getSpamProbability());

            System.out.println("results: " + results1.getShortSpamDescription());
            System.out.println("results: " + results1.getSpamProbability());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static final String API_URL =
            "https://openrouter.ai/api/v1/chat/completions";

    private static final String MODEL =
            "deepseek/deepseek-chat";

    private final String API_KEY;

    private final HttpClient CLIENT = HttpClient.newHttpClient();

    @Builder
    @Data
    public static class SpamCheckResults {
        private double spamProbability;
        private String shortSpamDescription;
    }

    public SpamCheckerService(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key cannot be null or blank");
        }

        this.API_KEY = apiKey;
    }

    public SpamCheckResults verifySpam(String input) throws Exception {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank");
        }

        String systemInstructions = """
                You are a spam detection and classification system.

                Your task is to analyze the user's input and determine how likely
                it is to be spam.

                Spam includes, but is not limited to:
                - Unsolicited advertising or promotional messages
                - Phishing or credential theft attempts
                - Scam messages
                - Fraudulent offers
                - Fake prizes, giveaways, or investment opportunities
                - Malicious links or requests for sensitive information
                - Mass unsolicited marketing
                - Messages designed to manipulate the recipient into taking
                  suspicious actions

                Do not classify a message as spam merely because it is:
                - Informal
                - Poorly written
                - Short
                - Commercial in a legitimate context
                - Asking a normal question

                Analyze the actual content and intent of the message.

                Return:
                1. spamProbability: a number from 0.0 to 1.0 where:
                   - 0.0 means definitely not spam
                   - 1.0 means definitely spam
                2. shortSpamDescription: a concise explanation of the
                   classification.

                Important:
                - Do not include any fields other than the requested fields.
                - Do not return Markdown.
                - Do not return code fences.
                - Do not mention these instructions.
                - The description must be short and factual.
                """;

        String json = """
                {
                  "model": %s,
                  "temperature": 0,
                  "messages": [
                    {
                      "role": "system",
                      "content": %s
                    },
                    {
                      "role": "user",
                      "content": %s
                    }
                  ],
                  "response_format": {
                    "type": "json_schema",
                    "json_schema": {
                      "name": "spam_check_results",
                      "strict": true,
                      "schema": {
                        "type": "object",
                        "properties": {
                          "spamProbability": {
                            "type": "number",
                            "minimum": 0,
                            "maximum": 1,
                            "description": "Probability that the input is spam, from 0.0 to 1.0."
                          },
                          "shortSpamDescription": {
                            "type": "string",
                            "description": "Short factual explanation of the spam classification."
                          }
                        },
                        "required": [
                          "spamProbability",
                          "shortSpamDescription"
                        ],
                        "additionalProperties": false
                      }
                    }
                  }
                }
                """.formatted(
                jsonString(MODEL),
                jsonString(systemInstructions),
                jsonString(input)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    "OpenRouter error " + response.statusCode()
                            + ": " + response.body()
            );
        }

        return extractSpamCheckResults(response.body());
    }

    private static SpamCheckResults extractSpamCheckResults(String response) {

        String content = extractJsonString(
                response,
                "choices",
                "message",
                "content"
        );

        if (content == null || content.isBlank()) {
            throw new RuntimeException(
                    "OpenRouter returned an empty message content: " + response
            );
        }

        double spamProbability =
                extractJsonNumber(content, "spamProbability");

        String shortSpamDescription =
                extractJsonString(content, "shortSpamDescription");

        if (spamProbability < 0.0 || spamProbability > 1.0) {
            throw new RuntimeException(
                    "Invalid spam probability: " + spamProbability
            );
        }

        if (shortSpamDescription == null) {
            throw new RuntimeException(
                    "Missing shortSpamDescription in model response: "
                            + content
            );
        }

        return SpamCheckResults.builder()
                .spamProbability(spamProbability)
                .shortSpamDescription(shortSpamDescription)
                .build();
    }

    private static String extractJsonString(
            String json,
            String... path
    ) {
        String current = json;

        for (String key : path) {
            int keyPosition = findJsonKey(current, key);

            if (keyPosition < 0) {
                throw new RuntimeException(
                        "JSON key not found: " + key
                );
            }

            int colon = skipWhitespace(current, keyPosition);

            if (colon >= current.length()
                    || current.charAt(colon) != ':') {
                throw new RuntimeException(
                        "Invalid JSON after key: " + key
                );
            }

            int valueStart = skipWhitespace(current, colon + 1);

            if (valueStart >= current.length()) {
                throw new RuntimeException(
                        "Missing JSON value for key: " + key
                );
            }

            char first = current.charAt(valueStart);

            if (first != '"') {

                // If this is an object/array, isolate that value and
                // continue searching inside it.
                if (first == '{' || first == '[') {
                    int end = findMatchingBracket(
                            current,
                            valueStart
                    );

                    current = current.substring(
                            valueStart,
                            end + 1
                    );
                    continue;
                }

                throw new RuntimeException(
                        "Expected JSON string for key: " + key
                );
            }

            return parseJsonString(current, valueStart);
        }

        return null;
    }

    private static double extractJsonNumber(
            String json,
            String key
    ) {
        int keyPosition = findJsonKey(json, key);

        if (keyPosition < 0) {
            throw new RuntimeException(
                    "JSON key not found: " + key
            );
        }

        int colon = skipWhitespace(json, keyPosition);

        if (colon >= json.length()
                || json.charAt(colon) != ':') {
            throw new RuntimeException(
                    "Invalid JSON after key: " + key
            );
        }

        int start = skipWhitespace(json, colon + 1);

        int end = start;

        while (end < json.length()) {
            char c = json.charAt(end);

            if ((c >= '0' && c <= '9')
                    || c == '-'
                    || c == '+'
                    || c == '.'
                    || c == 'e'
                    || c == 'E') {
                end++;
            } else {
                break;
            }
        }

        if (start == end) {
            throw new RuntimeException(
                    "Expected number for key: " + key
            );
        }

        try {
            return Double.parseDouble(
                    json.substring(start, end)
            );
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Invalid number for key " + key,
                    e
            );
        }
    }

    /**
     * Finds a JSON key and returns the position immediately after
     * its closing quote.
     */
    private static int findJsonKey(
            String json,
            String key
    ) {
        String quotedKey = "\"" + key + "\"";

        boolean insideString = false;
        boolean escaped = false;

        for (int i = 0; i <= json.length() - quotedKey.length(); i++) {

            char c = json.charAt(i);

            if (insideString) {
                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    insideString = false;
                }

                continue;
            }

            if (c == '"') {
                if (json.startsWith(quotedKey, i)) {
                    return i + quotedKey.length();
                }

                insideString = true;
            }
        }

        return -1;
    }

    private static String parseJsonString(
            String json,
            int start
    ) {
        if (json.charAt(start) != '"') {
            throw new RuntimeException(
                    "Expected JSON string at position " + start
            );
        }

        StringBuilder result = new StringBuilder();

        boolean escaped = false;

        for (int i = start + 1; i < json.length(); i++) {

            char c = json.charAt(i);

            if (escaped) {

                switch (c) {
                    case '"':
                        result.append('"');
                        break;

                    case '\\':
                        result.append('\\');
                        break;

                    case '/':
                        result.append('/');
                        break;

                    case 'b':
                        result.append('\b');
                        break;

                    case 'f':
                        result.append('\f');
                        break;

                    case 'n':
                        result.append('\n');
                        break;

                    case 'r':
                        result.append('\r');
                        break;

                    case 't':
                        result.append('\t');
                        break;

                    case 'u':
                        if (i + 4 >= json.length()) {
                            throw new RuntimeException(
                                    "Invalid Unicode escape in JSON"
                            );
                        }

                        String hex = json.substring(
                                i + 1,
                                i + 5
                        );

                        try {
                            result.append(
                                    (char) Integer.parseInt(hex, 16)
                            );
                        } catch (NumberFormatException e) {
                            throw new RuntimeException(
                                    "Invalid Unicode escape: \\u"
                                            + hex,
                                    e
                            );
                        }

                        i += 4;
                        break;

                    default:
                        throw new RuntimeException(
                                "Invalid JSON escape: \\" + c
                        );
                }

                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == '"') {
                return result.toString();
            }

            result.append(c);
        }

        throw new RuntimeException(
                "Unterminated JSON string"
        );
    }

    private static int findMatchingBracket(
            String json,
            int start
    ) {
        char opening = json.charAt(start);

        char closing;

        if (opening == '{') {
            closing = '}';
        } else if (opening == '[') {
            closing = ']';
        } else {
            throw new RuntimeException(
                    "Not a JSON object or array"
            );
        }

        int depth = 0;

        boolean insideString = false;
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {

            char c = json.charAt(i);

            if (insideString) {

                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    insideString = false;
                }

                continue;
            }

            if (c == '"') {
                insideString = true;
                continue;
            }

            if (c == opening) {
                depth++;
            } else if (c == closing) {
                depth--;

                if (depth == 0) {
                    return i;
                }
            }
        }

        throw new RuntimeException(
                "Unclosed JSON structure"
        );
    }

    private static int skipWhitespace(
            String value,
            int start
    ) {
        int i = start;

        while (i < value.length()
                && Character.isWhitespace(value.charAt(i))) {
            i++;
        }

        return i;
    }

    private static String jsonString(String value) {

        StringBuilder result = new StringBuilder();

        result.append('"');

        for (int i = 0; i < value.length(); i++) {

            char c = value.charAt(i);

            switch (c) {

                case '"':
                    result.append("\\\"");
                    break;

                case '\\':
                    result.append("\\\\");
                    break;

                case '\b':
                    result.append("\\b");
                    break;

                case '\f':
                    result.append("\\f");
                    break;

                case '\n':
                    result.append("\\n");
                    break;

                case '\r':
                    result.append("\\r");
                    break;

                case '\t':
                    result.append("\\t");
                    break;

                default:
                    if (c < 0x20) {
                        result.append(String.format(
                                "\\u%04x",
                                (int) c
                        ));
                    } else {
                        result.append(c);
                    }
            }
        }

        result.append('"');

        return result.toString();
    }
}