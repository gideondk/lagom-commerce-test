organization in ThisBuild := "com.sting"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lazy val itemApi = project("item-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslImmutables,
      lagomJavadslJackson
    )
  )

lazy val skuPusherApi = project("sku-pusher-api")
  .settings(version := "1.0-SNAPSHOT")
  .dependsOn(itemApi)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslImmutables,
      lagomJavadslKafkaBroker,
      lagomJavadslJackson
    )
  )


lazy val itemImpl = project("item-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomJavadslImmutables,
      lagomJavadslJackson,
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaClient,
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(itemApi)
  .dependsOn(skuPusherApi)

lazy val skuPusherImpl = project("sku-pusher-impl")
  .settings(version := "1.0-SNAPSHOT")
  .enablePlugins(LagomJava)
  .dependsOn(skuPusherApi)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslImmutables,
      lagomJavadslJackson,
      lagomJavadslKafkaBroker,
      lagomJavadslTestKit
    )
  )


def project(id: String) = Project(id, base = file(id))
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))
  .settings(jacksonParameterNamesJavacSettings: _*) // applying it to every project even if not strictly needed.


// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)