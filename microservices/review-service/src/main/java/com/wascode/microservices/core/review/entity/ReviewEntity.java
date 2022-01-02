package com.wascode.microservices.core.review.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(
    name = "reviews",
    indexes = {@Index(name = "prod-rev-id", unique = true, columnList = "productId,reviewId")})
public class ReviewEntity {

  @Id @GeneratedValue private int id;
  @Version private int version;

  private int productId;
  private int reviewId;
  private String author;
  private String subject;
  private String content;

  public ReviewEntity() {}

  public ReviewEntity(int productId, int reviewId, String author, String subject, String content) {
    this.productId = productId;
    this.reviewId = reviewId;
    this.author = author;
    this.subject = subject;
    this.content = content;
  }
}
