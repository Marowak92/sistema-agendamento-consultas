# Plano de Testes

## Sistema de Agendamento de Consultas

Grupo 24 – Plano de Testes

## 1 Introdução

Este documento apresenta o plano de testes do Sistema de Agendamento de Consultas, desenvolvido no componente Prática Profissional em Análise e Desenvolvimento de Sistemas, e o resultado da sua execução sobre a primeira iteração da fase de construção. O plano segue uma versão simplificada da estrutura da norma IEEE 829-2008 (INSTITUTE OF ELECTRICAL AND ELECTRONICS ENGINEERS, 2008), substituída pela ISO/IEC/IEEE 29119-3:2013 (INTERNATIONAL ORGANIZATION FOR STANDARDIZATION, 2013), e o texto de apoio da disciplina (SASAKI, [2023]).

O objetivo é verificar se o sistema entregue faz o que a especificação da A2 descreve. Cada caso de teste parte dos casos de uso (UC01 a UC08), dos objetivos funcionais (OF01 a OF10) e dos objetivos não funcionais (ONF01 a ONF08) da A2 (PAULO et al., 2026a), e do guia do usuário da primeira iteração da construção (A3) (PAULO et al., 2026b), e o resultado obtido é comparado com o resultado esperado. Quando o sistema não faz o que a A2 descreve, o caso é reprovado, ou é marcado como não implementado quando a tela ou a operação nem existe, e a diferença é registrada no capítulo 9, em linguagem simples, com uma sugestão de correção.

O capítulo 2 identifica o grupo e a versão testada. O capítulo 3 delimita o que foi e o que não foi testado, o capítulo 4 descreve a estratégia e o capítulo 5 descreve o ambiente e os dados. O capítulo 6 define os critérios de entrada, saída e aprovação. O capítulo 7 reúne os casos de teste, o capítulo 8 resume os resultados, o capítulo 9 lista as divergências e o capítulo 10 apresenta a matriz de rastreabilidade.

## 2 Identificação

### 2.1 Integrantes do grupo

O grupo, identificado no ambiente da disciplina como Projeto da Disciplina 24, é formado pelos cinco integrantes relacionados na Tabela 1.

| Integrante | RA |
|---|---|
| Caio Henrique Gomes Paulo | 10734799 |
| David Pessoa | 10722179 |
| Fabio Capalbo Payao Rodrigues | 10433738 |
| Jean Carlos Monteiro Santos | 10728494 |
| Jessica Isis Medeiros Oliveira | 10726405 |

Tabela 1 – Integrantes do grupo Projeto da Disciplina 24

Fonte: elaborada pelos autores.

### 2.2 Endereços e versão testada

A Tabela 2 reúne os endereços do projeto e a versão submetida aos testes. O grupo executou os testes em uma cópia local da aplicação, construída a partir do código da tag indicada, e não na aplicação publicada.

| Item | Endereço |
|---|---|
| Aplicação publicada | https://agendamento-consulta-six.vercel.app/ |
| Repositório de código-fonte | https://github.com/Marowak92/sistema-agendamento-consultas |
| Versão testada (tag) | v1-agendamento_consulta |

Tabela 2 – Endereços e versão testada

Fonte: elaborada pelos autores.

## 3 Escopo

O plano cobre todos os casos de uso do diagrama da A2 e o cadastro e acesso (OF01). A Tabela 3 separa o que foi testado do que não foi.

| Item | Descrição |
|---|---|
| Testado | Cadastro e autenticação (OF01); manter agenda de atendimento (OF02, UC06); consultar agenda do médico (OF03, UC01); agendar consulta (OF04, OF05, UC02); cancelar consulta (OF06, UC03); consultar pacientes do dia (OF09, UC07); consultar horas e valores previstos (OF10, UC08); desempenho, controle de acesso, concorrência e senha cifrada (ONF02 a ONF05). |
| Pendente de execução manual | Responsividade em tela de 375 px (ONF01, CT-ONF-01), a cargo do grupo. |
| Testado apenas quanto à ausência | UC04 – Adicionar consulta à agenda pessoal (OF07, ONF06) e UC05 – Receber lembrete (OF08). Ambos estão fora da primeira iteração; o plano verifica que a funcionalidade não existe, e cada um recebe um caso de teste com a situação Não implementado. |
| Não testado | Aplicação publicada na Vercel e banco PostgreSQL de produção; usabilidade para pessoas sem familiaridade com tecnologia (ONF07); vínculo entre commits e issues (ONF08); testes automatizados de código (JUnit); testes de carga. |

Tabela 3 – Escopo do plano de testes

Fonte: elaborada pelos autores.

## 4 Estratégia e tipos de teste

Os testes são de caixa-preta: o sistema é usado pelas telas e pelas rotas, sem olhar o código para decidir o resultado esperado. O resultado esperado vem da A2 e do guia do usuário da primeira iteração da construção (A3). O grupo consultou o código apenas para conferir nomes de rotas, campos e mensagens e para confirmar a ausência de funcionalidades. Os tipos de teste aplicados estão na Tabela 4.

| Tipo | O que verifica | Casos |
|---|---|---|
| Funcional | Fluxos principais e alternativos de cada caso de uso, com dados válidos e inválidos. | Todos os UC |
| Valores de borda | Períodos que se encostam, período que não comporta a última consulta, duração e valor iguais a zero. | CT-UC01-07, CT-UC06-04 a CT-UC06-07 |
| Segurança de acesso | Telas restritas sem login, perfis trocados, cancelamento e remoção de dados de terceiros. | CT-AUT-10, CT-AUT-11, CT-UC03-06, CT-UC03-07, CT-UC06-12 |
| Validação de dados de entrada | Pedidos de agendamento alterados, fora da grade de horários ou com dados inválidos. | CT-UC02-06 |
| Concorrência | Dois pacientes confirmando o mesmo horário. | CT-UC02-04 |
| Não funcional | Responsividade, tempo de resposta e senha cifrada. | CT-ONF-01, CT-ONF-02, CT-AUT-12 |
| Exploratório | Comportamentos que a A2 não detalha, registrados para decisão do grupo. | CT-AUT-13, CT-UC02-09, CT-UC06-13 |

Tabela 4 – Tipos de teste aplicados

Fonte: elaborada pelos autores.

O grupo executou os testes por requisições HTTP, com sessão de login e token CSRF, imitando o que o navegador envia, e por consultas ao banco H2. Onde o navegador impede o envio de um dado (por exemplo, duração menor que 1 ou e-mail sem @), o grupo enviou o dado direto ao servidor. As requisições levaram os cabeçalhos de um navegador em português (Accept-Language pt-BR e, quando relevante, Accept text/html), para que mensagens e páginas de erro sejam as que o usuário veria. As evidências 01 e 02 foram coletadas sem esses cabeçalhos; os casos afetados (CT-AUT-06 e CT-AUT-11) foram reexecutados com eles (evidência 18). Os casos que só podem ser conferidos olhando a tela, como a responsividade, ficam com a situação Manual e devem ser executados pelo grupo.

## 5 Ambiente e dados de teste

O grupo executou os testes em 12/10/2026, localmente, com a aplicação em contêiner Docker, a partir do código da tag v1-agendamento_consulta (Java 21, Spring Boot 3.5.0), no perfil dev, com banco H2 em memória e endereço http://localhost:8080. Nada foi executado no banco de produção. Cada resultado obtido cita o arquivo de evidência numerado (01 a 19) com as respostas do sistema, guardado pelo grupo junto a este plano. A Tabela 5 reúne os dados de teste.

| Item | Descrição |
|---|---|
| Médicos semeados | ana.lima@teste.com (Ana Lima, Cardiologista), bruno.melo@teste.com (Bruno Melo, Dermatologista), carlos.reis@teste.com (Carlos Reis, Pediatra); senha 1234. |
| Paciente semeado | paciente@teste.com (Paciente Teste); senha 1234. |
| Período semeado | Para cada médico semeado: 13/10/2026 (dia seguinte ao da subida da aplicação), das 08:00 às 12:00, consultas de 30 minutos, R$ 150,00. |
| Contas criadas nos testes | Maria Souza (maria.souza@teste.com, paciente), Dr. Paulo Novo (paulo.novo@teste.com, médico, Ortopedista), MARIA.SOUZA@TESTE.COM (paciente, criada no CT-AUT-13) e maria.dois@teste.com (paciente, conta auxiliar criada durante os testes de cadastro, sem influência nos resultados); senha abcd. |
| Data de referência | Hoje: 12/10/2026; amanhã: 13/10/2026. |

Tabela 5 – Dados de teste

Fonte: elaborada pelos autores.

Como os casos foram executados em sequência sobre o mesmo banco, algumas pré-condições dependem de casos anteriores; cada caso indica quais.

## 6 Critérios de entrada, saída e aprovação

A Tabela 6 define os critérios de entrada, saída e aprovação adotados.

| Critério | Descrição |
|---|---|
| Entrada | Aplicação no ar na versão testada; contas e períodos semeados disponíveis; este plano revisado pelo grupo. |
| Saída | Todos os casos executados, com resultado obtido e situação registrados, ou marcados como Manual com o motivo. |
| Aprovação de um caso | O resultado obtido é igual ao resultado esperado. Situações: Aprovado (igual); Reprovado (a operação existe e viola a regra da A2 ou do guia do usuário da primeira iteração da construção (A3)); Não implementado (a tela ou a operação não existe na versão); Manual (depende de conferência visual a cargo do grupo). |
| Aprovação da versão | A versão é aprovada quando nenhum caso Reprovado ou Não implementado atinge um passo do fluxo principal ou a pós-condição dos casos de uso detalhados na A2 (UC02, UC03, UC06 e UC07), ou o objetivo funcional dos demais casos da iteração (OF01, OF03 e OF10). UC04 e UC05 ficam fora, por estarem declarados fora da primeira iteração. Os demais casos Reprovado e Não implementado ficam registrados no capítulo 9 para decisão do grupo. |

Tabela 6 – Critérios de entrada, saída e aprovação

Fonte: elaborada pelos autores.

## 7 Casos de teste

Cada caso tem uma tabela própria. Os nomes de telas, botões e mensagens seguem o texto exato do sistema.

### 7.1 Cadastro e acesso (OF01, ONF03, ONF05)

Estes casos cobrem o cadastro, a autenticação e o controle de acesso. O CT-AUT-13 foi acrescentado ao plano durante a execução.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-01 |
| Caso de uso / requisito | OF01 – Cadastro de paciente |
| Objetivo | Cadastrar um paciente com dados válidos. |
| Pré-condições | Aplicação no ar; e-mail ainda não cadastrado. |
| Passos | 1. Abrir a tela Agendamento de Consultas e clicar em Cadastre-se.<br>2. Preencher Nome, E-mail e Senha, escolher o Perfil Paciente.<br>3. Clicar em Cadastrar.<br>4. Entrar com o e-mail e a senha cadastrados. |
| Dados de teste | Nome: Maria Souza; E-mail: maria.souza@teste.com; Senha: abcd; Perfil: Paciente. |
| Resultado esperado | O sistema volta ao login com a mensagem "Cadastro realizado. Entre com seu e-mail e senha." e o login leva à Área do Paciente. |
| Resultado obtido | Mensagem "Cadastro realizado. Entre com seu e-mail e senha." exibida. O login com a conta nova redirecionou para a Área do Paciente (/paciente/inicio). Evidência 01. |
| Situação | Aprovado |

