#!/bin/bash

if git diff-index --quiet HEAD --; then
  # no changes
  lein clean && lein test && git push origin master
else
  echo "There are uncommitted changes"
fi


