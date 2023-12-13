package com.zcore.mabokeserver.pub;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zcore.mabokeserver.common.Pagination;
import com.zcore.mabokeserver.common.filter.PubFilter;
import com.zcore.mabokeserver.common.treatments.PubTreatments;

@Service
public class PubService {
    private final List<JsonNode> pubs;

    @Autowired
    public PubService(List<JsonNode> pubs) {
        this.pubs = pubs;
    }

    private Logger logger = LoggerFactory.getLogger(PubService.class);

    public Map<String, JsonNode> getPubJson(PubFilter filter, Pagination pagination) {
        PubTreatments treatments = new PubTreatments(pubs);
        String page = filter.getPage();
        boolean isUse = filter.getIsUse();
        return treatments.getPub(page, isUse);
    }
}