Tabela 7 – CT-AUT-01: Cadastrar um paciente com dados válidos

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-02 |
| Caso de uso / requisito | OF01 – Cadastro de médico |
| Objetivo | Cadastrar um médico informando a especialidade. |
| Pré-condições | Aplicação no ar; e-mail ainda não cadastrado. |
| Passos | 1. Na tela Criar cadastro, preencher Nome, E-mail e Senha.<br>2. Escolher o Perfil Médico e preencher Especialidade do médico.<br>3. Clicar em Cadastrar.<br>4. Entrar com a conta nova e, em seguida, como paciente, abrir a tela Escolha um médico. |
| Dados de teste | Nome: Dr. Paulo Novo; E-mail: paulo.novo@teste.com; Senha: abcd; Perfil: Médico; Especialidade: Ortopedista. |
| Resultado esperado | Cadastro realizado; o login leva ao Painel do dia; o médico aparece com a especialidade na lista de médicos do paciente. |
| Resultado obtido | Cadastro realizado. O login do médico levou ao Painel do dia (/medico/painel). Na tela Escolha um médico apareceu o cartão de Dr. Paulo Novo, com a especialidade Ortopedista. Evidência 01. |
| Situação | Aprovado |

Tabela 8 – CT-AUT-02: Cadastrar um médico informando a especialidade

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-03 |
| Caso de uso / requisito | OF01 – Cadastro de médico |
| Objetivo | Recusar o cadastro de médico sem especialidade. |
| Pré-condições | Aplicação no ar. |
| Passos | 1. Na tela Criar cadastro, preencher Nome, E-mail e Senha e escolher o Perfil Médico.<br>2. Deixar Especialidade do médico em branco.<br>3. Clicar em Cadastrar. |
| Dados de teste | Nome: Medico Sem Esp; E-mail: sem.esp@teste.com; Senha: abcd; Perfil: Médico; Especialidade: vazia. |
| Resultado esperado | O cadastro é recusado com a mensagem "Informe a especialidade do médico.". |
| Resultado obtido | A tela Criar cadastro foi exibida de novo com a mensagem "Informe a especialidade do médico.". Evidência 01. |
| Situação | Aprovado |

Tabela 9 – CT-AUT-03: Recusar o cadastro de médico sem especialidade

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-04 |
| Caso de uso / requisito | OF01 – E-mail único |
| Objetivo | Recusar e-mail já usado, inclusive entre paciente e médico. |
| Pré-condições | Existem a paciente maria.souza@teste.com e o médico paulo.novo@teste.com (CT-AUT-01 e CT-AUT-02). |
| Passos | 1. Cadastrar novo paciente com o e-mail maria.souza@teste.com.<br>2. Cadastrar novo paciente com o e-mail do médico paulo.novo@teste.com.<br>3. Cadastrar novo médico com o e-mail da paciente maria.souza@teste.com.<br>4. Conferir a mensagem em cada tentativa. |
| Dados de teste | Senha: abcd em todas as tentativas; Especialidade (médico): Clinico. |
| Resultado esperado | As três tentativas são recusadas com "Já existe um usuário cadastrado com este e-mail.". |
| Resultado obtido | As três tentativas (paciente x paciente, paciente x médico, médico x paciente) foram recusadas com "Já existe um usuário cadastrado com este e-mail.". Evidência 01. |
| Situação | Aprovado |

Tabela 10 – CT-AUT-04: Recusar e-mail já usado, inclusive entre paciente e médico

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-05 |
| Caso de uso / requisito | OF01 – Senha mínima |
| Objetivo | Recusar senha com menos de 4 caracteres. |
| Pré-condições | Aplicação no ar. |
| Passos | 1. Na tela Criar cadastro, preencher Nome e E-mail válidos e Senha com 3 caracteres.<br>2. Clicar em Cadastrar. |
| Dados de teste | Senha: abc. |
| Resultado esperado | Cadastro recusado com "A senha deve ter pelo menos 4 caracteres.". |
| Resultado obtido | Cadastro recusado com a mensagem "A senha deve ter pelo menos 4 caracteres.". Evidência 01. |
| Situação | Aprovado |

Tabela 11 – CT-AUT-05: Recusar senha com menos de 4 caracteres

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-06 |
| Caso de uso / requisito | OF01 – Validação de e-mail |
| Objetivo | Recusar e-mail em formato inválido. |
| Pré-condições | Aplicação no ar. O campo E-mail do formulário usa type="email", e por isso o navegador já bloqueia o envio; o teste foi enviado direto ao servidor, com os cabeçalhos de um navegador em português (Accept-Language pt-BR). |
| Passos | 1. Enviar o cadastro com E-mail sem o caractere @.<br>2. Tentar entrar com esse e-mail para confirmar que a conta não foi gravada. |
| Dados de teste | E-mail: invalido.teste.com; Senha: abcd. |
| Resultado esperado | Cadastro recusado e conta não gravada. |
| Resultado obtido | O sistema devolveu a tela Criar cadastro com a mensagem "deve ser um endereço de e-mail bem formado", em português, ligada ao campo E-mail, e a conta não foi gravada (o login com esse e-mail falhou). Evidências 01 e 18. |
| Situação | Aprovado |

Tabela 12 – CT-AUT-06: Recusar e-mail em formato inválido

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-07 |
| Caso de uso / requisito | OF01 – Autenticação |
| Objetivo | Entrar como paciente e como médico e verificar a tela inicial de cada perfil. |
| Pré-condições | Contas semeadas paciente@teste.com e ana.lima@teste.com. |
| Passos | 1. Na tela de login, informar o e-mail e a senha do paciente e clicar em Entrar.<br>2. Sair e repetir com o médico. |
| Dados de teste | paciente@teste.com / 1234; ana.lima@teste.com / 1234. |
| Resultado esperado | O paciente vai para a Área do Paciente (opções Agendar consulta e Minhas consultas); o médico vai para o Painel do dia. |
| Resultado obtido | Paciente redirecionado para /paciente/inicio (Área do Paciente, com Agendar consulta e Minhas consultas). Médico redirecionado para /medico/painel (Painel do dia). Evidência 02. |
| Situação | Aprovado |

Tabela 13 – CT-AUT-07: Entrar como paciente e como médico e verificar a tela inicial de cada perfil

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-08 |
| Caso de uso / requisito | OF01 – Autenticação |
| Objetivo | Recusar login com senha errada ou e-mail inexistente. |
| Pré-condições | Aplicação no ar. |
| Passos | 1. Informar um e-mail existente com senha errada e clicar em Entrar.<br>2. Informar um e-mail inexistente e clicar em Entrar. |
| Dados de teste | paciente@teste.com / senhaerrada; ninguem@teste.com / 1234. |
| Resultado esperado | O login é recusado e aparece "E-mail ou senha inválidos.". |
| Resultado obtido | Nas duas tentativas o sistema voltou ao login (/login?error) com "E-mail ou senha inválidos.". Evidência 02. |
| Situação | Aprovado |

Tabela 14 – CT-AUT-08: Recusar login com senha errada ou e-mail inexistente

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-09 |
| Caso de uso / requisito | OF01 – Sair do sistema |
| Objetivo | Encerrar a sessão com o botão Sair. |
| Pré-condições | Paciente autenticado. |
| Passos | 1. Clicar em Sair.<br>2. Conferir a mensagem no login.<br>3. Tentar abrir a Área do Paciente sem entrar de novo. |
| Dados de teste | paciente@teste.com / 1234. |
| Resultado esperado | O sistema volta ao login com "Você saiu do sistema." e a Área do Paciente deixa de abrir sem login. |
| Resultado obtido | Login exibido com "Você saiu do sistema.". O acesso a /paciente/inicio depois disso redirecionou ao login. Evidência 02. |
| Situação | Aprovado |

Tabela 15 – CT-AUT-09: Encerrar a sessão com o botão Sair

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-10 |
| Caso de uso / requisito | ONF03 – Acesso autenticado |
| Objetivo | Verificar que as áreas restritas exigem login. |
| Pré-condições | Nenhuma sessão aberta (sem cookie). |
| Passos | 1. Abrir sem login: /paciente/inicio, /paciente/medicos, /paciente/minhas-consultas, /medico/painel, /medico/agenda e /medico/consultas.<br>2. Enviar sem login um POST para /paciente/consultas. |
| Dados de teste | Nenhum. |
| Resultado esperado | Todas as requisições são redirecionadas para a tela de login. |
| Resultado obtido | As seis páginas e o POST foram redirecionados para /login. Evidência 02. |
| Situação | Aprovado |

Tabela 16 – CT-AUT-10: Verificar que as áreas restritas exigem login

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-11 |
| Caso de uso / requisito | ONF03 – Perfis de acesso |
| Objetivo | Verificar que cada perfil só acessa as telas do próprio perfil. |
| Pré-condições | Paciente e médico autenticados em sessões separadas. As requisições foram feitas com os cabeçalhos de um navegador (Accept: text/html e Accept-Language pt-BR). |
| Passos | 1. Como paciente, abrir /medico/painel, /medico/agenda e /medico/consultas.<br>2. Como médico, abrir /paciente/inicio, /paciente/medicos e /paciente/minhas-consultas.<br>3. Como paciente, enviar um POST sem o token CSRF. |
| Dados de teste | paciente@teste.com / 1234; ana.lima@teste.com / 1234. |
| Resultado esperado | Acesso negado nos dois sentidos e recusa do POST sem token. |
| Resultado obtido | Todas as seis tentativas retornaram HTTP 403 (Forbidden) e o POST sem token CSRF também retornou 403. O sistema não tem página própria de acesso negado: aparece a página de erro padrão do Spring (Whitelabel Error Page, "type=Forbidden, status=403"). Evidências 02 e 18. |
| Situação | Aprovado |

Tabela 17 – CT-AUT-11: Verificar que cada perfil só acessa as telas do próprio perfil

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-12 |
| Caso de uso / requisito | ONF05 – Senha cifrada |
| Objetivo | Verificar que as senhas são gravadas cifradas. |
| Pré-condições | Aplicação em execução com banco H2 e usuários cadastrados: 4 pacientes (paciente@teste.com, maria.souza@teste.com, MARIA.SOUZA@TESTE.COM e maria.dois@teste.com, conta auxiliar criada durante os testes de cadastro, sem influência nos resultados) e 4 médicos. |
| Passos | 1. Abrir o console do banco H2 de dentro do contêiner.<br>2. Executar SELECT email, senha FROM pacientes e o mesmo para medicos.<br>3. Procurar por senha em texto puro (1234 e abcd) nas duas tabelas.<br>4. Contar as senhas que não começam com o prefixo BCrypt $2a$10$. |
| Dados de teste | Consultas SQL sobre as tabelas pacientes e medicos. |
| Resultado esperado | Todas as senhas aparecem como hash BCrypt e nenhuma em texto puro. |
| Resultado obtido | As 4 linhas de pacientes e as 4 de medicos têm senha no formato BCrypt ($2a$10$...). A contagem de senhas iguais a 1234 ou abcd deu 0 em pacientes e 0 em medicos, e nenhuma senha das duas tabelas fica fora do prefixo $2a$10$. Evidências 03 e 19. |
| Situação | Aprovado |

Tabela 18 – CT-AUT-12: Verificar que as senhas são gravadas cifradas

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-AUT-13 |
| Caso de uso / requisito | OF01 – E-mail único (exploratório) |
| Objetivo | Verificar se o mesmo e-mail escrito com letras maiúsculas é tratado como o mesmo e-mail. |
| Pré-condições | Existe a paciente maria.souza@teste.com. |
| Passos | 1. Cadastrar um paciente com o e-mail MARIA.SOUZA@TESTE.COM.<br>2. Conferir a mensagem. |
| Dados de teste | E-mail: MARIA.SOUZA@TESTE.COM; Senha: abcd. |
| Resultado esperado | Cadastro recusado com "Já existe um usuário cadastrado com este e-mail.", pois o guia do usuário da primeira iteração da construção (A3) diz que o mesmo e-mail não pode ser usado em duas contas. |
| Resultado obtido | O cadastro foi aceito ("Cadastro realizado.") e o banco passou a ter duas contas, maria.souza@teste.com e MARIA.SOUZA@TESTE.COM. Evidências 01 e 03. |
| Situação | Reprovado |

