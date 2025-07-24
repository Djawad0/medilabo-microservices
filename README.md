# Medilabo-Microservices

*Prérequis : avoir Docker d’installé.*  

## Instructions
Lancer d'abord la commande:
```
docker-compose up -d MySQL
```
Attendre 10 secondes, puis lancer la commande:
```
docker-compose up -d
```
Lorsque les conteneurs sont lancés, allez sur http://localhost:8761/ et attendez que tous les services soient reconnus par Eureka.

Accédez à l'application via http://localhost:8082/ (si le statut est "inconnu", attendez quelques secondes puis actualisez la page ; le microservice note met un peu plus de temps à se lancer).

Vous pouvez vous connecter soit en créant un compte, soit en utilisant le compte déjà créé :
```
Identifiant = test
Mot de passe = test123
```
En ce qui concerne le Green Code, voici quelques suggestions d’actions à mener pour appliquer cette approche au projet :

- Concevoir des algorithmes qui accomplissent les tâches avec le moins d’opérations possible.
- Éviter le code redondant ainsi que les boucles inutiles.
- Choisir un langage de programmation à faible consommation énergétique.
