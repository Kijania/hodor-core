//package com.core.controllers
//
//import akka.actor.Actor
//import com.core.controllers.EventCtrl._
//import com.core_api.Event
//import com.github.nscala_time.time.Imports._
//
//class EventCtrl extends Actor {
//
//  override def receive: Receive = {
//    case GetAll =>
//      List(Event("", DateTime.now))
//    case g: GetEvent =>
//      Event(g.name, DateTime.now)
//    case e: EditEvent =>
//      e
//    case a: AddEvent =>
//      a
//    case d: DeleteEvent =>
//      Event(d.name, DateTime.now)
//  }
//}
//
//object EventCtrl {
//  case object GetAll
//  case class GetEvent(name: String)
//  case class EditEvent(event: Event)
//  case class AddEvent(event: Event)
//  case class DeleteEvent(name: String)
//}
