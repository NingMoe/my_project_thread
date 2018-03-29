name := """elite-cloud-interface"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  jdbc,
  filters,
  "org.mybatis" % "mybatis" % "3.3.0",
  "org.mybatis" % "mybatis-guice" % "3.6",
  "mysql" % "mysql-connector-java" % "5.1.18",
  "com.google.inject.extensions" % "guice-multibindings" % "4.0",
  "com.alibaba" % "fastjson" % "1.2.20",
  "aopalliance"%"aopalliance"%"1.0",
  "commons-httpclient" % "commons-httpclient" % "3.0.1",
  "org.jdom" % "jdom" % "1.1.3",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.apache.httpcomponents" % "httpcore" % "4.4.5",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.6",
  "org.jdom" % "jdom" % "1.1.3",
  "com.squareup.okhttp3" % "okhttp" % "3.5.0",
  "org.mockito" % "mockito-all" % "1.10.19",
  "com.hht" % "hht-core" % "1.2.0-SNAPSHOT",
  "com.thoughtworks.xstream" % "xstream" % "1.4.9",
  "org.apache.xmlbeans" % "xmlbeans" % "2.6.0",
  "org.apache.poi" % "poi" % "3.9",
  "org.apache.poi" % "poi-ooxml" % "3.9",
  "org.apache.poi" % "poi-ooxml-schemas" % "3.9",
  "com.aliyun.oss" % "aliyun-sdk-oss" % "2.5.0",
  "org.apache.dom4j" % "dom4j" % "1.6.1",
  "org.commons" % "commons-io" % "2.2",
  "com.aliyun.oss"%"aliyun-sdk-oss"%"2.5.0",
  "org.apache.tika" % "tika-core" % "1.14"

)
routesGenerator := InjectedRoutesGenerator