// package com.SnapSeek.integration;
// import java.io.IOException;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;

// public class ApiService {

//     private static final String BASE_URL = "http://127.0.0.1:8000";
//     private final HttpClient client;

//     public ApiService() {
//         this.client = HttpClient.newHttpClient();
//     }

//     public String generateCaption(String imagePath, String tone) throws IOException, InterruptedException {
//         String safeImagePath = imagePath
//                 .replace("\\", "\\\\")
//                 .replace("\"", "\\\"");
//         String safeTone = tone == null ? "" : tone.replace("\"", "\\\"");

//         String jsonBody = """
//                 {
//                   "image_path": "%s",
//                   "tone": "%s"
//                 }
//                 """.formatted(safeImagePath, safeTone);

//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(BASE_URL + "/caption"))
//                 .header("Content-Type", "application/json")
//                 .header("Accept", "application/json")
//                 .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//                 .build();

//         HttpResponse<String> response =
//                 client.send(request, HttpResponse.BodyHandlers.ofString());

//         if (response.statusCode() != 200) {
//             throw new IOException("Caption API error: " + response.body());
//         }

//         return response.body();
//     }

//     public String searchImages(String query) throws IOException, InterruptedException {
//         String safeQuery = query.replace("\"", "\\\"");

//         String jsonBody = """
//                 {
//                   "text_query": "%s"
//                 }
//                 """.formatted(safeQuery);

//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(BASE_URL + "/search"))
//                 .header("Content-Type", "application/json")
//                 .header("Accept", "application/json")
//                 .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//                 .build();

//         HttpResponse<String> response =
//                 client.send(request, HttpResponse.BodyHandlers.ofString());

//         if (response.statusCode() != 200) {
//             throw new IOException("Search API error: " + response.body());
//         }

//         return response.body();
//     }

//     public String checkHealth() throws IOException, InterruptedException {
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(BASE_URL + "/health"))
//                 .header("Accept", "application/json")
//                 .GET()
//                 .build();

//         HttpResponse<String> response =
//                 client.send(request, HttpResponse.BodyHandlers.ofString());

//         if (response.statusCode() != 200) {
//             throw new IOException("Backend health check failed: " + response.body());
//         }

//         return response.body();
//     }
// }

package com.SnapSeek.integration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {

    private static final String BASE_URL = "http://127.0.0.1:8000";
    private final HttpClient client;

    public ApiService() {
        this.client = HttpClient.newHttpClient();
    }

    public String generateCaption(String imagePath, String tone, String folderPath) throws IOException, InterruptedException {
        String safeImagePath = imagePath
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

        String safeTone = tone == null ? "" : tone.replace("\"", "\\\"");

        String jsonBody = """
                {
                  "image_path": "%s",
                  "tone": "%s"
                }
                """.formatted(safeImagePath, safeTone);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/caption"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Caption API error: " + response.body());
        }

        return response.body();
    }

//     public String searchImages(String query) throws IOException, InterruptedException {
//         String safeQuery = query.replace("\"", "\\\"");

//         String jsonBody = """
//                 {
//                   "text_query": "%s"
//                 }
//                 """.formatted(safeQuery);

//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(BASE_URL + "/search"))
//                 .header("Content-Type", "application/json")
//                 .header("Accept", "application/json")
//                 .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
//                 .build();

//         HttpResponse<String> response =
//                 client.send(request, HttpResponse.BodyHandlers.ofString());

//         if (response.statusCode() != 200) {
//             throw new IOException("Search API error: " + response.body());
//         }

//         return response.body();
//     }

    public String searchImages(String query, String folderPath) throws IOException, InterruptedException {
        // String safeQuery = query.replace("\\", "\\\\").replace("\"", "\\\"");
        // String safeFolderPath = folderPath.replace("\\", "\\\\").replace("\"", "\\\"");

        // String jsonBody = """
        //         {
        //         "text_query": "%s",
        //         "folder_path": "%s"
        //         }
        //         """.formatted(safeQuery, safeFolderPath);

         String safeQuery = query.replace("\\", "\\\\").replace("\"", "\\\"");
        String safeFolderPath = folderPath.replace("\\", "\\\\").replace("\"", "\\\"");

        String jsonBody = """
            {
              "text_query": "%s",
              "folder_path": "%s"
            }
            """.formatted(safeQuery, safeFolderPath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/search"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
                throw new IOException("Search API error: " + response.body());
        }

        return response.body();
}

    public String checkHealth() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/health"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Backend health check failed: " + response.body());
        }

        return response.body();
    }
}
