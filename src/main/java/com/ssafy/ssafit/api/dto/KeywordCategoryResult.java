package com.ssafy.ssafit.api.dto;

import java.util.List;

public record KeywordCategoryResult(
        String category,
        List<String> keywords
) {}