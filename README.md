# Car Rental System

A console-based car rental management application developed in Java 21, using Hibernate ORM and PostgreSQL for data persistence. The project focuses on object-oriented programming, relational database modeling, entity relationships, ORM mapping, transaction management, input validation, and business logic implementation.

The application provides a complete rental management workflow, covering vehicle and customer registration, rental operations, availability management, rental history, and statistical reports.

This project was developed as part of my journey toward becoming a backend developer, with an emphasis on understanding how Java applications interact with relational databases through Hibernate ORM and Jakarta Persistence (JPA).

## Technologies

- Java 21
- Apache Maven
- Hibernate ORM 7.4.12.Final
- PostgreSQL
- PostgreSQL JDBC Driver 42.7.13
- Jakarta Persistence (JPA)
- IntelliJ IDEA

## Features

### Car Management

- Register vehicles with license plate, brand, model, daily rate, and available features.
- Search vehicles by license plate.
- View vehicle information and rental history.
- Update vehicle daily rental rates.
- Associate multiple features with a single vehicle.
- Track vehicles currently assigned to active rentals.

### Customer Management

- Register customers with CPF, name, email, and phone number.
- Search customers by CPF.
- View customer information and rental history.
- Update customer email addresses.
- Update customer phone numbers.
- Track customers with active rentals.
- Prevent duplicate customer registrations through database constraints.

### Rental Management

- Register rentals for one or multiple vehicles.
- Associate rentals with registered customers.
- Calculate rental prices based on vehicle daily rates and rental duration.
- Automatically classify rentals as `ACTIVE` or `RESERVED` according to their initial date.
- Prevent customers and vehicles from being assigned to conflicting active rentals.
- Cancel existing rentals.
- Complete active rentals.
- Change rental return dates and recalculate rental prices.
- Maintain rental history for both customers and vehicles.

### Rental Reports and Statistics

- Search rental records using available filters.
- Retrieve rental information by rental ID.
- Search rentals by customer information.
- Filter rental records by dates and status.
- Generate general rental statistics.
- Calculate average rental values.
- Retrieve customer-specific rental statistics.

### Data Validation and Integrity

- Validate user input using regular expressions and dedicated validation methods.
- Validate CPF, email, phone numbers, license plates, vehicle information, and rental dates.
- Validate monetary values using `BigDecimal`.
- Use `LocalDate` for rental date management.
- Apply database constraints to enforce uniqueness and valid data.
- Handle database constraint violations and invalid operations.
- Use Hibernate transactions to manage database operations.
- Separate business logic from database access through Service and DAO layers.

## Project Structure

The project follows a layered structure that separates user interaction, input validation, business logic, database operations, and entity mapping.

```text
FinalRentalSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java
│       │           ├── InputValidation.java
│       │           ├── Service.java
│       │           ├── DAO classes
│       │           ├── Car.java
│       │           ├── Customer.java
│       │           ├── Rental.java
│       │           ├── Feature.java
│       │           └── HibernateUtil.java
│       └── resources/
│           └── hibernate.cfg.xml
├── rentalSystem_database
├── pom.xml
└── README.md
```

### Architecture

- **Main:** Handles application menus, user interaction, and application flow.
- **InputValidation:** Centralizes user input validation and prevents invalid data from reaching business operations.
- **Service:** Implements business logic, rental rules, calculations, and transaction management.
- **DAO:** Handles database operations, including entity persistence, retrieval, updates, and queries.
- **Car:** Represents vehicles and their associated features, active rentals, and rental history.
- **Customer:** Represents registered customers, their personal information, active rentals, and rental history.
- **Rental:** Represents rental records, including associated customers, vehicles, dates, prices, and statuses.
- **Feature:** Represents vehicle features and their associations with vehicles.
- **HibernateUtil:** Configures and provides the application's Hibernate `SessionFactory`.
- **hibernate.cfg.xml:** Contains Hibernate configuration settings.
- **rentalSystem_database:** Contains the PostgreSQL database structure required to recreate the project schema.

## Database Structure and Entity Relationships

The application uses PostgreSQL with a dedicated schema named `final_rental`.

The database model was designed around four main entities: `Car`, `Customer`, `Rental`, and `Feature`.

### Entity Relationships

The project implements the following relationships using Jakarta Persistence annotations:

**1. Customer ↔ Rental**

