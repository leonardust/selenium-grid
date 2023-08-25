#!/bin/bash

# Uruchomienie Event Bus
java -jar selenium-server-4.11.0.jar event-bus &

# Uruchomienie Session Map
java -jar selenium-server-4.11.0.jar sessions &

# Uruchomienie new session queue
java -jar selenium-server-4.11.0.jar sessionqueue &

# Uruchomienie Distributora
java -jar selenium-server-4.11.0.jar distributor --sessions http://localhost:5556 --sessionqueue http://localhost:5559 --bind-bus false &

# Uruchomienie Routera
java -jar selenium-server-4.11.0.jar router --sessions http://localhost:5556 --distributor http://localhost:5553 --sessionqueue http://localhost:5559 &

# Dodanie Noda
java -jar selenium-server-4.11.0.jar node --selenium-manager true --detect-drivers true