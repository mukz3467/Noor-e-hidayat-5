# Scholarly Tafseer Live API Architecture

Scholarly Tafseer in **Noor-e-Hidayat 4** is fetched **live online** per Ayah and kept separate from the Quran Translation feature. Full multi-volume Tafseer corpora are **not** bundled offline in the APK.

## Verified Online Endpoints
1. **Quran.com / Quran Foundation API v4**:
   - Catalog: `https://api.quran.com/api/v4/resources/tafsirs`
   - Ayah Tafseer: `https://api.quran.com/api/v4/tafsirs/{resource_id}/by_ayah/{surah}:{ayah}`
   - Surah Info: `https://api.quran.com/api/v4/chapters/{surah}/info?language={lang}`
2. **spa5k/tafsir_api (via jsDelivr CDN, sourced from QUL & Al-Tafsir.com)**:
   - Editions: `https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@v1.2.3/tafsir/editions.json`
   - Ayah JSON: `https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@main/tafsir/{slug}/{surah}/{ayah}.json`
   - Surah JSON fallback: `https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@main/tafsir/{slug}/{surah}.json`

## Strict Principles
- **Separation**: Quran Translation (`assets/tarjuma/` & `TranslationCatalog`) and Scholarly Tafseer (`OnlineTafseerEdition`) remain strictly separate features.
- **No Offline Bulk Tafseer Bundling**: Live online Tafseer content is held in volatile in-memory session cache only.
- **Language Sync**: Automatically follows global Settings `AppLanguage` (`ur`, `en`, `hi`, `ar`).
- **No Fabrication / No Machine Translation**: Unavailable languages/ayahs are honestly reported with Retry or alternate scholar selection.
