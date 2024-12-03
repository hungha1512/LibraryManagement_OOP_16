package com.hunghq.librarymanagement.Controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hunghq.librarymanagement.Model.Entity.Document;
import com.hunghq.librarymanagement.Respository.DocumentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class AskAIController extends BaseController {

    @FXML
    private TextField questionInput;

    @FXML
    private TextArea responseArea;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "";

    private DocumentDAO documentDAO = new DocumentDAO();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionInput.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("Enter key is pressed.");
                handleSend();
            }
        });
    }

    @FXML
    public void handleSend() {
        String question = questionInput.getText().trim();
        if (question.isEmpty()) {
            responseArea.setText("Please enter a question.");
            return;
        }

        try {
            String extractedKeywords = extractKeywordsWithChatGPT(question);

            if (extractedKeywords.isEmpty()) {
                responseArea.setText("Sorry, I couldn't extract any relevant keywords from your question.");
                return;
            }
            String[] keywordArray = extractedKeywords.split(",");
            String keyword = keywordArray[0].trim();

            String searchResponse = searchLibraryDatabase(keyword);
            System.out.println("Search Response: " + searchResponse);

            if (!searchResponse.isEmpty()) {
                String detailedResponse = generateDetailedResponse(keyword);
                responseArea.setText(detailedResponse);
            } else {
                responseArea.setText("Sorry, no relevant information found in the library database.");
            }

        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
        }
    }

    private String generateDetailedResponse(String searchResponse) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String prompt = """
                I have the following information related to books from my database: "%s".
                Can you provide a detailed, conversational response to help explain this information to someone in a simple way?
                """.formatted(searchResponse);

        String requestBody = String.format("""
                {
                    "model": "gpt-3.5-turbo",
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "max_tokens": 500
                }
                """, escapeJsonString(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response from API: " + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        if (jsonResponse.has("error")) {
            JsonObject error = jsonResponse.getAsJsonObject("error");
            return "Error: " + error.get("message").getAsString();
        }

        if (!jsonResponse.has("choices") || jsonResponse.getAsJsonArray("choices").size() == 0) {
            return "Error: API response does not contain valid choices.";
        }

        return jsonResponse
                .getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .get("message")
                .getAsJsonObject()
                .get("content")
                .getAsString()
                .trim();
    }

    private String extractKeywordsWithChatGPT(String question) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String prompt = """
                Please extract the main keywords related to books, authors, and genres from the following query: "%s".
                Return only the keywords, separated by commas.
                """.formatted(question);

        String requestBody = String.format("""
                {
                    "model": "gpt-3.5-turbo",
                    "messages": [
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "max_tokens": 500
                }
                """, escapeJsonString(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response from API: " + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        if (jsonResponse.has("error")) {
            JsonObject error = jsonResponse.getAsJsonObject("error");
            return "Error: " + error.get("message").getAsString();
        }

        if (!jsonResponse.has("choices") || jsonResponse.getAsJsonArray("choices").size() == 0) {
            return "";
        }

        return jsonResponse
                .getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .get("message")
                .getAsJsonObject()
                .get("content")
                .getAsString()
                .trim();
    }

    private String escapeJsonString(String str) {
        return str.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String searchLibraryDatabase(String keyword) {
        ObservableList<Document> searchResults = FXCollections.observableArrayList();

        if (!keyword.isEmpty()) {
            ObservableList<Document> byTitle = documentDAO.findByName(keyword);
            ObservableList<Document> byAuthor = documentDAO.searchByAuthor(keyword);
            ObservableList<Document> byGenre = documentDAO.searchByGenre(keyword);

            searchResults.addAll(byTitle);
            searchResults.addAll(byAuthor);
            searchResults.addAll(byGenre);
        }

        if (searchResults.isEmpty()) {
            return "";
        }

        StringBuilder response = new StringBuilder("Library Results:\n");
        for (Document doc : searchResults) {
            response.append("Title: ").append(doc.getTitle())
                    .append(", Author: ").append(doc.getAuthor())
                    .append(", Genre: ").append(doc.getGenre()).append("\n");
        }
        return response.toString();
    }
}
