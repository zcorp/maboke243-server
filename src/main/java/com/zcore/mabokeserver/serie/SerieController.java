package com.zcore.mabokeserver.serie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.zcore.mabokeserver.common.Pagination;
import com.zcore.mabokeserver.common.filter.SerieFilter;

@RestController
@RequestMapping("/serie")
public class SerieController {
    @Autowired
    private SerieService serieService;
    private Logger logger = LoggerFactory.getLogger(SerieController.class);

    @GetMapping
    public Optional<JsonNode> getSerie(@RequestParam(defaultValue = "") String videoId) {
        return this.serieService.getSerieByVideoId(videoId);
    }

    @GetMapping(value = "/test")
    public List<JsonNode> getSeriesTest(@RequestParam Map<String, String> params) {
        SerieFilter filter = new SerieFilter();
        Pagination pagination = new Pagination();

        filter.mapToFilter(params);
        pagination.mapToPagination(params);

        return this.serieService.getSeriesByState(filter, pagination);
    }

    @GetMapping(value = "/search")
    public Map<String, List<Map<String, Object>>> getSeriesCategoryTest(@RequestParam Map<String, String> params) {
        SerieFilter filter = new SerieFilter();
        Pagination pagination = new Pagination();

        filter.mapToFilter(params);
        pagination.mapToPagination(params);

        return this.serieService.getSeriesByGroupe(filter, pagination);
    }
}
