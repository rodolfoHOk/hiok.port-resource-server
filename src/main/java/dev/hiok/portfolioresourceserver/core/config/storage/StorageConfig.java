package dev.hiok.portfolioresourceserver.core.config.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import dev.hiok.portfolioresourceserver.core.config.storage.StorageProperties.StorageType;
import dev.hiok.portfolioresourceserver.domain.service.StorageService;
import dev.hiok.portfolioresourceserver.infrastructure.service.storage.ExternalStorageService;
import dev.hiok.portfolioresourceserver.infrastructure.service.storage.LocalStorageService;

@Configuration
public class StorageConfig {
  
  @Autowired
  private StorageProperties storageProperties;

  @Bean
  @ConditionalOnProperty(name = "portfolio.storage.type", havingValue = "external")
  public DbxClientV2 dbxClient() {
    DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("portfolio-hiok-dev/1.0.0").build();
    return new DbxClientV2(requestConfig, storageProperties.getExternal().getApiToken());
  }

  @Bean
  public StorageService storageService() {
    
    if (StorageType.EXTERNAL.toString().equalsIgnoreCase(storageProperties.getType().toString())) {
      return new ExternalStorageService();
    } else {
      return new LocalStorageService();
    }
  } 
}
