
package cdlceltest

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object CdlCelTest {


   def main(args: Array[String]): Unit =
   {

      val spark = SparkSession
         .builder()
         .appName("testing")
         //.config("spark.some.config.option", "some-value")
         .master("local[*]")
         .getOrCreate()

      //      import org.apache.log4j.PropertyConfigurator
      //      val log4jConfPath = "/home/shuvam/Downloads/log4j.properties"
      //      PropertyConfigurator.configure(log4jConfPath)
      //      println("Main class imports running!")

      var path = "/home/shuvam/Downloads/cdlcel.parquet"
      val df = spark.read.parquet("/home/shuvam/Downloads/cdlcel.parquet")
      df.printSchema()
      println(df.agg(max("rank")).head().getInt(0))
      //      rankLimit(df, spark)

   }
   //
      def rankLimit(df1: DataFrame): Boolean = {

         println("Running rank limit:")
         val listValues: List[Int] = df1.select("rank").collect.map(f => f.getInt(0)).toList
         val mx = listValues.max
   //      val mx = df1.select("max").agg(max("rank")).as("rank").head().getInt(0)

         mx <= 3

      }

   def verify(df1: DataFrame, colname: String): Boolean = {

      var col1 = ""
      var col2 = ""
      var col3 = ""
      var col4 = ""
      if (colname == "cdl_last_seen") {
         col1 = "cdl_num_days"
         col2 = "cdl_brq"
         col3 = "weighted_cdl_num_days"
         col4 = "weighted_cdl_brq"
      }
      else if (colname == "cel_last_seen") {
         col1 = "cel_num_days"
         col2 = "cel_brq"
         col3 = "weighted_cel_num_days"
         col4 = "weighted_cel_brq"
      }
      else if (colname == "mostviewed_last_seen") {
         col1 = "mostviewed_num_days"
         col2 = "mostviewed_brq"
         col3 = "weighted_mostviewed_num_days"
         col4 = "weighted_mostviewed_brq"
      }


      val df = df1.na.drop(Seq("cdl_last_seen", "cdl_num_days", "cdl_brq", "weighted_cdl_num_days", "weighted_cdl_brq", "cel_num_days", "cel_brq", "weighted_cel_num_days",
         "weighted_cel_brq", "mostviewed_last_seen", "mostviewed_num_days", "mostviewed_brq", "weighted_mostviewed_num_days", "weighted_mostviewed_brq"))

      val listValues = df.select(colname).collect.map(f => f.getString(0)).toList
      val num_days = df.select(col1).collect.map(f => f.getString(0)).toList
      val brq = df.select(col2).collect.map(f => f.getString(0)).toList
      val weighted_num_days = df.select(col3).collect.map(f => f.getString(0)).toList
      val weighted_brq = df.select(col4).collect.map(f => f.getString(0)).toList

      var i = 0

      while (i < listValues.size) {
         //Will avoid null and empty values
         while ((listValues(i) == null || listValues(i) == "" || num_days(i) == null || num_days(i) == "" || brq(i) == null || brq(i) == "" ||
            weighted_num_days(i) == null || weighted_num_days(i) == "" || weighted_brq(i) == null || weighted_brq(i) == "") && i < listValues.size) {
            i = i + 1
         }
         val start = LocalDate.parse(listValues(i))
         val end = LocalDate.parse("2022-04-30")
         val x = ChronoUnit.DAYS.between(start, end).toInt
         val y = scala.math.pow(0.99, x)
         val val1 = y * (num_days(i).toDouble)
         val target = weighted_num_days(i).toDouble

         if (target != val1) return false
      }
      true
   }



   def ranksPresent(df1: DataFrame): Boolean = {
      println("Ranks Present Check: ")
      //GroupBy on multiple columns
      //      val df = df1.withColumn("rank",col("rank").cast(IntegerType))
      //      val df1 = spark.read.format("csv").option("header","true").load("/home/shuvam/Downloads/DateTime2.csv")
      val rankListDf = df1.groupBy("ifa").agg(collect_list("rank"))
      //      rankListDf.show()

      val SortedDF = rankListDf.withColumn("sortedRanks", sort_array(col("collect_list(rank)")))
      SortedDF.show()
      val flag = SortedDF.rdd.mapPartitions {
         rows =>
            val result = rows.map { row =>
               val list = row.getList(1)
               var RES = true
               if (list.size() == 1 && list.get(0) != 1) RES = false
               else if (list.size() == 2 && (list.get(0) != 1 || list.get(1) != 2)) RES = false
               else if (list.size() == 3 && (list.get(0) != 1 || list.get(1) != 2 || list.get(2) != 3)) RES = false
               RES
            }
            result
      }
      val isValid = flag.min()
      isValid

   }

   def geohashDensity(df1: DataFrame, colname: String, threshold: Long): Boolean = {
      //      val df1 = spark.read.format("csv").option("header","true").load("/home/shuvam/Downloads/DateTime3.csv")
      val x = df1.groupBy(colname).agg(countDistinct("ifa"))
      x.show()
      val df_withoutNull = df1.na.drop(Seq(colname, "ifa"))
      val df = df_withoutNull.groupBy(colname).agg(countDistinct("ifa")) //Verify whether to use count Distinct or not.
      df.show()
      val mx = df.select("count(ifa)").agg(max("count(ifa)")).head().getLong(0)
      mx <= threshold
   }



}
