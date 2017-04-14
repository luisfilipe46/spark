package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._


object FriendsByFirstName {
  def parseLine(line: String) : (String, Int) = {
    val fields = line.split(",")
    
    val name = fields(1)
    val numFriends = fields(3).toInt
    
    return (name, numFriends)
  }
  
  def main (args: Array[String]) {
    
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sc = new SparkContext("local[*]", "FriendsByFirstName")
    
    val lines = sc.textFile("../fakefriends.csv")
    
    val rdd = lines.map(parseLine)
    
    val totalsByName = rdd.mapValues(x => (x, 1)).reduceByKey((v1, v2) => (v1._1 + v2._1, v1._2 + v2._2))
    
    val averageByName = totalsByName.mapValues(x => x._1 / x._2)
        
    // Collect the results from the RDD (This kicks off computing the DAG and actually executes the job)
    val results = averageByName.collect()
    
    // Sort and print the final results.
    results.sorted.foreach(println)
  }
}