package com.wascode.microservices.composite.product.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wascode.api.core.product.Product;
import com.wascode.api.core.product.ProductApi;
import com.wascode.api.core.recommendation.Recommendation;
import com.wascode.api.core.recommendation.RecommendationApi;
import com.wascode.api.core.review.Review;
import com.wascode.api.core.review.ReviewApi;
import com.wascode.api.exceptions.InvalidInputException;
import com.wascode.api.exceptions.NotFoundException;
import com.wascode.util.http.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ProductCompositeIntegration implements ProductApi, RecommendationApi, ReviewApi {
  public static final String HTTP = "http://";
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  public ProductCompositeIntegration(
      RestTemplate restTemplate,
      ObjectMapper objectMapper,
      @Value("${app.product-service.host}") String productServiceHost,
      @Value("${app.product-service.port}") String productServicePort,
      @Value("${app.recommendation-service.host}") String recommendationServiceHost,
      @Value("${app.recommendation-service.port}") String recommendationServicePort,
      @Value("${app.review-service.host}") String reviewServiceHost,
      @Value("${app.review-service.port}") String reviewServicePort) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;

    this.productServiceUrl = HTTP + productServiceHost + ":" + productServicePort + "/product/";

    this.recommendationServiceUrl =
        HTTP + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation";

    this.reviewServiceUrl = HTTP + reviewServiceHost + ":" + reviewServicePort + "/review";
  }

  @Override
  public Product getProduct(int productId) {
    try {
      String url = this.productServiceUrl + productId;
      log.debug("Will post a new product to URL: {}", url);

      Product product = restTemplate.getForObject(url, Product.class);
      log.debug("Created a product with id: {}", product.getProductId());

      return product;
    } catch (HttpClientErrorException exception) {
      throw handleHttpClientException(exception);
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    try {
      String url = recommendationServiceUrl + "?productId=" + productId;

      log.debug("Will call the getRecommendations API on URL: {}", url);
      List<Recommendation> recommendations =
          restTemplate
              .exchange(
                  url,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<Recommendation>>() {})
              .getBody();

      log.debug(
          "Found {} recommendations for a product with id: {}", recommendations.size(), productId);
      return recommendations;

    } catch (Exception ex) {
      log.warn(
          "Got an exception while requesting recommendations, return zero recommendations: {}",
          ex.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public List<Review> getReviews(int productId) {
    try {
      String url = reviewServiceUrl + "?productId=" + productId;

      log.debug("Will call the getReviews API on URL: {}", url);
      List<Review> reviews =
          restTemplate
              .exchange(
                  url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {})
              .getBody();

      log.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
      return reviews;

    } catch (Exception ex) {
      log.warn(
          "Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }

  private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
    switch (ex.getStatusCode()) {
      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(ex));

      case UNPROCESSABLE_ENTITY:
        return new InvalidInputException(getErrorMessage(ex));

      default:
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException exception) {
      return exception.getMessage();
    }
  }
}
