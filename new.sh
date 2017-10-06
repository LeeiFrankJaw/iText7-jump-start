#!/usr/bin/env bash

mvn archetype:generate \
    -DgroupId="tutorial.chapter0$1" \
    -DartifactId="Chapter$1" \
    -DarchetypeCatalog=local \
    -DinteractiveMode=false &&

    rm -Rf "Chapter$1/src/test" \
       "Chapter$1/src/main/java/tutorial/chapter0$1/App.java" &&
    
    cp Chapter2/pom.xml "Chapter$1"/ &&

    cp Chapter3/prj.el "Chapter$1"/ &&

    sed -i -r "s/([Cc]hapter0?)2/\1$1/g" "Chapter$1"/pom.xml
