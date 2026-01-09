# Third-Party Resources (COMP2000)

This project was produced for an undergraduate coursework submission (educational, non-commercial use).

## Images / Media
Some UI and menu images used in the app were obtained via Google Images during development.  
At the time of submission, the original source page URLs are no longer available.

**How images are stored in this project**
- Images are included locally in the Android project under: `app/src/main/res/drawable/`
- Example filenames include (may vary): `background`, `savory_logo`, `salmon`, `lamb`, `pasta`, `coffee`, `profile`, etc.

**Important note**
- These images are used strictly for coursework demonstration.
- No claim of ownership is made over third-party images.
- If required, all third-party images can be replaced with lecturer-approved royalty-free assets (e.g., Unsplash/Pexels) or self-created images without affecting application logic.

`-

## Fonts
- `Itim` font is used in the UI (loaded via `res/font/itim.ttf` or `@font/itim` depending on setup).
  - If this font was downloaded externally and not bundled via Google Fonts/Android Studio, it should be treated as third-party.

## Libraries / Dependencies
Dependencies are managed via Gradle. Key libraries used include:
- AndroidX (AppCompat, ConstraintLayout, etc.)
- Material Components
- Volley (network requests for the REST API authentication/user endpoints)
