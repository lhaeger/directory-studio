#!/bin/sh
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

set -e

for archive in ../../../../product/target/products/ApacheDirectoryStudio-*-macosx.*.tar.gz; do

    echo "Building ${archive//tar.gz/dmg}"

    # cleanup
    rm -rf dmg/* TMP*dmg

    # prepare DMG content
    mkdir -p dmg/.background/
    cp -av background.png dmg/.background/
    cp -av DS_Store dmg/.DS_Store
    ln -sv /Applications dmg/Applications

    # Copy the application
    tar -xvf $archive -C dmg

    # Codesign the App and verify
    if [[ $1 =~ -s|--sign ]] && [[ $2 =~ .+ ]]; then
        echo "Signing with ID: $2"
        codesign --verbose --force --deep --timestamp --options runtime --entitlements entitlements.plist -s "$2" dmg/ApacheDirectoryStudio.app
        codesign -dv --verbose=4 dmg/ApacheDirectoryStudio.app
    fi

    # Creating the disk image
    hdiutil create -verbose -srcfolder dmg/ -volname "ApacheDirectoryStudio" -o TMP.dmg
    hdiutil convert -verbose -format UDZO TMP.dmg -o ${archive//tar.gz/dmg}

    rm -rf dmg/* TMP*dmg

done
