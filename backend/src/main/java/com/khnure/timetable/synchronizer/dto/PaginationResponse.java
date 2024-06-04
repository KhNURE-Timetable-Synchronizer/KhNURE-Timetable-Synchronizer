package com.khnure.timetable.synchronizer.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
@Builder
public class PaginationResponse<T> {
    @JsonIgnore
    private Map.Entry<String, Long> totalItemNumber;
    private Integer totalPageNumber;
    private Integer currentPageNumber;
    @JsonIgnore
    private Map.Entry<String, Collection<T>> data;

    @JsonAnyGetter
    public Map<String, Object> data() {
        return Map.of(
                totalItemNumber.getKey(), totalItemNumber.getValue(),
                data.getKey(), data.getValue()
        );
    }
}
