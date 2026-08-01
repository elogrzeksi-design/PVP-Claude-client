# Claude Client

Mod Fabric do Minecraft **1.21.11** ("Mounts of Mayhem", PvP utility mod, nie standalone client).

> **Uwaga:** 1.21.11 to ostatnia obfuskowana wersja Minecrafta korzystająca
> z Yarn mappings — od kolejnych wersji Fabric przechodzi na oficjalne
> Mojang Mappings, a Loom traci wsparcie dla remapowania w obecnej formie.
> Ten projekt celuje jeszcze w klasyczny toolchain (Loom 1.14 + Yarn).

## Funkcje

| Moduł | Klawisz/dostęp | Opis |
|---|---|---|
| Ustawienia | **Prawy Shift** | Otwiera/zamyka panel ustawień |
| FPS | HUD | Licznik klatek na sekundę |
| CPS | HUD | Kliknięcia myszy/s (LPM/PPM osobno) |
| Keystrokes | HUD | Wizualizacja WSAD + spacja |
| Gamma | Panel ustawień | Suwak jasności 0–100 |
| Boost FPS | Panel ustawień | Wyłącza chmury/cząsteczki/mgłę |
| Custom Crosshair | Panel ustawień | Podmiana tekstury celownika |
| Hitbox Highlight | Panel ustawień | Podświetla hitbox gracza **tylko** gdy jest w zasięgu ataku i w polu widzenia (zwykły raycast gry — **nie działa przez ściany**, to nie jest ESP) |
| Freecam | **F4** | Odłącza kamerę od ciała gracza |
| Waypoints | Panel ustawień | Zapisywanie punktów, strzałka/dystans na HUD — **bez auto-teleportacji** |
| Auto Totem | Panel ustawień | Automatyczne uzupełnianie totemu w offhandzie |

## Status implementacji

Ten pakiet zawiera **kompletny szkielet moda z pełną implementacją** wszystkich
10 modułów: strukturę Gradle/Loom, system modułów, GUI ustawień w motywie
pomarańczowo-biało-żółtym, keybindy, geometrię renderowania obrysu hitboxa,
sterowanie kamerą Freecam (WSAD + mysz) oraz komplet tekstur (celownik, ikona).

**Pozostaje zweryfikować lokalnie przy pierwszym imporcie do IDE** (typowe dla
każdego świeżego projektu Fabric, nie specyficzne dla tego moda):
- dokładne nazwy metod w Yarn `1.21.11+build.1` — Loom uruchomi `genSources`
  przy pierwszym imporcie; jeśli któraś nazwa mappingu (np. `Camera#setPos`,
  `WorldRenderContext` gettery) zmieniła się między buildami Yarn, IDE od razu
  to pokaże jako błąd kompilacji do poprawienia,
- czy `RenderLayer.getLines()` w `BoxOutlineRenderer` to wciąż aktualna nazwa
  warstwy renderowania linii w tej wersji (Mojang czasem przemianowuje render
  layery między aktualizacjami).

## Budowanie

```bash
./gradlew build
```

Wymaga JDK 21. Wygenerowany plik JAR pojawi się w `build/libs/`.

### Wersje toolchaina (1.21.11)

| Komponent | Wersja |
|---|---|
| Minecraft | 1.21.11 |
| Fabric Loader | 0.18.1+ |
| Fabric API | 0.141.6+1.21.11 |
| Fabric Loom | 1.14-SNAPSHOT |
| Yarn mappings | 1.21.11+build.1 |
| Java | 21 |

## Import do IDE

Zalecane: IntelliJ IDEA + wtyczka Minecraft Development, albo VS Code z
rozszerzeniem Extension Pack for Java. Po otwarciu projektu uruchom
`genSources` z Gradle, żeby mieć podpowiedzi do zmapowanych metod Yarn.