Tabela 19 – CT-AUT-13: Verificar se o mesmo e-mail escrito com letras maiúsculas é tratado como o mesmo e-mail

Fonte: elaborada pelos autores.

### 7.2 UC01 – Consultar agenda do médico (OF03)

| Campo | Descrição |
|---|---|
| ID | CT-UC01-01 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Listar os médicos com nome e especialidade. |
| Pré-condições | Paciente autenticado; médicos cadastrados. Executado depois do CT-AUT-02, para que Dr. Paulo Novo conste na lista. |
| Passos | 1. Na Área do Paciente, clicar em Agendar consulta.<br>2. Conferir os cartões da tela Escolha um médico. |
| Dados de teste | Médicos semeados mais Dr. Paulo Novo. |
| Resultado esperado | Cada cartão mostra nome e especialidade e o botão Ver agenda. |
| Resultado obtido | A tela listou Ana Lima (Cardiologista), Bruno Melo (Dermatologista), Carlos Reis (Pediatra) e Dr. Paulo Novo (Ortopedista), cada um com o botão Ver agenda. Evidência 04. |
| Situação | Aprovado |

Tabela 20 – CT-UC01-01: Listar os médicos com nome e especialidade

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC01-02 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Listar os horários livres de uma data com período cadastrado. |
| Pré-condições | Ana Lima tem o período 13/10/2026, 08:00 às 12:00, consultas de 30 minutos; sem consultas marcadas. Executado antes do CT-UC02-01. |
| Passos | 1. Clicar em Ver agenda no cartão de Ana Lima.<br>2. Escolher a data 13/10/2026 em Escolha a data e clicar em Ver horários. |
| Dados de teste | Médica: Ana Lima; data: 13/10/2026. |
| Resultado esperado | Aparecem 8 horários livres: 08:00, 08:30, 09:00, 09:30, 10:00, 10:30, 11:00 e 11:30. |
| Resultado obtido | Foram listados os 8 horários esperados, de 08:00 a 11:30. Evidência 04. |
| Situação | Aprovado |

Tabela 21 – CT-UC01-02: Listar os horários livres de uma data com período cadastrado

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC01-03 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Verificar que data passada não oferece horários, mesmo quando existe período cadastrado nela. |
| Pré-condições | Data de execução: 12/10/2026. Executado depois do CT-UC06-13: Dr. Paulo Novo tem um período cadastrado em 01/10/2026 (data passada), das 08:00 às 09:00. Ana Lima não tem período em 11/10 nem em 12/10. |
| Passos | 1. Como paciente, abrir a agenda de Dr. Paulo Novo com a data 01/10/2026 (passada, com período cadastrado).<br>2. Abrir a agenda de Ana Lima com a data 11/10/2026 (passada, sem período).<br>3. Abrir a agenda de Ana Lima com a data 12/10/2026 (hoje, sem período). |
| Dados de teste | Datas: 01/10/2026 (com período), 11/10/2026 e 12/10/2026. |
| Resultado esperado | Nenhum horário é oferecido e aparece "Não há horários livres para esta data.". |
| Resultado obtido | Com o período de Dr. Paulo Novo existindo em 01/10/2026 (filtro da agenda do médico), o paciente recebeu "Não há horários livres para esta data." e nenhum botão de horário. Em 11/10/2026 e 12/10/2026 (Ana Lima) apareceu a mesma mensagem. Evidências 04 e 19. |
| Situação | Aprovado |

Tabela 22 – CT-UC01-03: Verificar que data passada não oferece horários, mesmo quando existe período cadastrado nela

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC01-04 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Informar que não há horários quando a data não tem período. |
| Pré-condições | Ana Lima não tem período em 20/10/2026. |
| Passos | 1. Abrir a agenda de Ana Lima com a data 20/10/2026.<br>2. Conferir a mensagem e a presença do campo de data para escolher outro dia. |
| Dados de teste | Data: 20/10/2026. |
| Resultado esperado | Mensagem "Não há horários livres para esta data." e campo de data disponível para nova escolha. |
| Resultado obtido | Mensagem exibida, sem botões de horário; o campo Escolha a data e o botão Ver horários continuaram disponíveis. Evidência 04. |
| Situação | Aprovado |

Tabela 23 – CT-UC01-04: Informar que não há horários quando a data não tem período

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC01-05 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Informar que o médico ainda não tem agenda cadastrada. |
| Pré-condições | Dr. Paulo Novo acabou de ser cadastrado e não adicionou períodos. |
| Passos | 1. Na tela Escolha um médico, clicar em Ver agenda no cartão de Dr. Paulo Novo. |
| Dados de teste | Médico: Dr. Paulo Novo (id 4). |
| Resultado esperado | O sistema volta à lista de médicos com "Este médico ainda não possui agenda cadastrada.". |
| Resultado obtido | O sistema redirecionou para a tela Escolha um médico com "Este médico ainda não possui agenda cadastrada.". Para um médico inexistente (id 999) a mensagem foi "Médico não encontrado.". Evidência 04. |
| Situação | Aprovado |

Tabela 24 – CT-UC01-05: Informar que o médico ainda não tem agenda cadastrada

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC01-06 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Verificar que um horário já agendado não aparece como livre. |
| Pré-condições | Dr. Paulo Novo tem o período 14/10/2026, 08:00 às 09:15, 30 minutos, R$ 200,00 (CT-UC06-01). |
| Passos | 1. Como Paciente Teste, agendar o horário 08:30 de 14/10/2026 com Dr. Paulo Novo.<br>2. Abrir novamente a agenda do médico nessa data. |
| Dados de teste | Médico: Dr. Paulo Novo; data: 14/10/2026; horário: 08:30. |
| Resultado esperado | O horário 08:30 deixa de ser listado como livre. |
| Resultado obtido | Depois do agendamento a lista de horários livres de 14/10/2026 passou a mostrar apenas 08:00. Evidência 12. |
| Situação | Aprovado |

Tabela 25 – CT-UC01-06: Verificar que um horário já agendado não aparece como livre

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC01-07 |
| Caso de uso / requisito | UC01 – Consultar agenda do médico (OF03) |
| Objetivo | Verificar a borda do período que não comporta uma consulta inteira. |
| Pré-condições | Dr. Paulo Novo tem o período 14/10/2026, 08:00 às 09:15, consultas de 30 minutos. |
| Passos | 1. Como paciente, abrir a agenda do médico em 14/10/2026 e conferir os horários. |
| Dados de teste | Período: 08:00 às 09:15; duração: 30 minutos. |
| Resultado esperado | Somente 08:00 e 08:30 são oferecidos, pois 09:00 terminaria às 09:30, depois do fim do período. |
| Resultado obtido | Foram listados apenas 08:00 e 08:30. Evidência 12. |
| Situação | Aprovado |

Tabela 26 – CT-UC01-07: Verificar a borda do período que não comporta uma consulta inteira

Fonte: elaborada pelos autores.

### 7.3 UC02 – Agendar consulta (OF04, OF05, ONF04)

| Campo | Descrição |
|---|---|
| ID | CT-UC02-01 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF04, OF05) – fluxo principal |
| Objetivo | Agendar uma consulta pelo fluxo principal e conferir o resumo e a lista Minhas consultas. |
| Pré-condições | Paciente Teste autenticado; Ana Lima com horários livres em 13/10/2026. |
| Passos | 1. Em Escolha um médico, clicar em Ver agenda de Ana Lima.<br>2. Escolher 13/10/2026, clicar em Ver horários e clicar no horário 08:00.<br>3. Conferir o Resumo da consulta.<br>4. Clicar em Confirmar agendamento.<br>5. Abrir Minhas consultas. |
| Dados de teste | Paciente: paciente@teste.com; médica: Ana Lima; data: 13/10/2026; horário: 08:00. |
| Resultado esperado | O resumo mostra médico, especialidade, data, horário, duração e valor. Após confirmar, aparece "Consulta agendada com sucesso." e a consulta consta em Minhas consultas. |
| Resultado obtido | O Resumo da consulta mostrou Médico Ana Lima, Especialidade Cardiologista, Data 13/10/2026, Horário 08:00, Duração 30 minutos e Valor R$ 150,00. Após Confirmar agendamento apareceu "Consulta agendada com sucesso." e a consulta de 13/10/2026 às 08:00 consta em Minhas consultas. Evidência 06. |
| Situação | Aprovado |

Tabela 27 – CT-UC02-01: Agendar uma consulta pelo fluxo principal e conferir o resumo e a lista Minhas consultas

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-02 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF04) – desistência |
| Objetivo | Verificar que o botão Voltar do resumo não agenda a consulta. |
| Pré-condições | Paciente Teste com uma consulta em 13/10/2026 às 08:00 com Ana Lima. |
| Passos | 1. Escolher o horário 08:30 de Ana Lima em 13/10/2026 e abrir o Resumo da consulta.<br>2. Clicar em Voltar.<br>3. Abrir Minhas consultas. |
| Dados de teste | Horário: 08:30. |
| Resultado esperado | A agenda é exibida com o horário 08:30 ainda livre e Minhas consultas não ganha nova consulta. |
| Resultado obtido | O botão Voltar levou à agenda de Ana Lima, com 08:30 ainda livre, e Minhas consultas continuou com apenas a consulta das 08:00. Evidência 07. |
| Situação | Aprovado |

Tabela 28 – CT-UC02-02: Verificar que o botão Voltar do resumo não agenda a consulta

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-03 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF04) – fluxo alternativo 2a |
| Objetivo | Informar quando o médico não tem horário livre e permitir avançar para outra data com horários. |
| Pré-condições | Todos os 8 horários de Carlos Reis em 13/10/2026 foram agendados por Maria Souza. Carlos Reis cadastrou um período em 15/10/2026, das 08:00 às 09:00, consultas de 30 minutos. |
| Passos | 1. Abrir a agenda de Carlos Reis em 13/10/2026 e conferir a mensagem.<br>2. No campo Escolha a data, digitar 15/10/2026 (data com período) e clicar em Ver horários.<br>3. Conferir os horários listados. |
| Dados de teste | Médico: Carlos Reis; datas: 13/10/2026 (sem horários livres) e 15/10/2026 (período de 08:00 às 09:00). |
| Resultado esperado | A2, fluxo 2a: o sistema informa que não há horário livre e "permite avançar para as próximas datas". |
| Resultado obtido | Em 13/10/2026 apareceu "Não há horários livres para esta data.", sem botões de horário, e o campo Escolha a data continuou disponível. Ao escolher 15/10/2026, o sistema listou 08:00 e 08:30. A navegação é manual, pelo campo de data; não há atalho de "próxima data" com horários. Em 14/10/2026, data sem período, a mensagem foi a mesma. Evidências 07 e 19. |
| Situação | Aprovado |

