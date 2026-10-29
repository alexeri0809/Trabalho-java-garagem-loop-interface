# Manual do Utilizador — Programa Garagem

> 📗 Este documento é o **Manual do Utilizador**, com instruções para usar o programa. Para detalhes técnicos (arquitetura, padrões de desenho, testes unitários, erros e soluções), consulta o [Manual Técnico](./Manual_Tecnico.md).

---

## 1. O que é este programa

Um programa em Java que corre no terminal e simula uma **garagem com 4 lugares para veículos** (mais 1 vaga que fica sempre livre, para o loop). O utilizador escolhe, através de um menu, que veículos quer estacionar: **Carros**, **Barcos** ou **Motas**, em qualquer combinação (ex: 4 motas, 2 carros + 2 barcos, 1 carro + 2 barcos + 1 mota, etc.) — o único limite é o total de 4 veículos.

Depois de escolhidos os veículos, a opção **Iniciar loop** faz **rodá-los** numa garagem circular de 5 vagas (ver secção 6).

Cada veículo recebe automaticamente uma **matrícula aleatória** gerada pelo programa:

- **Carros e Motas:** formato tipo `FS-13-HD`
- **Barcos:** formato tipo `6º-CO-7-15-21`

---

## 2. Antes de começar: preparar o computador

É necessário ter o **Java (JDK)** e o **Maven** instalados. Se não tiveres acesso de administrador no computador, ambos podem ser instalados sem esse acesso, através de ficheiros `.zip`.

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

# Correr o programa (nota as aspas à volta do argumento inteiro)
mvn compile exec:java "-Dexec.mainClass=com.example.Main"
```

Se tudo correr bem, `mvn test` deve terminar com `BUILD SUCCESS`, e a seguir aparece o menu principal ao correres o programa.

**Atenção às aspas:** se escreveres `-Dexec.mainClass="com.example.Main"` (aspas só à volta do valor), o PowerShell pode partir o comando ao meio e dar erro. As aspas têm de envolver o argumento inteiro, como no exemplo acima.

### Alternativa sem Maven

Se preferires não usar o `exec:java`, ou este continuar a dar erro, podes compilar e correr o programa diretamente:

```powershell
Remove-Item -Recurse -Force target\classes
javac -d target\classes -encoding UTF-8 (Get-ChildItem -Recurse -Filter *.java src\main\java).FullName
java -cp target\classes com.example.Main
```

**Nota:** este método corre só o programa, não os testes unitários — para os testes continua a ser preciso usar `mvn test`.

---

## 4. Como usar o menu

Ao correr o programa, aparece:

```
--- Lugares livres: 4 ---
1 - Adicionar Carro
2 - Adicionar Barco
3 - Adicionar Mota
4 - Ver garagem
5 - Ver matrículas
6 - Iniciar loop
0 - Sair
Opção:
```

### Opções disponíveis

| Opção | O que faz |
|---|---|
| **1** | Adiciona um **Carro**. Pede a marca e o modelo; a matrícula é gerada automaticamente. |
| **2** | Adiciona um **Barco**. Pede a marca e o modelo; a matrícula é gerada automaticamente (formato marítimo). |
| **3** | Adiciona uma **Mota**. Pede a marca e o modelo; a matrícula é gerada automaticamente. |
| **4** | Mostra o estado atual da garagem, agrupado por tipo de veículo, **sem sair do programa**. |
| **5** | Mostra só a lista de matrículas de todos os veículos, pela ordem Carro → Barco → Mota. |
| **6** | Faz **rodar** os veículos que estão na garagem numa garagem circular de 5 vagas. Carrega em **Enter** para parar e voltar ao menu (ver secção 6). |
| **0** | Sai do programa e mostra o estado final da garagem. |

### Exemplo de utilização

```
Opção: 1
Marca: Toyota
Modelo: Corolla
Estacionado: Carro [FS-13-HD - Toyota Corolla]

Opção: 2
Marca: Quicksilver
Modelo: Activ 470
Estacionado: Barco [6º-CO-7-15-21 - Quicksilver Activ 470]

Opção: 4
Garagem (2/4)
  Carros (1)
    - Carro [FS-13-HD - Toyota Corolla]
  Barcos (1)
    - Barco [6º-CO-7-15-21 - Quicksilver Activ 470]
  Motas (0)
