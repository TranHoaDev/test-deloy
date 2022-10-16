package com.project.ecommerce.perfume.service.Impl;

import com.project.ecommerce.perfume.entity.Perfume;
import com.project.ecommerce.perfume.entity.Review;
import com.project.ecommerce.perfume.enums.SearchPerfume;
import com.project.ecommerce.perfume.exception.ApiRequestException;
import com.project.ecommerce.perfume.repository.PerfumeRepository;
import com.project.ecommerce.perfume.repository.projection.PerfumeProjection;
import com.project.ecommerce.perfume.service.PerfumeService;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;

//    @Value("${amazon.s3.bucket.name}")
//    private String bucketName;

    @Override
    public Perfume getPerfumeById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new ApiRequestException("Perfume not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Review> getReviewsByPerfumeId(Long perfumeId) {
        Perfume perfume = getPerfumeById(perfumeId);
        return perfume.getReviews();
    }

    @Override
    public Page<PerfumeProjection> getAllPerfumes(Pageable pageable) {
        return perfumeRepository.findAllByOrderByIdAsc(pageable);
    }

    @Override
    public List<PerfumeProjection> getPerfumesByIds(List<Long> perfumesId) {
        return perfumeRepository.getPerfumesByIds(perfumesId);
    }

    @Override
    public Page<PerfumeProjection> findPerfumesByFilterParams(List<String> perfumers, List<String> genders, List<Integer> prices, Pageable pageable) {
        return perfumeRepository.findPerfumesByFilterParams(perfumers, genders, prices.get(0), prices.get(1), pageable);
    }

    @Override
    public List<Perfume> findByPerfumer(String perfumer) {
        return perfumeRepository.findByPerfumerOrderByPriceDesc(perfumer);
    }

    @Override
    public List<Perfume> findByPerfumeGender(String perfumeGender) {
        return perfumeRepository.findByPerfumeGenderOrderByPriceDesc(perfumeGender);
    }

    @Override
    public Page<PerfumeProjection> findByInputText(SearchPerfume searchType, String text, Pageable pageable) {
        if (searchType.equals(SearchPerfume.BRAND)) {
            return perfumeRepository.findByPerfumer(text, pageable);
        } else if (searchType.equals(SearchPerfume.PERFUME_TITLE)) {
            return perfumeRepository.findByPerfumeTitle(text, pageable);
        } else {
            return perfumeRepository.findByManufacturerCountry(text, pageable);
        }
    }

    @Override
    @Transactional
    public Perfume savePerfume(Perfume perfume, MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileName = UUID.randomUUID().toString() + "." + multipartFile.getOriginalFilename();
        file.delete();
        return perfumeRepository.save(perfume);
    }

    @Override
    @Transactional
    public String deletePerfume(Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new ApiRequestException("Perfume not found.", HttpStatus.NOT_FOUND));
        perfumeRepository.delete(perfume);
        return "Perfume deleted successfully";
    }

    @Override
    public DataFetcher<Perfume> getPerfumeByQuery() {
        return dataFetchingEnvironment -> {
            Long perfumeId = Long.parseLong(dataFetchingEnvironment.getArgument("id"));
            return perfumeRepository.findById(perfumeId).get();
        };
    }

    @Override
    public DataFetcher<List<PerfumeProjection>> getAllPerfumesByQuery() {
        return dataFetchingEnvironment -> perfumeRepository.findAllByOrderByIdAsc();
    }

    @Override
    public DataFetcher<List<Perfume>> getAllPerfumesByIdsQuery() {
        return dataFetchingEnvironment -> {
            List<String> objects = dataFetchingEnvironment.getArgument("ids");
            List<Long> perfumesId = objects.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            return perfumeRepository.findByIdIn(perfumesId);
        };
    }
}
