package com.hunghq.librarymanagement.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class CallAPIService {

    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * Get image URL from document title.
     *
     * @param title document title
     * @return image URL string
     */
    public String getImageUrlFromTitle(String title) {
        title = title.replace(" ", "+");
        String url = GOOGLE_BOOKS_API + title;
        return fetchImageUrl(url);
    }

    /**
     * Fetch image URL from the Google Books API.
     *
     * @param url API URL
     * @return image URL or null if not found
     */
    private String fetchImageUrl(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (jsonResponse.has("items")) {
                for (int i = 0; i < jsonResponse.getJSONArray("items").length(); i++) {
                    JSONObject item = jsonResponse.getJSONArray("items").getJSONObject(i);
                    if (item.has("volumeInfo") && item.getJSONObject("volumeInfo").has("imageLinks")) {
                        JSONObject imageLinks = item.getJSONObject("volumeInfo").getJSONObject("imageLinks");
                        if (imageLinks.has("thumbnail")) {
                            return imageLinks.getString("thumbnail");
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during API call: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return null;
    }


    public static void main(String[] args) {
        CallAPIService callAPIService = new CallAPIService();

        String title = "The Guernsey Literary and Potato Peel Pie Society";
        String imageUrl = callAPIService.getImageUrlFromTitle(title);

        if (imageUrl != null) {
            System.out.println("Image URL: " + imageUrl);
        } else {
            System.out.println("No image found for the book: " + title);
        }
    }

}
