package dev.hiok.portfolioresourceserver.infrastructure.service.storage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.hiok.portfolioresourceserver.core.config.storage.StorageProperties;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.domain.service.StorageService.RecoveredFile;
import dev.hiok.portfolioresourceserver.domain.service.StorageService.NewFile;
import dev.hiok.portfolioresourceserver.infrastructure.exception.StorageException;


@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = StorageProperties.class)
@TestPropertySource("classpath:application-test.properties")
public class LocalStorageServiceTest {

  @Configuration
  static class StorageConfig {
    @Bean
    public StorageService storageService() {
      return new LocalStorageService();
    }
  }
    
  @Autowired
  private StorageService storageService;

  @Test
  void shouldReturnARecoveredFileWhenRecoverCalledWithAValidFilename() {
    String filename = "5f060449-7aec-476a-986f-8d9e26d7113c_screenshot.png";
    
    RecoveredFile recoveredFile = storageService.recover(filename);

    assertThat(recoveredFile.getInputStream(), is(notNullValue()));
  }

  @Test
  void shouldThrowStorageExceptionWhenRecoverCalledWithAInvalidFilename() {
    String invalidFilename = "invalid_filename_screenshot.png";
    
    assertThrows(StorageException.class, () -> storageService.recover(invalidFilename));
  }

  @Test
  void shouldStoreAFileWhenStoreCalled() throws IOException {
    String filename = "test.txt";
    InputStream fileData = new ByteArrayInputStream("test data".getBytes());
    NewFile newFile = NewFile.builder()
      .filename(filename)
      .contentType(MediaType.TEXT_PLAIN_VALUE)
      .inputStream(fileData)
      .build();

    storageService.store(newFile);
    RecoveredFile recoveredFile = storageService.recover(filename);
    assertThat(recoveredFile, is(notNullValue()));
  }

  @Test
  void shouldRemoveAFileWhenRemoveCalled() {
    String filename = "test.txt";
    InputStream fileData = new ByteArrayInputStream("test data".getBytes());
    NewFile newFile = NewFile.builder()
      .filename(filename)
      .contentType(MediaType.TEXT_PLAIN_VALUE)
      .inputStream(fileData)
      .build();
    storageService.store(newFile);

    assertDoesNotThrow(() -> storageService.remove(filename));
  }
}
