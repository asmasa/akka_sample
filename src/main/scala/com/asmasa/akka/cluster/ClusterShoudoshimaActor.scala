package com.asmasa.akka.cluster

import akka.actor.{ Actor, ActorSystem, ActorPath, Props }
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.ActorRef
import akka.actor.Terminated

object ClusterShoudoshimaActor extends App {
  val config = ConfigFactory.parseString("""
akka {
  actor {
    provider = "akka.cluster.ClusterActorRefProvider"
  }
  remote {
    netty.tcp {
      hostname = "127.0.0.1"
      port = 8100
    }
  }	
  cluster {
    seed-nodes = [
      "akka.tcp://Hayashishita@127.0.0.1:8100",
      "akka.tcp://Hayashishita@127.0.0.1:8110"
    ]
    roles = [doukyo]
  }
}
""")

  val system = ActorSystem("Hayashishita", config)
  val kiyoshi = system.actorOf(Props[KiyoshiActor], name = "kiyoshi")

  import system.dispatcher

  system.scheduler.schedule(0.seconds, 5.seconds) {
    kiyoshi ! "奄美に連絡しろ！"
  }
}

class KiyoshiActor extends Actor {
  var amamies = IndexedSeq.empty[ActorRef]
  var counter = 0

  def receive = {
    case AmamiRegistration if !amamies.contains(sender) =>
      context watch sender
      amamies :+ sender
    case Terminated(sender) => amamies = amamies.filterNot(_ == sender)
    case msg: String if amamies.isEmpty => sender ! Failed("No amami!")
    case msg: String =>
      println(s"清志は言われた。「${msg}」")
      counter += 1
      amamies(counter % amamies.size) forward "熱志、元気か！"
  }
}