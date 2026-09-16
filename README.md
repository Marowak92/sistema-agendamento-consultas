# Sistema de Agendamento de Consultas

Sistema web para organizar o atendimento entre pacientes e médicos. Pacientes podem encontrar horários disponíveis e agendar consultas; médicos podem organizar sua agenda, acompanhar consultas e visualizar o painel diário.

## Acesse o sistema

O sistema está disponível em produção em:

[https://teste-agendamento-six.vercel.app/login](https://teste-agendamento-six.vercel.app/login)

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

