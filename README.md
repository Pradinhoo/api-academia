# API Academia

API REST para gerenciamento de academias desenvolvida em Java com Spring Boot. Permite o controle de alunos, professores, treinos e exercícios, com persistência em banco de dados PostgreSQL.

---

## Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Spring Security 
- Swagger/OpenAPI 

---

## Funcionalidades

- CRUD completo para alunos, professores, treinos e exercícios
- Relacionamentos entre entidades para controle de treinos
- Estrutura organizada em camadas (Controller, Service, Repository)
- Integração com banco de dados PostgreSQL

---

## Como executar o projeto

### Pré-requisitos

- Java JDK 17+ instalado
- PostgreSQL configurado e rodando
- Maven instalado
- Git instalado

### Passos para rodar

1. Clone o repositório

```bash
git clone https://github.com/Pradinhoo/api-academia.git
cd api-academia
```

2. Configure o banco de dados PostgreSQL criando um banco para a aplicação.

3. Configure as variáveis no arquivo `src/main/resources/application.properties` (ou `application.yml`) com as informações do seu banco, por exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. Compile e rode a aplicação:

```bash
mvn clean spring-boot:run
```

5. A API estará disponível em `http://localhost:8080`

---

## Endpoints principais

- `GET /alunos` - Listar alunos
- `POST /alunos` - Criar novo aluno
- `GET /alunos/{id}` - Buscar aluno por ID
- `PUT /alunos/{id}` - Atualizar aluno
- `DELETE /alunos/{id}` - Deletar aluno

_Similar para professores, treinos e exercícios._

---

## Melhorias sugeridas

- Implementar autenticação e autorização (ex: Spring Security + JWT)
- Validar dados de entrada com Bean Validation (javax.validation)
- Documentar API com Swagger/OpenAPI
- Implementar testes unitários e de integração
- Criar tratamento global para exceções

---

## Contribuição

Contribuições são bem-vindas! Faça um fork, crie sua branch e envie um pull request.

## Contato

Abra uma issue ou envie mensagem para colaborar ou tirar dúvidas.
