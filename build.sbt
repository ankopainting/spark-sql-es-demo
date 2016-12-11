
name := "spark-sql-es-demo"

version := "1.0"

scalaVersion := "2.11.4"

scalaVersion in ThisBuild := "2.11.4"

// https://mvnrepository.com/artifact/org.scala-lang/scala-library
libraryDependencies += "org.scala-lang" % "scala-library" % "2.12.1"

// https://mvnrepository.com/artifact/org.scala-lang/scala-xml
libraryDependencies += "org.scala-lang" % "scala-xml" % "2.11.0-M4"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.10
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.2"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql_2.10
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.0.2"

// https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-20_2.11
// this won't work with elasticsearch 5.1.x
//libraryDependencies += "org.elasticsearch" % "elasticsearch-spark-20_2.11" % "5.0.2"

// https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-20_2.11
libraryDependencies += "org.elasticsearch" % "elasticsearch-spark-20_2.11" % "5.1.1"
