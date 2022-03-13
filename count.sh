stats=<<"EOF"
0	27	.github/workflows/count_lines.yml
85	0	.github/workflows/limit_changed_lines.yml
13	3	.github/workflows/review_dog.yml
0	10	build.gradle
15	0	count.sh
10	0	gradle/hoge.kt
EOF

LIMIT=10
BASE=main
EXCLUDE=.github
EXT="kt|ya?ml"
changed=$(git diff $BASE --numstat \
| if [ "$EXCLUDE" != "" ]; then grep -vE ".*\/?($EXCLUDE)\/.*"; else cat; fi \
| grep -E ".*\.($EXT)$" \
| awk '{ additions+=$1 } END { printf "%d", additions }')

echo "changed=$changed"

if [ $changed -gt $LIMIT ]; then
    echo "::error::(Limit: $LIMIT, Changed: $changed)"
    exit 1
fi