# 🏙️ Microservice Ville - Backend (Spring Boot)

Microservice métier de gestion des villes et des pays du projet **ProjetVille**, développé avec **Spring Boot** et **Java 21** dans le cadre du module **SOA (Architectures Orientées Services)**.

Ce microservice est sécurisé par un filtre d'autorisation **JWT (JSON Web Token)** vérifiant les jetons émis par le microservice d'authentification (`users-microservice`).

---

## 🚀 Fonctionnalités

- **Gestion des Villes (CRUD) :**
  - Consultation de la liste complète des villes.
  - Consultation d'une ville par son identifiant.
  - Ajout, mise à jour et suppression d'une ville.
  - Recherche et filtrage des villes par pays (`idPays`).
- **Gestion des Pays :**
  - Exposition des opérations sur les pays via **Spring Data REST** (`/ville/pays`).
  - Association bidirectionnelle entre Ville et Pays via JPA/Hibernate.
- **Sécurité & Contrôle d'Accès :**
  - Filtre d'autorisation sans état (`JWTAuthorizationFilter` basé sur Spring Security).
  - Vérification de la signature et de la validité du token JWT avec clé secrète partagée.
  - Gestion des rôles :
    - **`ADMIN` :** Ajout, modification et suppression de villes.
    - **`ADMIN` & `USER` :** Consultation et recherche des villes et des pays.
  - Configuration CORS autorisant les requêtes provenant du client Angular (`http://localhost:4200`).

---

## 🛠️ Technologies Utilisées

- **Framework :** Spring Boot
- **Langage :** Java 21
- **Persistance :** Spring Data JPA / Hibernate
- **API & REST :** Spring Web MVC, Spring Data REST
- **Sécurité :** Spring Security, Auth0 Java JWT (`com.auth0:java-jwt`)
- **Base de données :** PostgreSQL (support runtime MySQL également disponible)
- **Utilitaires :** Lombok, Spring Boot DevTools

---

## 📋 Endpoints de l'API

L'application est déployée sur le port `8080` avec le context-path `/ville`.

### 🏙️ Villes (`/ville/api`)

| Méthode | Endpoint | Description | Rôles Autorisés |
| :--- | :--- | :--- | :--- |
| `GET` | `/ville/api/all` | Liste de toutes les villes | `ADMIN`, `USER` |
| `GET` | `/ville/api/getbyid/{id}` | Obtenir les détails d'une ville par son ID | `ADMIN`, `USER` |
| `GET` | `/ville/api/villespays/{idPays}` | Liste des villes appartenant à un pays | `ADMIN`, `USER` |
| `POST` | `/ville/api/addville` | Ajouter une nouvelle ville | `ADMIN` |
| `PUT` | `/ville/api/updateville` | Mettre à jour une ville existante | `ADMIN` |
| `DELETE` | `/ville/api/delville/{id}` | Supprimer une ville par son ID | `ADMIN` |

### 🌍 Pays (Spring Data REST : `/ville/pays`)

| Méthode | Endpoint | Description | Rôles Autorisés |
| :--- | :--- | :--- | :--- |
| `GET` | `/ville/pays` | Liste paginée de tous les pays | Authentifié (`ADMIN`, `USER`) |
| `GET` | `/ville/pays/{id}` | Consulter un pays par son ID | Authentifié (`ADMIN`, `USER`) |
| `POST` | `/ville/pays` | Créer un pays | Authentifié (`ADMIN`) |
| `PUT` | `/ville/pays/{id}` | Modifier un pays | Authentifié (`ADMIN`) |
| `DELETE` | `/ville/pays/{id}` | Supprimer un pays | Authentifié (`ADMIN`) |

> **Format du jeton JWT :**
> Toutes les requêtes protégées doivent inclure l'en-tête HTTP suivant :
> ```http
> Authorization: Bearer <votre_token_jwt>
> ```

---

## ⚙️ Configuration & Base de Données

1. **Création de la base de données PostgreSQL :**
   Dans PostgreSQL (pgAdmin ou psql), créez la base de données :
   ```sql
   CREATE DATABASE "spring_DB";
   ```

2. **Configuration dans `src/main/resources/application.properties` :**
   ```properties
   spring.application.name=ville
   spring.datasource.url=jdbc:postgresql://localhost:5432/spring_DB
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.datasource.driver-class-name=org.postgresql.Driver

   spring.jpa.show-sql=true
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

   server.servlet.context-path=/ville
   server.port=8080
   ```

---

## 💻 Démarrage de l'Application

Depuis la racine du projet :

```bash
# Avec le wrapper Maven (Windows)
./mvnw.cmd spring-boot:run

# Ou avec Maven installé
mvn spring-boot:run
```

L'API sera accessible sur : `http://localhost:8080/ville`
