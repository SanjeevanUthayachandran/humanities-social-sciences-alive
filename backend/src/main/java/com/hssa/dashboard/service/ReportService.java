package com.hssa.dashboard.service;

import com.hssa.dashboard.dto.response.ChartResponse;
import com.hssa.dashboard.dto.response.ReportResponse;
import com.hssa.dashboard.dto.response.SystemHealthResponse;

public interface ReportService {
    SystemHealthResponse getSystemHealth();
    ChartResponse getChartsData(String chartType, String range);
    ReportResponse getReports(String reportType);
}