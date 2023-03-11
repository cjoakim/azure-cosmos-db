
const fs   = require('fs');
const os   = require("os");
const path = require("path");
const process = require('process');
const util = require('util');

const { exec } = require("child_process");
const child_process = require('child_process')

class Main {

  constructor() {
    this.processType = undefined;
    this.argv  = process.argv;
    this.apply = false;
    this.verbose = false;
    this.repoPaths = this.getRepoPaths();
    this.repoRootDir = this.pwd();
    this.gitLsFiles = Array();
    this.commonCodeFiles = {};
    this.appCodeFiles = {};
    this.ignoreCodeFiles = Array();
    this.ignoreCodeFiles = this.getIgnoreCodeFiles();

    for (var i = 0; i < this.argv.length; i++) {
      if (this.argv[i] == '--apply') {
        this.apply = true;
      }
      if (this.argv[i] == '--verbose') {
        this.verbose = true;
      }
    }
  }

  execute() {
    if (this.argv.length > 2) {
      this.processType = this.argv[2];
    }
    else {
      console.log('Invalid command-line args; no processType');
      this.displayHelp();
      return;
    }
    if (this.verbose) {
      console.log('processType: ' + this.processType);
    }

    switch (this.processType) {
      case 'help':
        this.displayHelp();
        break;
      case 'create_directory_structure':
        this.createDirectoryStructure();
        break;
      case 'common_code_changes':
        this.commonCodeChanges(this.argv[3]);
        break;
      default:
        console.log('undefined processType: ' + this.processType);
        this.displayHelp();
    }
  }

  displayHelp() {
    console.log('index.js help info:');
    console.log('node index.js help');
    console.log('node index.js create_directory_structure');
    console.log('node index.js common_code_changes');
    console.log('');
  }

  createDirectoryStructure() {
    console.log('createDirectoryStructure...');
    for (var i = 0; i < this.repoPaths.length; i++) {
      var path = this.repoPaths[i];
      if (this.verbose) {
        console.log('path: ' + path);
      }
      this.createDirWithReadme(path);
    }
  }

  commonCodeChanges(type) {
    console.log('commonCodeChanges, apply: ' + this.apply);
    this.executeGitLs();
    var diffList = this.identifyCodeDiffs();

    if (diffList.length == 0) {
      console.log('no common code diffs');
    }
    else {
      console.log('' + diffList.length + ' common code diffs');
      for (var i = 0; i < diffList.length; i++) {
        var diff = diffList[i];
        console.log(diff);
        if (this.apply) {
          var source = diff['appFilename'];
          var target = diff['commonFilename'];
          console.log('copying ' + source);
          fs.copyFileSync(source, target);
        }
      }
    }
  }

  // "private" methods below

  createDirWithReadme(path) {
    var readme_file = path + '/readme.md';
    if (fs.existsSync(path)) {
      if (this.verbose) {
        console.log('dir exists:    ' + path);
      }
    }
    else {
      console.log('creating dir:  ' + path);
      fs.mkdirSync(path, { recursive: true });
    }
    this.createReadme(path, readme_file);
  }

  createReadme(path, outfile) {
    if (fs.existsSync(outfile)) {
      if (this.verbose) {
        console.log('file exists:   ' + outfile);
      }
    }
    else {
      console.log('creating file: ' + outfile);
      var content = util.format("# readme for %s \n\n\n", path)
      this.writeFile(outfile, content);
    }
  }

