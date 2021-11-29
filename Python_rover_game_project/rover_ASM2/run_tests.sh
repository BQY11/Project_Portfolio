#! /usr/bin/env sh

echo "#########################"
echo "### Testing Rover256 ..."
echo "#########################\n"

for test in tests/*.in; do
	name=$(basename $test .in)
	answer=tests/$name.out
	python3 game.py < $test | diff - $answer || echo "Failed test: $name\n"
done

echo "Done!"
