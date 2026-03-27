# SafeVault

Application Android (Jetpack Compose) pour gerer un coffre de documents personnels:
- ajout/modification/suppression de documents
- categorisation
- image optionnelle par document
- interface moderne Material 3

## Stack technique
- Kotlin 2.3.20
- AGP 9.1.0
- Compose BOM 2026.03.01
- Navigation Compose 2.9.7
- Room 2.8.4 (avec migrations)
- Architecture MVVM + Repository

## Architecture
Le code est organise par couches:
- `feature/`: UI par ecran (list, add, edit, detail)
- `data/local`: Room (`Entity`, `Dao`, `Database`)
- `data/repository`: implementations d'acces aux donnees
- `domain/model`: modeles metier
- `navigation`: routes + NavHost
- `ui/theme`: design system (couleurs, typo, shapes)

## Prerequis
- Android Studio recent (2026+ recommande)
- JDK 17 (ou JBR Android Studio)
- SDK Android 36

## Lancer le projet
```bash
./gradlew :app:assembleDebug
```

## Verification qualite
```bash
./gradlew :app:test :app:lintDebug :app:lintRelease
```

## Base de donnees (Room)
- Nom: `safevault.db`
- Migrations gerees dans:
  - `app/src/main/java/com/example/safevault/data/local/SafeVaultDatabase.kt`
- Schemas exportes:
  - `app/schemas/com.example.safevault.data.local.SafeVaultDatabase/`

## Documentation de conformite cours
Le mapping detaille Compose / Navigation / Room / MVVM est disponible dans:
- `COURSE_COMPLIANCE.md`
