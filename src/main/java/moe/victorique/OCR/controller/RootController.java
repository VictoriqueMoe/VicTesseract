package moe.victorique.OCR.controller;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class RootController {

  @GetMapping
  @Operation(hidden = true)
  public void redirectWithUsingRedirectView(HttpServletResponse httpServletResponse) {
    httpServletResponse.setHeader("Location", "/swagger-ui/index.html");
    httpServletResponse.setStatus(302);
  }
}
