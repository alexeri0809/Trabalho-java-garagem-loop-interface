# Trabalho-java-garagem

## 📌 1. Sobre o projeto

Este projeto foi desenvolvido em **Java** e tem como objetivo representar o funcionamento de um sistema de garagem, utilizando conceitos de Programação Orientada a Objetos (POO).

O projeto permite trabalhar com diferentes tipos de veículos, como carros, motas e barcos, aplicando conceitos de organização do código, reutilização e separação de responsabilidades entre classes.

O objetivo não é apenas fazer o programa funcionar, mas também desenvolver competências de programação e compreender como estruturar um projeto Java.

### 🔄 Garagem em loop

Os veículos escolhidos (qualquer combinação de carros, barcos e motas, até 4) podem ser postos a rodar numa **garagem circular de 5 vagas** (opção 6 do menu). Os veículos ocupam as primeiras vagas e as restantes ficam `Vazio` (há sempre pelo menos 1). O `Vazio` anda à volta sem parar e, **no fim de cada volta, o veículo mais antigo sai e entra um novo do mesmo tipo com matrícula nova**. Carrega em Enter para parar e voltar ao menu.

Exemplo com 2 carros e 1 mota:

```text
[Carro UI-77-ND] [Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Vazio]
[Vazio] [Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Carro UI-77-ND]
[Carro CR-61-JP] [Vazio] [Mota GG-19-OX] [Vazio] [Carro UI-77-ND]
[Carro CR-61-JP] [Mota GG-19-OX] [Vazio] [Vazio] [Carro UI-77-ND]
=== Volta 1 completa === Saiu: Carro UI-77-ND | Entrou: Carro GP-36-RD
```

Esta funcionalidade junta este projeto com o [Trabalho-java-garagem-loop](https://github.com/alexeri0809/Trabalho-java-garagem-loop). Os detalhes estão no `Manual_Tecnico.md` (secção 3.1) e no `Manual_Utilizador.md` (secção 6).

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
Trabalho-java-garagem/
├── README.md
└── trabalho-java-garagem/
    └── demo/
        ├── pom.xml
        └── src/
            ├── main/
            │   └── java/
            │       └── com/
            │           └── example/
            │               ├── Main.java
            │               ├── facade/ fabrica/ garagem/ loop/ matricula/ veiculo/
            │               └── ...
            └── test/
                └── java/
                    └── ...
```

**Nota:** A árvore acima é ilustrativa. Os nomes e a localização exata das classes devem ser confirmados nos ficheiros do projeto.

### Para que serve cada elemento?

- **`Main.java`** — contém o ponto de entrada do programa, através do método `main()`, a partir do qual a execução começa.
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
cd .\trabalho-java-garagem\demo\
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

### Passo 4 — Executar o programa

```powershell
mvn compile exec:java "-Dexec.mainClass=com.example.Main"
```

Este comando compila o projeto e utiliza o plugin Exec do Maven para executar a classe principal `com.example.Main`.

### Alternativa — Compilar e executar diretamente com Java

Os seguintes comandos correspondem à alternativa manual documentada no projeto:

```powershell
Remove-Item -Recurse -Force target\classes

javac -d target\classes src\main\java\com\example\*.java

java -cp target\classes com.example.Main
```

**Explicação dos comandos:**

1. `Remove-Item -Recurse -Force target\classes` — remove os ficheiros compilados anteriormente. A pasta tem de existir para que o comando funcione sem erros.
2. `javac -d target\classes ...` — compila os ficheiros Java indicados e coloca os ficheiros `.class` na pasta `target\classes`.
3. `java -cp target\classes com.example.Main` — inicia o programa, indicando ao Java onde encontrar as classes compiladas e qual a classe principal.

> Estes comandos manuais são específicos do PowerShell e da estrutura de pastas indicada. O comando de compilação terá de incluir todos os ficheiros necessários caso o projeto tenha subpastas, dependências ou outras classes fora de `com.example`.

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
**Repositório:** [Trabalho-java-garagem no GitHub](https://github.com/alexeri0809/Trabalho-java-garagem-loop-interface)
