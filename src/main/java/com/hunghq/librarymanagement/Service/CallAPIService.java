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
    public String fetchImageUrl(String url) {
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

    //TODO: Write get infoLink from GoogleBook API, then use the dub API
    //Structure for dub API: https://api.dub.co/qr?url= + {link}
    public String getQR(String title) {
        title = title.replace(" ", "+");
        String url = GOOGLE_BOOKS_API + title;
        return fetchInfoLink(url);
    }
    public String fetchInfoLink(String url) {
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
                    if (item.has("volumeInfo") && item.getJSONObject("volumeInfo").has("infoLink")) {
                        return item.getJSONObject("volumeInfo").getString("infoLink");
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

    public String generateQRUrl(String infoLink) {
        if (infoLink == null || infoLink.isEmpty()) {
            System.err.println("Invalid infoLink for QR generation.");
            return null;
        }

        String dubApiUrl = "https://api.dub.co/qr?url=" + infoLink;
        return dubApiUrl;
    }


}
