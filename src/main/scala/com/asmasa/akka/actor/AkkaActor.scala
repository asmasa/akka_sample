package com.asmasa.akka.actor

import akka.actor.{ Actor, ActorSystem, Props }
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.Await

object AkkaActor extends App {
  val system = ActorSystem("Hayashishita")
  val props = Props[KiyoshiActor]
  val kiyoshi = system.actorOf(props)
  kiyoshi ! "きよしさん！"
  kiyoshi ! "返事きけ！"
}

class KiyoshiActor extends Actor {
  val arashi = context.actorOf(Props[ArashiActor], "Arashi")

  def receive = {
    case msg: String if "返事きけ！" == msg =>
      implicit val timeout = Timeout(5 seconds)
      val future = arashi ? "返事しろ！"
      val result = Await.result(future, timeout.duration).asInstanceOf[String]
      println(s"新志に言われた「${result}」")
    case msg: String => println(s"清志は言われた「${msg}」")
  }
}

class ArashiActor extends Actor {
  def receive = {
    case msg: String if "返事しろ！" == msg => sender ! "押忍！"
    case msg: String => println(s"新志は言われた「${msg}」")
  }
}