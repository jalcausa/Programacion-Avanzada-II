/*
PHILOSOPHERS:
-Exclusión mutua: la sección crítica en este caso sería el tenedor.
- Condición de sincronización: el filósofo debe esperar a que los dos
tenedores que necesita están libres.
-Deadlock / Livelock: en caso de que cada filósofo tenga un tenedor (el de
su izquierda por ejemplo) y necesite el otro para comer se bloquearía el sistema
-Postposición indefinida (Starvation)
* */