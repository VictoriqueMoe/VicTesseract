package moe.victorique.OCR.service;

public interface IOcrService {
  String getText(byte[] imageBytes) throws IllegalArgumentException;
}
