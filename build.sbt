ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "CdlCelTesting"
  )



//Global/concurrentRestrictions+=Tags.limit(Tags.Test,1)
//libraryDependencies += "org.scalatest" % "scalatest-app_native0.4_2.12" % "3.2.12"
//libraryDependencies += "commons-io" % "commons-io" % "2.11.0"



libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.2.1"
libraryDependencies +=  "org.apache.spark" %% "spark-sql" % "3.2.1"

assemblyMergeStrategy in assembly := {
   case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
   case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
   case PathList("org", "apache", xs @ _*) => MergeStrategy.last
   case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
   case PathList("com", "google", xs @ _*) => MergeStrategy.last
   case x if x.startsWith("META-INF/maven/org.slf4j/") => MergeStrategy.last
   case x if x.contains(".html") => MergeStrategy.last
   case x if x.contains(".thrift") => MergeStrategy.last
   case x if x.contains(".xml") => MergeStrategy.last
   case x if x.contains(".tooling") => MergeStrategy.last
   case x if x.contains("changelog.txt") => MergeStrategy.last
   case x if x.contains(".class") => MergeStrategy.first
   case x if x.contains(".txt") => MergeStrategy.last
   case x if x.contains(".DS_Store") => MergeStrategy.last
   case x if x.contains("mime") => MergeStrategy.last
   case x if x.contains("org.apache.lucene") => MergeStrategy.last
   case x if x.contains(".fmpp") => MergeStrategy.first
   case x if x.contains(".json") => MergeStrategy.first
   case x if x.contains("tz/data") => MergeStrategy.first
   case "about.html" => MergeStrategy.rename
   case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
}

//libraryDependencies ++= Seq(
//   ("org.apache.spark"%%"spark-core"%"3.2.1").
//      exclude("org.eclipse.jetty.orbit", "javax.servlet").
//      exclude("org.eclipse.jetty.orbit", "javax.transaction").
//      exclude("org.eclipse.jetty.orbit", "javax.mail").
//      exclude("org.eclipse.jetty.orbit", "javax.activation").
//      exclude("commons-beanutils", "commons-beanutils-core").
//      exclude("commons-collections", "commons-collections").
//      exclude("commons-collections", "commons-collections").
//      exclude("com.esotericsoftware.minlog", "minlog")
//)



//val defaultMergeStrategy: String => MergeStrategy = {
//   case x if Assembly.isConfigFile(x) =>
//      MergeStrategy.concat
//   case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
//      MergeStrategy.rename
//   case PathList("META-INF", xs @ _*) =>
//      (xs map {_.toLowerCase}) match {
//         case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
//            MergeStrategy.discard
//         case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
//            MergeStrategy.discard
//         case "plexus" :: xs =>
//            MergeStrategy.discard
//         case "services" :: xs =>
//            MergeStrategy.filterDistinctLines
//         case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
//            MergeStrategy.filterDistinctLines
//         case _ => MergeStrategy.deduplicate
//      }
//   case _ => MergeStrategy.deduplicate
//}

//libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.0" % "provided"


