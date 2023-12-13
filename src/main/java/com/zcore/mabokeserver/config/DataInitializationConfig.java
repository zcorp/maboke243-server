package com.zcore.mabokeserver.config;

import java.io.File;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcore.mabokeserver.serie.SerieService;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataInitializationConfig {
    @Value("${json.pub.path}")
    private String pubFilePath;
    @Value("${json.serie.path}")
    private String serieFilePath;

    @Value("${json.gvideo.path}")
    private String gvideoFilePath;

    private Logger logger = LoggerFactory.getLogger(SerieService.class);

    private List<JsonNode> pubs;
    private List<JsonNode> series;
    private List<JsonNode> gvideos;

    public static List<JsonNode> convertToList(JsonNode jsonNode) {
        Iterator<JsonNode> elements;
        List<JsonNode> jsonNodeList = new ArrayList<>();

        if (jsonNode.isArray()) {
            elements = jsonNode.elements();

            while (elements.hasNext()) {
                JsonNode element = elements.next();
                jsonNodeList.add(element);
            }
        }
        return jsonNodeList;
    }

    public List<JsonNode> initElements(String filePath) throws IOException {
        String root = new File("").getAbsolutePath();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(root + filePath));
        return convertToList(jsonNode);
    }

    @PostConstruct
    public void initializeData() {
        try {
            this.pubs = this.initElements(pubFilePath);
            this.series = this.initElements(serieFilePath);
            this.gvideos = this.initElements(gvideoFilePath);
            logger.info("Size pubs : " + this.pubs.size());
            logger.info("Size series : " + this.series.size());
            logger.info("Size gvideos : " + this.gvideos.size());
            logger.info("Size series : " + this.series.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean(name = "series")
    public List<JsonNode> getSeries() {
        return series;
    }

    @Bean(name = "gvideos")
    public List<JsonNode> getGvideos() {
        return gvideos;
    }

    @Bean(name = "pubs")
    public List<JsonNode> getPubs() {
        return pubs;
    }
}
