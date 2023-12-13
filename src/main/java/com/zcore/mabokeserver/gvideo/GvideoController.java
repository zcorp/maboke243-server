package com.zcore.mabokeserver.gvideo;

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
import com.zcore.mabokeserver.common.filter.GvideoFilter;

@RestController
@RequestMapping("/gvideo")
public class GvideoController {
    @Autowired
    private GvideoService gvideoService;

    private Logger logger = LoggerFactory.getLogger(GvideoController.class);

    @GetMapping
    public Optional<JsonNode> getVideo(@RequestParam(defaultValue = "") String videoId) {
        return this.gvideoService.getVideoById(videoId);
    }

    @GetMapping(value = "/test")
    public List<JsonNode> getSeriesTest(@RequestParam Map<String, String> params) {
        GvideoFilter filter = new GvideoFilter();
        Pagination pagination = new Pagination();

        filter.mapToFilter(params);
        pagination.mapToPagination(params);
        return this.gvideoService.getGvideosByGenre(filter, pagination);
    }

    @GetMapping(value = "/search")
    public Map<String, List<Map<String, Object>>> getSeriesCategoryTest(@RequestParam Map<String, String> params) {
        GvideoFilter filter = new GvideoFilter();
        Pagination pagination = new Pagination();

        filter.mapToFilter(params);
        pagination.mapToPagination(params);

        return this.gvideoService.getGvideosByGroupe(filter, pagination);
    }
}
