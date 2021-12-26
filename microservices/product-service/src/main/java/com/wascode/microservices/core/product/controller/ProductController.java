package com.wascode.microservices.core.product.controller;

import com.wascode.api.core.product.Product;
import com.wascode.api.core.product.ProductApi;
import com.wascode.api.exceptions.InvalidInputException;
import com.wascode.api.exceptions.NotFoundException;
import com.wascode.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductApi {

  private final ServiceUtil serviceUtil;

  @Override
  public Product getProduct(int productId) {
    log.debug("/product return the found product for productId={}", productId);

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 13) {
      throw new NotFoundException("No product found for productId: " + productId);
    }

    return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
  }
}
