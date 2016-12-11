
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import org.elasticsearch.spark
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.EsSpark

import org.apache.spark.sql.SQLContext    
import org.apache.spark.sql.SQLContext._

import org.elasticsearch.spark.sql._

import org.apache.spark.sql._

import java.io._
/*

This script sets up spark, 
connects to a local mysql with test data from https://github.com/datacharmer/test_db/,
creates some temporary spark tables
exports the titles table to elasticsearch
then does an sql query which joins between the employees table in mysql and the titles table in elasticsearch

*/

object Setup {
    def delete(file: File): Array[(String, Boolean)] = {
        Option(file.listFiles).map(_.flatMap(f => delete(f))).getOrElse(Array()) :+ (file.getPath -> file.delete)
    }

    def main(args: Array[String]): Unit = {
    /*
    
    make sure you have mysql and elasticsearch installed

    brew install mysql elasticsearch

    NOTE: you must have elasticsearch 5.1.x

    Next download the test data @ https://github.com/datacharmer/test_db/

    mysql -u root < employees.sql

    */

    // gets a spark session
        val spark = SparkSession
            .builder()
            .appName("Spark SQL basic example")
            .master("local")
            .getOrCreate()

        println("Loading employeess table as a tempory view inside spark sql")
        val employees = spark
            .read
            .option("url", "jdbc:mysql://localhost:3306/employees") 
            .option("dbtable", "employees")
            .option("user", "root")
            .option("password", "")
            .format("jdbc")
            .load

        employees.createOrReplaceTempView("employees")

        println("showing schema for employees table")
        employees.printSchema()

        val newEmployees = spark.sql("select * from employees where birth_date > '1960-01-01'")

        newEmployees.show()

        val titles = spark
            .read
            .option("url", "jdbc:mysql://localhost:3306/employees") 
            .option("dbtable", "titles")
            .option("user", "root")
            .option("password", "")
            .format("jdbc")
            .load
        titles.createOrReplaceTempView("titles")

        val titles_query = spark.sql("select * from titles")
        titles_query.show()

        titles_query.saveToEs("employees/titles", Map("es.mapping.id" -> "emp_no"))

        val esTitles = spark.read.format("org.elasticsearch.spark.sql").load("employees/titles")
        esTitles.createOrReplaceTempView("es_titles")

        // running a join between mysql and elasticsearch
        val merged = spark
            .sql("select employees.*, es_titles.* from employees inner join es_titles ON (es_titles.emp_no = employees.emp_no) WHERE es_titles.title = 'Senior Engineer'")
            
        println("merged")

        // we need to do this to remove one of the emp_no columns.  Otherwise json export won't work.
        val merged2 = merged.drop(merged.col("es_titles.emp_no"))
        println(merged2.columns.mkString(", "))
        merged2.printSchema()
        merged2.show()

        delete(new File("./output/"))
        merged2.coalesce(1)
            .write.format("json")
            .option("header", "true")
            .save("output")

        spark.stop()

    }
}
