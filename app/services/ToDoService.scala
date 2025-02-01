package services

import javax.inject.{Inject, Singleton}
import models.Todo
import repositories.TodoRepository
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ToDoService @Inject()(todoRepository: TodoRepository)(implicit executionContext: ExecutionContext) {
  def getAll(): Future[Seq[Todo]] = todoRepository.getAll()

  def getById(id: Long): Future[Option[Todo]] = todoRepository.getByid(id)

  def create(todo: Todo): Future[Todo] = todoRepository.create(todo)

  def update(id: Long, todo: Todo): Future[Int] = todoRepository.update(id, todo)

  def delete(id: Long): Future[Int] = todoRepository.delete(id)
}
