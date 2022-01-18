package moe.victorique.OCR.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import moe.victorique.OCR.service.IOcrService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class OcrController {

  Logger logger = LoggerFactory.getLogger(OcrController.class);

  private final IOcrService ocrService;

  private final ObjectMapper mapper;

  @Autowired
  public OcrController(IOcrService ocrService, ObjectMapper mapper) {
    this.ocrService = ocrService;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(hidden = true)
  public void redirectWithUsingRedirectView(HttpServletResponse httpServletResponse) {
    httpServletResponse.setHeader("Location", "/swagger-ui/index.html");
    httpServletResponse.setStatus(302);
  }

  @Operation(summary = "Get text from image")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK", content = {
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = String.class)),
              examples = {
                  @ExampleObject("{\"result\":\"Some text\"}")
              }
          )
      })
  })
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ObjectNode handleFileUpload(HttpServletResponse response, @RequestPart("file") MultipartFile file) throws IOException {
    byte[] bytes = file.getBytes();
    ObjectNode objectNode = mapper.createObjectNode();
    String resp = "";
    try {
      resp = this.ocrService.getText(bytes);
      logger.info("Image text recognised as: " + resp);
    } catch (Exception e) {
      objectNode.put("error", e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return objectNode;
    }

    objectNode.put("result", resp);
    return objectNode;
  }
}