Tabela 29 – CT-UC02-03: Informar quando o médico não tem horário livre e permitir avançar para outra data com horários

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-04 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF05, ONF04) – fluxo alternativo 5a |
| Objetivo | Impedir que dois pacientes reservem o mesmo horário do mesmo médico, inclusive com solicitações simultâneas. |
| Pré-condições | Paciente Teste e Maria Souza autenticados em sessões separadas; Ana Lima com os horários 10:00 e 10:30 livres em 13/10/2026. |
| Passos | 1. Parte A: os dois pacientes abrem o Resumo da consulta do horário 10:00. Paciente Teste clica em Confirmar agendamento e, depois, Maria Souza clica em Confirmar agendamento.<br>2. Conferir a mensagem de Maria Souza e a agenda recarregada.<br>3. Parte B: os dois abrem o resumo do horário 10:30 e confirmam ao mesmo tempo (duas requisições em paralelo).<br>4. Consultar o banco para conferir quantas consultas existem em cada horário. |
| Dados de teste | Médica: Ana Lima; data: 13/10/2026; horários: 10:00 (parte A) e 10:30 (parte B). |
| Resultado esperado | Só o primeiro a confirmar fica com o horário; o segundo recebe "Este horário não está mais disponível." e a agenda é recarregada. No banco existe uma única consulta por horário. |
| Resultado obtido | Parte A: Paciente Teste recebeu "Consulta agendada com sucesso."; Maria Souza foi levada à agenda de Ana Lima com "Este horário não está mais disponível." e a lista de livres foi recarregada sem o 10:00. Parte B: apenas uma das requisições foi aceita e o banco ficou com uma única consulta AGENDADA às 10:30 (paciente@teste.com). Evidência 08. |
| Situação | Aprovado |

Tabela 30 – CT-UC02-04: Impedir que dois pacientes reservem o mesmo horário do mesmo médico, inclusive com solicitações simultâneas

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-05 |
| Caso de uso / requisito | UC02 – Agendar consulta – fluxo alternativo 5b |
| Objetivo | Bloquear segundo agendamento do mesmo paciente com o mesmo médico na mesma data. |
| Pré-condições | Paciente Teste sem consulta com Bruno Melo em 13/10/2026. |
| Passos | 1. Agendar Bruno Melo em 13/10/2026 às 08:00.<br>2. Agendar Bruno Melo em 13/10/2026 às 09:00 com o mesmo paciente.<br>3. Conferir Minhas consultas. |
| Dados de teste | Paciente: paciente@teste.com; médico: Bruno Melo; data: 13/10/2026; horários: 08:00 e 09:00. |
| Resultado esperado | Conforme a A2, o segundo agendamento é bloqueado e o sistema apresenta a consulta já existente. |
| Resultado obtido | Os dois agendamentos receberam "Consulta agendada com sucesso." e Minhas consultas passou a listar as duas consultas com Bruno Melo no mesmo dia (08:00 e 09:00). A operação de agendar existe, mas a regra da A2 não é aplicada. Evidência 09. |
| Situação | Reprovado |

Tabela 31 – CT-UC02-05: Bloquear segundo agendamento do mesmo paciente com o mesmo médico na mesma data

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-06 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF04, OF05) – requisição manipulada |
| Objetivo | Recusar pedidos de agendamento fora da grade de horários. |
| Pré-condições | Paciente Teste autenticado, com token CSRF válido. Os pedidos foram enviados direto ao servidor, simulando um formulário alterado. |
| Passos | 1. Enviar o agendamento de Bruno Melo em 13/10/2026 com horários 08:15, 07:00, 12:00 e 13:00.<br>2. Enviar um horário em data sem período (20/10/2026) e em data passada (11/10/2026).<br>3. Enviar médico inexistente (id 999), horário inválido (abc) e horário ausente.<br>4. Enviar o horário 08:00 já ocupado.<br>5. Conferir Minhas consultas. |
| Dados de teste | Médico: Bruno Melo (id 2); valores citados nos passos. |
| Resultado esperado | Os pedidos são recusados e redirecionados à agenda do médico ou à lista de médicos, sem gravar consulta. |
| Resultado obtido | Os dez pedidos receberam redirecionamento (HTTP 302) à agenda do médico ou à lista de médicos (esta, nos casos de horário inválido e horário ausente). A evidência mostra a mensagem "Este horário não está mais disponível." para 08:15 e 07:00, "Médico não encontrado." para o médico inexistente e "Dados do agendamento inválidos." para o horário inválido (abc) e para o horário ausente (evidência 19). Para os demais pedidos ela registra apenas o redirecionamento. Minhas consultas não ganhou nenhuma consulta indevida. Evidências 09 e 19. |
| Situação | Aprovado |

Tabela 32 – CT-UC02-06: Recusar pedidos de agendamento fora da grade de horários

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-07 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF05) – pós-condição |
| Objetivo | Verificar que o horário agendado deixa de ser livre e aparece como Agendada no Painel do dia. |
| Pré-condições | Paciente Teste agendou 08:00, 10:00 e 10:30 com Ana Lima em 13/10/2026. |
| Passos | 1. Como paciente, abrir a lista de horários livres de Ana Lima em 13/10/2026.<br>2. Como Ana Lima, abrir o Painel do dia em 13/10/2026. |
| Dados de teste | Médica: Ana Lima; data: 13/10/2026. |
| Resultado esperado | Os horários agendados somem da lista de livres e aparecem no painel com o nome do paciente e "Agendada". |
| Resultado obtido | A lista de livres ficou com 08:30, 09:00, 09:30, 11:00 e 11:30. O painel mostrou 08:00, 10:00 e 10:30 como "Paciente Teste - Agendada", com Consultas 3, Horas previstas 1h30 e Valor previsto R$ 450,00. Evidência 09. |
| Situação | Aprovado |

Tabela 33 – CT-UC02-07: Verificar que o horário agendado deixa de ser livre e aparece como Agendada no Painel do dia

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-08 |
| Caso de uso / requisito | UC02 – Agendar consulta – pós-condição "lembrete programado" |
| Objetivo | Verificar se o agendamento programa um lembrete para o paciente. |
| Pré-condições | Consulta agendada. |
| Passos | 1. Procurar no código, nos templates e nas dependências qualquer rotina de lembrete, notificação ou envio de e-mail.<br>2. Conferir a existência de rotas relacionadas a lembrete. |
| Dados de teste | Termos pesquisados: lembrete, notific, calend, .ics, mail, @Scheduled. |
| Resultado esperado | Segundo a A2, o lembrete fica programado ao agendar. |
| Resultado obtido | Nenhuma ocorrência no código, nos templates ou nas dependências, e a rota /paciente/lembretes retornou 404. O lembrete não existe nesta versão (depende do UC05). Evidência 17. |
| Situação | Não implementado |

Tabela 34 – CT-UC02-08: Verificar se o agendamento programa um lembrete para o paciente

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC02-09 |
| Caso de uso / requisito | UC02 – Agendar consulta (OF04) – exploratório |
| Objetivo | Verificar se um horário de hoje que já passou ainda pode ser agendado. |
| Pré-condições | Hora da execução: 10:47 de 12/10/2026. Dr. Paulo Novo cadastra o período 12/10/2026, 06:00 às 06:30 (horário já passado). |
| Passos | 1. Como paciente, abrir a agenda de Dr. Paulo Novo em 12/10/2026.<br>2. Se o horário 06:00 for listado, agendá-lo. |
| Dados de teste | Médico: Dr. Paulo Novo; data: 12/10/2026; horário: 06:00. |
| Resultado esperado | Um horário que já passou não deve ser oferecido nem aceito, por extensão da regra do guia do usuário da primeira iteração da construção (A3), que informa que datas passadas não oferecem horários. |
| Resultado obtido | O horário 06:00 foi listado como livre às 10:47 e o agendamento recebeu "Consulta agendada com sucesso.". O sistema só barra datas anteriores a hoje; não compara o horário com a hora atual. Evidência 14. |
| Situação | Reprovado |

Tabela 35 – CT-UC02-09: Verificar se um horário de hoje que já passou ainda pode ser agendado

Fonte: elaborada pelos autores.

### 7.4 UC03 – Cancelar consulta (OF06, ONF03)

| Campo | Descrição |
|---|---|
| ID | CT-UC03-01 |
| Caso de uso / requisito | UC03 – Cancelar consulta (OF06) – paciente |
| Objetivo | Cancelar uma consulta como paciente, com confirmação, liberando o horário. |
| Pré-condições | Paciente Teste tem a consulta de Ana Lima em 13/10/2026 às 10:30. |
| Passos | 1. Em Minhas consultas, clicar em Cancelar na linha da consulta das 10:30.<br>2. Conferir a tela Cancelar consulta (paciente, médico, data, horário).<br>3. Clicar em Sim, cancelar consulta.<br>4. Conferir Minhas consultas e a agenda de Ana Lima. |
| Dados de teste | Consulta de Ana Lima, 13/10/2026, 10:30. |
| Resultado esperado | Aparece "Consulta cancelada com sucesso.", a consulta some da lista e o horário volta a ficar livre. |
| Resultado obtido | A tela de confirmação mostrou Paciente Teste, Ana Lima, 13/10/2026 e 10:30. Após confirmar apareceu "Consulta cancelada com sucesso.", a consulta saiu de Minhas consultas e o 10:30 voltou à lista de horários livres. Evidência 11. |
| Situação | Aprovado |

Tabela 36 – CT-UC03-01: Cancelar uma consulta como paciente, com confirmação, liberando o horário

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-02 |
| Caso de uso / requisito | UC03 – Cancelar consulta (OF06) – fluxo 4a, paciente |
| Objetivo | Manter a consulta quando o paciente desiste do cancelamento. |
| Pré-condições | Paciente Teste tem a consulta de Ana Lima em 13/10/2026 às 10:00. |
| Passos | 1. Em Minhas consultas, clicar em Cancelar na consulta das 10:00.<br>2. Na tela Cancelar consulta, clicar em Não, voltar.<br>3. Conferir Minhas consultas. |
| Dados de teste | Consulta de Ana Lima, 13/10/2026, 10:00. |
| Resultado esperado | A consulta permanece agendada. |
| Resultado obtido | O botão Não, voltar levou a Minhas consultas e a consulta das 10:00 continuou na lista. Evidência 11. |
| Situação | Aprovado |

Tabela 37 – CT-UC03-02: Manter a consulta quando o paciente desiste do cancelamento

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-03 |
| Caso de uso / requisito | UC03 – Cancelar consulta (OF06) – médico |
| Objetivo | Cancelar uma consulta como médico, com confirmação, liberando o horário. |
| Pré-condições | Bruno Melo tem a consulta de Paciente Teste em 13/10/2026 às 09:00. |
| Passos | 1. Na tela Consultas da minha agenda, clicar em Cancelar na linha das 09:00.<br>2. Clicar em Sim, cancelar consulta.<br>3. Conferir as listas do médico e do paciente e os horários livres. |
| Dados de teste | Consulta de Paciente Teste com Bruno Melo, 13/10/2026, 09:00. |
| Resultado esperado | Aparece "Consulta cancelada com sucesso."; a consulta sai das duas listas; o horário volta a ser livre. |
| Resultado obtido | Apareceu "Consulta cancelada com sucesso.". A consulta saiu da lista do médico e da lista do paciente, o horário 09:00 voltou a ser listado como livre e, no banco, o status ficou CANCELADA. Evidência 10. |
| Situação | Aprovado |

Tabela 38 – CT-UC03-03: Cancelar uma consulta como médico, com confirmação, liberando o horário

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-04 |
| Caso de uso / requisito | UC03 – Cancelar consulta (OF06) – fluxo 4a, médico |
| Objetivo | Manter a consulta quando o médico desiste do cancelamento. |
| Pré-condições | Bruno Melo tem a consulta de Paciente Teste em 13/10/2026 às 08:00. |
| Passos | 1. Em Consultas da minha agenda, clicar em Cancelar na consulta das 08:00.<br>2. Na tela Cancelar consulta, clicar em Não, voltar.<br>3. Conferir a lista. |
| Dados de teste | Consulta de Paciente Teste com Bruno Melo, 13/10/2026, 08:00. |
| Resultado esperado | A consulta permanece agendada. |
| Resultado obtido | A tela de confirmação mostrou paciente, médico, data e horário; Não, voltar retornou a Consultas da minha agenda com a consulta ainda listada. Evidência 10. |
| Situação | Aprovado |

