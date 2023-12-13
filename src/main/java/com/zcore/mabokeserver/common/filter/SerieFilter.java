package com.zcore.mabokeserver.common.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SerieFilter {
    private String type;// serie, serie attr, category, categry attr
    private String title;
    private Boolean fail;
    private List<String> pages;
    private List<String> states;
    private List<String> genres;
    private List<String> excludes;
    private List<String> categories;
    private int categoryElements;

    private String defaultPages;
    private String defaultStates;
    private String defaultGenres;
    private String defaultCategories;

    public void scan() {
        type = type == null ? "" : type;
        fail = fail == null ? false : fail;
        pages = pages == null ? new ArrayList<>() : pages;
        states = states == null ? new ArrayList<>() : states;
        genres = genres == null ? new ArrayList<>() : genres;
        excludes = excludes == null ? new ArrayList<>() : excludes;
        categories = categories == null ? new ArrayList<>() : categories;
        title = title != null && !title.equals("") ? ".*" + title.replaceAll(" ", ".*") + ".*" : ".*";

        defaultPages = pages.size() == 0 ? ".*" : "^[^ ]$";// "^$";
        defaultStates = states.size() == 0 ? ".*" : "^[^ ]$";// "^$";
        defaultGenres = genres.size() == 0 ? ".*" : "^[^ ]$";// "^$";
        defaultCategories = categories.size() == 0 ? ".*" : "^[^ ]$";// "^$";
    }

    public void mapToFilter(Map<String, String> params) {
        String separator = ",";
        String pageStr = params.get("pages");
        String stateStr = params.get("states");
        String genresStr = params.get("genres");
        String categorieStr = params.get("categories");
        String excludeStr = params.get("excludes");
        String categoryElements_str = params.get("categoryElements");

        type = params.get("type");
        title = params.get("title");
        fail = Boolean.valueOf(params.get("fail"));
        categoryElements = categoryElements_str == null ? 12 : Integer.valueOf(categoryElements_str);

        if (pageStr != null && !pageStr.equals(""))
            pages = Arrays.asList(pageStr.toLowerCase().split(separator));

        if (stateStr != null && !stateStr.equals(""))
            states = Arrays.asList(stateStr.split(separator));

        if (genresStr != null && !genresStr.equals(""))
            genres = Arrays.asList(genresStr.split(separator));

        if (categorieStr != null && !categorieStr.equals(""))
            categories = Arrays.asList(categorieStr.split(separator));

        if (excludeStr != null && !excludeStr.equals(""))
            excludes = Arrays.asList(excludeStr.split(separator));

        scan();
    }
}
