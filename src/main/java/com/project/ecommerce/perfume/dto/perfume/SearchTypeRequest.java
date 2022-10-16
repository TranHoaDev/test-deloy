package com.project.ecommerce.perfume.dto.perfume;

import com.project.ecommerce.perfume.enums.SearchPerfume;
import lombok.Data;

@Data
public class SearchTypeRequest {
    private SearchPerfume searchType;
    private String text;
}
