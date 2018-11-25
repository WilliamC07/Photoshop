#!/usr/bin/env bash
mvn compile assembly:single
java -jar target/Photoshop-1.0-jar-with-dependencies.jar