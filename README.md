# InteliFiscal

> **Transformando documentos fiscais em informação confiável para tomada de decisão.**

---

## Sobre o projeto

O **InteliFiscal** é um sistema desenvolvido em Java para importação, armazenamento e análise de arquivos XML de Nota Fiscal Eletrônica (NF-e).

Seu objetivo é transformar os dados fiscais das empresas em informações estratégicas, permitindo análises comerciais, financeiras e gerenciais de maneira simples, rápida e confiável.

O projeto foi concebido com foco em qualidade de software, arquitetura limpa, escalabilidade e facilidade de manutenção.

---

# Objetivos

O InteliFiscal permitirá:

- Importação de XML de NF-e;
- Cadastro de estabelecimentos próprios;
- Identificação automática de notas de Entrada e Saída;
- Cadastro automático de fornecedores;
- Cadastro automático de produtos;
- Histórico completo de compras;
- Histórico completo de vendas;
- Inteligência comercial;
- Estatísticas de compras;
- Estatísticas de vendas;
- Relatórios gerenciais;
- Dashboard com indicadores.

---

# Tecnologias

- Java 21
- Maven
- JavaFX
- SQLite
- JDBC
- Git
- GitHub

---

# Arquitetura

O projeto segue princípios de arquitetura limpa e separação de responsabilidades.

Camadas principais:

- View
- Controller
- Service
- Repository
- DTO
- Entity
- Database
- Util

Princípios utilizados:

- Clean Code
- SOLID
- Repository Pattern
- DTO Pattern
- MVC
- Single Responsibility Principle

---

# Estrutura do Projeto

```
src
└── main
    └── java
        └── br.com.intelifiscal
            ├── app
            ├── config
            ├── constants
            ├── controller
            ├── database
            ├── dto
            ├── entity
            ├── exception
            ├── fx
            ├── model
            ├── repository
            ├── security
            ├── service
            ├── util
            └── xml
```

---

# Banco de Dados

O sistema utiliza SQLite.

O banco de dados **não é versionado no Git**.

Ele será criado automaticamente pela aplicação durante a primeira execução através do componente responsável pela inicialização do banco.

Essa decisão garante:

- maior organização;
- repositório limpo;
- facilidade para novos desenvolvedores;
- independência do ambiente local.

---

# Situação Atual

### Concluído

- Estrutura inicial do projeto
- Arquitetura definida
- Modelagem do banco
- Git configurado
- Maven configurado
- Organização dos pacotes

### Em desenvolvimento

- Infraestrutura do banco
- Inicialização automática
- Repositórios
- Serviços
- Importador XML

---

# Filosofia do Projeto

O InteliFiscal segue um princípio simples:

> **Armazenar fatos. Gerar informação. Apoiar decisões.**

Todas as decisões arquiteturais priorizam:

- simplicidade;
- organização;
- rastreabilidade;
- desempenho;
- facilidade de manutenção;
- escalabilidade.

---

# Controle de Versão

Cada funcionalidade concluída será registrada através de commits organizados.

Fluxo adotado:

```
Desenvolvimento

↓

Build

↓

Teste

↓

Commit
```

Nenhuma funcionalidade será considerada concluída sem passar por esse processo.

---

# Roadmap

- [x] Definição da arquitetura
- [x] Modelagem do banco
- [x] Configuração Git
- [x] Estrutura Maven
- [ ] Inicialização do banco
- [ ] Versionamento do banco
- [ ] Entidades
- [ ] Repositórios
- [ ] Serviços
- [ ] Importador XML
- [ ] Dashboard
- [ ] Relatórios
- [ ] Inteligência Comercial

---

# Autor

**José Roberto Bizaio**

Projeto desenvolvido com foco em arquitetura de software, qualidade de código e evolução contínua.

---

## InteliFiscal

**Transformando documentos fiscais em informação confiável para tomada de decisão.**