
echo 'Common Code Apply/Deploy:'
echo '1)  copy_common_cs'
echo '2)  copy_common_java'
echo '3)  copy_common_node'
echo '4)  copy_common_py'
echo '7)  list changes'
echo '8)  apply changes'
echo '9)  create directory structure'

$choice = Read-Host -Prompt 'Enter menu option'

if ($choice -eq '1') {
    ant -f copy_common_cs.xml
}
elseif ($choice -eq '2') {
    ant -f copy_common_java.xml
}
elseif ($choice -eq '3') {
    ant -f copy_common_node.xml
}
elseif ($choice -eq '4') {
    ant -f copy_common_py.xml
}
elseif ($choice -eq '7') {
    node index.js common_code_changes
}
elseif ($choice -eq '8') {
    node index.js common_code_changes --apply
}
elseif ($choice -eq '9') {
    node index.js create_directory_structure
}
else {
    echo "invalid choice: "$choice
}
