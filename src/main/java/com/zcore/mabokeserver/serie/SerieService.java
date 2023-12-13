package com.zcore.mabokeserver.serie;

import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zcore.mabokeserver.common.Pagination;
import com.zcore.mabokeserver.common.filter.SerieFilter;
import com.zcore.mabokeserver.common.treatments.SerieTreatments;

@Service
public class SerieService {
    private final List<JsonNode> series;

    @Autowired
    public SerieService(List<JsonNode> series) {
        this.series = series;
    }

    private Logger logger = LoggerFactory.getLogger(SerieService.class);

    public Optional<JsonNode> getSerieByVideoId(String videoId) {
        return series.stream()
                .filter(serie -> String.valueOf(serie.get("img").asText()).equals(videoId))
                .findFirst();
    }

    public List<JsonNode> getSeriesByState(SerieFilter filter, Pagination pagination) {
        int page = pagination.getPageNumber();
        String state = filter.getStates().get(0);
        SerieTreatments treatments = new SerieTreatments(series);
        return treatments.getSeries(page, state);
    }

    public Map<String, List<Map<String, Object>>> getSeriesByGroupe(SerieFilter filter, Pagination pagination) {
        String type = filter.getType();
        Map<String, List<Map<String, Object>>> categories;
        SerieTreatments treatments = new SerieTreatments(series);
        // logger.info("Filter : " + filter);
        if (type.equals("serie attr"))
            categories = treatments.getSeriesAttr(filter, pagination);
        else
            categories = treatments.grouperByCategory(filter, pagination);

        return categories;
    }
}
