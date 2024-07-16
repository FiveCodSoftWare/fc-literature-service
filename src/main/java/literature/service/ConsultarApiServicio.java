package literature.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConsultarApiServicio {

    public String requestData(String url){
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(httpRequest, BodyHandlers.ofString());

        try {
            HttpResponse<String> response = future.get();
            return response.body();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}