- `@OneToMany` on Customer, using `mappedBy = "customer"`.
- `@ManyToOne` on Rental, using `customer_cpf` as the foreign key.
- `@OneToOne` on Customer for direct access to the customer's active rental.

Each customer can have multiple historical rental records, while each rental is associated with a customer. The additional active rental reference allows the application to retrieve the customer's current rental directly.

**2. Rental ↔ Car**

- `@ManyToMany` on Rental.
- `@ManyToMany(mappedBy = "carList")` on Car.
- Uses the `rental_cars` join table.

A rental can contain multiple vehicles, and a vehicle can be associated with multiple rental records over time. This relationship allows the application to maintain rental history without losing the association between vehicles and previous rentals.

**3. Car ↔ Feature**

- `@ManyToMany` on Car.
- `@ManyToMany(mappedBy = "carFeatures")` on Feature.
- Uses the `car_features` join table.

A vehicle can have multiple features, and the same feature can be associated with multiple vehicles.

**4. Car → Active Rental**

- `@ManyToOne` on Car.
- Uses the `active_rental` foreign key.

This association allows multiple vehicles belonging to the same rental to reference the rental currently occupying them.

### ORM Mapping Concepts

The project demonstrates practical use of:

- `@Entity` and `@Table` for entity-to-table mapping.
- `@Id` and `@GeneratedValue` for primary key configuration.
- `@Column` for column configuration and constraints.
- `@OneToOne`, `@OneToMany`, `@ManyToOne`, and `@ManyToMany` for relationship mapping.
- `@JoinColumn` for foreign key configuration.
- `@JoinTable` for many-to-many join tables.
- `mappedBy` for bidirectional relationship ownership.
- `FetchType.EAGER` for explicitly configured eager associations.
- `@Generated` for database-generated entity values.

These mappings allow Hibernate to manage entity relationships and synchronize changes with the relational database.

### Database Tables

The application uses the following main tables:

- `final_rental.cars`
- `final_rental.customers`
- `final_rental.rentals`
- `final_rental.features`
- `final_rental.rental_cars`
- `final_rental.car_features`

Additional foreign keys and database constraints enforce the relationships and integrity rules.

## Getting Started

### Prerequisites

Install the following:

- Java JDK 21
- Apache Maven
- PostgreSQL
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/bastosarthur/FinalRentalSystem.git
cd FinalRentalSystem
```

### 2. Create a PostgreSQL Database

Create a PostgreSQL database to host the project schema.

For example:

```sql
CREATE DATABASE projects;
```

You can use IntelliJ IDEA's PostgreSQL interface or another PostgreSQL client to execute this command.

### 3. Import the Database Schema

The repository contains the `rentalSystem_database` schema script.

Execute the script against the database you created:

```bash
psql -U postgres -d projects -f rentalSystem_database
```

Replace `postgres` with your PostgreSQL username if necessary.

If `psql` is not available in your system's PATH, use its full installation path. For example, on Windows with PostgreSQL 17 installed in the default directory:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d projects -f rentalSystem_database
```

The schema import creates the required database structure, including tables, relationships, and constraints.

No application records are provided.

### 4. Configure Environment Variables

The application retrieves database connection settings through environment variables, avoiding hardcoded database credentials in the Java source code.

Configure the following variables in your operating system or IDE run configuration:

