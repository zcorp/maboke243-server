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
public class GvideoFilter {
    private String type;// gvideo, gvideo attr, gvideo type
    private String title;
    private Boolean fail;
    private List<String> pages;
    private List<String> types; // movie, show, kid
    private List<String> genres;
    private List<String> excludes;
    private List<String> categories;
    private int categoryElements;

    private String defaultPages;
    private String defaultTypes;
    private String defaultGenres;
    private String defaultCategories;

    private final String ignoreTitles = "(.*serie congolaise.*|.*[F]ilm congolais.*)";

    public void scan() {
        type = type == null ? "" : type;
        fail = fail == null ? false : fail;
        pages = pages == null ? new ArrayList<>() : pages;
        types = types == null ? new ArrayList<>() : types;
        genres = genres == null ? new ArrayList<>() : genres;
        excludes = excludes == null ? new ArrayList<>() : excludes;
        categories = categories == null ? new ArrayList<>() : categories;

        defaultPages = pages.size() == 0 ? ".*" : "^[^ ]$"; // "^$";
        defaultTypes = types.size() == 0 ? ".*" : "^[^ ]$";// "^$";
        defaultGenres = genres.size() == 0 ? ".*" : "^[^ ]$"; // "^$";
        defaultCategories = categories.size() == 0 ? ".*" : "^[^ ]$";// "^$";

        title = title != null && !title.equals("") ? ".*" + title.replaceAll(" ", ".*") + ".*" : ".*";
    }

    public void mapToFilter(Map<String, String> params) {
        String separator = ",";
        String pageStr = params.get("pages");
        String typeStr = params.get("types");
        String genresStr = params.get("genres");
        String categorieStr = params.get("categories");
        String excludeStr = params.get("excludes");
        String categoryElements_str = params.get("categoryElements");

        type = params.get("type");
        title = params.get("title");
        fail = Boolean.valueOf(params.get("fail"));
        categoryElements = categoryElements_str == null ? 12 : Integer.valueOf(categoryElements_str);

        if (pageStr != null && !pageStr.equals(""))
            pages = Arrays.asList(pageStr.split(separator));

        if (typeStr != null && !typeStr.equals(""))
            types = Arrays.asList(typeStr.split(separator));

        if (genresStr != null && !genresStr.equals(""))
            genres = Arrays.asList(genresStr.split(separator));

        if (categorieStr != null && !categorieStr.equals(""))
            categories = Arrays.asList(categorieStr.split(separator));

        if (excludeStr != null && !excludeStr.equals(""))
            excludes = Arrays.asList(excludeStr.split(separator));
        scan();
    }
}
