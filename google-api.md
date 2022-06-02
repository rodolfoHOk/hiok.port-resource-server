### Google Drive API

- Google Drive for developers - Java Quickstart: 

        https://developers.google.com/drive/api/quickstart/java

- Create a Google Cloud project:

        https://developers.google.com/workspace/guides/create-project

- Google Cloud Console:

        https://console.cloud.google.com/

- Enable Google Workspace APIs:

        https://developers.google.com/workspace/guides/enable-apis

- Configure the OAuth consent screen:

        https://developers.google.com/workspace/guides/configure-oauth-consent

- Create access credentials

        https://developers.google.com/workspace/guides/create-credentials

- Passo a passo:

        I - Criar um projeto no Google Cloud:
          Acessar o Google Cloud Console
          Menu > IAM e administrador > Criar um projeto
          Preencher Nome do projeto > Criar

        II - Habilitar Google Workspace APIs:
          Acessar o Google Cloud Console
          Menu > APIs e serviços > Biblioteca
          Pesquisar APIs e serviços > pesquisar google drive api
          Acessar o Google Drive API > Ativar

        III - Configurar Tela de permissão OAuth:
          Acessar o Google Cloud Console
          Menu > APIs e serviços > Tela de permissão OAuth
          User Type: selecionar Externo > Criar
          Informações do app: preencher Nome do app e E-mail para suporte do usuário
          Dados de contato do desenvolvedor: preencher Endereços de e-mail > Salvar e continuar
          Escopos > Salvar e continuar
          Usuários de teste > Salvar e continuar
          Resumo > Voltar para o painel

        IV - Criar Credenciais de Acesso:
          Acessar o Google Cloud Console
          Menu > APIs e serviços > Credenciais
          > + Criar credenciais > ID do cliente do OAuth
          Tipo de Aplicativo: selecionar Aplicativo da WEB
          Nome: preencher Nome
          Origens JavaScript autorizadas > Adicionar URI: preencher (ex: http://localhost)
          URIs de redirecionamento autorizados > Adicionar URI: 
            preencher (ex: http://localhost:3000/auth/callback/google)
          > Criar
          Copiar e guardar em local seguro
          Fazer o download do json