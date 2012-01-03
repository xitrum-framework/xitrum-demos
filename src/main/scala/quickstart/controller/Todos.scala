package quickstart.controller

import com.codahale.jerkson.Json
import xitrum.RequestVar

case class Todo(done: Boolean, desc: String)
case class TodoList(todos: Seq[Todo])

object RVTodoList extends RequestVar[TodoList]

object Todos extends Todos

class Todos extends AppController {
  val index = GET("todos") {
    val todos = Seq(Todo(true, "Task1"), Todo(false, "Task2"))
    RVTodoList.set(TodoList(todos))
    renderView()
  }

  val save = POST("todos") {
    val json      = param("model")
    val todoList1 = Json.parse[TodoList](json)
    val todos1    = todoList1.todos

    val todos2    = todos1.map { todo => Todo(!todo.done, todo.desc) }
    val todoList2 = TodoList(todos2)
    renderJson(todoList2)  // The model on the browser will be updated
  }
}
