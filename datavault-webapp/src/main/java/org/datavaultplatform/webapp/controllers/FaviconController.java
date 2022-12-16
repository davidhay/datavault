package org.datavaultplatform.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController {

  @GetMapping("/favicon.ico")
  @SneakyThrows
  public String favicon() {
    return "forward:/resources/favicon.ico";
  }

}
