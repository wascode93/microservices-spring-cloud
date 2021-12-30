package com.wascode.microservices.composite.product;

import com.wascode.api.core.product.Product;
import com.wascode.api.core.recommendation.Recommendation;
import com.wascode.api.core.review.Review;
import com.wascode.api.exceptions.InvalidInputException;
import com.wascode.api.exceptions.NotFoundException;
import com.wascode.microservices.composite.product.integration.ProductCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductCompositeServiceApplicationTests {

  public static final Product PRODUCT_OK = new Product(1, "name", 1, "mock-address");
  public static final List<Review> PRODUCT_OK_REVIEWS =
      singletonList(
          new Review(PRODUCT_OK.getProductId(), 1, "author", "subject", "content", "mock-address"));
  public static final List<Recommendation> PRODUCT_OK_RECOMMENDATIONS =
      singletonList(
          new Recommendation(PRODUCT_OK.getProductId(), 1, "author", 1, "content", "mock-address"));

  private static final int PRODUCT_ID_NOT_FOUND = 2;
  private static final int PRODUCT_ID_INVALID = 3;

  @Autowired private WebTestClient client;

  @MockBean private ProductCompositeIntegration integration;

  @BeforeEach
  public void beforeEach() {
    Mockito.when(integration.getProduct(PRODUCT_OK.getProductId())).thenReturn(PRODUCT_OK);
    Mockito.when(integration.getRecommendations(PRODUCT_OK.getProductId()))
        .thenReturn(PRODUCT_OK_RECOMMENDATIONS);
    Mockito.when(integration.getReviews(PRODUCT_OK.getProductId())).thenReturn(PRODUCT_OK_REVIEWS);

    Mockito.when(integration.getProduct(PRODUCT_ID_NOT_FOUND))
        .thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));

    Mockito.when(integration.getProduct(PRODUCT_ID_INVALID))
        .thenThrow(new InvalidInputException("INVALID: " + PRODUCT_ID_INVALID));
  }

  @Test
  void should_get_product_by_id() {
    client
        .get()
        .uri("/product-composite/" + PRODUCT_OK.getProductId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(PRODUCT_OK.getProductId())
        .jsonPath("$.recommendations.length()")
        .isEqualTo(1)
        .jsonPath("$.reviews.length()")
        .isEqualTo(1);
  }

  @Test
  void should_throw_not_found_exception() {
    client
        .get()
        .uri("/product-composite/" + PRODUCT_ID_NOT_FOUND)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.path")
        .isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND)
        .jsonPath("$.message")
        .isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);
  }

  @Test
  void should_throw_invalid_exception() {
    client
        .get()
        .uri("/product-composite/" + PRODUCT_ID_INVALID)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.path")
        .isEqualTo("/product-composite/" + PRODUCT_ID_INVALID)
        .jsonPath("$.message")
        .isEqualTo("INVALID: " + PRODUCT_ID_INVALID);
  }
}
