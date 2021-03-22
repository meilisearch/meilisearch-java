#!/bin/sh

# Checking if current tag matches the package version
current_tag=$(echo $GITHUB_REF | tr -d 'refs/tags/v')
check1='build.gradle'
check2='README.md'
check3='README.md'

tag1=$(grep '^version = ' $check1 | cut -d "'" -f 2 | tr -d ' ')
tag2=$(grep '<version>' $check2 | cut -d '>' -f2 | cut -d '<' -f1)
tag3=$(grep 'implementation' $check3 | cut -d : -f3 | tr -d "'")
if [ "$current_tag" != "$tag1" ] || [ "$current_tag" != "$tag2" ] || [ "$current_tag" != "$tag3" ]; then
  echo "Error: the current tag does not match the version in package check(s)."
  echo "$check1: found $tag1 - expected $current_tag"
  echo "$check2: found $tag2 - expected $current_tag"
  echo "$check3: found $tag3 - expected $current_tag"
  exit 1
fi

echo 'OK'
exit 0
