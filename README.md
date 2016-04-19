# JAVA_ISDS

*Multiplatformní knihovna v Javě pro přístup k ISDS (informačnímu systému datových schránek).*

Knihovna se skládá ze čtyř modulů:

1) ISDSCommon -- knihovna definující společné rozhraní pro obě implementace.

3) ISDSWebServices -- vygenerované webové služby.

2) TinyISDS -- minimalistická knihovna pro přístup k ISDS, podporuje stažení
seznamu přijatých zpráv, stažení zprávy a získání haše zprávy. Ostatní operace
nejsou podporovány.

4) ISDS -- knihovna pro přístup k ISDS s plnou funkcionalitou, tzn.
odesílání zpráv, ověření integrity stažených zpráv, vyhledávání datových
schránek a podobně.

Build se provádí mavenem, takže jen stačí spustit příkaz "mvn package" a ten se
o vše potřebné postará.

## Continuous Integration

CI server hosted on Travis-ci.org: [![Build Status](https://travis-ci.org/czgov/JAVA_ISDS.svg?branch=master)](https://travis-ci.org/czgov/JAVA_ISDS)
