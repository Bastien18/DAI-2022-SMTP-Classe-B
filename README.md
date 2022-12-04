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
le repo [MockMock](https://github.com/DominiqueComte/MockMock) détail comment lancer le serveur.

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




MockMock est un outil permettant de "mock" / simuler un vrai serveur SMTP. Ce serveur aura pour but de récupérer tous les mails envoyés depuis votre machine et de les afficher de manière graphique grâce à une interface web. De cette façon, il est facile de tester l'application sans envoyer des e-mails à de vraies personnes.

* **What is MockMock (or any other mock SMTP server you decided to use)?**

* **Instructions for setting up your mock SMTP server (with Docker - which you will learn all about in the next 2 weeks)**. The user who wants to experiment with your tool but does not really want to send pranks immediately should be able to use a mock SMTP server. For people who are not familiar with this concept, explain it to them in simple terms. Explain which mock server you have used and how you have set it up.

* **Clear and simple instructions for configuring your tool and running a prank campaign**. If you do a good job, an external user should be able to clone your repo, edit a couple of files and send a batch of e-mails in less than 10 minutes.

* **A description of your implementation**: document the key aspects of your code. It is a good idea to start with a **class diagram**. Decide which classes you want to show (focus on the important ones) and describe their responsibilities in text. It is also certainly a good idea to include examples of dialogues between your client and an SMTP server (maybe you also want to include some screenshots here).

## References

* [Here is our fork of MockMock server](https://github.com/HEIGVD-Course-API/MockMock), in which we resolved an issues with a dependency (see this [pull request](https://github.com/tweakers/MockMock/pull/8) if you want to have more information).
* The [mailtrap](<https://mailtrap.io/>) online service for testing SMTP
* The [SMTP RFC](<https://tools.ietf.org/html/rfc5321#appendix-D>), and in particular the [example scenario](<https://tools.ietf.org/html/rfc5321#appendix-D>)
* Testing SMTP with TLS: `openssl s_client -connect smtp.mailtrap.io:2525 -starttls smtp -crlf`
