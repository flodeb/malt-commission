# Spécifications

Ce microservice permet de calculer un taux de commissionnement pour une mission donnée, en fonction de règles de pricing :
* Si une règle peut s'appliquer à la mission, le taux associé à cet règle est donné
* Si plusieurs règles peuvent s'appliquer à la mission, le taux le plus bas parmi ces règles est donné (c'est à dire le taux le plus avantageux pour le freelancer)
* Si aucune règle ne peut s'appliquer à la mission, le taux par défaut est donné, configurable en propriété. Le taux par défaut actuel est de 10%

## Gestion des règles

* Une règle de pricing se définit comme un ensemble de contraintes. Si toutes ces contraintes sont validées pour une mission donnée, on dit que la règle peut s'appliquer à la mission.
* Ces contraintes peuvent porter sur différents attributs de la mission. La définition d'une règle est donnée dans le CI, via Swagger (voir ci-après)

## Contrat d'interface des APIs

* Contrat d'interface des APIs : voir Swagger ci-dessous

# Développement

## Prérequis

* Lancer la base de données. Via docker : `docker run --name postgres -e POSTGRES_PASSWORD=root -d -p 5432:5432 postgres`

## Lancement de l'application

* Via IntelliJ Idea : Clique droit sur `CommissionApplication.java` > `Run` - **Penser à activer le profil `dev`**
* Via Maven : `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`

## Lancement des tests

* Via IntelliJ Idea : Clique droit sur `src/test/java` > `Run All Tests (TestNG)`
* Via Maven : `./mvnw test`

## Swagger
   
* URL (dev) : http://localhost:8080/swagger-ui.html

## Requêtes exemples Postman

* Importer la collection JSON dans Postman : `src/requests/postman.json`

## Roadmap Tech

* Ajout sécurisation de la route `/api/admin/rules`
* Automatisation des tests (CI)
* Déploiement continu
* Mise en place de SONAR
* Liquibase pour Production