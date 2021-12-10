#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=1.8 \
--packaging=jar \
--name=product-service \
--package-name=com.wascode.microservices.core.product \
--groupId=com.wascode.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=1.8 \
--packaging=jar \
--name=review-service \
--package-name=com.wascode.microservices.core.review \
--groupId=com.wascode.microservices.core.review \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
review-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=1.8 \
--packaging=jar \
--name=recommendation-service \
--package-name=com.wascode.microservices.core.recommendation \
--groupId=com.wascode.microservices.core.recommendation \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
recommendation-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=1.8 \
--packaging=jar \
--name=product-composite-service \
--package-name=com.wascode.microservices.composite.product \
--groupId=com.wascode.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service

cd ..
