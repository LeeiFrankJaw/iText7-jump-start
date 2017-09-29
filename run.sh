#!/usr/bin/env bash
SCRIPTDIR="$(dirname "`realpath "$0"`")"
DIR=.
if [ -n "$1" ]; then
    DIR="$1"
fi
cd "$DIR"
"$SCRIPTDIR/clean.sh" &&
    mvn -e package &&
    JAR=`echo target/Chapter*.jar` &&
    for i in `jar tf "$JAR" | sed '/tutorial.*\.class/!d; s/\//./g; s/\.class$//'`; do
        echo "$i" is running
        java -cp "$JAR" "$i"
    done
