# Setup
## Linter
[Ktlint](https://github.com/JLLeitschuh/ktlint-gradle) is used to check code formatting before making a commit.<br/>
Add the following code to a file titled `pre-commit` in `<project-directory>/git/hooks/`:
```
#!/bin/sh

CHANGED_FILES="$(git --no-pager diff --name-status --no-color --cached | awk '$1 != "D" && $NF ~ /\.kts?$/ { print $NF }')"

if [ -z "$CHANGED_FILES" ]; then
echo "No Kotlin staged files."
exit 0
fi;

echo "Running ktlint over these files:"
echo "$CHANGED_FILES"

./gradlew ktlintCheck
gradle_command_exit_code=$?
exit $gradle_command_exit_code
```
## Firestore Rules
[Firebase CLI](https://firebase.google.com/docs/cli) is used to update Firestore rules.<br/>
1. Download and run Firebase CLI
2. Login to Firebase using `firebase login`
   - If error occurs, try `firebase login --reauth` 
4. Verify Firebase projects using `firebase projects:list`
5. Once logged in, Firestore rules can be modified in file titled `firestore.rules`
6. Push new Firestore rules using `firebase deploy`
