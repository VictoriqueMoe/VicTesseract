package moe.victorique.OCR.service.impl;

import moe.victorique.OCR.service.IOcrService;
import net.sourceforge.tess4j.Tesseract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

@Service
public class OcrService implements IOcrService {

  private final Tesseract tesseract;

  @Autowired
  public OcrService(Tesseract tesseract) {
    this.tesseract = tesseract;
  }

  @Override
  public String getText(byte[] imageBytes) throws IllegalArgumentException {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
      BufferedImage image = ImageIO.read(bais);
      return this.tesseract.doOCR(image);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}
