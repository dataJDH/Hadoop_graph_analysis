package edu.gatech.cse6242;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Q1 {

/* mapper class for each src,tgt wt - spit out tgt, wgt if wgt > 0 */
  public static class EdgeWeightMapper
       extends Mapper<Object, Text, Text, Text>{

    Text textKey = new Text();
    Text textValue = new Text();

    @Override
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {


      String line = value.toString();
      String[] field = line.split("\t");
      if (field.length == 3) {
	int i = Integer.parseInt(field[1]);
	int f = Integer.parseInt(field[2]);
	String v = String.valueOf(i) + "," + String.valueOf(f);
	
	textKey.set(field[0]);
	textValue.set(v);

	context.write(textKey, textValue);

      }
    }
   }


  /* 
   * mapper class to sum the incoming edge weights of the target nodes as key
   *   
   */
  public static class EdgeWeightReducer
       extends Reducer<Text, Text, Text, Text> {
    Text textValue = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
    int max = 0;
    int node2 = 999999999;

      for (Text value : values) {

       String line = value.toString();
       
       String[] field = line.split(",");
       
       int i = Integer.parseInt(field[0]);
       int f = Integer.parseInt(field[1]);
	
       if (f >= max) {
	if(f > max) {  
	  max = f;
	  node2 = i;
        }
	if(f == max) {  
	  if(i < node2){
	  node2 = i;
	  }
        }
       }
      }
    String v = String.valueOf(node2) + "," + String.valueOf(max);
    textValue.set(v);
    context.write(key, textValue);
  }
 }




  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    //conf.set("mapreduce.output.textoutputformat.separator", "\t");


    Job job = Job.getInstance(conf, "Q1");

    job.setJarByClass(Q1.class);
    job.setMapperClass(EdgeWeightMapper.class);
    //job.setCombinerClass(EdgeWeightReducer.class);
    job.setReducerClass(EdgeWeightReducer.class);
    //job.setInputFormatClass(TextInputFormat.class);
    //job.setOutputFormatClass(TextOutputFormat.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
