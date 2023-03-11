# readme for other/jupyter 

Run **jupyter notebooks** on your laptop/workstation
and optionally access **Azure** services such as **CosmosDB**.

## Links

- https://jupyter.org/

## Getting Started

```
$ ./venv.sh                   <-- create a python virtual environment (on linux & macOS)

$ source venv/bin/activate    <-- activate the python virtual environment

$ jupyter kernelspec list     <-- list available jupyter kernel names and locations

$ jupyter kernelspec install-self  <-- create a jupyter kernel from the current directory

$ ./jupyter_start.sh          <-- start the jupyter notebooks
```

In **Windows PowerShell** execute **venv.ps1** instead of venv.sh; the other commands
should be the same.

---

## Example Running Jupyter Notebooks

### Initial Page - select an ipynb notebook

<p align="center"><img src="img/initial-list-of-files.png" width="100%"></p>

### Running Notebook

<p align="center"><img src="img/running-notebook.png" width="100%"></p>

---

## CosmosDB/Graph

See example notebook file **cosmos-gremlin.ipynb** 

---

## Visual Studio Code

Notebooks also run in Visual Studio Code (VSC).
See https://code.visualstudio.com/docs/datascience/jupyter-notebooks

---

## Maybe Later

- https://ipython-gremlin.readthedocs.io/en/latest/index.html
- https://github.com/aws/graph-notebook

