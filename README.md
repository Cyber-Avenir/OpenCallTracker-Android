# OpenCallTracker

Application Android (Kotlin, Jetpack Compose) qui répond automatiquement aux appels entrants, envoie la touche `#` puis raccroche. Chaque appel traité est stocké dans un historique local. Aucun serveur, aucune connexion réseau.

## Fonctionnement

- Un grand bouton sur l'écran principal active ou désactive la détection (principe d'un bouton VPN).
- Détection activée : chaque appel entrant, quelle que soit la SIM, est décroché automatiquement, la touche `#` est envoyée, puis l'appel est raccroché.
- Un second écran affiche l'historique local des appels traités.
- L'état de la détection est persistant : il survit au passage en arrière-plan, à la fermeture de l'application et au redémarrage du téléphone.

> [!IMPORTANT]
> Pour pouvoir envoyer la touche `#` pendant un appel, l'application doit être définie comme **application Téléphone par défaut** (rôle `ROLE_DIALER`). Voir [Points d'attention](#points-dattention).

## Statut

Setup initial : le projet Compose, l'outillage de qualité et la CI sont en place. Les fonctionnalités ne sont pas encore implémentées.

## Stack

| Domaine | Choix |
| --- | --- |
| Langage | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Build | Gradle (Kotlin DSL), version catalog (`gradle/libs.versions.toml`) |
| Format et lint | Spotless + ktlint (`.editorconfig`) |
| CI | GitHub Actions |

Bibliothèques prévues, à ajouter au fil des fonctionnalités : ViewModel Compose, Navigation Compose, DataStore (état de la détection), Room (historique), Coroutines et Flow, injection de dépendances (Hilt ou injection manuelle).

## Architecture

MVVM, avec une séparation stricte entre la vue, la logique et l'accès aux données. Un seul module `app`, découpé en packages.

```
com.example.opencalltracker
├── ui/           Écrans Compose, ViewModels, navigation, thème (aucune logique métier)
├── domain/       Modèles et interfaces de repositories
├── data/         Implémentations : Room (historique), DataStore (état de la détection)
├── service/      InCallService : réception des appels, décrochage, DTMF, raccrochage
└── di/           Câblage des dépendances
```

Règles :

- Les composables n'accèdent jamais à Room, DataStore ni aux services : ils observent l'état exposé par un ViewModel (`StateFlow`) et lui envoient des événements.
- Le service d'appel et les ViewModels ne dépendent que des interfaces du `domain`.
- La détection est pilotée par un état unique (DataStore) lu par le service d'appel et par l'UI. L'UI ne "démarre" pas la détection, elle écrit l'état.

## Prérequis

- Android Studio (version compatible AGP 9.5)
- Le JDK 25 est téléchargé automatiquement par Gradle (toolchain `gradle/gradle-daemon-jvm.properties`)
- Android SDK 37 (installé via le SDK Manager d'Android Studio)
- Un téléphone physique avec SIM pour tester le comportement réel

## Démarrage

```bash
git clone <url-du-repo>
cd OpenCallTracker
./gradlew assembleDebug
```

Ou ouvrir le dossier dans Android Studio et lancer la configuration `app`.

> [!NOTE]
> `local.properties` (chemin du SDK) est généré par Android Studio et ignoré par git.

## Commandes

| Commande | Rôle |
| --- | --- |
| `./gradlew spotlessCheck` | Vérifie le format (Kotlin et scripts Gradle) |
| `./gradlew spotlessApply` | Corrige le format automatiquement |
| `./gradlew assembleDebug` | Compile l'APK debug |
| `./gradlew test` | Lance les tests unitaires |

> [!TIP]
> Lancer `./gradlew spotlessApply` avant chaque commit. La CI échoue si `spotlessCheck` échoue.

## Qualité de code

Le format est défini dans `.editorconfig` (style `android_studio`, pas de limite de largeur de ligne) et appliqué par Spotless avec ktlint. C'est l'équivalent de Prettier et ESLint côté Android. Android Studio lit aussi `.editorconfig` pour son formatage.

## CI

Le workflow `.github/workflows/ci.yml` s'exécute sur chaque push et pull request vers `main` : vérification du format, puis compilation de l'APK debug. Pas de tests ni de déploiement pour l'instant.

## Points d'attention

> [!WARNING]
> **Application Téléphone par défaut.** Envoyer une touche DTMF n'est possible que pour l'application qui détient le rôle `ROLE_DIALER` (via `InCallService`). Cette application doit donc proposer un clavier d'appel, une interface d'appel entrant et une interface d'appel en cours, sinon le téléphone n'est plus utilisable normalement une fois le rôle accordé. Le rôle est demandé à l'utilisateur au premier lancement (`RoleManager`), il ne peut pas être accordé automatiquement.

> [!WARNING]
> **Numéros d'urgence.** Android utilise toujours l'application Téléphone d'origine pour les appels d'urgence, même si l'application détient le rôle.

> [!WARNING]
> **Version Android minimale.** Le projet est actuellement en `minSdk = 24`. Le décrochage par `TelecomManager` demande l'API 26 et `RoleManager` l'API 29. Relever le `minSdk` avant d'implémenter la détection (29 recommandé).

> [!CAUTION]
> **Distribution.** Une application qui devient application Téléphone par défaut est soumise aux règles du Google Play Store sur les permissions d'appel. Pour une distribution hors Play Store (APK direct), il n'y a pas cette contrainte. À valider avec le client avant toute publication.

> [!CAUTION]
> **Usage légal.** Décrocher automatiquement des appels et envoyer des tonalités ne doit pas être utilisé pour dissimuler une activité ou perturber des tiers. L'application ne doit enregistrer ni l'audio ni le contenu des appels.

> [!NOTE]
> **Constructeurs Android.** Certaines surcouches (Xiaomi, Huawei, Samsung, etc.) limitent agressivement les applications en arrière-plan. Pour la fiabilité de la détection, exclure l'application des optimisations de batterie et tester sur les appareils cibles du client.

> [!NOTE]
> **Identifiant de l'application.** `com.example.opencalltracker` est un identifiant provisoire. Le définir avant la première distribution : il ne peut plus changer ensuite.

## Tester

- Émulateur : un appel entrant peut être simulé avec `adb emu gsm call 5551234`. Cela permet de tester le décrochage et le raccrochage, pas la réception réelle de la touche `#`.
- Téléphone physique : seul moyen de valider l'envoi du `#` de bout en bout (appeler le téléphone depuis un autre numéro et vérifier côté appelant).
- Tester les cas : écran verrouillé, application en arrière-plan, application fermée depuis les récents, redémarrage du téléphone, téléphone double SIM, deuxième appel pendant un appel en cours.
