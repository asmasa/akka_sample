package com.asmasa.akka.remote

import akka.actor.{ Actor, ActorSystem, ActorPath, Props }
import com.typesafe.config.ConfigFactory

object RemoteClientActor extends App {
  val config = ConfigFactory.parseString("""
akka {
  actor {
    provider = "akka.remote.RemoteActorRefProvider"
  }
}
""")

  val system = ActorSystem("Hayashishita", config)

  val kiyoshi = system.actorOf(Props[KiyoshiActor])
  kiyoshi ! "美奈子に謝れ！"
}

class KiyoshiActor extends Actor {
  val path = ActorPath.fromString("akka.tcp://Hayashishita@127.0.0.1:8100/user/minako")
  val minako = context.actorSelection(path)

  def receive = {
    case msg: String =>
      println(s"清志は言われた。「${msg}」")
      minako ! "美奈子、スマン！"
  }
}