Tabela 39 – CT-UC03-04: Manter a consulta quando o médico desiste do cancelamento

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-05 |
| Caso de uso / requisito | UC03 – Cancelar consulta – fluxo alternativo 2a |
| Objetivo | Bloquear o cancelamento pelo paciente dentro do prazo mínimo. |
| Pré-condições | Hora da execução: 10:47 de 12/10/2026. Maria Souza autenticada, com duas consultas de hoje com Dr. Paulo Novo: às 06:00 (horário já passado, agendada no CT-UC02-09) e às 11:12 (cerca de 20 minutos à frente do momento do cancelamento). |
| Passos | 1. Em Minhas consultas, clicar em Cancelar na consulta de hoje às 06:00 (horário que já passou) e em Sim, cancelar consulta (10:47).<br>2. Em Minhas consultas, clicar em Cancelar na consulta de hoje às 11:12 e em Sim, cancelar consulta (10:51, cerca de 20 minutos antes do início). |
| Dados de teste | Consultas de Maria Souza em 12/10/2026, às 06:00 e às 11:12, com Dr. Paulo Novo. |
| Resultado esperado | Conforme a A2, o sistema informa a restrição de prazo e bloqueia o cancelamento pelo paciente. |
| Resultado obtido | Nos dois casos o cancelamento foi concluído com "Consulta cancelada com sucesso.". A operação de cancelar existe, mas o sistema não aplica a regra de prazo mínimo e também permite cancelar uma consulta cujo horário já passou. Evidência 14. |
| Situação | Reprovado |

Tabela 40 – CT-UC03-05: Bloquear o cancelamento pelo paciente dentro do prazo mínimo

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-06 |
| Caso de uso / requisito | UC03 – Cancelar consulta (ONF03) – paciente |
| Objetivo | Impedir que um paciente cancele a consulta de outro paciente. |
| Pré-condições | Consulta 14 pertence a Maria Souza (Bruno Melo, 13/10/2026, 10:30); Paciente Teste autenticado. |
| Passos | 1. Como Paciente Teste, abrir /paciente/consultas/14/cancelar.<br>2. Enviar o POST do cancelamento da consulta 14.<br>3. Conferir a lista de Maria Souza.<br>4. Como médico, tentar o POST de cancelamento de paciente. |
| Dados de teste | Consulta de id 14. |
| Resultado esperado | Cancelamento recusado e consulta de Maria Souza mantida. |
| Resultado obtido | Tanto o GET quanto o POST foram redirecionados para Minhas consultas com "Você só pode cancelar suas próprias consultas.". A consulta 14 continuou agendada. O POST feito como médico retornou 403. Evidência 11. |
| Situação | Aprovado |

Tabela 41 – CT-UC03-06: Impedir que um paciente cancele a consulta de outro paciente

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-07 |
| Caso de uso / requisito | UC03 – Cancelar consulta (ONF03) – médico |
| Objetivo | Impedir que um médico cancele a consulta da agenda de outro médico. |
| Pré-condições | Consulta 1 pertence à agenda de Ana Lima; Bruno Melo autenticado. |
| Passos | 1. Como Bruno Melo, abrir /medico/consultas/1/cancelar.<br>2. Enviar o POST de cancelamento da consulta 1.<br>3. Conferir a lista de Ana Lima. |
| Dados de teste | Consulta de id 1. |
| Resultado esperado | Cancelamento recusado e consulta mantida. |
| Resultado obtido | GET e POST redirecionaram para Consultas da minha agenda com "Você só pode cancelar consultas da sua agenda.". A consulta 1 continuou agendada na lista de Ana Lima. Evidência 11. |
| Situação | Aprovado |

Tabela 42 – CT-UC03-07: Impedir que um médico cancele a consulta da agenda de outro médico

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-08 |
| Caso de uso / requisito | UC03 – Cancelar consulta |
| Objetivo | Recusar o cancelamento de uma consulta já cancelada ou inexistente. |
| Pré-condições | A consulta 11 já foi cancelada (CT-UC03-01). |
| Passos | 1. Enviar de novo o POST de cancelamento da consulta 11.<br>2. Abrir /paciente/consultas/11/cancelar.<br>3. Abrir /paciente/consultas/9999/cancelar. |
| Dados de teste | Consultas de id 11 e 9999. |
| Resultado esperado | Aparece "Esta consulta não está mais agendada." para a cancelada e mensagem de não encontrada para a inexistente. |
| Resultado obtido | O POST e o GET da consulta 11 retornaram "Esta consulta não está mais agendada."; a consulta 9999 retornou "Consulta não encontrada.". Evidência 11. |
| Situação | Aprovado |

Tabela 43 – CT-UC03-08: Recusar o cancelamento de uma consulta já cancelada ou inexistente

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-09 |
| Caso de uso / requisito | UC03 – Cancelar consulta – passo 5 do fluxo principal |
| Objetivo | Verificar se o cancelamento notifica a outra parte e cancela o lembrete programado. |
| Pré-condições | Consulta cancelada. |
| Passos | 1. Procurar no código e nas dependências qualquer notificação ou lembrete.<br>2. Conferir o resultado do cancelamento nas telas. |
| Dados de teste | Termos pesquisados: notific, lembrete, mail. |
| Resultado esperado | Segundo a A2, o sistema cancela o lembrete e notifica a outra parte. |
| Resultado obtido | Não há nenhuma rotina de notificação ou de lembrete. O cancelamento apenas muda o status da consulta e mostra a mensagem na tela de quem cancelou. Evidência 17. |
| Situação | Não implementado |

Tabela 44 – CT-UC03-09: Verificar se o cancelamento notifica a outra parte e cancela o lembrete programado

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC03-10 |
| Caso de uso / requisito | UC03 – Cancelar consulta (OF06) – regressão |
| Objetivo | Permitir novo agendamento do horário liberado por um cancelamento. |
| Pré-condições | Consulta de Paciente Teste com Bruno Melo às 09:00 de 13/10/2026 cancelada (CT-UC03-03). |
| Passos | 1. Como Maria Souza, agendar Bruno Melo em 13/10/2026 às 09:00.<br>2. Conferir o Painel do dia de Bruno Melo. |
| Dados de teste | Horário: 09:00. |
| Resultado esperado | O horário é aceito e aparece no painel com o nome da nova paciente. |
| Resultado obtido | Apareceu "Consulta agendada com sucesso." e o painel mostrou 09:00 como "Maria Souza - Agendada", com 3 consultas. Evidência 10. |
| Situação | Aprovado |

Tabela 45 – CT-UC03-10: Permitir novo agendamento do horário liberado por um cancelamento

Fonte: elaborada pelos autores.

### 7.5 UC04 – Adicionar consulta à agenda pessoal (OF07, ONF06)

| Campo | Descrição |
|---|---|
| ID | CT-UC04-01 |
| Caso de uso / requisito | UC04 – Adicionar consulta à agenda pessoal (OF07, ONF06) |
| Objetivo | Verificar se a consulta pode ser exportada em formato padrão de calendário (.ics). |
| Pré-condições | Paciente Teste com consulta agendada. |
| Passos | 1. Procurar na tela Minhas consultas e no código algum botão, rota ou geração de arquivo de calendário.<br>2. Tentar rotas prováveis de exportação (/paciente/consultas/1/calendario, /ics e .ics). |
| Dados de teste | Consulta de id 1. |
| Resultado esperado | O paciente consegue baixar a consulta em formato de calendário. |
| Resultado obtido | Não há botão, código nem rota de exportação; as três rotas retornaram 404. O UC04 não faz parte da primeira iteração. Evidência 17. |
| Situação | Não implementado |

Tabela 46 – CT-UC04-01: Verificar se a consulta pode ser exportada em formato padrão de calendário (.ics)

Fonte: elaborada pelos autores.

### 7.6 UC05 – Receber lembrete (OF08)

| Campo | Descrição |
|---|---|
| ID | CT-UC05-01 |
| Caso de uso / requisito | UC05 – Receber lembrete (OF08) |
| Objetivo | Verificar se o paciente recebe lembrete antes da data da consulta. |
| Pré-condições | Paciente Teste com consulta agendada. |
| Passos | 1. Procurar no código e nas dependências envio de e-mail, tarefa agendada ou tela de lembretes. |
| Dados de teste | Termos pesquisados: lembrete, notific, mail, @Scheduled. |
| Resultado esperado | Um lembrete é enviado ao paciente antes da data da consulta. |
| Resultado obtido | Não há envio de e-mail, tarefa agendada ou tela de lembretes, e o projeto não tem dependência de e-mail. O UC05 não faz parte da primeira iteração. Evidência 17. |
| Situação | Não implementado |

Tabela 47 – CT-UC05-01: Verificar se o paciente recebe lembrete antes da data da consulta

Fonte: elaborada pelos autores.

### 7.7 UC06 – Manter agenda de atendimento (OF02)

| Campo | Descrição |
|---|---|
| ID | CT-UC06-01 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) |
| Objetivo | Adicionar um período válido e conferi-lo ao filtrar a data. |
| Pré-condições | Dr. Paulo Novo autenticado, sem períodos cadastrados. |
| Passos | 1. No Painel do dia, clicar em Minha agenda.<br>2. Em Adicionar período, preencher Data, Início, Fim, Duração (min.) e Valor (R$).<br>3. Clicar em Adicionar horário.<br>4. Em Visualizar data, escolher 14/10/2026 e clicar em Filtrar. |
| Dados de teste | Data: 14/10/2026; Início: 08:00; Fim: 09:15; Duração (min.): 30; Valor (R$): 200,00. |
| Resultado esperado | Aparece "Horário de atendimento adicionado com sucesso." e o período fica listado na data. |
| Resultado obtido | A mensagem esperada apareceu e o filtro de 14/10/2026 listou o período 08:00 às 09:15 com o botão Remover. A tela abriu com o campo Data preenchido com a data de hoje e o campo Duração (min.) preenchido com 30. Evidência 12. |
| Situação | Aprovado |

Tabela 48 – CT-UC06-01: Adicionar um período válido e conferi-lo ao filtrar a data

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-02 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) |
| Objetivo | Recusar período em que a hora final não é maior que a inicial. |
| Pré-condições | Dr. Paulo Novo autenticado. |
| Passos | 1. Adicionar período com Início 10:00 e Fim 09:00.<br>2. Adicionar período com Início 10:00 e Fim 10:00. |
| Dados de teste | Data: 15/10/2026; duração 30; valor 100,00. |
| Resultado esperado | As duas tentativas são recusadas com "A hora final deve ser maior que a hora inicial.". |
| Resultado obtido | As duas tentativas foram recusadas com a mensagem esperada. Evidência 12. |
| Situação | Aprovado |

Tabela 49 – CT-UC06-02: Recusar período em que a hora final não é maior que a inicial

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-03 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento – fluxo alternativo 3a |
| Objetivo | Recusar período que se sobrepõe a outro já cadastrado. |
| Pré-condições | Existe o período 14/10/2026, 08:00 às 09:15. |
| Passos | 1. Adicionar 14/10/2026 das 08:30 às 09:30 (sobreposição parcial).<br>2. Adicionar das 07:00 às 08:01 (sobreposição de 1 minuto).<br>3. Adicionar das 08:00 às 09:15 (igual).<br>4. Adicionar das 08:15 às 08:45 (contido).<br>5. Filtrar 14/10/2026. |
| Dados de teste | Data: 14/10/2026; duração 30; valor 200,00. |
| Resultado esperado | As quatro tentativas são recusadas com "Esse período entra em conflito com outro horário de atendimento." e a agenda não muda. |
| Resultado obtido | As quatro tentativas foram recusadas com a mensagem esperada e o filtro mostrou apenas o período original. Evidência 12. |
| Situação | Aprovado |

