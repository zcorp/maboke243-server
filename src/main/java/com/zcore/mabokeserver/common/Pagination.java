package com.zcore.mabokeserver.common;

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
public class Pagination {
    private int page;
    private int skip;
    private int total;
    private int totalpage;
    private int pageNumber;
    private int numberOfElements;

    public void scan() {
        skip = (pageNumber - 1) * numberOfElements;
    }

    public void mapToPagination(Map<String, String> params) {
        String pageNbr_str = params.get("pageNumber");
        String nbrOfElements_str = params.get("numberOfElements");
        pageNumber = pageNbr_str == null ? 1 : Integer.valueOf(pageNbr_str);
        numberOfElements = nbrOfElements_str == null ? 5 : Integer.valueOf(nbrOfElements_str);
        scan();
    }
}
