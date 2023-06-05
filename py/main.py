
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

def plot():
    print('plot')



if __name__ == "__main__":
    if len(sys.argv) < 2:
        print_options('Error: no command-line args')
    else:
        func = sys.argv[1].lower()
        if func == 'penguins':
            penguins()
        elif func == 'plot':
            plot()
        else:
            print_options('Error: invalid function: {}'.format(func))
