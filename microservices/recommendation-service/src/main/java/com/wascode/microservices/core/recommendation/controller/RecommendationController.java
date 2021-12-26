package com.wascode.microservices.core.recommendation.controller;

import com.wascode.api.core.recommendation.Recommendation;
import com.wascode.api.core.recommendation.RecommendationApi;
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
public class RecommendationController implements RecommendationApi {
  private final ServiceUtil serviceUtil;

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 113) {
      log.debug("No recommendations found for productId: {}", productId);
      return new ArrayList<>();
    }

    List<Recommendation> list =
        Arrays.asList(
            new Recommendation(
                productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()),
            new Recommendation(
                productId, 2, "Author 2", 2, "Content 2", serviceUtil.getServiceAddress()),
            new Recommendation(
                productId, 3, "Author 3", 3, "Content 3", serviceUtil.getServiceAddress()));

    log.debug("/recommendation response size: {}", list.size());

    return list;
  }
}
