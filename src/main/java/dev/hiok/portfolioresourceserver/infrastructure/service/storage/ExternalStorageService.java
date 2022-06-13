package dev.hiok.portfolioresourceserver.infrastructure.service.storage;

import org.springframework.beans.factory.annotation.Autowired;

import com.dropbox.core.v2.DbxClientV2;

import dev.hiok.portfolioresourceserver.core.config.storage.StorageProperties;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.infrastructure.exception.StorageException;

public class ExternalStorageService implements StorageService {
  
  @Autowired
  private StorageProperties storageProperties;

  @Autowired
  private DbxClientV2 dbxClient;

  @Override
  public RecoveredFile recover(String filename) {
    try {
      String filePath = getFilePath(filename);

      String url = dbxClient.files().getTemporaryLink(filePath).getLink();
      
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
      String filePath = getFilePath(newFile.getFilename());

      dbxClient.files().uploadBuilder(filePath)
        .uploadAndFinish(newFile.getInputStream());

    } catch (Exception ex) {
      throw new StorageException("Could not store file", ex);
    }
  }

  @Override
  public void remove(String filename) {
    try {
      String filePath = getFilePath(filename);

      dbxClient.files().deleteV2(filePath);

    } catch (Exception ex) {
      throw new StorageException("Could not remove file", ex);
    }
  }

  private String getFilePath(String filename) {
    return String.format("%s/%s", storageProperties.getExternal().getDirectory(), filename);
  }

}
