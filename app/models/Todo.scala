package models

import play.api.libs.json._
import slick.jdbc.PostgresProfile.api._

case class Todo(id: Option[Long], title: String, completed: Boolean)

object Todo {
  implicit val todoFormat: OFormat[Todo] = Json.format[Todo]
  def tupled: ((Option[Long], String, Boolean)) => Todo = (apply _).tupled
}

class TodoTable(tag: Tag) extends Table[Todo](tag, "todos") {
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title: Rep[String] = column[String]("title")
  def isCompleted: Rep[Boolean] = column[Boolean]("is_completed")

  def * = (id.?, title, isCompleted) <> (Todo.tupled, Todo.unapply)
}

object TodoTable {
  val todos = TableQuery[TodoTable]
}