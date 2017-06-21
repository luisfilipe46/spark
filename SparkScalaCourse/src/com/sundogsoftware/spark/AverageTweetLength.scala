package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._

/** Listens to a stream of Tweets and keeps track of the most popular
 *  hashtags over a 5 minute window.
 */
object AverageTweetLength {
  
    /** Makes sure only ERROR messages get logged to avoid log spam. */
  def setupLogging() = {
    import org.apache.log4j.{Level, Logger}   
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)   
  }
  
  /** Configures Twitter service credentials using twiter.txt in the main workspace directory */
  def setupTwitter() = {
    import scala.io.Source
    
    for (line <- Source.fromFile("../twitter.txt").getLines) {
      val fields = line.split(" ")
      if (fields.length == 2) {
        System.setProperty("twitter4j.oauth." + fields(0), fields(1))
      }
    }
  }
  
  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Configure Twitter credentials using twitter.txt
    setupTwitter()
    
    // Set up a Spark streaming context named "PopularHashtags" that runs locally using
    // all CPU cores and one-second batches of data
    val ssc = new StreamingContext("local[*]", "PopularHashtags", Seconds(1))
    
    // Get rid of log spam (should be called after the context is set up)
    setupLogging()

    // Create a DStream from Twitter using our streaming context
    val tweets = TwitterUtils.createStream(ssc, None)
    
    // Now extract the text of each status update into DStreams using map()
    val tweetSizeAndCount = tweets.map(status => (status.getText().size, 1))
    
    // Now count them up over a 5 minute window sliding every one second
    val tweetsSizeAndCount = tweetSizeAndCount.reduceByWindow( (tuple1,tuple2) => (tuple1._1 + tuple2._1, tuple1._2 + tuple2._2),
        (tuple1,tuple2) => (tuple1._1 - tuple2._1, tuple1._2 - tuple2._2), Seconds(30), Seconds(1))
    
    val averageTweetSize = tweetsSizeAndCount.map(x => x._1 / x._2)
    
    
    // Sort the results by the count values
    tweetsSizeAndCount.print
    averageTweetSize.print
    
    // Set a checkpoint directory, and kick it all off
    // I could watch this all day!
    ssc.checkpoint("/home/luis/")
    ssc.start()
    ssc.awaitTermination()
  }  
}
