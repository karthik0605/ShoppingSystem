#!/bin/bash

JAVA_FX_PATH="/Users/yourusername/javafx-sdk-17/lib"
SRC_DIR="src/com/shopmatic"
OUT_DIR="out"

# Compile
javac --module-path $JAVA_FX_PATH --add-modules javafx.controls,javafx.fxml \
    -d $OUT_DIR \
    $SRC_DIR/*.java

# Run
java --module-path $JAVA_FX_PATH --add-modules javafx.controls,javafx.fxml \
    -cp $OUT_DIR \
    com.shopmatic.Shopmatic
