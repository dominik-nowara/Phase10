# Basis-Image mit OpenJDK 17 und Scala
FROM hseeberger/scala-sbt:17.0.1_1.5.5_2.13.6

# Installiere notwendige Pakete für JavaFX und X11-Forwarding
USER root
RUN apt-get update && apt-get install -y \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1-mesa-glx \
    libgtk-3-0 \
    x11-apps

# Arbeitsverzeichnis setzen
WORKDIR /app

# Kopiere die SBT-Build-Datei und die Projektdateien
COPY build.sbt .
COPY project ./project

# Installiere die Abhängigkeiten
RUN sbt update

# Kopiere den Rest des Projekts
COPY . .

# Kompiliere das Projekt
RUN sbt compile

# Stelle das Projekt bereit
CMD ["sbt", "run"]