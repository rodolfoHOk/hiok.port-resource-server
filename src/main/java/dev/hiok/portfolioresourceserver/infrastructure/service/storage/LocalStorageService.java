package dev.hiok.portfolioresourceserver.infrastructure.service.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import dev.hiok.portfolioresourceserver.core.config.storage.StorageProperties;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.infrastructure.exception.StorageException;


@Service
public class LocalStorageService implements StorageService {

  @Autowired
  private StorageProperties storageProperties;

  @Override
  public RecoveredFile recover(String filename) {
    try {
      Path filePath = getFilePath(filename);

      RecoveredFile recoveredFile = RecoveredFile.builder()
        .inputStream(Files.newInputStream(filePath))
        .build();

      return recoveredFile;
      
    } catch (IOException ex) {
      throw new StorageException("Could not recover file", ex);
    }
  }

  @Override
  public void store(NewFile newFile) {
    try {
      Path arquivoPath = getFilePath(newFile.getFilename());

      FileCopyUtils.copy(newFile.getInputStream(), Files.newOutputStream(arquivoPath));

    } catch (IOException ex) {
      throw new StorageException("Could not store file", ex);
    }
  }

  @Override
  public void remove(String filename) {
    try {
      Path arquivoPath = getFilePath(filename);

      Files.deleteIfExists(arquivoPath);

    } catch (IOException ex) {
      throw new StorageException("Could not remove file", ex);
    }
  }

  private Path getFilePath(String filename) {
    return storageProperties.getLocal().getDirectory().resolve(Path.of(filename));
  }

}
