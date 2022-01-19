package moe.victorique.OCR.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Controller
public class FileUploadExceptionAdvice extends AbstractController {

  @Autowired
  protected FileUploadExceptionAdvice(ObjectMapper mapper) {
    super(mapper);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public @ResponseBody
  ObjectNode handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exc, HttpServletRequest request, HttpServletResponse response) {
    return this.doError("File too large", response);
  }
}
