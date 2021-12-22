package com.wascode.api.core.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Review {
  private final int productId;
  private final int reviewId;
  private final String author;
  private final String subject;
  private final String content;
  private final String serviceAddress;
}