| Variable | Example value |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/projects` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | Your local PostgreSQL password |

Use your own PostgreSQL credentials. If you chose a different database name, update `DB_URL` accordingly.

**Important:** Never commit your actual database password or other credentials to the repository.

### 5. Build the Project

From the project directory, execute:

```bash
mvn clean package
```

Maven will resolve the required dependencies and compile the application.

### 6. Run the Application

Open the project in IntelliJ IDEA and execute `Main.java` after configuring the environment variables and importing the database schema.

The application is ready to use.

## Learning Outcomes

Through this project, I practiced and applied:

- Designing and implementing a relational database model for a multi-entity application.
- Applying object-oriented programming principles in a multi-class Java application.
- Mapping Java entities to PostgreSQL tables using Hibernate ORM and Jakarta Persistence.
- Implementing and managing One-to-One, One-to-Many, Many-to-One, and Many-to-Many relationships.
- Understanding owning and inverse sides of bidirectional entity relationships.
- Working with foreign keys and join tables.
- Managing Hibernate sessions, persistence contexts, and transactions.
- Understanding entity state changes and Hibernate dirty checking.
- Implementing database operations through DAO and Service layers.
- Implementing business rules and preventing conflicting rental operations.
- Working with HQL/JPQL and database queries.
- Handling database constraints and persistence errors.
- Using `BigDecimal` for monetary calculations and `LocalDate` for date operations.
- Implementing input validation and handling invalid operations.
- Using Maven for dependency management and project builds.
- Using Git and GitHub for version control and portfolio development.

## Future Improvements

Potential improvements as I continue learning include:

- Unit and integration tests.
- A REST API using Spring Boot.
- Centralized exception handling.
- Structured application logging.
- Docker-based application and database deployment.
- Cloud deployment and database management.

## Disclaimer

This project was developed for educational and portfolio purposes. It is a console-based application and is not intended to serve as a production-ready commercial car rental platform.

---

# Sistema de Locação de Veículos

Uma aplicação de gerenciamento de aluguel de veículos executada pelo console, desenvolvida em Java 21, utilizando Hibernate ORM e PostgreSQL para persistência de dados. O projeto tem como foco programação orientada a objetos, modelagem de bancos de dados relacionais, mapeamento de entidades, gerenciamento de transações, validação de entradas e implementação de regras de negócio.

A aplicação oferece um fluxo completo de gerenciamento de locações, incluindo cadastro de veículos e clientes, operações de aluguel, controle de disponibilidade, histórico de locações e relatórios estatísticos.

Este projeto foi desenvolvido como parte da minha jornada para me tornar desenvolvedor backend, com ênfase em compreender como aplicações Java interagem com bancos de dados relacionais por meio do Hibernate ORM e Jakarta Persistence (JPA).

## Tecnologias

- Java 21
- Apache Maven
- Hibernate ORM 7.4.12.Final
- PostgreSQL
- PostgreSQL JDBC Driver 42.7.13
- Jakarta Persistence (JPA)
- IntelliJ IDEA

## Funcionalidades

### Gerenciamento de Veículos

- Cadastrar veículos com placa, marca, modelo, diária e características.
- Pesquisar veículos pela placa.
- Consultar informações dos veículos e seus históricos de locação.
- Atualizar o valor da diária.
- Associar múltiplas características a um mesmo veículo.
- Controlar veículos atualmente associados a locações ativas.

### Gerenciamento de Clientes

- Cadastrar clientes com CPF, nome, email e telefone.
- Pesquisar clientes pelo CPF.
- Consultar informações e histórico de locações dos clientes.
- Atualizar endereços de email.
- Atualizar números de telefone.
- Identificar clientes com locações ativas.
- Impedir cadastros duplicados por meio de restrições no banco de dados.

### Gerenciamento de Locações

- Registrar locações de um ou múltiplos veículos.
- Associar locações a clientes cadastrados.
- Calcular o valor das locações com base nas diárias dos veículos e na duração do aluguel.
- Classificar automaticamente as locações como `ACTIVE` ou `RESERVED`, de acordo com a data inicial.
- Impedir que clientes e veículos sejam associados a locações ativas conflitantes.
- Cancelar locações existentes.
- Finalizar locações ativas.
- Alterar datas de devolução e recalcular os valores das locações.
- Manter o histórico de locações tanto para clientes quanto para veículos.

### Relatórios e Estatísticas

- Pesquisar registros de locações utilizando filtros disponíveis.
- Consultar informações de locações pelo ID.
- Pesquisar locações por informações do cliente.
- Filtrar registros por datas e status.
- Gerar estatísticas gerais das locações.
- Calcular valores médios de locação.
- Consultar estatísticas específicas de cada cliente.

### Validação e Integridade dos Dados

- Validar entradas utilizando expressões regulares e métodos específicos de validação.
- Validar CPF, email, telefone, placas, informações dos veículos e datas das locações.
- Validar valores monetários utilizando `BigDecimal`.
- Utilizar `LocalDate` para gerenciamento de datas.
- Aplicar restrições no banco de dados para garantir unicidade e validade dos dados.
- Tratar violações de restrições do banco e operações inválidas.
- Utilizar transações do Hibernate para gerenciar operações no banco.
- Separar a lógica de negócio do acesso ao banco por meio das camadas Service e DAO.

## Estrutura do Projeto

O projeto utiliza uma arquitetura em camadas para separar a interação com o usuário, validação de entradas, lógica de negócio, operações de banco de dados e mapeamento das entidades.

```text
FinalRentalSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java
│       │           ├── InputValidation.java
│       │           ├── Service.java
│       │           ├── Classes DAO
│       │           ├── Car.java
│       │           ├── Customer.java
│       │           ├── Rental.java
│       │           ├── Feature.java
│       │           └── HibernateUtil.java
│       └── resources/
│           └── hibernate.cfg.xml
├── rentalSystem_database
├── pom.xml
└── README.md
```

### Arquitetura

- **Main:** Gerencia os menus, a interação com o usuário e o fluxo da aplicação.
- **InputValidation:** Centraliza a validação das entradas e impede que dados inválidos cheguem às operações de negócio.
- **Service:** Implementa a lógica de negócio, as regras de locação, os cálculos e o gerenciamento de transações.
- **DAO:** Gerencia as operações de banco de dados, incluindo persistência, recuperação, atualização e consultas de entidades.
- **Car:** Representa os veículos e suas características, locações ativas e histórico de locações.
- **Customer:** Representa os clientes cadastrados, suas informações pessoais, locações ativas e histórico de locações.
- **Rental:** Representa os registros de locação, incluindo clientes, veículos, datas, valores e status.
- **Feature:** Representa as características dos veículos e suas associações.
- **HibernateUtil:** Configura e disponibiliza a `SessionFactory` do Hibernate.
- **hibernate.cfg.xml:** Contém as configurações do Hibernate.
- **rentalSystem_database:** Contém a estrutura PostgreSQL necessária para recriar o esquema do projeto.

## Estrutura do Banco de Dados e Relacionamentos entre Entidades

A aplicação utiliza PostgreSQL com um esquema dedicado chamado `final_rental`.

O modelo do banco foi desenvolvido em torno de quatro entidades principais: `Car`, `Customer`, `Rental` e `Feature`.

### Relacionamentos entre Entidades

O projeto implementa os seguintes relacionamentos utilizando as anotações do Jakarta Persistence:

**1. Customer ↔ Rental**

- `@OneToMany` em Customer, utilizando `mappedBy = "customer"`.
- `@ManyToOne` em Rental, utilizando `customer_cpf` como chave estrangeira.
- `@OneToOne` em Customer para acesso direto à locação ativa do cliente.

Cada cliente pode possuir diversos registros históricos de locação, enquanto cada locação está associada a um cliente. A referência adicional à locação ativa permite recuperar diretamente a locação atual do cliente.

**2. Rental ↔ Car**

- `@ManyToMany` em Rental.
- `@ManyToMany(mappedBy = "carList")` em Car.
- Utiliza a tabela intermediária `rental_cars`.

Uma locação pode conter múltiplos veículos, e um veículo pode estar associado a diversos registros de locação ao longo do tempo. Esse relacionamento permite manter o histórico de locações sem perder as associações entre veículos e locações anteriores.

**3. Car ↔ Feature**

- `@ManyToMany` em Car.
- `@ManyToMany(mappedBy = "carFeatures")` em Feature.
- Utiliza a tabela intermediária `car_features`.

Um veículo pode possuir diversas características, e uma mesma característica pode estar associada a diversos veículos.

**4. Car → Active Rental**

- `@ManyToOne` em Car.
- Utiliza a chave estrangeira `active_rental`.

Essa associação permite que múltiplos veículos pertencentes à mesma locação referenciem a locação que atualmente os ocupa.

### Conceitos de Mapeamento ORM

O projeto demonstra a utilização prática de:

- `@Entity` e `@Table` para mapeamento de entidades e tabelas.
- `@Id` e `@GeneratedValue` para configuração de chaves primárias.
- `@Column` para configuração de colunas e restrições.
- `@OneToOne`, `@OneToMany`, `@ManyToOne` e `@ManyToMany` para mapeamento de relacionamentos.
- `@JoinColumn` para configuração de chaves estrangeiras.
- `@JoinTable` para tabelas intermediárias de relacionamentos muitos-para-muitos.
- `mappedBy` para definição do lado inverso dos relacionamentos bidirecionais.
- `FetchType.EAGER` para associações configuradas explicitamente com carregamento imediato.
- `@Generated` para valores gerados pelo banco de dados.

Esses mapeamentos permitem que o Hibernate gerencie os relacionamentos entre entidades e sincronize as alterações com o banco de dados relacional.

### Tabelas do Banco de Dados

A aplicação utiliza as seguintes tabelas principais:

- `final_rental.cars`
- `final_rental.customers`
- `final_rental.rentals`
- `final_rental.features`
- `final_rental.rental_cars`
- `final_rental.car_features`

Chaves estrangeiras e restrições adicionais garantem os relacionamentos e a integridade dos dados.

## Como Executar

### Pré-requisitos

Instale os seguintes programas:

- Java JDK 21
- Apache Maven
- PostgreSQL
- Git

### 1. Clonar o Repositório

```bash
git clone https://github.com/bastosarthur/FinalRentalSystem.git
cd FinalRentalSystem
```

### 2. Criar um Banco de Dados PostgreSQL

Crie um banco PostgreSQL para armazenar o esquema do projeto.

Por exemplo:

```sql
CREATE DATABASE projects;
```

Você pode utilizar a interface PostgreSQL do IntelliJ IDEA ou outro cliente PostgreSQL para executar esse comando.

### 3. Importar o Esquema do Banco de Dados

O repositório contém o script de criação do esquema `rentalSystem_database`.

Execute o script no banco de dados criado:

```bash
psql -U postgres -d projects -f rentalSystem_database
```

Substitua `postgres` pelo seu usuário PostgreSQL, se necessário.

Caso o comando `psql` não esteja disponível no PATH do sistema, utilize o caminho completo da instalação. Por exemplo, no Windows com PostgreSQL 17 instalado no diretório padrão:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d projects -f rentalSystem_database
```

