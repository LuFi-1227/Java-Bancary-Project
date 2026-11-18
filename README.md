# Java Banking Project

Projeto bancario desenvolvido em Java como trabalho de conclusao de curso na DIO.

## Sobre o projeto

O sistema executa operacoes bancarias basicas em memoria e oferece uma interface
interativa no console. O projeto demonstra conceitos de Java, orientacao a objetos,
arquitetura em camadas e testes automatizados.

## Funcionalidades

- Cadastro de clientes com validacao de CPF.
- Bloqueio de cadastro com CPF duplicado.
- Abertura automatica de conta para cada cliente.
- Consulta de dados e saldo.
- Deposito.
- Saque com verificacao de saldo.
- Transferencia entre contas.
- Validacao de valores negativos, zerados e contas invalidas.
- Uso de `BigDecimal` para valores monetarios.
- Interface de menu no console.
- Extrato de movimentacoes da conta.
- Testes isolados com estado resetado.

## Tecnologias

- Java 17
- Maven
- JUnit 5

## Arquitetura

- `domain`: entidades, identificadores, contratos e regras do dominio.
- `application`: casos de uso e adaptadores entre dominio e interface.
- `infrastructure`: repositorios em memoria.
- `exceptions`: excecoes do dominio.
- `Main`: interface de console.

## Como executar

Requisitos: JDK 17 ou superior e Maven.

```bash
mvn clean compile
mvn test
mvn exec:java
```

## Executando os testes

```bash
mvn test
```

Os testes cobrem cadastro, conta, saldo, deposito, transferencia, saldo insuficiente,
CPF duplicado e valores invalidos.

## Decisoes tecnicas

- Os dados permanecem em memoria; reiniciar o programa limpa os cadastros.
- Os repositorios implementam contratos definidos no dominio.
- `ApplicationFunction` centraliza os casos de uso.
- A transferencia atualiza primeiro a origem e depois o destino, apos validar os
  identificadores e o valor.
- CPF e campos obrigatorios sao validados antes da criacao do cliente.
- `BigDecimal` evita imprecisoes comuns em operacoes financeiras com `double`.

## Limitacoes

Este projeto possui finalidade educacional e nao representa um banco em ambiente
de producao. Nao inclui criptografia, banco de dados real, concorrencia distribuida,
autorizacao ou auditoria financeira.

## Possiveis evolucoes

- Extrato de movimentacoes.
- Persistencia com JDBC ou JPA.
- Conta corrente e conta poupanca.
- API REST com Spring Boot.
- Interface web.
