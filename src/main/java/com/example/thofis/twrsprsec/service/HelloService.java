package com.example.thofis.twrsprsec.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {

  @PreAuthorize("hasAuthority('PROCESS_HELLO')")
  public void processHello() {
    log.info("do something useful with hello.");
  }

}
