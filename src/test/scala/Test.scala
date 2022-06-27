import cdlceltest.CdlCelTest
import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite


class Test extends AnyFunSuite {

   val spark = SparkSession
      .builder()
      .appName("testing")
      //.config("spark.some.config.option", "some-value")
      .master("local[*]")
      .getOrCreate()

   val path = "/home/shuvam/Downloads/cdlcel.parquet"
   val df = spark.read.parquet(path)

   //   def main(args: Array[String]): Unit = {


   test("Test for validating cdl formulas"){
      assert(CdlCelTest.verify(df,"cdl_last_seen") ==true,"error in cdl_last_seen")
   }
   test("Test for validating cel formulas"){
      assert(CdlCelTest.verify(df,"cel_last_seen") ==true,"error in cel_last_seen")
   }
   test("Test for validating most-viewed formulas"){
      assert(CdlCelTest.verify(df,"mostviewed_last_seen") ==true,"error in mostviewed_last_seen")
   }

         test("Test for Rank Limit") {


            assert(CdlCelTest.rankLimit(df) ,"ranks should be less than 4") //2
         }

   test("Test for Rank Validity ") {

      assert(CdlCelTest.ranksPresent(df), "ranks are not in valid order") //2
   }

   //   }





}

