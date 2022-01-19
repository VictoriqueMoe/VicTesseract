package moe.victorique.OCR;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class Beans {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean(name = "contentTypeDetector")
  public Detector detector() {
    final Tika tika = new Tika();
    return tika.getDetector();
  }

  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Victorique's API's")
            .description("List of API's I have made")
            .version("v0.0.1")
            .license(new License().name("Apache 2.0").url("https://victorique.moe")));
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
