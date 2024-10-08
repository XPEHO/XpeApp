## 1.1.1

_Feat_

- [Issue #98](https://github.com/XPEHO/XpeApp/issues/98) Skip login screen when user logged in previously, gracefully display UI before login is complete
- [Issue #99](https://github.com/XPEHO/XpeApp/issues/99) Enabled Hardware Acceleration
- [Issue #97](https://github.com/XPEHO/XpeApp/issues/97) Shortened navigation animations
- [Issue #108](https://github.com/XPEHO/XpeApp/issues/108) Make the app's dark theme readable
- [Issue #64](https://github.com/XPEHO/XpeApp/issues/64) Add iOS newsletters list
- [Issue #66](https://github.com/XPEHO/XpeApp/issues/66) Add iOS newsletter detail
- [Issue #113](https://github.com/XPEHO/XpeApp/issues/113) Store Newsletters using CoreData for instant loading
- [Issue #69](https://github.com/XPEHO/XpeApp/issues/69) Add basic list of qvst campaigns
- [Issue #130](https://github.com/XPEHO/XpeApp/issues/130) Import and apply new design to ios part existing content and create sidebar and header of the layout
- [Issue #130](https://github.com/XPEHO/XpeApp/issues/130) Add tests for sidebar and construct the home page and the qvst page with the new design
- [Issue #70](https://github.com/XPEHO/XpeApp/issues/70) Add QVST form, Refactor structure of API Call, Remove CoreData and finally Create Router, Data and Toast independant systems
- [Issue #133](https://github.com/XPEHO/XpeApp/issues/133) Add Completion indication on QVST Card
- [Issue #68](https://github.com/XPEHO/XpeApp/issues/68) Implement feature flipping
- [Issue #60](https://github.com/XPEHO/XpeApp/issues/60)
- [Issue #145](https://github.com/XPEHO/XpeApp/issues/145)
- [Issue #146](https://github.com/XPEHO/XpeApp/issues/146)
- Implement Login Feature and refactor the entire code to follow clean architecture

_Fix_

- Update version in build gradle
- [Issue #86](https://github.com/XPEHO/XpeApp/issues/86) Fix multiple back button presses
- [Issue #85](https://github.com/XPEHO/XpeApp/issues/85) Fix Feature Flipping overlay going out of bounds
- Include start and end day in qvstBreadcrumb `_isCurrent` check
- [Issue #93](https://github.com/XPEHO/XpeApp/issues/93) Fix bug where 2 bearer token are sent, crashing the app
- [Issue #96](https://github.com/XPEHO/XpeApp/issues/96) Fix bug where QVST Campaigns list was flashing because of a refetch on redraws
- [Issue #95](https://github.com/XPEHO/XpeApp/issues/95) Fix bug where app crashed upon calling showNotification on devices of API 31 or above
- [Issue #139](https://github.com/XPEHO/XpeApp/issues/139) Add injection dependency for state management
- Update components use from xpeho_ui_swift according to changes done on it

_Chore_

- Update changelog
- [Issue #83](https://github.com/XPEHO/XpeApp/issues/83) Add authorization header to WordpressAPI requests
- [Issue #78](https://github.com/XPEHO/XpeApp/issues/78) Create CI pipelines to automate release builds
- [Issue #62](https://github.com/XPEHO/XpeApp/issues/62) Create iOS folder structure
- [Issue #67](https://github.com/XPEHO/XpeApp/issues/67) Add firebase to the iOS app
- Change detekt scan and add README files
- [Detekt] Disable NewLineAtEndOfFile
- [Sonar] Add sonarqube to the iOS project
- [Sonar] Add iOS coverage

_Doc_

- [Issue #79](https://github.com/XPEHO/XpeApp/issues/79) Add the PUML diagram of the project

## 1.1.0

_Feat_

- Upgrade Material version to Material 3
- Open the app when the user click on a notification
- [Issue #4](https://github.com/XPEHO/XpeApp/issues/4) Add the home page
- [Issue #16](https://github.com/XPEHO/XpeApp/issues/16) Add the newsletters page
- [Issue #12](https://github.com/XPEHO/XpeApp/issues/12) Add the vacation page
- [Issue #13](https://github.com/XPEHO/XpeApp/issues/13) Add the colleagues page
- [Issue #18](https://github.com/XPEHO/XpeApp/issues/18) Add the feature flipping
- [Issue #20](https://github.com/XPEHO/XpeApp/issues/20) Add the newsletter detail page
- [Issue #3](https://github.com/XPEHO/XpeApp/issues/3) Add the Wordpress authentification
- [Issue #6](https://github.com/XPEHO/XpeApp/issues/6) Init the disconnection dialog
- [Issue #35](https://github.com/XPEHO/XpeApp/issues/35) Add the Firebase authentification
- [Issue #7](https://github.com/XPEHO/XpeApp/issues/7) Display notifications whe newsletter is created
- [Issue #54](https://github.com/XPEHO/XpeApp/issues/56) QVST Campaign feature
- [Issue #75](https://github.com/XPEHO/XpeApp/pull/75) Add environment variable Firebase
- [Issue #77](https://github.com/XPEHO/XpeApp/issues/77) Add QVST remaining days banner

_Fix_

- Add the missing features flipping composables in home page
- Remove all problems and warnings from the project from Detekt
- Fix android and kotlin analysis cicd (branch `main`)
- Fix detekt warnings
- [Issue #28](https://github.com/XPEHO/XpeApp/issues/28) Fix design of multiples pages
- [Issue #38](https://github.com/XPEHO/XpeApp/issues/38) Fix loader which don't display
- [Issue #37](https://github.com/XPEHO/XpeApp/issues/37) Fix gray screen which appear when the app is launched
- Add missing affectation of wordpressState in the viewmodel

_Chore_

- Update changelog
- Add privacy policy
- Reorganization of ui folder architecture
- Add properties for release app bundle
- Add environment variables for build
- [Issue #22](https://github.com/XPEHO/XpeApp/issues/22) Add the CI/CD of Xpeho Workflows
- [Issue #40](https://github.com/XPEHO/XpeApp/issues/40) Update the icon of the app
- [Issue #53](https://github.com/XPEHO/XpeApp/issues/53) Sonar CICD
- Fix the CI/CD Sonar
- Add folder xpeapp_android for the android project

## 0.0.1

_Feat_

- [Issue #2](https://github.com/XPEHO/XpeApp/issues/2) Add the login page

_Chore_

- Init the repository and the project
