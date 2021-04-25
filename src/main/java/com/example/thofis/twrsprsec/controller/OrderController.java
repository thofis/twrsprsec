package com.example.thofis.twrsprsec.controller;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {


  @GetMapping("/{order_id}")
  public String getOrder(@PathVariable("order_id") String orderId) {
    Objects.requireNonNull(orderId);
    log.info("loading order with id: {}", orderId);
    return orderId;
  }

  @PostMapping
  public ResponseEntity<Void> createOrder() {
    log.info("creating order");
    return ResponseEntity.created(UriComponentsBuilder.fromPath("/dummy")
                                                      .build()
                                                      .toUri())
                         .build();
  }

  @PutMapping("/{order_id}")
  public String updateOrder(@PathVariable("order_id") String orderId) {
    log.info("updating order with id: {}", orderId);
    return orderId;
  }

  @DeleteMapping("/{order_id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable("order_id") String orderId) {
    log.info("deleting order with id: {}", orderId);
    return ResponseEntity.noContent()
                         .build();
  }


}
