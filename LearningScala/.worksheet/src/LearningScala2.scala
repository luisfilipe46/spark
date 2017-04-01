object LearningScala2 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(140); 
  // Flow control

  // If / else syntax
  if (1 > 3) println("Impossible!") else println("The world makes sense.");$skip(97); 

  if (1 > 3) {
    println("Impossible!")
  } else {
    println("The world makes sense.")
  };$skip(67); 

  // Matching - like switch in other languages:
  val number = 3;System.out.println("""number  : Int = """ + $show(number ));$skip(150); 
  number match {
    case 1 => println("One")
    case 2 => println("Two")
    case 3 => println("Three")
    case _ => println("Something else")
  };$skip(88); 

  // For loops
  for (x <- 1 to 4) {
    val squared = x * x
    println(squared)
  };$skip(32); 

  // While loops
  var x = 10;System.out.println("""x  : Int = """ + $show(x ));$skip(49); 
  while (x >= 0) {
    println(x)
    x -= 1
  };$skip(10); 

  x = 0;$skip(44); 
  do { println(x); x += 1 } while (x <= 10);$skip(102); val res$0 = 

  // Expressions
  // "Returns" the final value in a block automatically

  { val x = 10; x + 20 };System.out.println("""res0: Int = """ + $show(res$0));$skip(36); 

  println({ val x = 10; x + 20 });$skip(445); 

  // EXERCISE
  // Write some code that prints out the first 10 values of the Fibonacci sequence.
  // This is the sequence where every number is the sum of the two numbers before it.
  // So, the result should be 0, 1, 1, 2, 3, 5, 8, 13, 21, 34
  def fibonnaciNumber(numberIndex: Int): Int = {
    numberIndex match {
      case 0 => 0
      case 1 => 1
      case _ => fibonnaciNumber(numberIndex-1)+fibonnaciNumber(numberIndex-2)
    }
  };System.out.println("""fibonnaciNumber: (numberIndex: Int)Int""");$skip(20); 

  var counter = 0;System.out.println("""counter  : Int = """ + $show(counter ));$skip(91); 
  
  do {
    println(fibonnaciNumber(counter))
    counter += 1
  } while (counter < 10)}
}
