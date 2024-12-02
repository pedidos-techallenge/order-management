# Gestão de Pedidos - Microserviço

Este é um microserviço para a gestão de pedidos de um restaurante. A aplicação permite que os pedidos sejam gerenciados através de diferentes status: **Aberto**, **Recebido**, **Em Preparação**, **Pronto** e **Entregue**.

## Tecnologias Utilizadas

- **Banco de Dados**: MongoDB Atlas
- **Implantação**: EKS (Elastic Kubernetes Service) na AWS
- **API Gateway**: Utiliza um proxy reverso para gerenciar as requisições
- **Infraestrutura como Código**: Terraform para provisionamento na AWS

## Pré-requisitos

Antes de começar, você precisará ter instalado:

- [Docker](https://www.docker.com/get-started)
- [Maven](https://maven.apache.org/install.html)
- [Terraform](https://www.terraform.io/downloads.html)
- Acesso à conta do [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
- Acesso à conta da [AWS](https://aws.amazon.com/)

## Implantação

1. **Clone o repositório**:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd <NOME_DO_REPOSITORIO>
   ```

2. **Configuração do MongoDB Atlas**:
   - Crie um projeto no MongoDB Atlas.
   - Configure as credenciais e o acesso ao banco de dados.
   - Atualize as variáveis de ambiente no arquivo `terraform/main.tf` com seu `org_id` do MongoDB Atlas e o IP de acesso.

3. **Provisionamento da Infraestrutura**:
   - Navegue até o diretório do Terraform:
     ```bash
     cd infrastructure/terraform
     ```
   - Execute os comandos do Terraform:
     ```bash
     terraform init
     terraform apply
     ```

4. **Construção da Imagem Docker**:
   - No diretório raiz do projeto, execute:
     ```bash
     docker build -t order-management .
     ```

## Comandos do Makefile

Você pode usar os seguintes comandos do Makefile para facilitar o processo de construção e testes:

```Makefile
build:
	mvn compile

unit-test:
	mvn test -P test

integration-test:
	mvn test -P integration-test

system-test:
	mvn test -P system-test

docker-build:
	docker build -t order-management .
```

## Implantação Automática com GitHub Actions

A implantação da aplicação é gerenciada automaticamente através do GitHub Actions. Quando você faz um push para a branch `main`, o fluxo de trabalho definido em `.github/workflows/deploy.yaml` é acionado. Este fluxo de trabalho inclui as seguintes etapas:

1. **CI Pipeline**: Executa testes e cria a imagem Docker.
2. **MongoDB Atlas**: Provisiona o cluster MongoDB Atlas.
3. **Deploy da Aplicação**: Implanta a aplicação no EKS, incluindo a criação de segredos e configurações necessárias.

Para mais detalhes sobre o fluxo de trabalho, consulte o arquivo `.github/workflows/deploy.yaml`.

## Execução

Após a implantação, você pode acessar a aplicação através do API Gateway configurado. Utilize as rotas apropriadas para interagir com os pedidos.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir um pull request ou relatar problemas.

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
