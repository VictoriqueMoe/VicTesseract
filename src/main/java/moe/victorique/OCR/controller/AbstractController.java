package moe.victorique.OCR.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractController {

  protected final ObjectMapper mapper;

  protected AbstractController(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  protected ObjectNode doError(String message, HttpServletResponse response) {
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("error", message);
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return objectNode;
  }
}
