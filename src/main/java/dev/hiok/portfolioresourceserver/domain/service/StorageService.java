package dev.hiok.portfolioresourceserver.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface StorageService {
  
  RecoveredFile recover(String filename);

  void store(NewFile newFile);

  void remove(String filename);

  default void replace(String existingFilename, NewFile newFile) {
    if (existingFilename != null) {
      this.remove(existingFilename);
    }
    this.store(newFile);
  }

  default String generateFileName(String originalFilename) {
    return UUID.randomUUID().toString() + "_" + originalFilename;
  }

  @Getter
  @Builder
  class RecoveredFile {
    private InputStream inputStream;
    private String url;

    public Boolean hasURL() {
      return url != null;
    }

    public Boolean hasInputStream() {
      return inputStream != null;
    }
  }

  @Getter
  @Builder
  class NewFile {
    private String filename;
    private String contentType;
    private InputStream inputStream;
  }
  
}
