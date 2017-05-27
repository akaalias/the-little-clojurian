#!/bin/bash

if ! lein cljfmt check; then
  # whitespace needs fixing
  echo "There are issues with whitespace. Run lein cljfmt fix, then add the changes and make your commit"
fi

if git diff-index --quiet HEAD --; then
  # no changes
  lein clean && lein test && git push origin master
else
  echo "There are uncommitted changes"
fi


