package dev.hiok.portfolioresourceserver.core.config.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("portfolio.storage")
public class StorageProperties {
  
  private Local local = new Local();
  private External external = new External();
  private StorageType type = StorageType.LOCAL;

  public enum StorageType {
    LOCAL,
    EXTERNAL
  }

  @Getter
  @Setter
  public class Local {
    private Path directory;
  }

  @Getter
  @Setter
  public class External {
    private String clientId;
    private String clientSecret;
    private String region;
    private String bucket;
    private String directory;
  }
  
}
