package com.hunghq.librarymanagement.Controller.AskAI;

import com.hunghq.librarymanagement.Controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AskAIController extends BaseController {

    @FXML
    private TextField questionInput;

    @FXML
    private TextArea responseArea;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "API_KEY";

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
                "model": "gpt-3.5-turbo",
                "messages": [{"role": "user", "content": "%s"}],
                "max_tokens": 500
            }\
            """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // In phản hồi JSON từ API để kiểm tra
        System.out.println("Response from API: " + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        // Kiểm tra trường hợp lỗi
        if (jsonResponse.has("error")) {
            JsonObject error = jsonResponse.getAsJsonObject("error");
            return "Error: " + error.get("message").getAsString();
        }

        // Kiểm tra và xử lý trường hợp không có "choices"
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
                .getAsString();
    }
}
