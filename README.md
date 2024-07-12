# Desafio Pagbank - Investimentos

Este é um desafio proposto para averiguar os conhecimentos com o desenvolvimento de aplicações backend

# Como executar
### Pré requisitos
- Ter o docker instalado em sua máquina
- Ter alguma IDE para executar java caso queira exutar em sua máquina - JDK 21 temurin

Para executar a aplicação basta seguir os passos listados abaixo:
#### 1) Clonar o repositório
> git clone git@github.com:JhonSNunes/pagbank-challenge.git

#### 2) Acessar a pasta sandbox e executar o comando para criação da network e pastas necessárias

    // Caso o sh não esteja como um executável para você rodar o comando chmod +x run-configuration.sh
	./run-configuration.sh
	
#### 3) Acessar a pasta sandbox/services e rodar o docker para subir os serviços

    docker compose up -d

#### 4) Após os serviços estarem no executar o Flyway para a criação das entidades

    // Executar diretamente da raiz do projeto
    ./gradlew flywayMigrate

#### 5) Acessar a pasta sandbox/app e rodar o docker para subir a aplicação
    docker compose up -d
