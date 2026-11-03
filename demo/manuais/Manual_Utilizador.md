# Manual do Utilizador — Programa Garagem

> 📗 Este documento é o **Manual do Utilizador**, com instruções para usar o programa. Para detalhes técnicos (arquitetura MVC, padrões de desenho, testes unitários), consulta o [Manual Técnico](./Manual_Tecnico.md).

---

## 1. O que é este programa

Um programa em Java com **interface gráfica (JavaFX)** que simula uma **garagem com 4 lugares para veículos** (mais 1 vaga que fica sempre livre, para o loop). Escolhes que veículos queres estacionar — **Carros**, **Barcos** ou **Motas**, em qualquer combinação — e depois pões-os a **rodar** numa garagem circular de 5 vagas.

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

À esquerda está o painel **Novo veículo**; à direita a lista dos veículos que já estão na garagem.

| Elemento | O que faz |
|---|---|
| **Tipo de veículo** | Escolhe Carro, Barco ou Mota. |
| **Marca** e **Modelo** | Escreves aqui o texto. A **matrícula não se escreve**: é gerada automaticamente. |
| **Adicionar à garagem** | Estaciona o veículo (também podes carregar em **Enter** num dos campos). A mensagem por baixo mostra a matrícula gerada. |
| **Lista de veículos** | Mostra tipo, matrícula, marca e modelo. O título mostra quantos lugares estão ocupados (ex: `2/4`). |
| **Remover selecionado** | Retira da garagem o veículo que escolheste na lista. |
| **Iniciar loop ▶** | Abre a janela do loop com os veículos que estão na lista. Fica desligado se a garagem estiver vazia. |

Se tentares adicionar sem preencher a marca e o modelo, aparece um aviso a vermelho. Quando a garagem tem **4 veículos** o botão *Adicionar* fica desligado: remove um veículo para poderes adicionar outro.

---

## 5. Janela 2 — Loop

É uma garagem **circular com 5 vagas**. Os veículos que escolheste ocupam as primeiras vagas e **todas as restantes ficam `Vazio`** (há sempre pelo menos 1). Com **2 carros e 1 mota** ficam 3 veículos e 2 vagas vazias. Os veículos têm cor consoante o tipo: **azul** (carro), **azul-esverdeado** (barco) e **laranja** (mota).

**Como roda:** a cada 0,7 segundos o `Vazio` avança e o veículo que estava à frente dele recua uma vaga para o lugar vago.

**Fim de cada volta:** o **contador no centro** sobe uma volta, o veículo **mais antigo** sai e entra um veículo **novo do mesmo tipo** (mesma marca e modelo) com **matrícula nova**. A mensagem por baixo mostra `Saiu` e `Entrou`. A combinação escolhida mantém-se: 2 carros e 1 mota continuam a ser 2 carros e 1 mota.

| Botão | O que faz |
|---|---|
| **Pausar / Retomar** | Pára ou continua a rotação. |
| **◀ Voltar à garagem** | Pára o loop e volta à janela 1. A lista já mostra os veículos novos que entraram. |

---

## 6. Alterar as cores e o aspeto

- **Cores dos botões:** ficheiro `src/main/java/com/example/view/componentes/BotaoVisual.java`, linhas **13 a 18** (verde principal, cinza secundário, vermelho de remover, cor da letra, cantos redondos e tamanho da letra).
- **Cores dos veículos no loop:** ficheiro `src/main/java/com/example/view/componentes/VagaView.java`, linhas **18 a 20**.
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
