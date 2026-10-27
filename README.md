# Sistema de Agendamento de Consultas

Sistema web para organizar o atendimento entre pacientes e médicos. Pacientes podem encontrar horários disponíveis e agendar consultas; médicos podem organizar sua agenda, acompanhar consultas e visualizar o painel diário.

## Acesse o sistema

O sistema está disponível em produção em:

[https://agendamento-consulta-six.vercel.app/](https://agendamento-consulta-six.vercel.app/)

1. Abra o link e entre com seu e-mail e senha.
2. Caso ainda não possua uma conta, selecione **Cadastre-se**.
3. Informe nome, e-mail e senha e escolha o perfil **Paciente** ou **Médico**. Para médicos, informe também a especialidade.

## Funcionalidades

### Para pacientes

- Criar uma conta de paciente e entrar no sistema.
- Consultar os médicos cadastrados e suas especialidades.
- Escolher uma data e visualizar os horários disponíveis de cada médico.
- Conferir o resumo antes de confirmar o agendamento.
- Consultar a lista de suas consultas.
- Cancelar uma consulta própria com confirmação.

### Para médicos

- Criar uma conta de médico, informando a especialidade.
- Definir horários de atendimento para datas específicas.
- Remover horários de atendimento que não estejam mais disponíveis.
- Consultar as consultas vinculadas ao seu perfil.
- Cancelar uma consulta sob sua responsabilidade, com confirmação.
- Acessar o painel diário para visualizar agenda, horários livres e ocupados, total de consultas, horas previstas e valor previsto em uma data selecionada.

## Tecnologias em produção

| Tecnologia | Uso no sistema |
| --- | --- |
| Java 21 | Linguagem usada pela aplicação. |
| Spring Boot | Estrutura principal do sistema e execução das regras de negócio. |
| Spring MVC e Thymeleaf | Rotas, páginas e formulários renderizados no servidor. |
| Spring Security e BCrypt | Login, controle de acesso por perfil e proteção das senhas. |
| Spring Data JPA e Hibernate | Comunicação entre a aplicação e o banco de dados. |
| PostgreSQL no Supabase | Armazenamento persistente de usuários, médicos, agendas, consultas e sessões. |
| Spring Session JDBC | Mantém a sessão do usuário mesmo quando a infraestrutura reinicia ou troca de instância. |
| HikariCP | Gerenciamento controlado das conexões da aplicação com o banco. |
| Bootstrap | Estilos responsivos das telas. |
| Docker | Empacotamento da aplicação Java em um container. |
| Vercel | Hospedagem e publicação do sistema em produção. |

## Executar a aplicação no computador pessoal

### 1. Preparar o ambiente

Instale os seguintes programas:

- **Git**, para baixar o código-fonte.
- **JDK 21**, por exemplo Eclipse Temurin 21. É necessário o JDK, pois a aplicação precisa ser compilada.
- **Apache Maven 3.9.x**, para baixar as dependências, compilar e iniciar o projeto. O repositório não inclui Maven Wrapper (`mvnw`).
- Um navegador atualizado.

Configure `JAVA_HOME` para a pasta do JDK 21 e inclua as pastas `bin` do Java e do Maven no `PATH`. Abra um novo terminal após a instalação e confira:

```shell
git --version
java -version
javac -version
mvn -version
```

Os comandos `java`, `javac` e `mvn -version` devem indicar o uso do Java 21. A primeira execução precisa de internet para o Maven baixar as dependências. As páginas também usam estilos do Bootstrap carregados pela internet.

### 2. Baixar o projeto e selecionar a branch

No terminal, entre na pasta em que deseja salvar o projeto e execute:

```shell
git clone https://github.com/Marowak92/sistema-agendamento-consultas.git
cd sistema-agendamento-consultas
```

Para criar a branch do exercício a partir da `main`, em uma cópia que ainda não tenha `iteracao2`:

```shell
git switch main
git switch -c iteracao2
```

Se a branch já existir, use apenas `git switch iteracao2`. Execute os próximos comandos na raiz do repositório, onde está o arquivo `pom.xml`.

### 3. Iniciar no perfil de desenvolvimento

```shell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Aguarde o terminal informar que a aplicação iniciou (`Started AgendaMedicoApplication`). Mantenha esse terminal aberto e acesse:

[http://localhost:8080/login](http://localhost:8080/login)

O perfil `dev` também é o padrão quando nenhum perfil é informado. Ele usa **H2 em memória**: não é necessário instalar PostgreSQL, configurar Supabase, criar tabelas manualmente ou fornecer credenciais de produção. O Hibernate cria as tabelas automaticamente e a aplicação carrega os dados de demonstração.

Os cadastros e agendamentos feitos localmente são perdidos ao encerrar o processo. Ao iniciar novamente, os dados de demonstração são recriados.

### 4. Acessar e conferir o funcionamento

As contas abaixo são criadas automaticamente somente no perfil `dev`:

| Perfil | E-mail | Senha local |
| --- | --- | --- |
| Paciente | `paciente@teste.com` | `1234` |
| Médica — Cardiologista | `ana.lima@teste.com` | `1234` |
| Médico — Dermatologista | `bruno.melo@teste.com` | `1234` |
| Médico — Pediatra | `carlos.reis@teste.com` | `1234` |

1. Entre como paciente e consulte os médicos disponíveis.
2. Selecione a data de **amanhã em relação ao dia em que a aplicação foi iniciada**. As agendas de exemplo têm atendimento das 08h às 12h, consultas de 30 minutos e valor de R$ 150,00.
3. Escolha um horário, confira o resumo e confirme o agendamento.
4. Consulte a lista de suas consultas para verificar o registro.
5. Saia da conta e entre como o médico escolhido para conferir a consulta e o painel diário na mesma data.

Também é possível criar novas contas pela opção **Cadastre-se** na tela de login.

Para inspecionar o banco local, acesse [http://localhost:8080/h2-console](http://localhost:8080/h2-console) enquanto a aplicação estiver em execução e use:

| Campo | Valor |
| --- | --- |
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:agendamedico;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` |
| User Name | `sa` |
| Password | Deixe em branco. |

### 5. Encerrar ou gerar um executável

Para parar o servidor, pressione `Ctrl+C` no terminal que está executando a aplicação.

Como alternativa à execução pelo plugin do Maven, gere o arquivo JAR e execute-o com Java:

```shell
mvn clean package
java -jar target/sistema-agendamento-consultas-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

O comando `mvn clean package` compila e empacota a aplicação e executa os testes existentes, caso sejam adicionados ao projeto. A geração do JAR, sozinha, não inicia o servidor.

### Problemas comuns

| Problema | Como resolver |
| --- | --- |
| `mvn` não é reconhecido ou `command not found` | Instale o Maven, adicione sua pasta `bin` ao `PATH` e abra um novo terminal. |
| `release version 21 not supported` | Confira `mvn -version` e ajuste `JAVA_HOME` para que o Maven use o JDK 21. |
| A porta 8080 já está em uso | Pare a outra aplicação ou execute `mvn spring-boot:run "-Dspring-boot.run.profiles=dev" "-Dspring-boot.run.arguments=--server.port=8081"`. Nesse caso, acesse `http://localhost:8081/login`. |
| A aplicação pede credenciais de PostgreSQL | Confira se há um perfil `prod` configurado no terminal ou na IDE. Remova essa configuração e inicie explicitamente com `dev`. |
| Falha ao baixar dependências | Confira a conexão com a internet e, se estiver em uma rede institucional, a configuração de proxy do Maven. |
| Não aparecem horários de demonstração | Selecione a data de amanhã em relação à inicialização do servidor ou entre como médico e cadastre novos horários. |
| Git informa `Filename too long` no Windows | Use uma pasta com caminho mais curto ou clone com `git -c core.longpaths=true clone -c core.longpaths=true https://github.com/Marowak92/sistema-agendamento-consultas.git`. |

