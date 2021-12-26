package com.wascode.microservices.core.review.controller;

import com.wascode.api.core.review.Review;
import com.wascode.api.core.review.ReviewApi;
import com.wascode.api.exceptions.InvalidInputException;
import com.wascode.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController implements ReviewApi {
  private final ServiceUtil serviceUtil;

  @Override
  public List<Review> getReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 213) {
      log.debug("No reviews found for productId: {}", productId);
      return new ArrayList<>();
    }

    List<Review> reviews =
        Arrays.asList(
            new Review(
                productId,
                1,
                "Author 1",
                "Subject 1",
                "Content 1",
                serviceUtil.getServiceAddress()),
            new Review(
                productId,
                2,
                "Author 2",
                "Subject 2",
                "Content 2",
                serviceUtil.getServiceAddress()),
            new Review(
                productId,
                3,
                "Author 3",
                "Subject 3",
                "Content 3",
                serviceUtil.getServiceAddress()));

    log.debug("/reviews response size: {}", reviews.size());

    return reviews;
  }
}
