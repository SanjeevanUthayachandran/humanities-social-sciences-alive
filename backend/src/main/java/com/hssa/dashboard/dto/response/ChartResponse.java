package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartResponse {
    private String chartType;
    private List<String> labels;
    private List<Object> data;
}