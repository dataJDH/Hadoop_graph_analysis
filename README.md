# Hadoop_graph_analysis
Analyze graph with source, target and weight using Hadoop/Java

In the directory, you will find `pom.xml`, `run1.sh`, and the src directory.

The src directory contains a main Java file that you will primarily work on. We have provided some code to help you get started. Feel free to edit it and add your files in the directory, but the main class should be Q1. Your code will be evaluated using the provided `run1.sh` file (details below).

`pom.xml` contains the necessary dependencies and compile configurations for each question. To compile, you can simply call Maven in the corresponding directory (i.e., where `pom.xml` exists) by this command:

    `mvn package`

It will generate a single JAR file in the target directory (i.e., `target/q1-1.0.jar`).

`run1.sh` is the shell script file that runs the code over `graph1.tsv` and download the output to a local directory. The output files are named based on its question number and graph number (e.g. `q1output1.tsv`). You can use these run scripts to test your code. Note that these scripts will be used in grading. 

To execute the shell scripts from the command prompt, enter:

`hadoop fs -mkdir data`

`hadoop fs -put ~/graph1.tsv data`

`hadoop fs -ls data`

`mvn package`

`./run1.sh`

Here’s what the above scripts do:

Run your JAR on Hadoop specifying the input file on HDFS (the first argument) and output directory on HDFS (the second argument)
Merge outputs from output directory and download to local file system.
Remove the output directory on HDFS.


The network in `graph1.tsv` is organized as a directed graph with a source node, target node and weight. The code is a MapReduce program in Java made from scratch to report, for each node X (the “source”, or “src” for short) in the graph, the node Y (the “target” or “tgt” for short) that X is connected to the most, and theoutbound “weight”, from X to Y). If the source is connected to multiple targets that have exactly the same (largest) number of connections,  return the target with smallest node id.

`graph1.tsv` Each line represents a single edge consisting of three columns: (source node ID, target node ID, edge weight), each of which is separated by a tab (\t). Node IDs and weights are positive integers. Below is a small toy graph, for illustration purposes (on your screen, the text may appear out of alignment).

src        tgt        weight

10        110        3

10        200        1

200        150        30

100        110        10

110        130        15

110        200        67

10        70        3

The program does not assume the edges to be sorted or ordered in any ways (i.e., your program works even when the edge ordering is random).

The code accepts two arguments. The first argument (args[0]) is the path for the input graph file on HDFS (e.g., data/graph1.tsv), and the second argument (args[1]) is the path for output directory on HDFS (e.g., data/q1output1). The default output mechanism of Hadoop will create multiple files on the output directory such as part-00000, part-00001, which will be merged and downloaded to a local directory by the supplied run script.

The format of the output: each line contains a node ID, followed by a tab (\t), and the expected “target node ID,weight” tuple (without the quotes; and note there is no space character before and after the comma). The following example result is computed based on the toy graph above. Nodes that do not have outgoing edges are excluded.

For the toy graph above, the output is as follows.

10        70,3

200        150,30

100        110,10

110        200,67
