# kotlin-movies

## Kotlin alapú szoftverfejlesztés - Házi feladat
Spec: Filmest szervező applikáció (Movie night planning app). Spring webalkalmazás formájában valósítom meg. Az alkalmazás célja a több felhasználó által javasolt filmek közül való választás segítése. 

###
Spring Boot alkalmazás, Gradle project, Java 11.

###
1. futtatható *gradlew bootRun* v. idea-ból
2. 8080-as portot használja
3. H2 fájl adatbázist használ (data mappa)

## Felületen eligazodás
1. <a href="http://localhost:8080/login">Login page</a>: teszt accountok
    - login: b, pw: b (Ben)
    - login: d, pw: d (Dixie)
    - ugyanígy azonos login-pw: j, k, l, m
2. Ezek egyikével belépbve tovább dob a [Home](http://localhost:8080) oldalra
    - Itt láthatóak a már létetző események. A hozzájuk tartozó *Details* gombbal jutunk a részletező oldalra
3. Az event részletei alatt intézhető a hozzá tartozó filmek mogyorózása.
    - *Add movie options* szekcióban a megadott stringet / filmcímet hozzáadhatjuk a listához
    - A *Movie options* listában látszanak a már hozzáadott filmek, a rájuk érkezet zsavazatok száma és az aktuálisan belépett felhasználó szavazatát jelentő checkboxok.
4. A <a href="http://localhost:8080/new-event">new event</a> oldalon készülhet új event a megadott dátummal és a kiválasztott felhasználókkal. 
    - *All users* szekcióban felsoroltak melletti "+" gomb az eseményhez adás
    
## actual kotlin munka
bme.kotlin.movies package