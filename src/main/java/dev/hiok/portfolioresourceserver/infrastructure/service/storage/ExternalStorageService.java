package dev.hiok.portfolioresourceserver.infrastructure.service.storage;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hiok.portfolioresourceserver.core.config.storage.StorageProperties;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.infrastructure.exception.StorageException;

@Service
public class ExternalStorageService implements StorageService {
  
  @Autowired
  private StorageProperties storageProperties;

  @Override
  public RecoveredFile recover(String filename) {
    try {
      String filePath = getFilePath(filename);

      //todo get from external
      URL url = new URL(filePath); 
      //todo get from external

      RecoveredFile recoveredFile = RecoveredFile.builder()
        .url(url.toString())
        .build();

      return recoveredFile;
      
    } catch (Exception ex) {
      throw new StorageException("Could not recover file", ex);
    }
  }

  @Override
  public void store(NewFile newFile) {
    try {
      String arquivoPath = getFilePath(newFile.getFilename());

      System.out.println(arquivoPath);
      //todo: store in external

    } catch (Exception ex) {
      throw new StorageException("Could not store file", ex);
    }
  }

  @Override
  public void remove(String filename) {
    try {
      String arquivoPath = getFilePath(filename);

      System.out.println(arquivoPath);
      //todo: delete in external

    } catch (Exception ex) {
      throw new StorageException("Could not remove file", ex);
    }
  }

  private String getFilePath(String filename) {
    return String.format("%s%s", storageProperties.getExternal().getDirectory(), filename);
  }

}
