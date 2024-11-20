package com.hunghq.librarymanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AskAIController {

    @FXML
    private TextField questionInput;

    @FXML
    private TextArea responseArea;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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"; // Thay bằng API Key của bạn

    @FXML
    public void handleSend() {
        String question = questionInput.getText();
        if (question.isEmpty()) {
            responseArea.setText("Please enter a question.");
            return;
        }

        try {
            // Gửi câu hỏi đến API ChatGPT
            String response = sendToChatGPT(question);
            responseArea.setText(response);
        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
        }
    }

    private String sendToChatGPT(String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // JSON body gửi đến API
        String requestBody = """
                {
                    "model": "gpt-4",
                    "messages": [{"role": "user", "content": "%s"}],
                    "max_tokens": 200
                }
                """.formatted(prompt);

        // Tạo request đến API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Nhận response từ API
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON response để lấy câu trả lời
        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        return jsonResponse
                .getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .get("message")
                .getAsJsonObject()
                .get("content")
                .getAsString();
    }
}
