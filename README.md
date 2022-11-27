<h1 align="center">Bienvenue dans le module smartnews-commun du projet Smartnews👋</h1>
<p>
  <a href="https://github.com/ugieiris/smartnews-commun#readme" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="https://github.com/ugieiris/smartnews-commun/graphs/commit-activity" target="_blank">
    <img alt="Maintenance" src="https://img.shields.io/badge/Maintained%3F-yes-green.svg" />
  </a>
</p>

> Module smartnews-commun<br />
> Composant qui permet le partage de code entre tous les µservices du projet Smartnews

## SDK Google
### API
Ce composant propose, entre autre, des bean "opionnels" Spring pour consommer les API Google suivantes :
- Google Drive
- Google Slides
- Google Sheets

Ils sont optionnels car leur création n'est effectuée que sous certaines conditions :
- Conf SDK Google : déclarer la clé `google.credentials.folder.path` dans les props du consommateur. D'autres props sont à déclarer : cf. [GoogleApiMainConfiguration.java](src/main/java/fr/su/smartnewscommun/services/googleapi/GoogleApiMainConfiguration.java)
- Google Drive API : déclarer la clé `google.api.drive.scopes` dans les props du consommateur. D'autres props sont à déclarer : cf. [GoogleDriveApiConfiguration.java](src/main/java/fr/su/smartnewscommun/services/googleapi/drive/GoogleDriveApiConfiguration.java)
- Google Slides API : déclarer la clé `google.api.slides.scopes` dans les props du consommateur. D'autres props sont à déclarer : cf. [GoogleSlidesApiConfiguration.java](src/main/java/fr/su/smartnewscommun/services/googleapi/slides/GoogleSlidesApiConfiguration.java)
- Google Sheets API : déclarer la clé `google.api.sheets.scopes` dans les props du consommateur. D'autres props sont à déclarer : cf. [GoogleSheetsApiConfiguration.java](src/main/java/fr/su/smartnewscommun/services/googleapi/sheets/GoogleSheetsApiConfiguration.java)

Les dépendances vers le SDK Google sont scopées Maven `provided` : c'est au consommateur de déclarer ces dépendances dans son `pom.xml`

### Authentification
Deux clés importantes : 
- `google.api.json.credentials.base64` :
  - fichier JSON de connexion OAuth2 au projet GCP, à récupérer sur la console GCP (API et services / Identifiants)
  - à encoder en base64
- `google.api.stored.credential.base64` :
  - fichier de credentials technique du SDK Google en local, à générer une première fois en local au lancement de l'application
  - en local, on passe par une variable d'environnement `GOOGLE_API_STORED_CREDENTIAL_B64` propre au développeur (cf. src/main/resources/env.properties du consommateur)
  - le fichier est créé par le SDK sur le disque : `${répertoire projet Intellj}\${google.credentials.folder.path}`
  - à encoder en base64

## Accès rapides

- [Pipelines](https://jenkins-iris.groupement.systeme-u.fr/job/DED_RD/job/_pipelines/job/smartnews-commun_Pipelines/)

## Technologies

Ce module est développé avec les technologies suivantes :

- `Java 11 :` le langage

- `SpringBoot 2 :` le cadre de développement

- `u-iris-back-java :` socle technique Iris pour ajouter JPA, l'authentification, l'actuator, ... [documentation complète](https://github.com/ugieiris/u-iris-back-java/blob/develop/README.md)

- `Junit :` développement des tests unitaires (obligatoires)

- `Springdoc :` annotations permettant la génération d'un fichier de spécification de l'API au format [OpenAPI](https://www.openapis.org/).

## Liens complémentaires

- 📄 [Tests unitaires](https://confluence.systeme-u.com/display/DTDD/Tests+Unitaires)