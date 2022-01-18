package moe.victorique.OCR;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class Beans {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public Tesseract tesseract() {
    Tesseract tesseract = new Tesseract();
    File tessDataFolder = LoadLibs.extractTessResources("tessdata");
    tesseract.setDatapath(tessDataFolder.getAbsolutePath());
    tesseract.setLanguage("eng");
    tesseract.setPageSegMode(1);
    tesseract.setOcrEngineMode(1);
    return tesseract;
  }

}