```

---

## 5. O que acontece quando a garagem enche

Assim que os 4 lugares estiverem ocupados, o programa deixa de aceitar novos veículos (a quinta vaga do loop tem de ficar sempre livre):

```
--- Lugares livres: 0 ---
...
Opção: 1
A garagem está cheia! Não é possível adicionar mais veículos.
```

Continua a ser possível usar as opções **4** (ver garagem), **5** (ver matrículas), **6** (iniciar loop) e **0** (sair) mesmo com a garagem cheia.

---

## 6. O loop (opção 6)

O loop é uma garagem **circular com 5 vagas**. Os veículos que escolheste ocupam as primeiras vagas e **todas as restantes ficam `Vazio`** (há sempre pelo menos 1). Por exemplo, com **2 carros e 1 mota** ficam 3 veículos e 2 vagas vazias.

**Como roda:** a cada 0,7 segundos o `Vazio` avança e o veículo que estava à frente dele recua uma vaga para o lugar vago. O programa mostra as 5 vagas depois de cada passo.

**Fim de cada volta:** quando o `Vazio` completa uma volta, o veículo **mais antigo** (o que foi adicionado primeiro) sai e entra um veículo **novo do mesmo tipo** (mesma marca e modelo) com **matrícula nova**. Por isso a combinação escolhida mantém-se: 2 carros e 1 mota continuam a ser 2 carros e 1 mota. A garagem (opções 4 e 5) passa a mostrar os veículos novos.

**Para parar:** carrega em **Enter** para voltar ao menu (podes adicionar mais veículos e iniciar o loop outra vez), ou em **Ctrl+C** para terminar o programa.

### Exemplo (2 carros e 1 mota)

```
=== Garagem em loop (5 vagas) ===
Estado inicial:
[Carro UI-77-ND] [Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Vazio]

[Vazio] [Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Carro UI-77-ND]
[Carro CR-61-JP] [Vazio] [Mota GG-19-OX] [Vazio] [Carro UI-77-ND]
[Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Vazio] [Carro UI-77-ND]
=== Volta 1 completa === Saiu: Carro UI-77-ND | Entrou: Carro GP-36-RD
[Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Carro GP-36-RD] [Vazio]
```

Se a garagem estiver vazia, o programa avisa que é preciso adicionar pelo menos 1 veículo antes de iniciar o loop.

---

## 7. Problemas comuns

| Sintoma | Solução |
|---|---|
| O loop não pára com Enter | Carrega em Enter com a janela do terminal em foco; se o terminal não o reconhecer, usa Ctrl+C |
| `javac`/`java`/`mvn` não reconhecido | Confirma que o JDK e o Maven estão instalados e no PATH (ver secção 2); depois de alterares o PATH, abre sempre um terminal novo |
| `winget install Apache.Maven` diz "No package found" | O Maven não está disponível pelo `winget` — usa a instalação manual em `.zip` (ver secção 2) |
| `mvn compile exec:java ...` → `Unknown lifecycle phase ".mainClass=..."` | Problema de aspas do PowerShell a cortar o comando a meio | Envolve o argumento inteiro em aspas: `"-Dexec.mainClass=com.example.Main"`, ou usa a alternativa sem Maven (secção 3) |
| Erro ao compilar com `*.java` | Usa `mvn compile` em vez de comandos `javac` manuais — o Maven trata disso automaticamente, incluindo ficheiros em subpastas |
| `pom.xml` com erro / pasta com ícone vermelho | Confirma que todas as tags (`<dependency>`, `<plugin>`, etc.) estão **dentro** de `<project>...</project>`, nunca depois do `</project>` de fecho |
| Acentos aparecem trocados (ex: "Op??o" em vez de "Opção") | Corre com `-Dfile.encoding=UTF-8`, ou adiciona `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>` ao `pom.xml` |
| `mvn test` mostra falhas | Confirma que os ficheiros de teste estão em `src/test/java/com/example/...`, com os mesmos subpacotes do código principal (`veiculo`, `matricula`, `garagem`, `fabrica`, `facade`, `loop`) |

Para mais detalhes técnicos sobre a arquitetura do programa, os padrões de desenho utilizados, os testes unitários e o histórico completo de erros/soluções encontrados durante o desenvolvimento, consulta o **[Manual Técnico](./Manual_Tecnico.md)**.