A importação cria a estrutura necessária do banco, incluindo tabelas, relacionamentos e restrições.

Nenhum registro da aplicação é fornecido.

### 4. Configurar as Variáveis de Ambiente

A aplicação recupera as configurações de conexão com o banco por meio de variáveis de ambiente, evitando credenciais diretamente no código-fonte Java.

Configure as seguintes variáveis no sistema operacional ou na configuração de execução da IDE:

| Variável | Valor de exemplo |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/projects` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | Sua senha local do PostgreSQL |

Utilize suas próprias credenciais do PostgreSQL. Caso tenha escolhido outro nome para o banco, atualize o valor de `DB_URL`.

**Importante:** Nunca envie sua senha real do banco ou outras credenciais para o repositório.

### 5. Compilar o Projeto

No diretório do projeto, execute:

```bash
mvn clean package
```

O Maven irá resolver as dependências necessárias e compilar a aplicação.

### 6. Executar a Aplicação

Abra o projeto no IntelliJ IDEA e execute `Main.java` após configurar as variáveis de ambiente e importar o esquema do banco de dados.

A aplicação estará pronta para utilização.

## Aprendizados

Durante o desenvolvimento deste projeto, pratiquei e apliquei:

- Modelagem e implementação de um banco de dados relacional para uma aplicação com múltiplas entidades.
- Aplicação dos princípios de programação orientada a objetos em uma aplicação Java com múltiplas classes.
- Mapeamento de entidades Java para tabelas PostgreSQL utilizando Hibernate ORM e Jakarta Persistence.
- Implementação e gerenciamento de relacionamentos One-to-One, One-to-Many, Many-to-One e Many-to-Many.
- Compreensão dos lados proprietário e inverso dos relacionamentos bidirecionais.
- Trabalho com chaves estrangeiras e tabelas intermediárias.
- Gerenciamento de sessões, contextos de persistência e transações do Hibernate.
- Compreensão das alterações de estado das entidades e do mecanismo de dirty checking do Hibernate.
- Implementação de operações de banco de dados por meio das camadas DAO e Service.
- Implementação de regras de negócio e prevenção de operações conflitantes de locação.
- Utilização de HQL/JPQL e consultas ao banco de dados.
- Tratamento de restrições do banco de dados e erros de persistência.
- Utilização de `BigDecimal` para cálculos monetários e `LocalDate` para operações com datas.
- Implementação de validações e tratamento de operações inválidas.
- Utilização do Maven para gerenciamento de dependências e compilação.
- Utilização do Git e GitHub para controle de versão e desenvolvimento de portfólio.

## Melhorias Futuras

Possíveis melhorias conforme avanço nos estudos:

- Testes unitários e de integração.
- Uma API REST utilizando Spring Boot.
- Tratamento centralizado de exceções.
- Logs estruturados da aplicação.
- Implantação da aplicação e do banco utilizando Docker.
- Implantação em nuvem e gerenciamento do banco de dados.

## Aviso

Este projeto foi desenvolvido para fins educacionais e de portfólio. Trata-se de uma aplicação executada pelo console, não sendo destinada à utilização como uma plataforma comercial de locação de veículos pronta para produção.