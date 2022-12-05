# Rapport Labo SMTP

## Introduction
Ce répertoire GitHub contient la réalisation du laboratoire SMTP. Ce laboratoire a pour but de mettre en pratique l'utilisation du protocole SMTP. Ce projet permet de créer des campagnes de "pranks". L'utilisateur peut définir une liste d'e-mails de victimes ainsi que les messages de farce à envoyer. Par la suite, le programme s'occupera de créer des groupes et enverra aléatoirement un des messages à chaque groupe.

Le but de ce labo est de réaliser une application capable de communiquer via le protocle SMTP en faisant des campagne de prank où l'utilisateur peut choisir le nombre de victime par campagne et il défini une liste de vicitme et de contenu des mails. Le programme va pars la suite determiner aléatoirement parmi les victime et le contenu des mails.

## MockMock
Il est possible de tester l'application en local grâce à l'outil [MockMock](https://github.com/DominiqueComte/MockMock). 
MockMock permet de simuler un server SMTP en local et il est possible de consulter le mail qui lui ont été envoyer via un navigateur internet.

## Installation / setup

### Requirement
- JDK 17.0 ou plus
- Maven
### Execution
Pour lancer l'application, il faut aller dans le dossier `/SRC/SMTP_Prank_Brasey_Pillonel` et executer les commandes suivante

```
maven clean package
cd target
java -jar SMTP_Prank_Brasey_Pillonel-1.0-SNAPSHOT.jar
```

### MockMock
Le repo [MockMock](https://github.com/DominiqueComte/MockMock) détail comment lancer le serveur.

### Configuration 
Pour la configuration du programme, il faut aller modifier les fichiers dans `SRC/SMTP_Prank_Brasey_Pillonel/ConfigFile/`
- **config.properties** pour les paramètres generaux (simplement changet les valeur en fin de ligne)
- **email_address.csv** pour la liste des addresses email (adresse email séparer par une virgule)
- **email_content.json** pour la liste des sujets et contenus des messages  
pour ajouter un nouvelle élément suiver le format ci-dessous:
````
{ 
  "message":{
    "Subject": "<my_subject>"
    "Content": "<my_content>"
  }
}
````
## Implementation
Dans cette section on va détailler comment nous avons implementer notre application.
Pour commencer nous avons séparer le travaille en trois:
- Envoyer un mail via SMTP
- Lire des fichier de configuration
- Application pricipale (faire des pranks et utiliser les 2 modules précédents) 

Pour ce faire nous avons fait un diagramme de classe:
![UML](figures/DiagrammeClasse.svg)

Les classes les plus importantes sont:
### FileConfigurer
Qui va venir lire et traduire les informations présentes dans les fichiers pour les transmettre a PrankGenerator.
### SMTPClient
Qui va faire le lien entre notre application et un serveur SMTP pour pouvoir envoyer nos emails.
### PrankGenerator
Qui va s'occuper d'instancier les Prank avec les inforamation reçu via FileConfigurer et qui va ensuite utilisé SMTPClient pour les envoyer.

##SMTP
pour la partie SMTP, nous avons implementer un model de requet simple:
````
EHLO SMTPManager \CRLF
MAIL FROM: <sender email> \CRLF
RCPT_TO: <receiver0 email>,<receiver1 email>,... \CRLF
DATA \CRLF
  Content-Type: text/plain; charset=utf-8 \CRLF
  From: <sender email> \CRLF
  Subject: <encoded_Subject> \CRLF
  To: <receiver0 email>,<receiver1 email>,... \CRLF
  <content> \CRLF
  . \CRLF    
````
Si tous se passe bien, le server va normalement répondre à tous ces messages avec un ou plusieurs code de réponse positif dans le cas contraire le programme retourne une erreur.
