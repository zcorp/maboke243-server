package com.zcore.mabokeserver.common.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceFilter {
    private String title;
    private List<String> origins;
    private String defaultOrigins;

    public void scan() {
        origins = origins == null ? new ArrayList<>() : origins;
        defaultOrigins = origins.size() == 0 ? ".*" : "^[^ ]$";// "^$";
        title = title != null && !title.equals("") ? ".*" + title.replaceAll(" ", ".*") + ".*" : ".*";
    }

    public void mapToFilter(Map<String, String> params) {
        String separator = ",";
        String orignStr = params.get("origins");

        title = params.get("title");

        if (orignStr != null && !orignStr.equals(""))
            origins = Arrays.asList(orignStr.split(separator));

        scan();
    }
}
