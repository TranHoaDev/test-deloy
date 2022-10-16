package com.project.ecommerce.perfume.service;

import com.project.ecommerce.perfume.entity.Perfume;
import com.project.ecommerce.perfume.entity.Review;
import com.project.ecommerce.perfume.entity.User;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    User getUserInfo(String email);

    Page<User> getAllUsers(Pageable pageable);

    List<Perfume> getCart(List<Long> perfumeIds);

    User updateUserInfo(String email, User user);

    Review addReviewToPerfume(Review review, Long perfumeId);

    DataFetcher<List<User>> getAllUsersByQuery();

    DataFetcher<User> getUserByQuery();
}
