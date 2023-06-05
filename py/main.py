
import sys

import matplotlib.pyplot as plt
import seaborn as sns


def print_options(msg):
    print(msg)

def penguins():
    print('plot')
    df = sns.load_dataset("penguins")
    sns.pairplot(df, hue="species")
    #plt.show()
    plt.savefig("tmp/penguins.png")

def barplot():
    sns.set_theme(style="whitegrid")
    penguins = sns.load_dataset("penguins")
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

    # Draw a nested boxplot to show bills by day and time
    sns.boxplot(x="day", y="total_bill",
                hue="smoker", palette=["m", "g"],
                data=tips)
    sns.despine(offset=10, trim=True)
    plt.savefig("tmp/boxplot.png")

def plot():
    print('plot')


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
        elif func == 'plot':
            plot()
        else:
            print_options('Error: invalid function: {}'.format(func))
