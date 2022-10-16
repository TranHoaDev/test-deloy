package com.project.ecommerce.perfume.mapper;

import com.project.ecommerce.perfume.dto.HeaderResponse;
import com.project.ecommerce.perfume.dto.perfume.PerfumeResponse;
import com.project.ecommerce.perfume.dto.review.ReviewRequest;
import com.project.ecommerce.perfume.dto.review.ReviewResponse;
import com.project.ecommerce.perfume.dto.user.BaseUserResponse;
import com.project.ecommerce.perfume.dto.user.UpdateUserRequest;
import com.project.ecommerce.perfume.dto.user.UserResponse;
import com.project.ecommerce.perfume.entity.Review;
import com.project.ecommerce.perfume.entity.User;
import com.project.ecommerce.perfume.exception.InputFieldException;
import com.project.ecommerce.perfume.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CommonMapper commonMapper;
    private final UserService userService;

    public UserResponse getUserById(Long userId) {
        return commonMapper.convertToResponse(userService.getUserById(userId), UserResponse.class);
    }

    public UserResponse getUserInfo(String email) {
        return commonMapper.convertToResponse(userService.getUserInfo(email), UserResponse.class);
    }

    public List<PerfumeResponse> getCart(List<Long> perfumesIds) {
        return commonMapper.convertToResponseList(userService.getCart(perfumesIds), PerfumeResponse.class);
    }

    public HeaderResponse<BaseUserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return commonMapper.getHeaderResponse(users.getContent(), users.getTotalPages(), users.getTotalElements(), BaseUserResponse.class);
    }

    public UserResponse updateUserInfo(String email, UpdateUserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        User user = commonMapper.convertToEntity(userRequest, User.class);
        return commonMapper.convertToResponse(userService.updateUserInfo(email, user), UserResponse.class);
    }

    public ReviewResponse addReviewToPerfume(ReviewRequest reviewRequest, Long perfumeId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        Review review = commonMapper.convertToEntity(reviewRequest, Review.class);
        return commonMapper.convertToResponse(userService.addReviewToPerfume(review, perfumeId), ReviewResponse.class);
    }
}
