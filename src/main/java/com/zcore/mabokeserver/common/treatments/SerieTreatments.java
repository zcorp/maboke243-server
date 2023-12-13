package com.zcore.mabokeserver.common.treatments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.zcore.mabokeserver.common.Pagination;
import com.zcore.mabokeserver.common.filter.SerieFilter;

public class SerieTreatments {
    private List<JsonNode> series;
    private static final int PAGE_SIZE = 12;
    private Logger logger = LoggerFactory.getLogger(SerieTreatments.class);

    public SerieTreatments(List<JsonNode> series) {
        this.series = series;
    }

    public List<JsonNode> getAllSeries() {
        return this.series;
    }

    public List<JsonNode> getSeries(int page, String state) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(page * PAGE_SIZE, series.size());

        List<JsonNode> filteredSeries = series.stream()
                .filter(serie -> {
                    String state2 = serie.get("state").asText();
                    return state.equals(state2);
                })
                .collect(Collectors.toList());
        return filteredSeries.subList(startIndex, Math.min(endIndex, filteredSeries.size()));
    }

    public boolean matchStr(String input, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public boolean sameTitle(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        String title = filter.getTitle();
        JsonNode titleNode = serie.get("title");

        if (titleNode.isTextual())
            res = matchStr(titleNode.asText(), title);

        return res;
    }

    public boolean isFail(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        boolean fail = filter.getFail();
        JsonNode failNode = serie.get("fail");

        if (failNode.isBoolean())
            res = fail == failNode.asBoolean();

        return res;
    }

    public boolean inImg(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        List<String> excludes = filter.getExcludes();
        JsonNode imgNode = serie.get("img");

        if (imgNode.isTextual())
            res = excludes.contains(imgNode.asText());

        return res;
    }

    public boolean inCategory(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        List<String> categories = filter.getCategories();
        String defaultCategories = filter.getDefaultCategories();
        JsonNode categoryNode = serie.get("category");

        if (categoryNode.isTextual())
            res = categories.contains(categoryNode.asText()) || matchStr(categoryNode.asText(), defaultCategories);

        return res;
    }

    public boolean inPage(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        List<String> pages = filter.getPages();
        String defaultPages = filter.getDefaultPages();
        JsonNode pageNode = serie.get("page");

        if (pageNode.isTextual())
            res = pages.contains(pageNode.asText()) || matchStr(pageNode.asText(), defaultPages);

        return res;
    }

    public boolean inGender(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        List<String> genders = filter.getGenres();
        String defaultGenres = filter.getDefaultGenres();
        JsonNode genderNode = serie.get("gender");

        if (genderNode.isTextual())
            res = genders.contains(genderNode.asText()) || matchStr(genderNode.asText(), defaultGenres);

        return res;
    }

    public boolean inState(JsonNode serie, SerieFilter filter) {
        boolean res = false;
        List<String> states = filter.getStates();
        String defaultStates = filter.getDefaultStates();
        JsonNode stateNode = serie.get("state");

        if (stateNode.isTextual())
            res = states.contains(stateNode.asText()) || matchStr(stateNode.asText(), defaultStates);

        return res;
    }

    public boolean matchFilter(JsonNode serie, SerieFilter filter) {
        return (inPage(serie, filter) && inState(serie, filter) && sameTitle(serie, filter) && inGender(serie, filter)
                && inCategory(serie, filter) && isFail(serie, filter) && !inImg(serie, filter));
    }

    public List<Map<String, Object>> getCategory(Map<String, List<JsonNode>> groupesByCategory, int page) {
        List<Map<String, Object>> data = new ArrayList<>();

        groupesByCategory.forEach((category, seriesDuGroupe) -> {
            int startIndex = (page - 1) * PAGE_SIZE;
            List<JsonNode> subSeries = new ArrayList<>();
            Map<String, Object> idInfo = new HashMap<>();
            Map<String, Object> paginationInfo = new HashMap<>();
            int endIndex = Math.min(page * PAGE_SIZE, seriesDuGroupe.size());

            if (startIndex < endIndex)
                subSeries = seriesDuGroupe.subList(startIndex, endIndex);

            idInfo.put("page", "serie");
            idInfo.put("category", category);

            paginationInfo.put("page", page);
            paginationInfo.put("total", seriesDuGroupe.size());
            paginationInfo.put("totalpage", (int) Math.ceil((double) seriesDuGroupe.size() / PAGE_SIZE));
            data.add(Map.of("series", subSeries, "metadata", paginationInfo, "id", idInfo));
        });

        return data;
    }

    public Map<String, List<Map<String, Object>>> categoryPagination(Map<String, List<JsonNode>> groupesByCategory,
            int page) {

        List<Map<String, Object>> data;
        int size = groupesByCategory.size();
        Map<String, Object> metadataInfo = new HashMap<>();
        List<Map<String, Object>> metaData = new ArrayList<>();
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        data = getCategory(groupesByCategory, page);
        metadataInfo.put("page", page);
        metadataInfo.put("total", size);
        metadataInfo.put("totalpage", (int) Math.ceil((double) size / PAGE_SIZE));
        metaData.add(metadataInfo);

        result.put("data", data);
        result.put("metadata", metaData);

        return result;
    }

    public Map<String, List<Map<String, Object>>> grouperByCategory(SerieFilter filter, Pagination pagination) {
        String category;
        JsonNode categoryNode;
        int page = pagination.getPageNumber();
        Map<String, List<JsonNode>> groupesByCategory = new HashMap<>();

        for (JsonNode serie : series) {
            if (matchFilter(serie, filter)) {
                categoryNode = serie.get("category");
                if (categoryNode != null && categoryNode.isTextual()) {
                    category = categoryNode.asText();
                    groupesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(serie);
                }
            }
        }

        return this.categoryPagination(groupesByCategory, page);
    }

    public Map<String, List<Map<String, Object>>> getSeriesAttr(SerieFilter filter, Pagination pagination) {
        int startIndex, endIndex;
        int page = pagination.getPageNumber();
        List<JsonNode> series = new ArrayList<>();
        List<JsonNode> subSeries = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> paginationInfo = new HashMap<>();
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        for (JsonNode serie : this.series) {
            if (matchFilter(serie, filter)) {
                series.add(serie);
            }
        }

        startIndex = (page - 1) * PAGE_SIZE;
        endIndex = Math.min(page * PAGE_SIZE, series.size());

        if (startIndex < endIndex)
            subSeries = series.subList(startIndex, endIndex);

        paginationInfo.put("page", page);
        paginationInfo.put("total", series.size());
        paginationInfo.put("totalpage", (int) Math.ceil((double) series.size() / PAGE_SIZE));
        data.add(Map.of("series", subSeries, "metadata", paginationInfo));
        result.put("data", data);

        return result;
    }
}
