package com.zcore.mabokeserver.pub;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.zcore.mabokeserver.common.Pagination;
import com.zcore.mabokeserver.common.filter.PubFilter;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/pub")
@ApiIgnore
public class PubController {
    @Autowired
    private PubService service;

    private Logger logger = LoggerFactory.getLogger(PubController.class);

    @GetMapping
    public Map<String, JsonNode> getPubsJson(@RequestParam Map<String, String> params) {
        PubFilter filter = new PubFilter();
        Pagination pagination = new Pagination();

        filter.mapToFilter(params);
        pagination.mapToPagination(params);
        return this.service.getPubJson(filter, pagination);
    }
}
