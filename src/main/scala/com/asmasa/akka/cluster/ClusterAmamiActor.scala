package com.asmasa.akka.cluster

import akka.actor.{ Actor, ActorContext, ActorSystem, Props, RootActorPath }
import akka.cluster.{ Cluster, Member, MemberStatus }
import akka.cluster.ClusterEvent.{ CurrentClusterState, MemberUp }
import com.typesafe.config.ConfigFactory

object ClusterAmamiActor extends App {

  val config = ConfigFactory.parseString("""
akka {
  actor {
    provider = "akka.cluster.ClusterActorRefProvider"
  }
  remote {
    netty.tcp {
      hostname = "127.0.0.1"
      port = 8110
    }
  }	
  cluster {
    seed-nodes = [
      "akka.tcp://Hayashishita@127.0.0.1:8100",
      "akka.tcp://Hayashishita@127.0.0.1:8110"
    ]
    roles = [bekkyo]
  }
}
""")

  val system = ActorSystem("Hayashishita", config)

  val atsushi = system.actorOf(Props[AtsushiActor], "atsushi")
  val musashi = system.actorOf(Props[MusashiActor], "musashi")
}

class AtsushiActor extends Actor with Register {
  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case msg: String => println(s"熱志は言われた。「${msg}」")
    case MemberUp(member) => register(member, context)
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach (register(_, context))
  }
}

class MusashiActor extends Actor with Register {
  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case msg: String => println(s"武志は言われた。「${msg}」")
    case MemberUp(member) => register(member, context)
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach (register(_, context))
  }
}

trait Register {
  def register(member: Member, context: ActorContext): Unit =
    if (member.hasRole("doukyo")) {
      val actorPath = RootActorPath(member.address) / "user" / "doukyo"
      context.actorSelection(actorPath) ! AmamiRegistration
    }
}
