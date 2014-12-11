package com.asmasa.akka.remote

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object RemoteServerActor extends App {
  val config = ConfigFactory.parseString("""
akka {
  actor {
    provider = "akka.remote.RemoteActorRefProvider"
  }
  remote {
    netty.tcp {
      hostname = "127.0.0.1"
      port = 8100
    }
 }	
}
""")

  val system = ActorSystem("Hayashishita", config)
  val props = Props[MinakoActor]

  val atsushi = system.actorOf(props, "minako")
}

class MinakoActor extends Actor {
  def receive = {
    case msg: String => println(s"美奈子は言われた。「${msg}」")
  }
}