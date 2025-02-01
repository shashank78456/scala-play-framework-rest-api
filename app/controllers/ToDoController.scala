package controllers

import javax.inject._
import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.ToDoService
import models.Todo

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ToDoController @Inject()(controllerComponents: ControllerComponents, todoService: ToDoService)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

  def getAll(): Action[AnyContent] = Action.async { implicit request =>
    todoService.getAll().map { todos =>
      Ok(Json.toJson(todos))
    }
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    todoService.getById(id).map {
      case Some(todo) => Ok(Json.toJson(todo))
      case None => NotFound(Json.obj("error" -> "Todo not found"))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Todo].fold(
      _ => Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON"))),
      todo => {
        todoService.create(todo).map { createdTodo =>
          Created(Json.toJson(createdTodo))
        }
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Todo].fold(
      _ => Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON"))),
      todo => {
        todoService.update(id, todo).map {
          case 0 => NotFound(Json.obj("error" -> "Todo not found"))
          case _ => Ok(Json.obj("message" -> "Todo updated"))
        }
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    todoService.delete(id).map {
      case 0 => NotFound(Json.obj("error" -> "Todo not found"))
      case _ => Ok(Json.obj("message" -> "Todo deleted"))
    }
  }
}
