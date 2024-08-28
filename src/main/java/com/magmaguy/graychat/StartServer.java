package com.magmaguy.graychat;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StartServer {

    private static HttpServer server;

    public static void start() throws IOException {
        // Create an HTTP server listening on port 5000
        server = HttpServer.create(new InetSocketAddress(5000), 0);

        // Define a handler for the /analyze endpoint
        server.createContext("/analyze", new ChatHandler());

        // Start the server
        server.setExecutor(null); // creates a default executor
//        FastLogger.logStatic("Server is running on http://127.0.0.1:5000");
        server.start();
    }

    public static void shutdown() {
        server.stop(0);
    }

    static class ChatHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
//            FastLogger.logStatic("Handling!");

            // Handle CORS preflight request
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                FastLogger.logStatic("Options!");
                handleCors(exchange);
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
//                FastLogger.logStatic("Post!");

                // Read the request body
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
//                FastLogger.logStatic("Request Body: " + requestBody);

                // Initialize the data map
                JsonObject data = null;

                // Parse the JSON to extract messages using Rson
                try {
                    data = Rson.DEFAULT.fromJson(requestBody, JsonObject.class);
//                    FastLogger.logStatic("Parsed JSON successfully.");
                } catch (Exception e) {
//                    FastLogger.logStatic("Failed to parse JSON: " + e.getMessage());
                    exchange.sendResponseHeaders(400, -1); // Respond with 400 Bad Request
                    return;
                }

                // Safeguard message extraction
                List<JsonObject> messages = new ArrayList<>();
                try {
                    if (data != null && data.containsKey("messages")) {
                        JsonElement rawMessages = data.get("messages");
                        if (rawMessages.isJsonArray()) {
                            JsonArray jsonArray = rawMessages.getAsArray();
                            for (JsonElement element : jsonArray) {
                                if (element.isJsonObject()) {
                                    messages.add(element.getAsObject());
                                }
                            }
                        } else {
                            throw new IllegalArgumentException("Expected 'messages' to be a JsonArray.");
                        }
                    } else {
                        throw new IllegalArgumentException("'messages' key not found in the JSON.");
                    }
//                    FastLogger.logStatic("Extracted messages successfully. Message count: " + messages.size());
                } catch (Exception e) {
//                    FastLogger.logStatic("Failed to extract messages: " + e.getMessage());
                    exchange.sendResponseHeaders(500, -1); // Respond with 500 Internal Server Error
                    return;
                }

                // Log the messages and send them to CaffeinatedChatInjector
                for (JsonObject messageObj : messages) {
                    String message = messageObj.getString("message");
                    String author = messageObj.getString("author");
//                    FastLogger.logStatic("Message: " + message + " | Author: " + author);
                    try {
                        CaffeinatedChatInjector.sendMessage(message, author);
                    } catch (Exception e) {
//                        FastLogger.logStatic("Failed to send message: " + e.getMessage());
                    }
                }

                // Send an empty response with CORS headers
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");  // Allow all origins
                exchange.sendResponseHeaders(200, -1); // Respond with 200 OK and no body
            } else {
                // Respond with 405 Method Not Allowed if not a POST request
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private void handleCors(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);  // No content for preflight response
        }
    }
}
