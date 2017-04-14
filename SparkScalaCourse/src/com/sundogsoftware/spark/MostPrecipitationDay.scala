package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object MostPrecipitationDay {
  def parseLine (line: String) = {
    val fields = line.split(",")
    val stationID = fields(0)
    val day = fields(1)//.substring(4, 8)
    val entryType = fields(2)
    val precipitation = fields(3).toFloat
    (stationID, day, entryType, precipitation)
  }
  
  def maxPrcpDay (t1: (String, Float), t2: (String, Float)) = {
    if (t1._2 > t2._2)
      t1
    else
      t2
  }

  def main(args: Array[String]) {
   
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "MostPrecipitationDay")
    
    // Read each line of input data
    val lines = sc.textFile("../1800.csv")
    
    // Convert to (stationID, day, entryType, precipitation) tuples
    val parsedLines = lines.map(parseLine)
    
    // Filter out all but PRCP entries
    val precipitation = parsedLines.filter(x => x._3 == "PRCP")
    
    // Convert to (stationID, (day, precipitation))
    val stationPrcp = precipitation.map(x => (x._1, (x._2, x._4)))
    
    // Reduce by stationID retaining the maximum precipitation level and day found
    val maxPrcpDayAndPrcpByStation = stationPrcp.reduceByKey( (x,y) => maxPrcpDay(x,y))
    
    // MapValues by stationID retaining the day with most precipitation level
    val maxPrcpDayByStation = maxPrcpDayAndPrcpByStation.mapValues(x => x._1)
    //val maxPrcpDayByStation = maxPrcpDayAndPrcpByStation
    
    // Collect, format, and print the results
    val results = maxPrcpDayByStation.collect()
    
    for (result <- results.sorted) {
       val station = result._1
       val prcpDay = result._2
       println(s"$station maximum precipitation day: $prcpDay") 
    }
      
  }
}