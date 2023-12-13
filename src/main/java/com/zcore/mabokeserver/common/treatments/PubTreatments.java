package com.zcore.mabokeserver.common.treatments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;

public class PubTreatments {
    private List<JsonNode> pubs;
    private static final int PAGE_SIZE = 12;
    private Logger logger = LoggerFactory.getLogger(PubTreatments.class);

    public PubTreatments(List<JsonNode> pubs) {
        this.pubs = pubs;
    }

    public Map<String, JsonNode> getPub(String page, boolean isUse) {
        Map<String, JsonNode> pubRes = new HashMap<>();
        List<JsonNode> filteredPubs = pubs.stream()
                .filter(pub -> {
                    String page2;
                    boolean isUse2;
                    boolean res = false;

                    if (pub.get("page").isTextual() && pub.get("isUse").isBoolean()) {
                        page2 = pub.get("page").asText();
                        isUse2 = pub.get("isUse").asBoolean();
                        res = page.equals(page2) && isUse == isUse2;
                    }

                    return res;
                })
                .collect(Collectors.toList());

        if (0 < filteredPubs.size())
            pubRes.put("pub", filteredPubs.get(0));

        return pubRes;
    }
}
