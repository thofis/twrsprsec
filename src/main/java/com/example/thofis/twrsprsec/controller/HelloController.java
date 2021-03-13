package com.example.thofis.twrsprsec.controller;

import com.example.thofis.twrsprsec.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

  private final HelloService helloService;

  @GetMapping("/hello")
  public String hello() {
//    helloService.processHello();
    return "Hello!";
  }
}