Tabela 50 – CT-UC06-03: Recusar período que se sobrepõe a outro já cadastrado

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-04 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) – borda |
| Objetivo | Aceitar períodos que apenas se encostam. |
| Pré-condições | Dr. Paulo Novo sem períodos em 16/10/2026. |
| Passos | 1. Adicionar 16/10/2026 das 08:00 às 10:00.<br>2. Adicionar 16/10/2026 das 10:00 às 12:00.<br>3. Conferir a lista de horários livres para o paciente. |
| Dados de teste | Duração: 30; valor: 200,00. |
| Resultado esperado | Os dois períodos são aceitos e o paciente vê horários de 08:00 a 11:30. |
| Resultado obtido | Os dois períodos foram aceitos e o paciente viu os 8 horários de 08:00 a 11:30. Evidência 12. |
| Situação | Aprovado |

Tabela 51 – CT-UC06-04: Aceitar períodos que apenas se encostam

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-05 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) |
| Objetivo | Recusar duração igual a zero, negativa ou vazia. |
| Pré-condições | Dr. Paulo Novo autenticado. Os valores foram enviados direto ao servidor, porque o navegador já bloqueia valores menores que 1. |
| Passos | 1. Adicionar período com Duração (min.) 0.<br>2. Adicionar período com Duração (min.) -10.<br>3. Adicionar período com Duração (min.) vazia. |
| Dados de teste | Data: 17/10/2026; Início 08:00; Fim 09:00; valor 100,00. |
| Resultado esperado | Recusa com "A duração deve ser maior que zero." (e mensagem de campo obrigatório quando vazia). |
| Resultado obtido | Duração 0 e -10 retornaram "A duração deve ser maior que zero."; duração vazia retornou "Informe a duração da consulta.". Evidência 12. |
| Situação | Aprovado |

Tabela 52 – CT-UC06-05: Recusar duração igual a zero, negativa ou vazia

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-06 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) |
| Objetivo | Recusar valor negativo e campos obrigatórios vazios. |
| Pré-condições | Dr. Paulo Novo autenticado. |
| Passos | 1. Adicionar período com Valor (R$) -1.<br>2. Adicionar período com Valor (R$), Início ou Data vazios.<br>3. Filtrar 17/10/2026 para conferir que nada foi gravado. |
| Dados de teste | Data: 17/10/2026; Início 08:00; Fim 09:00; duração 30. |
| Resultado esperado | Recusa com "O valor não pode ser negativo." e mensagens de campo obrigatório; nada é gravado. |
| Resultado obtido | Valor -1 retornou "O valor não pode ser negativo."; valor vazio, hora inicial vazia e data vazia retornaram "Informe o valor da consulta.", "Informe a hora inicial." e "Informe a data de atendimento.". O filtro de 17/10/2026 mostrou "Nenhum período cadastrado nesta data.". Evidência 12. |
| Situação | Aprovado |

Tabela 53 – CT-UC06-06: Recusar valor negativo e campos obrigatórios vazios

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-07 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) |
| Objetivo | Aceitar valor da consulta igual a zero. |
| Pré-condições | Dr. Paulo Novo autenticado. |
| Passos | 1. Adicionar 22/10/2026 das 09:00 às 10:00 com Valor (R$) 0.<br>2. Como Maria Souza, agendar o horário 09:00 e conferir o Resumo da consulta. |
| Dados de teste | Data: 22/10/2026; duração 30; valor 0,00. |
| Resultado esperado | O período é aceito e o resumo mostra o valor zerado. |
| Resultado obtido | O período foi aceito e o resumo da consulta mostrou "Valor R$ 0,00". Evidência 13. |
| Situação | Aprovado |

Tabela 54 – CT-UC06-07: Aceitar valor da consulta igual a zero

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-08 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) |
| Objetivo | Remover período sem consultas e verificar que os horários somem para o paciente. |
| Pré-condições | Dr. Paulo Novo com período 17/10/2026, 09:00 às 10:00, sem consultas. |
| Passos | 1. Como paciente, conferir os horários livres de 17/10/2026.<br>2. Como médico, filtrar 17/10/2026 e clicar em Remover no período.<br>3. Conferir a lista do médico e a visão do paciente. |
| Dados de teste | Data: 17/10/2026. |
| Resultado esperado | Aparece "Horário de atendimento removido." e os horários deixam de ser oferecidos. |
| Resultado obtido | Antes: 09:00 e 09:30 livres. Após Remover apareceu "Horário de atendimento removido.", a data ficou sem períodos e o paciente passou a ver "Não há horários livres para esta data.". Evidência 13. |
| Situação | Aprovado |

Tabela 55 – CT-UC06-08: Remover período sem consultas e verificar que os horários somem para o paciente

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-09 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento – fluxo alternativo 3b |
| Objetivo | Bloquear a remoção de período que tem consulta agendada. |
| Pré-condições | Dr. Paulo Novo com período 18/10/2026, 09:00 às 10:00; Maria Souza agendou o horário 09:00. |
| Passos | 1. Como médico, filtrar 18/10/2026 e clicar em Remover no período.<br>2. Conferir Minhas consultas da paciente, Consultas da minha agenda e o Painel do dia do médico em 18/10/2026. |
| Dados de teste | Data: 18/10/2026; consulta de Maria Souza às 09:00. |
| Resultado esperado | Conforme a A2, o sistema relaciona as consultas afetadas e exige que sejam tratadas antes de concluir a remoção. |
| Resultado obtido | O período foi removido sem aviso ("Horário de atendimento removido."). A consulta de Maria Souza continuou como agendada, mas ficou sem período: o Painel do dia de 18/10/2026 mostra Consultas 1 e Valor previsto R$ 200,00, porém "Não há período de atendimento cadastrado para esta data.". O guia do usuário da primeira iteração da construção (A3) já alerta que a remoção não cancela a consulta. Evidência 13. |
| Situação | Reprovado |

Tabela 56 – CT-UC06-09: Bloquear a remoção de período que tem consulta agendada

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-10 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento – passo 3 "altera" |
| Objetivo | Alterar um período já cadastrado. |
| Pré-condições | Existe o período 14/10/2026, 08:00 às 09:15. |
| Passos | 1. Em Minha agenda, filtrar 14/10/2026 e conferir os botões da linha do período.<br>2. Enviar GET /medico/agenda/4/editar.<br>3. Enviar GET /medico/agenda/editar.<br>4. Enviar GET /medico/agenda/alterar.<br>5. Enviar POST /medico/agenda/alterar. |
| Dados de teste | Período de id 4. |
| Resultado esperado | Conforme a A2, o médico pode alterar dia, horários, duração e valor de um período. |
| Resultado obtido | A linha do período só tem o botão Remover e as quatro requisições de edição retornaram 404. Para alterar, é preciso remover e incluir de novo. Evidência 14. |
| Situação | Não implementado |

Tabela 57 – CT-UC06-10: Alterar um período já cadastrado

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-11 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) – regra de duração e valor |
| Objetivo | Verificar se duração e valor de um novo período afetam os períodos já existentes. |
| Pré-condições | Dr. Paulo Novo com período 25/10/2026, 08:00 às 09:00, 30 min, R$ 200,00, e consulta de Maria Souza às 08:30 nesse dia. |
| Passos | 1. Abrir o Painel do dia de 25/10/2026 e conferir a linha das 08:30.<br>2. Adicionar outro período (26/10/2026, 10:00 às 12:00) com Duração (min.) 60.<br>3. Abrir novamente o Painel do dia de 25/10/2026, Consultas da minha agenda e os horários livres do paciente. |
| Dados de teste | Período novo: 26/10/2026, 10:00 às 12:00, duração 60, valor 200,00. |
| Resultado esperado | O novo período vale apenas para a sua data; a consulta das 08:30 continua visível no Painel do dia e os horários de 25/10/2026 não mudam. |
| Resultado obtido | Antes, o painel mostrava 08:00 Livre e 08:30 Maria Souza - Agendada. Depois, a linha das 08:30 sumiu do painel (apenas 08:00 Livre), embora o contador continue com Consultas 1 e a tela Consultas da minha agenda ainda liste a consulta. A duração e o valor ficam na agenda inteira, e não em cada período; o mesmo ocorreu em 14/10/2026, cujo painel passou a mostrar só 08:00 Livre com Consultas 1, embora a consulta seja das 08:30. Evidência 15. |
| Situação | Reprovado |

Tabela 58 – CT-UC06-11: Verificar se duração e valor de um novo período afetam os períodos já existentes

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-12 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (ONF03) |
| Objetivo | Impedir que um médico remova período de outro médico. |
| Pré-condições | Ana Lima tem o período de id 1 (13/10/2026); Dr. Paulo Novo autenticado. |
| Passos | 1. Como Dr. Paulo Novo, enviar o POST de remoção do período 1.<br>2. Enviar o POST de remoção de um período inexistente (id 9999).<br>3. Conferir a agenda e o painel de Ana Lima. |
| Dados de teste | Períodos de id 1 e 9999. |
| Resultado esperado | Remoção recusada e período de Ana Lima mantido. |
| Resultado obtido | O sistema retornou "Você não pode alterar a agenda de outro médico." e, para o id 9999, "Horário de atendimento não encontrado.". O período de Ana Lima e o painel permaneceram íntegros. Evidência 14. |
| Situação | Aprovado |

Tabela 59 – CT-UC06-12: Impedir que um médico remova período de outro médico

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC06-13 |
| Caso de uso / requisito | UC06 – Manter agenda de atendimento (OF02) – exploratório |
| Objetivo | Registrar o comportamento ao cadastrar período em data passada. |
| Pré-condições | Data de execução: 12/10/2026. |
| Passos | 1. Adicionar período em 01/10/2026 (data passada), das 08:00 às 09:00.<br>2. Como paciente, abrir a agenda do médico em 01/10/2026. |
| Dados de teste | Data: 01/10/2026; duração 30; valor 100,00. |
| Resultado esperado | A A2 não define regra para o cadastro; espera-se ao menos que o paciente não receba horários de data passada. |
| Resultado obtido | O período foi aceito ("Horário de atendimento adicionado com sucesso.") e o paciente viu "Não há horários livres para esta data." em 01/10/2026. Evidência 14. |
| Situação | Aprovado |

Tabela 60 – CT-UC06-13: Registrar o comportamento ao cadastrar período em data passada

Fonte: elaborada pelos autores.

### 7.8 UC07 – Consultar pacientes do dia (OF09)

| Campo | Descrição |
|---|---|
| ID | CT-UC07-01 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) |
| Objetivo | Abrir o Painel do dia na data atual. |
| Pré-condições | Ana Lima autenticada. Executado antes do CT-UC02-01, com o banco ainda sem consultas. |
| Passos | 1. Entrar como Ana Lima e abrir o Painel do dia sem escolher data. |
| Dados de teste | Data de execução: 12/10/2026. |
| Resultado esperado | O painel abre na data de hoje. |
| Resultado obtido | O painel abriu com Data 12/10/2026, Consultas 0, Horas previstas 0h00 e Valor previsto R$ 0,00. Evidência 05. |
| Situação | Aprovado |

Tabela 61 – CT-UC07-01: Abrir o Painel do dia na data atual

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-02 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) – fluxo principal |
| Objetivo | Listar em ordem de horário os pacientes agendados e os horários livres. |
| Pré-condições | Bruno Melo com consultas de Paciente Teste às 08:00 e 09:00 e de Maria Souza às 10:30, em 13/10/2026. |
| Passos | 1. Abrir o Painel do dia de Bruno Melo e escolher 13/10/2026.<br>2. Conferir as linhas da agenda. |
| Dados de teste | Data: 13/10/2026. |
| Resultado esperado | Horários em ordem; os ocupados mostram o nome do paciente e "Agendada"; os demais mostram "Livre". |
| Resultado obtido | O painel mostrou os 8 horários de 08:00 a 11:30 em ordem, com 08:00 e 09:00 "Paciente Teste - Agendada", 10:30 "Maria Souza - Agendada" e os demais "Livre". Evidência 10. |
| Situação | Aprovado |

