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
import com.zcore.mabokeserver.common.filter.GvideoFilter;

public class GvideoTreatments {
    private List<JsonNode> gvideos;
    private static final int PAGE_SIZE = 12;
    private Logger logger = LoggerFactory.getLogger(SerieTreatments.class);

    public GvideoTreatments(List<JsonNode> gvideos) {
        this.gvideos = gvideos;
    }

    public List<JsonNode> getAllGvideos() {
        return this.gvideos;
    }

    public List<JsonNode> getGvideos(int page, String gender) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(page * PAGE_SIZE, gvideos.size());

        List<JsonNode> filteredSeries = gvideos.stream()
                .filter(serie -> {
                    String gender2 = serie.get("gender").asText();
                    return gender.equals(gender2);
                })
                .collect(Collectors.toList());
        return filteredSeries.subList(startIndex, Math.min(endIndex, filteredSeries.size()));
    }

    public boolean matchStr(String input, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public boolean inPage(JsonNode video, GvideoFilter filter) {
        boolean res = false;
        List<String> pages = filter.getPages();
        String defaultPages = filter.getDefaultPages();
        JsonNode pageNode = video.get("page");

        if (pageNode.isTextual())
            res = pages.contains(pageNode.asText()) || matchStr(pageNode.asText(), defaultPages);

        return res;
    }

    public boolean sameTitle(JsonNode video, GvideoFilter filter) {
        boolean res = false;
        String title = filter.getTitle();
        JsonNode titleNode = video.get("title");

        if (titleNode.isTextual())
            res = matchStr(titleNode.asText(), title);

        return res;
    }

    public boolean inType(JsonNode gvideo, GvideoFilter filter) {
        boolean res = false;
        List<String> types = filter.getTypes();
        String defaultTypes = filter.getDefaultTypes();
        JsonNode typeNode = gvideo.get("type");

        if (typeNode.isTextual())
            res = types.contains(typeNode.asText()) || matchStr(typeNode.asText(), defaultTypes);

        return res;
    }

    public boolean inGender(JsonNode gvideo, GvideoFilter filter) {
        boolean res = false;
        List<String> genders = filter.getGenres();
        String defaultGenres = filter.getDefaultGenres();
        JsonNode genderNode = gvideo.get("gender");

        if (genderNode != null && genderNode.isTextual())
            res = genders.contains(genderNode.asText()) || matchStr(genderNode.asText(), defaultGenres);

        return res;
    }

    public boolean inCategory(JsonNode video, GvideoFilter filter) {
        boolean res = false;
        List<String> categories = filter.getCategories();
        String defaultCategories = filter.getDefaultCategories();
        JsonNode categoryNode = video.get("category");

        if (categoryNode.isTextual())
            res = categories.contains(categoryNode.asText()) || matchStr(categoryNode.asText(), defaultCategories);

        return res;
    }

    public boolean isFail(JsonNode video, GvideoFilter filter) {
        boolean res = false;
        boolean fail = filter.getFail();
        JsonNode failNode = video.get("fail");

        if (failNode.isBoolean())
            res = fail == failNode.asBoolean();

        return res;
    }

    public boolean inImg(JsonNode video, GvideoFilter filter) {
        boolean res = false;
        List<String> excludes = filter.getExcludes();
        JsonNode imgNode = video.get("img");

        if (imgNode.isTextual())
            res = excludes.contains(imgNode.asText());

        return res;
    }

    public boolean matchFilter(JsonNode gvideo, JsonNode video, GvideoFilter filter) {
        return (inPage(video, filter) && inType(gvideo, filter) && sameTitle(video, filter)
                && inGender(gvideo, filter) && inCategory(video, filter) && isFail(video, filter)
                && !inImg(video, filter));
    }

    public List<Map<String, Object>> getCategory(Map<String, List<JsonNode>> groupesByCategory, int page,
            String mediaType) {
        List<Map<String, Object>> data = new ArrayList<>();

        groupesByCategory.forEach((category, seriesDuGroupe) -> {
            int startIndex = (page - 1) * PAGE_SIZE;
            int endIndex = Math.min(page * PAGE_SIZE, seriesDuGroupe.size());
            Map<String, Object> idInfo = new HashMap<>();
            List<JsonNode> subGvideos = new ArrayList<>();
            Map<String, Object> paginationInfo = new HashMap<>();

            if (startIndex < endIndex)
                subGvideos = seriesDuGroupe.subList(startIndex, endIndex);

            idInfo.put("page", mediaType);
            idInfo.put("category", category);
            paginationInfo.put("page", page);
            paginationInfo.put("total", seriesDuGroupe.size());
            paginationInfo.put("totalpage", (int) Math.ceil((double) seriesDuGroupe.size() / PAGE_SIZE));
            data.add(Map.of("videos", subGvideos, "metadata", paginationInfo, "id", idInfo));
        });

        return data;
    }

    public Map<String, List<Map<String, Object>>> categoryPagination(Map<String, List<JsonNode>> groupesByCategory,
            int page, String mediaType) {

        List<Map<String, Object>> data;
        int size = groupesByCategory.size();
        Map<String, Object> metadataInfo = new HashMap<>();
        List<Map<String, Object>> metaData = new ArrayList<>();
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        data = getCategory(groupesByCategory, page, mediaType);
        metadataInfo.put("page", page);
        metadataInfo.put("total", size);
        metadataInfo.put("totalpage", (int) Math.ceil((double) size / PAGE_SIZE));
        metaData.add(metadataInfo);

        result.put("data", data);
        result.put("metadata", metaData);

        return result;
    }

    public Map<String, List<Map<String, Object>>> grouperByCategory(GvideoFilter filter, Pagination pagination) {
        String category;
        String mediaType = "";
        JsonNode categoryNode, videosNode;
        int page = pagination.getPageNumber();
        Map<String, List<JsonNode>> groupesByCategory = new HashMap<>();

        for (JsonNode gvideo : gvideos) {
            videosNode = gvideo.get("videos");

            if (gvideo.get("type") != null && mediaType.equals(""))
                mediaType = gvideo.get("type").asText();

            if (videosNode.isArray()) {
                for (JsonNode video : videosNode) {
                    if (matchFilter(gvideo, video, filter)) {
                        categoryNode = video.get("category");
                        if (categoryNode != null && categoryNode.isTextual()) {
                            category = categoryNode.asText();
                            groupesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(video);
                        }
                    }
                }
            }
        }

        return this.categoryPagination(groupesByCategory, page, mediaType);
    }

    public Map<String, List<Map<String, Object>>> getGvideosAttr(GvideoFilter filter, Pagination pagination) {
        JsonNode videosNode;
        int startIndex, endIndex;
        int page = pagination.getPageNumber();
        List<JsonNode> gvideos = new ArrayList<>();
        List<JsonNode> subGvideos = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> paginationInfo = new HashMap<>();
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        // logger.info(String.valueOf(filter));

        for (JsonNode gvideo : this.gvideos) {
            videosNode = gvideo.get("videos");
            if (videosNode.isArray()) {
                for (JsonNode video : videosNode) {
                    if (matchFilter(gvideo, video, filter)) {
                        gvideos.add(video);
                    }
                }
            }
        }

        startIndex = (page - 1) * PAGE_SIZE;
        endIndex = Math.min(page * PAGE_SIZE, gvideos.size());

        if (startIndex < endIndex)
            subGvideos = gvideos.subList(startIndex, endIndex);

        paginationInfo.put("page", page);
        paginationInfo.put("total", gvideos.size());
        paginationInfo.put("totalpage", (int) Math.ceil((double) gvideos.size() / PAGE_SIZE));
        data.add(Map.of("videos", subGvideos, "metadata", paginationInfo));
        result.put("data", data);

        return result;
    }
}
