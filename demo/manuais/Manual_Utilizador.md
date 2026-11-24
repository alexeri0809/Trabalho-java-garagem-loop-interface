# Manual do Utilizador — Programa Garagem

> 📗 Este documento é o **Manual do Utilizador**, com instruções para usar o programa. Para detalhes técnicos (arquitetura MVC, padrões de desenho, testes unitários), consulta o [Manual Técnico](./Manual_Tecnico.md).

---

## 1. O que é este programa

Um programa em Java com **interface gráfica (JavaFX)** que simula **três garagens circulares interligadas (rodas)**: a da **esquerda**, a **central** e a da **direita**. Cada roda tem 5 vagas e aceita até **4 veículos** (uma vaga fica sempre livre, para o loop). Escolhes a roda e os veículos de cada uma — **Carros**, **Barcos** ou **Motas**, em qualquer combinação — e depois pões as três rodas a **rodar ao mesmo tempo**.

Cada veículo recebe automaticamente uma **matrícula aleatória**:

- **Carros e Motas:** formato tipo `FS-13-HD`
- **Barcos:** formato tipo `6º-CO-7-15-21`

O programa tem **duas janelas**: a da garagem (onde escolhes os veículos) e a do loop (onde os vês a rodar).

---

## 2. Antes de começar: preparar o computador

É necessário ter o **Java (JDK 17 ou mais recente)** e o **Maven** instalados. O JavaFX **não** precisa de ser instalado à parte: o Maven descarrega-o sozinho na primeira vez (precisa de internet).

Se não tiveres acesso de administrador no computador, ambos podem ser instalados sem esse acesso, através de ficheiros `.zip`.

### Java (JDK)

1. Verifica se já o tens:
   ```powershell
   javac -version
   ```
