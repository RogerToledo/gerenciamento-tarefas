## 🛠️ Stack Tecnológica

| Categoria | Tecnologia | Descrição / Detalhes |
| :--- | :--- | :--- |
| **Linguagem** | Java 25 | Versão mais recente do Java (uso de Records, Pattern Matching) |
| **Framework Web** | Spring Boot 3.x | Framework base para a criação da API REST |
| **Gerenciador de Build**| Maven | Gestão de dependências e automação de build |
| **Banco de Dados** | MySQL 8.0 | Banco relacional executado via Docker |
| **Persistência / ORM** | Spring Data JPA / Hibernate | Mapeamento objeto-relacional e queries |
| **Migrações de Banco** | Flyway | Versionamento do schema do banco de dados |
| **Testes** | JUnit 5 & Mockito | Testes unitários e de integração |

---

## 🏗️ Arquitetura de Código (Clean Architecture)

O projeto adota os princípios de **Clean Architecture** (Arquitetura Limpa) e **Clean Code**, garantindo alto desacoplamento, testabilidade e independência de frameworks. O domínio da aplicação não possui dependências de bibliotecas externas.

### Estrutura de Pacotes (`src/main/java/com/bootcamp/app/`):

```text
├── domain/                      # [Core] Regras de negócio puras
│   ├── model/                   # Entidades e Objetos de Valor (Records/Classes)
│   └── exception/               # Exceções de domínio
│
├── application/                 # [Casos de Uso]
│   ├── usecases/                # Interfaces e implementações dos fluxos da aplicação
│   ├── dto/                     # Objetos de transferência de dados (Request/Response)
│   └── ports/                   # Interfaces dos adaptadores (Repositories/Gateways)
│
└── infrastructure/              # [Detalhes de Implementação]
    ├── web/                     # REST Controllers, Handlers de erro global
    ├── persistence/             # Mappers, JPA Repositories e Entidades de Banco
    └── config/                  # Beans do Spring e configurações do sistema

