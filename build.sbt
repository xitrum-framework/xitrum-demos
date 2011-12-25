organization := "my.organization"

name         := "xitrum-quickstart"

version      := "1.0-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked"
)

// Remove this when Netty 4 is released (this must be put before Xitrum below)
libraryDependencies += "io.netty" % "netty" % "4.0.0.Alpha1-SNAPSHOT" from "http://cloud.github.com/downloads/ngocdaothanh/xitrum/netty-4.0.0.Alpha1-SNAPSHOT.jar"

// Xitrum uses Jerkson: https://github.com/codahale/jerkson
resolvers += "repo.codahale.com" at "http://repo.codahale.com"

libraryDependencies += "tv.cntt" %% "xitrum" % "1.7"

autoCompilerPlugins := true

addCompilerPlugin("tv.cntt" %% "xitrum-xgettext" % "1.1")

// An implementation of SLF4J must be provided for Xitrum
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.0"

// For "sbt console"
unmanagedClasspath in Compile <+= (baseDirectory) map { bd => Attributed.blank(bd / "config") }

// For "sbt run"
unmanagedClasspath in Runtime <+= (baseDirectory) map { bd => Attributed.blank(bd / "config") }
