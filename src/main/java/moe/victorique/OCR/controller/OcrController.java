package moe.victorique.OCR.controller;


import moe.victorique.OCR.service.IOcrService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/ocr")
public class OcrController {

  private final IOcrService ocrService;

  private final ObjectMapper mapper;

  @Autowired
  public OcrController(IOcrService ocrService, ObjectMapper mapper) {
    this.ocrService = ocrService;
    this.mapper = mapper;
  }

  @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ObjectNode handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
    byte[] bytes = file.getBytes();
    ObjectNode objectNode = mapper.createObjectNode();
    String resp = "";
    try {
      resp = this.ocrService.getText(bytes);
    } catch (Exception e) {
      objectNode.put("error", e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return objectNode;
    }

    objectNode.put("result", resp);
    return objectNode;
  }
}
