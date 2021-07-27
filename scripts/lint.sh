#!/bin/bash

# Run spotless on the src/ directory
# if there are any changes identified by git diff we print out the diff and exit 1
# else we exit with 0

./gradlew spotlessApply

wordCount=`git diff src/ | wc -l`;

if [ $wordCount -gt 0 ]
then
    git --no-pager diff --color
    exit 1
fi

exit 0
