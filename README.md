# XpeApp

The XPEHO mobile application.

## Project structure

**XpeApp C4 diagram**

![XpeApp C4 diagram](http://www.plantuml.com/plantuml/png/ZP3DZjCm4CVlVeeXfwoqD09nuhIXAmHIAqhJgjmg9vusbXpRyWUbGhmxOvhMAbHiStB6F_y_via7xa8S57opM5lBNadTgIWmEWLz2DQ_hoh73vEz37rieqVN6HrGXqbdXcglZOW_gkcxrml5TLZFPzN0VK17e-sKG2urkeab0yPu5uo3Wenw0sjIAe1xI9ACDKTwDMm_cAW5TyUoU_EOo1G9QZ58NJp0JmRqNLJB0xPRRYs16so1I2Kx7gJH1Oq-gXW2kdHSzGZ40iy4x8mR6nSFxDU_zc-pVKfwmtqmhLGu1fZ_-VaxGws5Cr901V9B0vhct_4SwFJyvKpbJZfikSVKQJpIzNXvC4vOXzxNLgOzd7-XNdo-GJtpm4UjZ9w7nBCbgZ9ItqV8owN-FgN-j5ej6e8oS-hm7-mYaajStMU1kv3hjYD6X-bo_PmTxsUla5jBMY8Nljeu6KxTcg-pjAKdr28EwZS0 "XpeApp C4 diagram")

**XpeApp functional C4 diagram**

![XpeApp functional C4 diagram](http://www.plantuml.com/plantuml/png/RO_1IyCm5CRl-IlYJYleUl7aQNK82awwjOgtaRNFDP2yXEJ5ROZ_koyJf6psr7nyl_o-p2EfGCaQ-UwXyhxghKPnfR4pgOSKZ1o8VBmhow3sivsc8MrJXD0v942QTSwMEtHzEfIBsvlQAAJNvNDfLIG876qMN5GQ8SmyMuIe8KI7quC7vVr6PTsrdFpA9_pN3f2d34ug3WeXccCai9jxbx1NuJZbc6jBjzK6SiVeJf5sE2dabv3y_HbpzGNsqG0HMvaQdRAh0P0uYefp7kCUmkep4fTNRqsReUeKIdAIU6gdh5TwXz9zo5EZ4Dz2hC6SlV1YolgXQMLLF-Kh7zksRfYyn4OB_aV47B1Frlm0 "XpeApp functional C4 diagram")

## Requirement

- An IDE : [Android Studio](https://developer.android.com/studio)
- [Jetpack Compose] (https://developer.android.com/jetpack/compose) : Android UI toolkit

## Subprojects

### XpeApp Android

Folder containing the implementation of the android application.

Including:

- Source code (Java/Kotlin);
- Gradle configuration for compilation;
- Unit and integration tests specific to Android.

[README](./xpeapp_android/README.md) of the android application.

### XpeApp IOS

Folder containing the implementation of the ios application.

Including:

- Source code (Swift/Objective-C);
- Xcode configuration for compilation;
- Unit and integration tests specific to iOS.

[README](./xpeapp_ios/XpeApp/README.md) of the ios application.

### XpeApp Admin

Repository containing the implementation of the administration panel.

Including:

- Source code (Flutter/Dart);
- Generated source code (Web);
- Unit and integration tests specific to Flutter.

[README](https://github.com/XPEHO/xpeapp_admin/blob/main/README.md) of the admin panel.

### XpeApp Backend

Repository containing the implementation of the backend.

Including:

- Source code (Php);
- Docker deployment configuration;

[README](https://github.com/XPEHO/xpeapp_backend/blob/main/README.md) of the backend.

## CI/CD

This repository contain a lot of [workflows](./.github/workflows/) :

- android : Perform continuous integration for an Android project;
- codeql : Perform CodeQL analysis on the source code to identify vulnerabilities and errors;
- debugAppBundle : Make a build and upload of a debug app bundle on artifacts (See if it's not useless);
- detekt : Perform Detekt analysis on the source code to identify bad practices;
- git_checks : Perform various checks about the pull request, like commit names, branch name, etc...;
- labels : Automatically add a label using the tag of the branch of the pull request;
- releaseAppBundle : Make a build and upload of a release app bundle on artifacts (See if it's not useless);
- sonar_android : Send to the XPEHO SonarQube the source code of the android part and coverage report for analysis;
- sonar_ios : Send to the XPEHO SonarQube the source code of the ios part and coverage report for analysis.

## Automated tests

Some automated tests are done for the android and ios mobile applications using [Maestro](https://maestro.mobile.dev/).

[Tests folder](./maestroTest/)
