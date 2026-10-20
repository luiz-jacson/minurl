# MinUrl

MinUrl é um encurtador de URLs desenvolvido como projeto de estudo e portfólio. A aplicação permite transformar URLs longas em links curtos e redirecionar os visitantes para o endereço original.

**Aplicação online:** https://minurl.onrender.com

## Funcionalidades

- Criação de links curtos.
- Persistência dos dados em PostgreSQL.
- Data de expiração opcional e tratamento de links expirados.
- Gerar/baixar QR Code.
- Páginas personalizadas para erros, como 404 e 405.
- Segurança de URLs e Google Safe Browsing para validar urls e prevenir ameaças conhecidas, como phishing ou distribuição de malware.

## Tecnologias

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA e Hibernate
- Thymeleaf e Thymeleaf Layout Dialect
- PostgreSQL
- Bootstrap 5.3.3
- Maven
- Docker
- Render para hospedagem e Neon como serviço PostgreSQL

## Endpoints principais

| Método | Caminho         | Descrição |
|---|-----------------|---|
| `GET` | `/`             | Exibe a página inicial e o formulário de encurtamento. |
| `POST` | `/create-url`   | Recebe o formulário e cria uma URL curta. |
| `GET` | `/s/{shortKey}` | Redireciona para a URL original, se o link existir e estiver válido. |
| `GET` | `/about`        | Exibe a página Sobre o projeto. |

As páginas de erro são renderizadas pelos templates do projeto. A página de expiração é exibida pelo fluxo de validação de links expirados, sem exigir uma rota pública `/expired`.

## Estrutura do projeto

A aplicação segue uma organização por responsabilidades:

```text
src/main/java/minurl/
├── domain/
│   ├── entities/       # Entidades persistidas
│   ├── exceptions/     # Exceções de domínio
│   ├── models/         # Objetos usados no domínio e nos comandos
│   ├── repositories/   # Acesso aos dados
│   ├── service/        # Regras de negócio
│   └── util/           # Utilitários, como geração de chave curta
└── web/
    ├── controllers/    # Endpoints e páginas
    └── dto/            # Formulários e objetos de transporte

src/main/resources/
├── static/             # CSS, JavaScript e outros arquivos estáticos
├── templates/          # Templates Thymeleaf
└── application.properties
```


O MinUrl é um projeto de portfólio para praticar desenvolvimento web com Java e Spring Boot, organização em camadas, persistência relacional, validação de formulários e publicação de uma aplicação.
