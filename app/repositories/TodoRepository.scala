package repositories

import models.{Todo, TodoTable}
import slick.jdbc.PostgresProfile.api._
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
class TodoRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) {
  private val db = dbConfigProvider.get[JdbcProfile].db

  def getAll: Future[Seq[Todo]] = db.run(TodoTable.todos.result)

  def getById(id: Long): Future[Option[Todo]] = db.run(TodoTable.todos.filter(_.id === id).result.headOption)

  def create(todo: Todo): Future[Todo] = db.run(TodoTable.todos returning TodoTable.todos.map(_.id) into ((todo, id) => todo.copy(id = Some(id))) += todo)

  def update(id: Long, todo: Todo): Future[Int] = db.run(TodoTable.todos.filter(_.id === id).update(todo))

  def delete(id: Long): Future[Int] = db.run(TodoTable.todos.filter(_.id === id).delete)
}
