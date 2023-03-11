
# Chris Joakim, Microsoft

$outDir='baseball'

echo 'delete/recreate output directory ...'
Remove-Item $outDir -Force  -Recurse -ErrorAction SilentlyContinue
New-Item -itemtype directory -force -path $outDir | Out-Null

python baseball_data_gen.py create_baseball_documents

