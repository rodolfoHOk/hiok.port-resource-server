package dev.hiok.portfolioresourceserver.core.config.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.hiok.portfolioresourceserver.core.config.storage.StorageProperties.StorageType;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.infrastructure.service.storage.ExternalStorageService;
import dev.hiok.portfolioresourceserver.infrastructure.service.storage.LocalStorageService;

@Configuration
public class StorageConfig {
  
  @Autowired
  private StorageProperties storageProperties;

  @Bean
  public StorageService storageService() {
    
    if (StorageType.EXTERNAL.equals(storageProperties.getType())) {
      return new ExternalStorageService();
    } else {
      return new LocalStorageService();
    }
  } 
}