Tabela 62 – CT-UC07-02: Listar em ordem de horário os pacientes agendados e os horários livres

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-03 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) – fluxo alternativo 2a |
| Objetivo | Informar quando não há consulta e mostrar a agenda inteira livre. |
| Pré-condições | Ana Lima com período em 13/10/2026 e sem consultas marcadas. Executado antes do CT-UC02-01. |
| Passos | 1. Abrir o Painel do dia de Ana Lima em 13/10/2026. |
| Dados de teste | Data: 13/10/2026. |
| Resultado esperado | O sistema informa a falta de consultas pela contagem "Consultas 0" e pela agenda inteira como "Livre", sem mensagem própria. |
| Resultado obtido | O painel mostrou Consultas 0, Horas previstas 0h00, Valor previsto R$ 0,00 e os 8 horários como "Livre", sem mensagem própria. Evidência 05. |
| Situação | Aprovado |

Tabela 63 – CT-UC07-03: Informar quando não há consulta e mostrar a agenda inteira livre

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-04 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) |
| Objetivo | Informar quando não há período de atendimento na data. |
| Pré-condições | Ana Lima sem período em 20/10/2026. Executado antes do CT-UC02-01, com o banco ainda sem consultas. |
| Passos | 1. Abrir o Painel do dia de Ana Lima em 20/10/2026. |
| Dados de teste | Data: 20/10/2026. |
| Resultado esperado | Mensagem "Não há período de atendimento cadastrado para esta data.". |
| Resultado obtido | A mensagem apareceu com Consultas 0 e totais zerados. Evidência 05. |
| Situação | Aprovado |

Tabela 64 – CT-UC07-04: Informar quando não há período de atendimento na data

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-05 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) – passo 5 |
| Objetivo | Trocar a data do painel e ver os dados da data escolhida. |
| Pré-condições | Ana Lima autenticada. Executado antes do CT-UC02-01, com o banco ainda sem consultas. |
| Passos | 1. No Painel do dia, alterar o campo Data para 13/10/2026 e clicar em Consultar.<br>2. Repetir com 20/10/2026. |
| Dados de teste | Datas: 13/10/2026 e 20/10/2026. |
| Resultado esperado | O painel passa a mostrar os dados de cada data escolhida. |
| Resultado obtido | Cada consulta ao painel mostrou a data escolhida e os dados correspondentes (agenda de 13/10/2026 e mensagem de falta de período em 20/10/2026). Evidência 05. |
| Situação | Aprovado |

Tabela 65 – CT-UC07-05: Trocar a data do painel e ver os dados da data escolhida

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-06 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) – fluxo alternativo 4a |
| Objetivo | Omitir o valor quando o médico não informou valor. |
| Pré-condições | Dr. Paulo Novo com período 22/10/2026, 09:00 às 10:00, valor 0,00; consulta de Maria Souza às 09:00. Como o campo Valor (R$) é obrigatório (CT-UC06-06), usou-se o valor 0 como equivalente a "não informou valor". |
| Passos | 1. Abrir o Painel do dia de Dr. Paulo Novo em 22/10/2026. |
| Dados de teste | Data: 22/10/2026. |
| Resultado esperado | Conforme a A2, o painel mostra a previsão de horas e omite o valor. |
| Resultado obtido | O painel mostrou Horas previstas 0h30 e Valor previsto R$ 0,00. O valor não é omitido; a tela também mostra R$ 0,00 em Consultas da minha agenda. Evidência 13. |
| Situação | Reprovado |

Tabela 66 – CT-UC07-06: Omitir o valor quando o médico não informou valor

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-07 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) |
| Objetivo | Verificar que consulta cancelada não aparece no painel nem na lista. |
| Pré-condições | Consulta de Bruno Melo às 09:00 de 13/10/2026 cancelada (CT-UC03-03). |
| Passos | 1. Abrir o Painel do dia de Bruno Melo em 13/10/2026.<br>2. Abrir Consultas da minha agenda. |
| Dados de teste | Data: 13/10/2026. |
| Resultado esperado | O horário 09:00 aparece como "Livre" e a consulta cancelada não é listada. |
| Resultado obtido | A linha das 09:00 apareceu como "Livre" e a lista de Consultas da minha agenda não trouxe a consulta cancelada. Evidência 10. |
| Situação | Aprovado |

Tabela 67 – CT-UC07-07: Verificar que consulta cancelada não aparece no painel nem na lista

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC07-08 |
| Caso de uso / requisito | UC07 – Consultar pacientes do dia (OF09) – tela Consultas |
| Objetivo | Listar as consultas do médico com data, horário, paciente e valor, e informar quando não há nenhuma. |
| Pré-condições | Carlos Reis sem consultas (executado antes do CT-UC02-03); Bruno Melo com consultas (executado depois do CT-UC02-05). |
| Passos | 1. Como Carlos Reis, clicar em Consultas.<br>2. Como Bruno Melo, clicar em Consultas. |
| Dados de teste | Médicos: Carlos Reis e Bruno Melo. |
| Resultado esperado | A lista mostra Data, Horário, Paciente e Valor; vazia, mostra "Não há consultas agendadas.". |
| Resultado obtido | Carlos Reis viu "Não há consultas agendadas.". Bruno Melo viu a tela Consultas da minha agenda com as colunas Data, Horário, Paciente, Valor e Ação (por exemplo, 13/10/2026, 08:00, Paciente Teste, R$ 150,00). Evidências 05 e 10. |
| Situação | Aprovado |

Tabela 68 – CT-UC07-08: Listar as consultas do médico com data, horário, paciente e valor, e informar quando não há nenhuma

Fonte: elaborada pelos autores.

### 7.9 UC08 – Consultar horas e valores previstos (OF10)

O UC08 aparece no diagrama de casos de uso da A2, mas não tem descrição detalhada; o resultado esperado vem do objetivo OF10 e do passo 4 do UC07.

| Campo | Descrição |
|---|---|
| ID | CT-UC08-01 |
| Caso de uso / requisito | UC08 – Consultar horas e valores previstos (OF10) |
| Objetivo | Conferir horas e valor previstos com duas consultas. |
| Pré-condições | Bruno Melo com duas consultas de 30 minutos a R$ 150,00 em 13/10/2026. |
| Passos | 1. Abrir o Painel do dia de Bruno Melo em 13/10/2026. |
| Dados de teste | Duas consultas de 30 minutos, R$ 150,00 cada. |
| Resultado esperado | Consultas 2, Horas previstas 1h00 e Valor previsto R$ 300,00. |
| Resultado obtido | O painel mostrou Consultas 2, Horas previstas 1h00 e Valor previsto R$ 300,00. Evidência 10. |
| Situação | Aprovado |

Tabela 69 – CT-UC08-01: Conferir horas e valor previstos com duas consultas

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC08-02 |
| Caso de uso / requisito | UC08 – Consultar horas e valores previstos (OF10) |
| Objetivo | Verificar o recálculo dos totais após um cancelamento. |
| Pré-condições | Bruno Melo com 3 consultas em 13/10/2026 (R$ 450,00). |
| Passos | 1. Cancelar uma das consultas (CT-UC03-03).<br>2. Abrir o Painel do dia em 13/10/2026. |
| Dados de teste | Cancelamento da consulta das 09:00. |
| Resultado esperado | Os totais diminuem: 2 consultas, 1h00 e R$ 300,00. |
| Resultado obtido | O painel mostrou Consultas 2, Horas previstas 1h00 e Valor previsto R$ 300,00. Evidência 10. |
| Situação | Aprovado |

Tabela 70 – CT-UC08-02: Verificar o recálculo dos totais após um cancelamento

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC08-03 |
| Caso de uso / requisito | UC08 – Consultar horas e valores previstos (OF10) |
| Objetivo | Somar os valores gravados em cada consulta quando o valor muda entre agendamentos. |
| Pré-condições | Dr. Paulo Novo autenticado. |
| Passos | 1. Adicionar 23/10/2026, 08:00 às 09:00, duração 30, valor 100,00, e agendar o horário 08:00 com Paciente Teste.<br>2. Adicionar 23/10/2026, 13:00 às 14:00, duração 30, valor 300,00, e agendar o horário 13:00 com Paciente Teste.<br>3. Abrir o Painel do dia e a tela Consultas em 23/10/2026. |
| Dados de teste | Valores: R$ 100,00 e R$ 300,00. |
| Resultado esperado | Consultas 2, Horas previstas 1h00 e Valor previsto R$ 400,00; cada consulta mantém o valor da época do agendamento. |
| Resultado obtido | O painel mostrou Consultas 2, Horas previstas 1h00 e Valor previsto R$ 400,00, e a tela Consultas listou R$ 100,00 e R$ 300,00. Evidência 13. |
| Situação | Aprovado |

Tabela 71 – CT-UC08-03: Somar os valores gravados em cada consulta quando o valor muda entre agendamentos

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-UC08-04 |
| Caso de uso / requisito | UC08 – Consultar horas e valores previstos (OF10) – período |
| Objetivo | Consultar horas e valor previstos de um período de vários dias. |
| Pré-condições | Médico autenticado. |
| Passos | 1. Procurar no Painel do dia um campo de intervalo de datas.<br>2. Conferir o formulário do painel. |
| Dados de teste | Nenhum. |
| Resultado esperado | O objetivo OF10 fala em consultas "do período": o médico deveria poder consultar os totais de um intervalo de datas. |
| Resultado obtido | O painel tem apenas um campo Data e mostra os totais de um único dia. Não existe consulta por período. Evidência 05. |
| Situação | Não implementado |

Tabela 72 – CT-UC08-04: Consultar horas e valor previstos de um período de vários dias

Fonte: elaborada pelos autores.

### 7.10 Requisitos não funcionais (ONF01, ONF02)

Os objetivos ONF03, ONF04 e ONF05 são cobertos por casos já descritos: ONF03 por CT-AUT-10, CT-AUT-11, CT-UC03-06, CT-UC03-07 e CT-UC06-12; ONF04 por CT-UC02-04; ONF05 por CT-AUT-12.

| Campo | Descrição |
|---|---|
| ID | CT-ONF-01 |
| Caso de uso / requisito | ONF01 – Web e responsivo |
| Objetivo | Verificar que as telas funcionam em tela de celular (375 px de largura). |
| Pré-condições | Aplicação aberta no navegador com a largura reduzida a 375 px. |
| Passos | 1. Abrir as telas de login, cadastro, Escolha um médico, Resumo da consulta, Minhas consultas, Painel do dia, Minha agenda e Consultas da minha agenda.<br>2. Conferir se há rolagem horizontal ou botões cortados. |
| Dados de teste | Largura de 375 px. |
| Resultado esperado | Todas as telas ficam legíveis e utilizáveis, sem rolagem horizontal. |
| Resultado obtido | A executar pelo grupo. A verificação é visual. Nas respostas HTTP observou-se o cabeçalho de visualização (viewport) e o uso do Bootstrap, o que indica a intenção de responsividade, mas não substitui a conferência. |
| Situação | Manual |

Tabela 73 – CT-ONF-01: Verificar que as telas funcionam em tela de celular (375 px de largura)

Fonte: elaborada pelos autores.

