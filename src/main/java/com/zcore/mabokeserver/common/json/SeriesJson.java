package com.zcore.mabokeserver.common.json;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SeriesJson {
    private static final int PAGE_SIZE = 10;

    private List<JsonNode> series;

    public SeriesJson(String jsonFilePath) {
        try {
            String root = new File("").getAbsolutePath();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(root + jsonFilePath));
            this.series = jsonNode.findValues("product");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<JsonNode> getSeries(int page, double minPrice, double maxPrice) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(page * PAGE_SIZE, series.size());

        List<JsonNode> filteredSeries = series.stream()
                .filter(product -> {
                    double price = product.get("price").asDouble();
                    return price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());

        return filteredSeries.subList(startIndex, Math.min(endIndex, filteredSeries.size()));
    }

    public static void main(String[] args) {
        String jsonFilePath = "chemin/vers/votre/fichier.json";
        SeriesJson series = new SeriesJson(jsonFilePath);

        int pageNumber = 1;
        double minPrice = 20.0;
        double maxPrice = 50.0;
        List<JsonNode> filteredProductsPage = series.getSeries(pageNumber, minPrice, maxPrice);

        // Exemple d'utilisation de la page de produits filtrée
        for (JsonNode serie : filteredProductsPage) {
            System.out.println("Product: " + serie);
        }
    }
}
