"""
Exploring plots with matplotlib.pyplot and seaborn.
Usage:
  python main.py <func>
  python main.py penguins
  python main.py barplot
  python main.py boxplot
  python main.py plot1 n
  python main.py plot2 n
Options:
  -h --help     Show this screen.
  --version     Show version.
"""

import sys

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns


from docopt import docopt

from pysrc.minbundle import Bytes, Counter, Env, FS, Storage, System

def print_options(msg):
    print(msg)
    arguments = docopt(__doc__, version='1.0.0')
    print(arguments)

def penguins():
    print('plot')
    df = sns.load_dataset("penguins")
    sns.pairplot(df, hue="species")
    #plt.show()
    plt.savefig("tmp/penguins.png")

def barplot():
    sns.set_theme(style="whitegrid")
    penguins = sns.load_dataset("penguins")
    print(str(type(penguins))) # <class 'pandas.core.frame.DataFrame'>
    penguins.to_csv("tmp/penguins_df.csv")

    g = sns.catplot(
        data=penguins, kind="bar",
        x="species", y="body_mass_g", hue="sex",
        errorbar="sd", palette="dark", alpha=.6, height=6
    )
    g.despine(left=True)
    g.set_axis_labels("", "Body mass (g)")
    g.legend.set_title("")
    plt.savefig("tmp/barplot.png")

def boxplot():
    sns.set_theme(style="ticks", palette="pastel")
    tips = sns.load_dataset("tips")
    print(str(type(tips)))
    tips.to_csv("tmp/tips_df.csv")

    # Draw a nested boxplot to show bills by day and time
    sns.boxplot(x="day", y="total_bill",
                hue="smoker", palette=["m", "g"],
                data=tips)
    sns.despine(offset=10, trim=True)
    plt.savefig("tmp/boxplot.png")

def physical_partition_plot(n):
    # https://seaborn.pydata.org/generated/seaborn.catplot.html
    infile = "partitions_case{}.csv".format(n)
    outfile = "tmp/physical_partitions_{}.png".format(n)
    sns.set_theme(style="whitegrid")
    df = pd.read_csv(infile)
    print(df)

    g = sns.catplot(
        data=df, kind="bar",
        x="RU", y="GB", hue="partition_number",
        errorbar="sd", palette="dark", alpha=.6, height=6
    )
    #g.despine(left=True)
    g.set_axis_labels("GB", "RU")
    g.legend.set_title("")
    plt.savefig(outfile)

def physical_partition_plot_v2(n):
    # https://seaborn.pydata.org/generated/seaborn.catplot.html
    infile = "partitions_case{}.csv".format(n)
    outfile = "tmp/physical_partitions_{}.png".format(n)
    sns.set_theme(style="whitegrid")
    df = pd.read_csv(infile)
    print(df)

    sns.displot(data=df, x="GB")

    plt.savefig(outfile)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print_options('Error: no command-line args')
    else:
        func = sys.argv[1].lower()
        if func == 'penguins':
            penguins()
        elif func == 'barplot':
            barplot()
        elif func == 'boxplot':
            boxplot()
        elif func == 'plot1':
            physical_partition_plot(1)
        elif func == 'plot2':
            physical_partition_plot(2)
        else:
            print_options('Error: invalid function: {}'.format(func))