| Campo | Descrição |
|---|---|
| ID | CT-ONF-02 |
| Caso de uso / requisito | ONF02 – Desempenho |
| Objetivo | Medir o tempo da consulta de horários livres e da confirmação de agendamento. |
| Pré-condições | Paciente Teste autenticado; aplicação em contêiner local. |
| Passos | 1. Medir 10 vezes o carregamento da agenda de Ana Lima em 13/10/2026.<br>2. Medir 10 vezes o carregamento da agenda de Bruno Melo em 13/10/2026.<br>3. Medir 10 vezes o carregamento da agenda de Carlos Reis em 15/10/2026 (data com período).<br>4. Medir uma confirmação de agendamento (POST /paciente/consultas). |
| Dados de teste | Medição por curl (tempo total de cada resposta). |
| Resultado esperado | Cada resposta em menos de 3 segundos. |
| Resultado obtido | Nas 30 medições de carregamento de agendas com período, a consulta de horários livres levou entre 0,004 s e 0,027 s, e a confirmação levou 0,026 s. A medição foi feita no próprio computador, sem rede e com poucos dados, e não representa o ambiente publicado. Evidências 16 e 19. |
| Situação | Aprovado |

Tabela 74 – CT-ONF-02: Medir o tempo da consulta de horários livres e da confirmação de agendamento

Fonte: elaborada pelos autores.

## 8 Resultado da execução

O grupo elaborou 68 casos de teste, todos com situação definida. A Tabela 75 resume o resultado por caso de uso.

| Caso de uso | Casos | Aprovado | Reprovado | Não implementado | Manual |
|---|---|---|---|---|---|
| Cadastro e acesso (OF01) | 13 | 12 | 1 | 0 | 0 |
| UC01 – Consultar agenda do médico | 7 | 7 | 0 | 0 | 0 |
| UC02 – Agendar consulta | 9 | 6 | 2 | 1 | 0 |
| UC03 – Cancelar consulta | 10 | 8 | 1 | 1 | 0 |
| UC04 – Adicionar consulta à agenda pessoal | 1 | 0 | 0 | 1 | 0 |
| UC05 – Receber lembrete | 1 | 0 | 0 | 1 | 0 |
| UC06 – Manter agenda de atendimento | 13 | 10 | 2 | 1 | 0 |
| UC07 – Consultar pacientes do dia | 8 | 7 | 1 | 0 | 0 |
| UC08 – Consultar horas e valores previstos | 4 | 3 | 0 | 1 | 0 |
| Requisitos não funcionais | 2 | 1 | 0 | 0 | 1 |
| **Total** | **68** | **54** | **7** | **6** | **1** |

Tabela 75 – Resultado da execução por caso de uso

Fonte: elaborada pelos autores.

Dos 68 casos, 54 foram aprovados, 7 reprovados, 6 não implementados e 1 ficou para execução manual. Cadastro, acesso, consulta da agenda, agendamento, cancelamento com confirmação, manutenção de períodos e Painel do dia funcionam no caminho feliz, mas há exceções em passos do fluxo principal: o passo 5 do UC03 (notificar a outra parte e cancelar o lembrete, CT-UC03-09), o passo 3 do UC06 (alterar período, CT-UC06-10) e a pós-condição do UC02 (programar o lembrete, CT-UC02-08). A duração e o valor guardados para a agenda inteira desfazem a lista de horários do Painel do dia quando o médico cadastra um novo período (CT-UC06-11), e o OF10 só é atendido por dia (CT-UC08-04).

Aplicando o critério de aprovação da versão definido no capítulo 6, a versão testada não é aprovada: há casos Reprovado ou Não implementado em passos do fluxo principal (CT-UC03-09, CT-UC06-10 e CT-UC06-11), na pós-condição do UC02 (CT-UC02-08) e no objetivo OF10, que o UC08 realiza sem descrição detalhada (CT-UC08-04). Os demais casos Reprovado ou Não implementado estão em fluxos alternativos ou em regras não previstas na A2 (CT-UC02-05, CT-UC03-05, CT-UC06-09, CT-UC07-06, CT-UC02-09 e CT-AUT-13) ou no que a primeira iteração declarou fora do escopo (CT-UC04-01 e CT-UC05-01).

## 9 Divergências entre especificação e implementação

Esta seção explica, em linguagem simples, onde o sistema entregue se afasta do que a A2 descreve. A Tabela 76 traz as divergências, com a mesma classificação de situação do capítulo 6. As sugestões servem de apoio às próximas iterações; nenhum código foi alterado.

| Código | Divergência | Caso de teste | Situação | Sugestão |
|---|---|---|---|---|
| D-01 | Um paciente consegue marcar duas consultas com o mesmo médico no mesmo dia. A A2 (UC02, fluxo 5b) manda bloquear e mostrar a consulta que já existe. | CT-UC02-05 | Reprovado | Antes de gravar, procurar consulta agendada do mesmo paciente, médico e data; se existir, recusar e mostrá-la. |
| D-02 | Não existe prazo mínimo para o paciente cancelar. Ele cancela até uma consulta que começa em poucos minutos ou que já passou. A A2 (UC03, fluxo 2a) diz que o sistema deve bloquear o cancelamento do paciente dentro do prazo, sem valer para o médico. | CT-UC03-05 | Reprovado | Definir o prazo (por exemplo, 24 horas), aplicá-lo só ao paciente e recusar o cancelamento de consultas já realizadas. |
| D-03 | O médico consegue remover um período que já tem consulta marcada, sem nenhum aviso. A consulta continua agendada, mas o horário deixa de existir na agenda. A A2 (UC06, fluxo 3b) pede que o sistema liste as consultas afetadas e exija que sejam tratadas antes. | CT-UC06-09 | Reprovado | Ao remover, listar as consultas do período e só concluir depois que o médico as cancelar. |
| D-04 | Não é possível alterar um período já cadastrado; só incluir e remover. A A2 (UC06) diz que o médico define e altera os períodos. | CT-UC06-10 | Não implementado | Incluir a tela e a rota de edição de período, com as mesmas validações da inclusão. |
| D-05 | A duração e o valor da consulta valem para a agenda inteira, e não para cada período. Ao cadastrar um novo período com duração diferente, os períodos antigos passam a ser divididos de outro jeito e uma consulta já marcada some da lista de horários do Painel do dia, embora continue contada nos totais. | CT-UC06-11 | Reprovado | Gravar duração e valor em cada período, ou impedir a mudança quando houver consultas marcadas. |
| D-06 | Quando o valor da consulta é zero, o Painel do dia mostra "Valor previsto R$ 0,00". A A2 (UC07, fluxo 4a) diz que, sem valor informado, o painel deve omitir o valor. | CT-UC07-06 | Reprovado | Não exibir o valor previsto quando o valor da consulta for zero ou não informado. |
| D-07 | O painel do médico mostra horas e valor de um único dia. O objetivo OF10 fala em consultas do período. | CT-UC08-04 | Não implementado | Acrescentar a consulta por intervalo de datas (data inicial e final). |
| D-08 | O sistema não programa lembrete ao agendar, não cancela o lembrete nem avisa a outra parte ao cancelar, e não envia lembrete antes da data (UC02 pós-condição, UC03 passo 5 e UC05). | CT-UC02-08, CT-UC03-09, CT-UC05-01 | Não implementado | Implementar o UC05 (envio de e-mail agendado) e ligá-lo ao agendamento e ao cancelamento. |
| D-09 | O paciente não consegue levar a consulta para a agenda pessoal em formato de calendário (UC04, OF07, ONF06). Está fora da primeira iteração. | CT-UC04-01 | Não implementado | Gerar um arquivo .ics por consulta, com botão em Minhas consultas. |
| D-10 | O sistema oferece e aceita agendamento de horário de hoje que já passou; só as datas anteriores a hoje são barradas. | CT-UC02-09 | Reprovado | Na data de hoje, oferecer apenas horários posteriores à hora atual e validar o mesmo no servidor. |
| D-11 | O mesmo e-mail escrito com letras maiúsculas é aceito como uma conta nova, e o guia do usuário da primeira iteração da construção (A3) diz que o mesmo e-mail não pode ser usado em duas contas. | CT-AUT-13 | Reprovado | Gravar e comparar o e-mail sempre em minúsculas. |
| D-12 | Observação: o sistema não tem página própria de acesso negado. Quando um perfil abre a tela do outro, aparece a página de erro padrão do Spring (Whitelabel Error Page, erro 403), sem explicação para o usuário. O caso passa porque o acesso é negado. | CT-AUT-11 | Aprovado | Criar uma página de acesso negado com mensagem em português e link para voltar. |

Tabela 76 – Divergências entre especificação e implementação

Fonte: elaborada pelos autores.

## 10 Matriz de rastreabilidade

A Tabela 77 liga cada objetivo funcional e não funcional da A2 ao caso de uso que o realiza e aos casos de teste que o verificam.

| Objetivo | Caso de uso | Casos de teste |
|---|---|---|
| OF01 | Cadastro e acesso | CT-AUT-01 a CT-AUT-13 |
| OF02 | UC06 | CT-UC06-01 a CT-UC06-13 |
| OF03 | UC01 | CT-UC01-01 a CT-UC01-07 |
| OF04 | UC02 | CT-UC02-01, CT-UC02-02, CT-UC02-03, CT-UC02-05, CT-UC02-06, CT-UC02-09 |
| OF05 | UC02 | CT-UC02-01, CT-UC02-04, CT-UC02-06, CT-UC02-07 |
| OF06 | UC03 | CT-UC03-01 a CT-UC03-10 |
| OF07 | UC04 | CT-UC04-01 |
| OF08 | UC05 | CT-UC05-01, CT-UC02-08, CT-UC03-09 |
| OF09 | UC07 | CT-UC07-01 a CT-UC07-08 |
| OF10 | UC08 (e UC07) | CT-UC08-01 a CT-UC08-04, CT-UC07-02 |
| ONF01 | Transversal | CT-ONF-01 |
| ONF02 | UC01, UC02 | CT-ONF-02 |
| ONF03 | Transversal | CT-AUT-10, CT-AUT-11, CT-UC03-06, CT-UC03-07, CT-UC06-12 |
| ONF04 | UC02 | CT-UC02-04 |
| ONF05 | Cadastro e acesso | CT-AUT-12 |
| ONF06 | UC04 | CT-UC04-01 |
| ONF07 | Transversal | Não coberto neste plano (exige teste de usabilidade com usuários) |
| ONF08 | Processo | Não coberto neste plano (verificado no repositório e no quadro do projeto) |

Tabela 77 – Matriz de rastreabilidade (OF/ONF, caso de uso e caso de teste)

Fonte: elaborada pelos autores.

## Referências

INSTITUTE OF ELECTRICAL AND ELECTRONICS ENGINEERS. **IEEE Std 829-2008**: IEEE Standard for Software and System Test Documentation. New York: IEEE, 2008. Substituída pela ISO/IEC/IEEE 29119-3:2013.

INTERNATIONAL ORGANIZATION FOR STANDARDIZATION. **ISO/IEC/IEEE 29119-3:2013**: Software and systems engineering – Software testing – Part 3: Test documentation. Geneva: ISO, 2013.

PAULO, C. H. G. et al. **Sistema de Agendamento de Consultas**: A2 - Aplicando Conhecimento. São Paulo: Universidade Presbiteriana Mackenzie, 2026a.

PAULO, C. H. G. et al. **Sistema de Agendamento de Consultas**: primeira iteração da fase de construção. São Paulo: Universidade Presbiteriana Mackenzie, 2026b. Inclui o guia do usuário da primeira iteração da construção (A3).

SASAKI, T. M. **Prática profissional em análise e desenvolvimento de sistemas**: texto de apoio. São Paulo: Universidade Presbiteriana Mackenzie, [2023].

