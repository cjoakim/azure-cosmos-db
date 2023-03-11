#!/bin/bash

# List the defined jupyter kernels on your system.
# Chris Joakim, Microsoft

jupyter kernelspec list

# Output of the above:
# Available kernels:
#   localml    /Users/cjoakim/Library/Jupyter/kernels/localml
#   python3    /Users/cjoakim/Library/Jupyter/kernels/python3

# jupyter kernelspec remove localml

# Output of the above:
# Kernel specs to remove:
#   localml             	/Users/cjoakim/Library/Jupyter/kernels/localml
# Remove 1 kernel specs [y/N]: y
# [RemoveKernelSpec] Removed /Users/cjoakim/Library/Jupyter/kernels/localml

# Install the current directory as a kernel 
jupyter kernelspec install-self

# Output of the above:
# [InstallNativeKernelSpec] WARNING | `jupyter kernelspec install-self` is DEPRECATED as of 4.0. You probably want `ipython kernel install` to install the IPython kernelspec.
# [InstallNativeKernelSpec] Removing existing kernelspec in /usr/local/share/jupyter/kernels/python3
# [InstallNativeKernelSpec] Installed kernelspec python3 in /usr/local/share/jupyter/kernels/python3

# cat the updated kernel.json file; it's pointing to the correct directory now
# cat /usr/local/share/jupyter/kernels/python3/kernel.json
# {
#  "argv": [
#   "/Users/cjoakim/github/azure-jupyter/venv/bin/python3.9",
#   "-m",
#   "ipykernel_launcher",
#   "-f",
#   "{connection_file}"
#  ],
#  "display_name": "Python 3",
#  "language": "python"
# }

jupyter kernelspec list

# Output of the above:
# Available kernels:
#   python3    /Users/cjoakim/github/azure-jupyter/venv/share/jupyter/kernels/python3

