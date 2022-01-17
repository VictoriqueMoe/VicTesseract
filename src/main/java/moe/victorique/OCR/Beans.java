package moe.victorique.OCR;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class Beans {

  @Autowired
  ResourceLoader resourceLoader;

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public Tesseract tesseract() throws IOException, URISyntaxException {
    Tesseract tesseract = new Tesseract();
    File tessDataFolder = LoadLibs.extractTessResources("tessdata");
    tesseract.setDatapath(tessDataFolder.getAbsolutePath());
    tesseract.setLanguage("eng");
    tesseract.setPageSegMode(1);
    tesseract.setOcrEngineMode(1);
    return tesseract;
  }
}
