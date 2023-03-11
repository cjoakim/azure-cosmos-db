#!/bin/bash

echo 'Common Code Apply/Deploy:'
echo '1)  copy_common_cs'
echo '2)  copy_common_java'
echo '3)  copy_common_node'
echo '4)  copy_common_py'
echo '7)  list changes'
echo '8)  apply changes'
echo '9)  create directory structure'

echo 'Enter menu option:'
read choice

if [[ $choice = '1' ]]
then
    ant -f copy_common_cs.xml
fi

if [[ $choice = '2' ]]
then
    ant -f copy_common_java.xml
fi

if [[ $choice = '3' ]]
then
    ant -f copy_common_node.xml
fi

if [[ $choice = '4' ]]
then
    ant -f copy_common_py.xml
fi

if [[ $choice = '7' ]]
then
    node index.js common_code_changes
fi

if [[ $choice = '8' ]]
then
    node index.js common_code_changes --apply
fi

if [[ $choice = '9' ]]
then
    node index.js create_directory_structure
fi
