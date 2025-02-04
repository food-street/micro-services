#!/bin/bash

# Ensure there are no uncommited changes, else exit
if [ -n "$(git status --porcelain)" ]; then
  echo "There are uncommitted changes. Please commit or stash them before releasing."
  exit 1
fi

# Check if the correct number of arguments are provided
if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <service-name> <release-type>"
  echo "Service name must be one of: api-gateway, inventory, order, product"
  echo "Release type must be one of: major, minor, patch"
  exit 1
fi

SERVICE_NAME=$1
RELEASE_TYPE=$2
POM_FILE="$SERVICE_NAME/pom.xml"

# Check if the POM file exists
if [ ! -f "$POM_FILE" ]; then
  echo "POM file not found for service: $SERVICE_NAME"
  exit 1
fi

# Extract the current version from the POM file
CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout -f "$POM_FILE")

# Split the version into major, minor, and patch components
IFS='.' read -r -a VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR=${VERSION_PARTS[0]}
MINOR=${VERSION_PARTS[1]}
PATCH=${VERSION_PARTS[2]}

echo "major $MAJOR minor $MINOR patch $PATCH"

# Increment the version based on the release type
case $RELEASE_TYPE in
  major)
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    ;;
  minor)
    MINOR=$((MINOR + 1))
    PATCH=0
    ;;
  patch)
    PATCH=$((PATCH + 1))
    ;;
  *)
    echo "Invalid release type: $RELEASE_TYPE"
    echo "Release type must be one of: major, minor, patch"
    exit 1
    ;;
esac

# Construct the new version
NEW_VERSION="$MAJOR.$MINOR.$PATCH"

echo "New version: $NEW_VERSION"

# Update the version in the POM file
mvn versions:set -DnewVersion="$NEW_VERSION" -f "$POM_FILE"
mvn versions:commit -f "$POM_FILE"

# Commit the changes
git add "$POM_FILE"
git commit -m "Bump $SERVICE_NAME version to $NEW_VERSION"

# Create a new tag
TAG_NAME="${SERVICE_NAME}_v${NEW_VERSION}"
git tag "$TAG_NAME"

# Push the changes and the tag
git push origin main
git push origin "$TAG_NAME"

echo "Successfully updated $SERVICE_NAME to version $NEW_VERSION and pushed tag $TAG_NAME"