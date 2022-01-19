package moe.victorique.OCR.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import moe.victorique.OCR.service.IOcrService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/ocr")
@Tag(name = "OCR", description = "Api's to extract text from images")
public class OcrController extends AbstractController {

  private final Logger logger = LoggerFactory.getLogger(OcrController.class);
  private final Detector detector;
  private final IOcrService ocrService;

  @Autowired
  public OcrController(IOcrService ocrService, ObjectMapper mapper, Detector detector) {
    super(mapper);
    this.ocrService = ocrService;
    this.detector = detector;
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
      }),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = {
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = String.class)),
              examples = {
                  @ExampleObject("{\"error\":\"error description\"}")
              }
          )
      })
  })
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ObjectNode handleFileUpload(HttpServletResponse response, @RequestPart("file") MultipartFile file) throws IOException {
    byte[] bytes = file.getBytes();
    ObjectNode objectNode = mapper.createObjectNode();
    try (InputStream is = new ByteArrayInputStream(bytes); BufferedInputStream bis = new BufferedInputStream(is);) {
      Metadata metadata = new Metadata();
      metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, file.getName());
      org.apache.tika.mime.MediaType mediaType = this.detector.detect(bis, metadata);
      if (!mediaType.getType().equals("image")) {
        return this.doError("File must be an image", response);
      }
    }
    String resp = "";
    try {
      resp = this.ocrService.getText(bytes);
      logger.info("Image text recognised as: " + resp);
    } catch (Exception e) {
      return this.doError(e.getMessage(), response);
    }

    objectNode.put("result", resp);
    return objectNode;
  }
}
