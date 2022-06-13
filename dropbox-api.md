## Dropbox Api

- Dropbox for Java Developers

        https://www.dropbox.com/developers/documentation/java

- Dropbox for Java tutorial

        https://github.com/dropbox/dropbox-sdk-java#dropbox-for-java-tutorial

- Dropbox for Java Documentation

        https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/

- Dropbox developer console

        https://www.dropbox.com/developers/apps

- Dropbox login

        https://www.dropbox.com/login

### Guia passo a passo

        I - Fazer login ou criar conta no Dropbox:
          Acessar Dropbox login

        II - Criar um app no developer console do Dropbox:
          Acessar Dropbox developer console
          > Create app:
            1. checar Scoped access
            2. checar App folder
            3. preencher App name
          Aba Settings:
            App folder name: > change: mudar se quiser
            OAuth 2: Generated access token: > Generate
            Copiar e gradar em local seguro
          Aba Permissions:
            Files and folders: checar files.content.write e files.content.read
            > Submit

        III - Environment Variables:
          Adicionar storage type "dropbox"
          Adicionar o Dropbox api token que foi gerado
          Adicionar o Dropbox app folder configurado

        IV - Dependência no pom.xml
          <dependency>
            <groupId>com.dropbox.core</groupId>
            <artifactId>dropbox-core-sdk</artifactId>
            <version>5.2.0</version>
          </dependency>

        V - Storage config:
          @Autowired
          private StorageProperties storageProperties;

          @Bean
          @ConditionalOnProperty(name = "app-name.storage.type", havingValue = "dropbox")
          public DbxClientV2 dbxClient() {
            DbxRequestConfig requestConfig = 
              DbxRequestConfig.newBuilder("app-name/version ex: 1.0.0").build();
            return new DbxClientV2(requestConfig, storageProperties.getExternal().getApiToken());
          }

        VI - Storage service:
          Injetar StorageProperties storageProperties;

          Injetar DbxClientV2 dbxClient;

          private String getFilePath(String filename) {
            return String.format("%s/%s", storageProperties.getExternal().getDirectory(), 
              filename);
          }

          1. Upload:
            String filePath = getFilePath(newFile.getFilename());
            dbxClient.files().uploadBuilder(filePath).uploadAndFinish(newFile.getInputStream());
          
          2. Get url:
            String filePath = getFilePath(filename);
            String url = dbxClient.files().getTemporaryLink(filePath).getLink();

          3. Delete:
            String filePath = getFilePath(filename);
            dbxClient.files().deleteV2(filePath);
