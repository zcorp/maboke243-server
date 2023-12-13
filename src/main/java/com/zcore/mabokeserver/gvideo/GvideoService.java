package com.zcore.mabokeserver.gvideo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zcore.mabokeserver.common.Pagination;
import com.zcore.mabokeserver.common.filter.GvideoFilter;
import com.zcore.mabokeserver.common.treatments.GvideoTreatments;

@Service
public class GvideoService {
    private final List<JsonNode> gvideos;
    private Logger logger = LoggerFactory.getLogger(GvideoService.class);

    @Autowired
    public GvideoService(List<JsonNode> gvideos) {
        this.gvideos = gvideos;
    }

    public Optional<JsonNode> getVideoById(String videoId) {
        return gvideos.stream()
                .filter(serie -> String.valueOf(serie.get("img").asText()).equals(videoId))
                .findFirst();
    }

    public List<JsonNode> getGvideosByGenre(GvideoFilter filter, Pagination pagination) {
        int page = pagination.getPageNumber();
        String gender = filter.getGenres().get(0);
        GvideoTreatments treatments = new GvideoTreatments(gvideos);
        return treatments.getGvideos(page, gender);
    }

    public Map<String, List<Map<String, Object>>> getGvideosByGroupe(GvideoFilter filter, Pagination pagination) {
        String type = filter.getType();
        GvideoTreatments treatments = new GvideoTreatments(gvideos);
        Map<String, List<Map<String, Object>>> categories;

        if (type.equals("gvideo attr"))
            categories = treatments.getGvideosAttr(filter, pagination);
        else
            categories = treatments.grouperByCategory(filter, pagination);

        return categories;
    }
}
