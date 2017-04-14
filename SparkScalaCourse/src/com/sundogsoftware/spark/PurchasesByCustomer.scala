package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object PurchasesByCustomer {
  
  def parseLine(line: String) = {
     val fields = line.split(",")
     val customerID = fields(0).toInt
     val value = fields(2).toFloat
     (customerID, value)
  }

  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using the local machine
    val sc = new SparkContext("local", "PurchasesByCustomer")
    
    // Load each line of purchases into an RDD
    val lines = sc.textFile("../customer-orders.csv")
    
    val parsedLines = lines.map(parseLine)
    
    val amountSpendByCustomerID = parsedLines.reduceByKey((x, y) => x + y)
    
    val result = amountSpendByCustomerID.collect()
    
    result.foreach(println)
  }

}