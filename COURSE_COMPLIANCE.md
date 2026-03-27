# Conformite Cours (Compose / Navigation / Room / MVVM)

Ce document mappe les exigences du support de cours sur l'etat reel du projet.

## 1) Jetpack Compose (UI)
- Activite + `setContent`: `app/src/main/java/com/example/safevault/MainActivity.kt`
- Theme Compose Material 3: `app/src/main/java/com/example/safevault/ui/theme/`
- Ecrans Compose:
  - `feature/documents/list/DocumentsListScreen.kt`
  - `feature/documents/add/AddDocumentScreen.kt`
  - `feature/documents/edit/EditDocumentScreen.kt`
  - `feature/documents/detail/DocumentDetailScreen.kt`
- Composants du cours utilises: `Scaffold`, `TopAppBar`, `LazyColumn`, `Card`, `Button`, `TextField`, `IconButton`, `FilterChip`, etc.

## 2) Navigation Compose
- `rememberNavController`: `app/src/main/java/com/example/safevault/SafeVaultApp.kt`
- `NavHost` + destinations: `app/src/main/java/com/example/safevault/navigation/SafeVaultNavHost.kt`
- Destinations type-safe (`@Serializable`): `app/src/main/java/com/example/safevault/navigation/SafeVaultDestination.kt`
- Navigation avant/arriere: `navigate`, `popBackStack` dans `SafeVaultNavHost.kt`

## 3) Room (donnees locales)
- Entite principale:
  - `DocumentEntity`: `app/src/main/java/com/example/safevault/data/local/entity/DocumentEntity.kt`
- DAO:
  - `DocumentsDao`: `app/src/main/java/com/example/safevault/data/local/dao/DocumentsDao.kt`
- Database + migrations:
  - `SafeVaultDatabase`: `app/src/main/java/com/example/safevault/data/local/SafeVaultDatabase.kt`
  - Migration `1 -> 2`: suppression de la colonne obsolete `expiresAtMillis`
  - Migration `2 -> 4`: compatibilite schema actuelle (no-op)
  - Migration `3 -> 4`: nettoyage de la table legacy `document_audits`
- Schemas Room exportes: `app/schemas/com.example.safevault.data.local.SafeVaultDatabase/`

## 4) MVVM
- ViewModels:
  - `DocumentsListViewModel`
  - `AddDocumentViewModel`
  - `EditDocumentViewModel`
  - `DocumentDetailViewModel`
  (dans `app/src/main/java/com/example/safevault/feature/documents/...`)
- Etats UI: `StateFlow` + events `SharedFlow`
- Liaison VM <-> UI: `*Route.kt`
- Couche data/repository:
  - `DocumentsRepository` (contrat)
  - `RoomDocumentsRepository` (impl production)
  - `InMemoryDocumentsRepository` (tests)

## 5) Versions / dependances
- AGP: `9.1.0`
- Kotlin: `2.3.20`
- Navigation Compose: `2.9.7`
- Room: `2.8.4`
- Lifecycle: `2.10.0`
- Coroutines: `1.10.2`
- Source: `gradle/libs.versions.toml`

## 6) Validation technique (26/03/2026)
- `:app:assembleDebug` OK
- `:app:assembleRelease` OK
- `:app:test` OK
- `:app:lintDebug` OK (`No issues found`)
- `:app:lintRelease` OK (`No issues found`)
