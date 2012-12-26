package demos.controller

import xitrum.RequestVar
import xitrum.util.Json

// Controller ------------------------------------------------------------------

// Request var for passing data from action to Scalate view
object RVTodoList extends RequestVar[TodoList]

// For use in AppController.jade
object Todos extends Todos

class Todos extends AppController {
  def index = GET("todos") {
    val todoList = TodoList.get()
    RVTodoList.set(todoList)
    respondView()
  }

  def save = POST("todos") {
    val json     = param("model")
    val todoList = Json.parse[TodoList](json)
    TodoList.update(todoList)
    jsRespondFlash("Todo list has been saved")

    // From here, you can update the model on the browser like this:
    // val newTodoList = ...
    // respondJson(newTodoList)
    //
    // Whenever the model on the browser is updated, Knockout.js will automagically
    // update the UI!
  }
}

// Model -----------------------------------------------------------------------

case class Todo(done: Boolean, desc: String)
case class TodoList(todos: Seq[Todo])

object TodoList {
  private var storage = TodoList(Seq(Todo(true, "Task1"), Todo(false, "Task2")))

  def get() = storage
  def update(todoList: TodoList) { storage = todoList }
}
