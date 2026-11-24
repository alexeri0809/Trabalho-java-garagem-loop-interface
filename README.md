# Trabalho-java-garagem-loop-interface

## 📌 1. Sobre o projeto

Este projeto foi desenvolvido em **Java** e tem como objetivo representar o funcionamento de um sistema de garagem, utilizando conceitos de Programação Orientada a Objetos (POO).

O projeto permite trabalhar com diferentes tipos de veículos, como carros, motas e barcos, aplicando conceitos de organização do código, reutilização e separação de responsabilidades entre classes.

O objetivo não é apenas fazer o programa funcionar, mas também desenvolver competências de programação e compreender como estruturar um projeto Java.

### 🔄 Garagem em loop

O sistema tem **três rodas interligadas** (esquerda, central e direita). Cada roda é uma **garagem circular de 5 vagas** com os seus próprios veículos (qualquer combinação de carros, barcos e motas, até 4 por roda), escolhidos na primeira janela. Os veículos ocupam as primeiras vagas e as restantes ficam `Vazio` (há sempre pelo menos 1). Ao carregar em *Iniciar loop* as **três rodas andam ao mesmo tempo, sincronizadas**: em cada uma o `Vazio` anda à volta sem parar, nas rodas da esquerda e central no **sentido horário** e na da direita no **sentido anti-horário**. **No fim de cada volta, em cada roda, o veículo mais antigo sai e entra um novo do mesmo tipo com matrícula nova**. Se as três rodas estiverem cheias, no fim de volta da roda central o veículo mais antigo da esquerda troca com o mais antigo do meio e depois o da direita faz o mesmo. As vagas mostram uma foto de cada tipo de veículo (pasta `demo/src/main/resources/imagens`, ficheiros `carro1.png`, `barco1.png`, `mota1.png`, ...: basta substituí-los pelas tuas fotos). Há um botão para pausar e outro para voltar à janela da garagem.

Exemplo de uma roda com 2 carros e 1 mota (as outras duas rodas rodam em simultâneo, cada uma com os seus veículos):

```text
[Carro UI-77-ND] [Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Vazio]
[Vazio] [Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Carro UI-77-ND]
[Carro CR-61-JP] [Vazio] [Mota GG-19-OX] [Vazio] [Carro UI-77-ND]
[Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Vazio] [Carro UI-77-ND]
=== Volta 1 completa === Saiu: Carro UI-77-ND | Entrou: Carro GP-36-RD
```

