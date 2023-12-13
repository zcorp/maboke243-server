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
public class PubFilter {
    private String type;
    private String page;
    private String title;
    private Boolean isUse;
    private List<String> pages;
    private String defaultPages;

    public void scan() {
        type = type == null ? "" : type;
        page = page == null ? "" : page;
        isUse = isUse == null ? false : isUse;
        pages = pages == null ? new ArrayList<>() : pages;
        title = title != null && !title.equals("") ? ".*" + title.replaceAll(" ", ".*") + ".*" : ".*";
        defaultPages = pages.size() == 0 ? ".*" : "^[^ ]$";// "^$";
    }

    public void mapToFilter(Map<String, String> params) {
        String separator = ",";
        String pageStr = params.get("pages");

        type = params.get("type");
        page = params.get("page");
        title = params.get("title");
        isUse = Boolean.valueOf(params.get("isUse"));

        if (pageStr != null && !pageStr.equals(""))
            pages = Arrays.asList(pageStr.split(separator));

        scan();
    }
}
