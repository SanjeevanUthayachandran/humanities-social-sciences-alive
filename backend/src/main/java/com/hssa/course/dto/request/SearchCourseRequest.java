package com.hssa.course.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class SearchCourseRequest {
    private String keyword;
    private UUID categoryId;
    private UUID instructorId;
    private String level;
    private String language;
    private String status;
    private String visibility;
}