Este projeto junta o [Trabalho-java-garagem](https://github.com/alexeri0809/Trabalho-java-garagem) com o [Trabalho-java-garagem-loop](https://github.com/alexeri0809/Trabalho-java-garagem-loop) e acrescenta a interface gráfica. Os detalhes estão no `Manual_Tecnico.md` (secções 3.1 e 3.1.1) e no `Manual_Utilizador.md` (secção 5).

### 🖥️ Interface gráfica (JavaFX + MVC)

O programa tem duas janelas (cenas): a da **garagem**, onde se escolhe a roda e se escrevem a marca e o modelo (a matrícula é gerada automaticamente), e a do **loop**, onde as três rodas rodam. O código segue o padrão **MVC**: o *Model* (`GaragemFacade` e restantes) não conhece a interface, as *Views* (`view`) só desenham e os *Controllers* (`controller`) ligam as duas partes. Os botões têm todas as suas regras de aspeto num só ficheiro (`BotaoVisual.java`, linhas 13 a 18) e o sentido de cada roda está em `PosicaoRoda.java` (linhas 10 a 12). Ver `Manual_Tecnico.md`, secções 3.1.1 e 3.2.

## 🎯 2. Objetivos do trabalho

- Aplicar os princípios da Programação Orientada a Objetos.
- Organizar o programa através de classes e métodos.
- Representar diferentes tipos de veículos.
- Compreender a relação entre as classes.
- Utilizar ferramentas como Java e Maven para compilar, executar e testar o projeto.
- Desenvolver um código organizado, legível e fácil de manter.

## 🛠️ 3. Tecnologias utilizadas

| Tecnologia | Função |
|---|---|
| Java | Linguagem utilizada para desenvolver o programa. |
| Maven | Ferramenta que permite compilar o projeto e executar testes. |
| JUnit (se configurado) | Biblioteca para criar e executar testes automatizados. |
| Git e GitHub | Ferramentas para controlar versões e guardar o código do projeto. |

## 📂 4. Estrutura do projeto

O projeto utiliza uma estrutura Java com Maven.

```text
Trabalho-java-garagem-loop-interface/
├── README.md
└── demo/
    ├── pom.xml
    ├── manuais/
    └── src/
        ├── main/
        │   └── java/
        │       └── com/
        │           └── example/
        │               ├── Main.java
        │               ├── App.java
        │               ├── controller/ view/ (interface JavaFX)
        │               ├── facade/ fabrica/ garagem/ loop/ matricula/ veiculo/ (model)
        │               └── ...
        └── test/
            └── java/
                └── ...
```

**Nota:** A árvore acima é ilustrativa. Os nomes e a localização exata das classes devem ser confirmados nos ficheiros do projeto.

### Para que serve cada elemento?

- **`Main.java`** — contém o ponto de entrada do programa, através do método `main()`, que abre a janela JavaFX (`App.java`).
- **`pom.xml`** — ficheiro de configuração do Maven, onde são definidas informações do projeto e as suas dependências.
- **`src/main/java/`** — diretório destinado ao código principal da aplicação.
- **`src/test/java/`** — diretório destinado aos testes automatizados.
- **`target/`** — diretório gerado durante a compilação, onde ficam os ficheiros produzidos pelo Maven.

## ▶️ 5. Como executar o programa

### Pré-requisitos

É necessário ter o Java Development Kit (JDK) e o Maven instalados, caso se pretenda utilizar os comandos Maven.

### Passo 1 — Entrar na pasta do projeto

No PowerShell do Windows:

```powershell
cd .\demo\
```

Este comando muda a diretoria atual para a pasta onde se encontra o projeto Maven.

Se já estiveres dentro dessa pasta, não precisas de executar novamente o comando.

### Passo 2 — Compilar o código

```powershell
mvn compile
```

Este comando pede ao Maven para compilar o código-fonte do projeto.

Se existirem erros de sintaxe ou de compilação, estes serão apresentados no terminal.

### Passo 3 — Executar os testes

```powershell
mvn test
```

Este comando executa os testes configurados no projeto, permitindo verificar se os comportamentos testados estão a funcionar como esperado.

### Passo 4 — Abrir o programa

```powershell
mvn javafx:run
```

Este comando compila o projeto, descarrega o JavaFX (na primeira vez) e abre a janela **Garagem**. É preciso JDK 17 ou mais recente.

> Não se usa `java -cp ... com.example.Main` diretamente: o JavaFX precisa de ser indicado com `--module-path`. Por isso o caminho recomendado é o Maven.

## 🧠 6. Conceitos de programação aplicados

### Programação Orientada a Objetos

A Programação Orientada a Objetos permite organizar o código em classes que representam entidades e comportamentos.

Neste projeto, os veículos constituem um exemplo de entidades que podem ser representadas através de classes.

### Classes e objetos

Uma classe define a estrutura e os comportamentos de um determinado tipo de objeto.

Um objeto é uma instância dessa classe, criada durante a execução do programa.

### Herança e polimorfismo

Se o projeto utilizar uma classe comum para os veículos e subclasses para os diferentes tipos, a herança permite partilhar características e comportamentos.

O polimorfismo permite trabalhar com objetos de diferentes tipos através de uma interface ou classe comum.

*Estes dois conceitos devem ser associados às classes concretas do projeto apenas se estiverem efetivamente implementados.*

## 🧪 7. Testes e validação

A validação do programa pode ser feita através de:

- Compilação do código com `mvn compile`.
- Execução dos testes com `mvn test`.
- Execução da aplicação e verificação dos resultados.
- Verificação dos comportamentos dos diferentes tipos de veículos.
- Identificação e correção de erros.

Os resultados dos testes devem ser analisados antes de concluir que o programa funciona corretamente.

## 👨‍💻 8. Conclusão

Este trabalho permite consolidar conhecimentos de Java, organização de projetos e Programação Orientada a Objetos.

Através da implementação de um sistema de garagem, é possível compreender melhor como dividir um problema em classes, definir responsabilidades e criar um programa organizado e mais fácil de manter.

O projeto constitui também uma oportunidade para praticar a compilação, a execução e a validação de aplicações Java com recurso ao Maven.

---

**Projeto:** Trabalho Java — Garagem  
**Linguagem:** Java  
**Gestão do projeto:** Maven  
**Repositório:** [Trabalho-java-garagem-loop-interface no GitHub](https://github.com/alexeri0809/Trabalho-java-garagem-loop-interface)