  getRepoPaths() {
    var paths = [];
    paths.push('apis');
    paths.push('apis/cassandra');
    paths.push('apis/cassandra/cli');
    paths.push('apis/cassandra/java');
    paths.push('apis/cassandra/python');
    paths.push('apis/mongo');
    paths.push('apis/mongo/cli');
    paths.push('apis/mongo/java');
    paths.push('apis/mongo/node');
    paths.push('apis/mongo/node/server/src');
    paths.push('apis/mongo/python');
    paths.push('apis/nosql');
    paths.push('apis/nosql/az');
    paths.push('apis/nosql/dotnet');
    paths.push('apis/nosql/java');
    paths.push('apis/nosql/javaspring');
    paths.push('apis/nosql/node');
    paths.push('apis/nosql/powershell');
    paths.push('apis/nosql/python');
    paths.push('apis/pg');
    paths.push('apis/pg/cli');
    paths.push('apis/pg/java');
    paths.push('apis/pg/python');
    paths.push('automation');
    paths.push('automation/az');
    paths.push('common');
    paths.push('common/code');
    paths.push('common/code/dotnet');
    paths.push('common/code/java');
    paths.push('common/code/node');
    paths.push('common/code/python');
    paths.push('common/data');
    paths.push('common/data/generator');
    paths.push('common/data/retail');
    paths.push('common/data/zipcodes');
    paths.push('docs');
    paths.push('docs/img');
    paths.push('other');
    paths.push('other/emulator');
    paths.push('other/emulator/data');
    paths.push('other/functions');
    paths.push('other/functions/changefeed');
    paths.push('other/functions/changefeed/dotnet');
    paths.push('other/functions/changefeed/java');
    paths.push('other/functions/changefeed/node');
    paths.push('other/functions/http/python');
    paths.push('other/jupyter');
    paths.push('other/kusto');
    paths.push('other/kusto/cli');
    paths.push('other/rest');
    paths.push('other/search/python');
    paths.push('other/server_side');
    paths.push('other/synapse');
    paths.push('other/synapse/adf');
    paths.push('other/synapse/spark');
    return paths;
  }

  getIgnoreCodeFiles() {
    var list = Array();
    list.push('App.java');
    list.push('AppTest.java');

    list.push('main.py');
    list.push('__init__.py');
    return list;
  }

  executeGitLs() {
    // Get the lists of common and app code files from 'git ls-files'
    var response = child_process.execSync("git ls-files").toString();
    if (this.verbose) {
      console.log(response);
    }
    this.gitLsFiles = response.split(/\r?\n/);
    const codeFiletypes = ['.cs', '.java', '.js', '.py'];

    for (var i = 0; i < this.gitLsFiles.length; i++) {
      var line = this.gitLsFiles[i].trim();
      for (var t = 0; t < codeFiletypes.length; t++) {
        var filetype = codeFiletypes[t];
        if (line.endsWith(filetype)) {
          if (line.startsWith("common")) {
            var size = fs.statSync(line).size;
            this.commonCodeFiles[line] = size;
          }
          else {
            var size = fs.statSync(line).size;
            this.appCodeFiles[line] = size;
          }
        }
      }
    }
    this.writeFile(
        'tmp/commonCodeFiles.json',
        JSON.stringify(this.commonCodeFiles, null, 4));
    this.writeFile(
        'tmp/appCodeFiles.json',
        JSON.stringify(this.appCodeFiles, null, 4));
  }

  identifyCodeDiffs() {
    var commonNameMap = {};
    var commonSizeMap = {};
    var diffs = Array();

    // First collect commonSizeMap with basename as key and size as value
    for (const [filename, size] of Object.entries(this.commonCodeFiles)) {
      //console.log(`${filename}: ${size}`);
      var basename = path.basename(filename);
      commonSizeMap[basename] = size;
      commonNameMap[basename] = filename;
    }

    // Next collect the app code diffsList
    for (const [filename, size] of Object.entries(this.appCodeFiles)) {
      //console.log(`${filename}: ${size}`);
      var basename = path.basename(filename);
      if (this.includeForCodeDiffs(basename)) {
        if (commonSizeMap.hasOwnProperty(basename)) {
          var commonSize = commonSizeMap[basename];
          if (size != commonSize) {
            var diff = {};
            diff['commonFilename'] = commonNameMap[basename];
            diff['commonSize'] = commonSize;
            diff['appFilename'] = filename;
            diff['appSize'] = size;
            diffs.push(diff);
          }
        }
        else {
          console.log('not common: ' + filename);
        }
      }
    }
    this.writeFile(
        'tmp/diffs.json',
        JSON.stringify(diffs, null, 4));
    return diffs;
  }

  includeForCodeDiffs(basename) {
    return (this.ignoreCodeFiles.includes(basename) ? false : true);
  }

  pwd() {
    return process.cwd();
  }

  readLines(infile) {
    const data = fs.readFileSync(infile, 'UTF-8');
    return data.split(/\r?\n/);
  }

  writeFile(filename, contents) {
    fs.writeFileSync(filename, contents);
    console.log('file written: ' + filename);
  }
}

new Main().execute();
