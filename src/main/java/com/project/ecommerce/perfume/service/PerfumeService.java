package com.project.ecommerce.perfume.service;

import com.project.ecommerce.perfume.entity.Perfume;
import com.project.ecommerce.perfume.entity.Review;
import com.project.ecommerce.perfume.enums.SearchPerfume;
import com.project.ecommerce.perfume.repository.projection.PerfumeProjection;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PerfumeService {

    Perfume getPerfumeById(Long perfumeId);

    Page<PerfumeProjection> getAllPerfumes(Pageable pageable);

    List<PerfumeProjection> getPerfumesByIds(List<Long> perfumesId);

    Page<PerfumeProjection> findPerfumesByFilterParams(List<String> perfumers, List<String> genders, List<Integer> prices, Pageable pageable);

    List<Perfume> findByPerfumer(String perfumer);

    List<Perfume> findByPerfumeGender(String perfumeGender);

    Page<PerfumeProjection> findByInputText(SearchPerfume searchType, String text, Pageable pageable);

    Perfume savePerfume(Perfume perfume, MultipartFile file);

    String deletePerfume(Long perfumeId);

    List<Review> getReviewsByPerfumeId(Long perfumeId);

    DataFetcher<Perfume> getPerfumeByQuery();

    DataFetcher<List<PerfumeProjection>> getAllPerfumesByQuery();

    DataFetcher<List<Perfume>> getAllPerfumesByIdsQuery();
}