2. Se aparecer erro "não reconhecido", descarrega a versão em **`.zip`** (não o instalador) em [adoptium.net](https://adoptium.net/temurin/releases/), extrai para uma pasta tua (ex: `C:\Users\<utilizador>\java\jdk-21`) e adiciona ao PATH do teu utilizador:
   ```powershell
   [Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Users\<utilizador>\java\jdk-21\bin", "User")
   ```

### Maven

1. Verifica se já o tens:
   ```powershell
   mvn -version
   ```
2. Se aparecer erro "não reconhecido", descarrega o **Binary zip archive** em [maven.apache.org/download.cgi](https://maven.apache.org/download.cgi), extrai para uma pasta tua (ex: `C:\Users\<utilizador>\java\apache-maven-3.9.16`) e adiciona ao PATH do teu utilizador:
   ```powershell
   [Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Users\<utilizador>\java\apache-maven-3.9.16\bin", "User")
   ```

**Importante:** depois de definires o PATH, **fecha o terminal completamente e abre um novo**. Se não quiseres fechar a janela, podes atualizar o PATH só na sessão atual com:
```powershell
$env:Path = [Environment]::GetEnvironmentVariable("Path", "User") + ";" + [Environment]::GetEnvironmentVariable("Path", "Machine")
```

---

## 3. Compilar, testar e correr o programa

A partir da pasta `demo` do projeto (onde está o `pom.xml`):

```powershell
# Compilar
mvn compile

# Correr os testes unitários
mvn test

# Abrir o programa (janela gráfica)
mvn javafx:run
```

Se tudo correr bem, `mvn test` termina com `BUILD SUCCESS` e `mvn javafx:run` abre a janela **Garagem**.

---

## 4. Janela 1 — Garagem

À esquerda está o painel **Novo veículo**; à direita há três separadores, um por roda, com os veículos de cada uma.

| Elemento | O que faz |
|---|---|
| **Roda** | Escolhe em que roda o veículo vai ficar (esquerda, central ou direita). O separador da direita acompanha esta escolha, e vice-versa. |
| **Tipo de veículo** | Escolhe Carro, Barco ou Mota. |
| **Marca** e **Modelo** | Escreves aqui o texto. A **matrícula não se escreve**: é gerada automaticamente. |
| **Adicionar à garagem** | Estaciona o veículo na roda escolhida (também podes carregar em **Enter** num dos campos). A mensagem por baixo mostra a matrícula gerada. |
| **Separadores (listas)** | Cada um mostra tipo, matrícula, marca e modelo dos veículos dessa roda. O título mostra quantos lugares estão ocupados (ex: `Roda da esquerda (2/4)`). |
| **Remover selecionado** | Retira da garagem o veículo que escolheste na lista do separador aberto. |
| **Iniciar loop ▶** | Abre a janela do loop com os veículos de todas as rodas. Fica desligado se não houver nenhum veículo. |

Se tentares adicionar sem preencher a marca e o modelo, aparece um aviso a vermelho. Quando uma roda tem **4 veículos** o botão *Adicionar* fica desligado para essa roda: remove um veículo ou escolhe outra roda. As rodas são independentes: uma roda cheia não impede de adicionar às outras. Podes deixar uma roda sem veículos (fica parada), desde que haja pelo menos 1 veículo no total.

---

## 5. Janela 2 — Loop

São **três rodas lado a lado**, ligadas entre si: **esquerda**, **central** e **direita**. Cada roda é uma garagem circular de 5 vagas: os veículos dessa roda ocupam as primeiras vagas e **todas as restantes ficam `Vazio`** (há sempre pelo menos 1). Os veículos têm cor consoante o tipo: **azul** (carro), **azul-esverdeado** (barco) e **laranja** (mota).

**Como rodam:** as três rodas andam **ao mesmo tempo**, a cada 0,7 segundos. Em cada roda, o `Vazio` avança e o veículo que estava à frente dele recua uma vaga para o lugar vago. O sentido em que o `Vazio` anda está escrito debaixo de cada roda:

| Roda | `Vazio` anda no sentido | Os veículos recuam no sentido |
|---|---|---|
| Esquerda | horário ↻ | anti-horário |
| Central | horário ↻ | anti-horário |
| Direita | anti-horário ↺ | horário |

**Fim de cada volta:** em cada roda, quando o `Vazio` completa uma volta, o **contador dessa roda** sobe uma volta, o veículo **mais antigo dessa roda** sai e entra um veículo **novo do mesmo tipo** (mesma marca e modelo) com **matrícula nova**. A mensagem por baixo mostra `Saiu` e `Entrou` (uma linha por cada roda que completou a volta). Uma roda sem veículos fica parada.

**Troca entre rodas:** quando as **três rodas estão cheias** (4 veículos cada), no fim de volta da roda central o veículo mais antigo da roda da **esquerda** passa para a roda do meio e é substituído pelo mais antigo do meio; a seguir faz-se o mesmo com a roda da **direita**. A mensagem mostra uma linha `Troca: ...` por cada troca. Se alguma roda não estiver cheia, não há trocas e cada roda mantém os seus veículos.

| Botão | O que faz |
|---|---|
| **Pausar / Retomar** | Pára ou continua a rotação das três rodas. |
| **◀ Voltar à garagem** | Pára o loop e volta à janela 1. As listas já mostram os veículos novos que entraram. |

---

## 5.1 Fotos dos veículos

As fotos ficam na pasta `demo/src/main/resources/imagens` e chamam-se `carro1.png`, `carro2.png`, `carro3.png`, `barco1.png` ... `barco3.png`, `mota1.png` ... `mota3.png`. Vêm com imagens de exemplo (só o nome do ficheiro): para usar as tuas, **substitui cada ficheiro por uma foto com o mesmo nome** (de preferência PNG com fundo transparente, na proporção 2:1, por exemplo 280x140 px). Cada veículo usa sempre a mesma foto, escolhida pelo seu número, e a foto roda com ele nas rodas e aparece também nas listas da garagem. Para ter mais fotos por tipo, acrescenta `carro4.png`, `carro5.png`, etc. (com números seguidos): o programa conta sozinho as fotos que existem. Se um ficheiro não existir, a vaga mostra só o ícone e o texto. Depois de trocar as fotos, corre `mvn javafx:run` outra vez.

## 6. Alterar as cores e o aspeto

- **Cores dos botões:** ficheiro `src/main/java/com/example/view/componentes/BotaoVisual.java`, linhas **13 a 18** (verde principal, cinza secundário, vermelho de remover, cor da letra, cantos redondos e tamanho da letra).
- **Cores dos veículos no loop:** ficheiro `src/main/java/com/example/view/componentes/VagaView.java`, linhas **18 a 20**.
- **Sentido de cada roda:** ficheiro `src/main/java/com/example/loop/PosicaoRoda.java`, linhas **10 a 12** (`HORARIO` ou `ANTI_HORARIO` para cada roda). A janela ajusta-se sozinha.
- **Velocidade do loop:** ficheiro `src/main/java/com/example/controller/LoopController.java`, constante `PAUSA_MS`.
- **Tamanho e fundo das janelas:** no topo de `CenaGaragemView.java` e `CenaLoopView.java`.

As cores são em formato hexadecimal (ex: `#2e7d32`). Se acrescentares linhas ao ficheiro, os números acima deslocam-se; procura o nome da constante (`COR_PRINCIPAL`, etc.).

---

## 7. Problemas comuns

| Sintoma | Solução |
|---|---|
| `javac`/`java`/`mvn` não reconhecido | Confirma que o JDK e o Maven estão instalados e no PATH (ver secção 2); depois de alterares o PATH, abre sempre um terminal novo |
| `winget install Apache.Maven` diz "No package found" | O Maven não está disponível pelo `winget` — usa a instalação manual em `.zip` (ver secção 2) |
| `mvn javafx:run` falha a descarregar dependências | O Maven precisa de internet na primeira execução para obter o JavaFX; confirma a ligação e volta a tentar |
| `Error: JavaFX runtime components are missing` | Estás a correr com `java` diretamente. Usa `mvn javafx:run`, que põe o JavaFX no sítio certo |
| `release version 17 not supported` ou erro de versão do Java | O JavaFX 21 precisa de JDK 17 ou mais recente; confirma com `javac -version` |
| Acentos aparecem trocados | O `pom.xml` já define UTF-8; se persistir, corre `mvn clean javafx:run` |
| `pom.xml` com erro / pasta com ícone vermelho | Confirma que todas as tags (`<dependency>`, `<plugin>`, etc.) estão **dentro** de `<project>...</project>`, nunca depois do `</project>` de fecho |
| `mvn test` mostra falhas | Confirma que os ficheiros de teste estão em `src/test/java/com/example/...`, com os mesmos subpacotes do código principal |

Para mais detalhes técnicos, consulta o **[Manual Técnico](./Manual_Tecnico.md)**.
