# Desafio Pagbank - Investimentos

Este é um desafio proposto para averiguar os conhecimentos com o desenvolvimento de aplicações backend

# Como executar
### Pré requisitos
- Ter o docker instalado em sua máquina
- Ter o java instalado caso queira exutar em sua máquina - JDK 21 temurin

Para executar a aplicação basta seguir os passos listados abaixo:
#### 1) Clonar o repositório
> git clone git@github.com:JhonSNunes/pagbank-challenge.git

#### 2) Subir o container do banco de dados
> docker compose -f docker-compose-db.yml up

#### 3) Rodar o Flyway para a criação das entidades através de migração
> ./gradlew flywayMigrate

#### 4) Subir a aplicação
> docker compose -f docker-compose-app.yml up
