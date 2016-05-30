package demos.action

import xitrum.RequestVar
import xitrum.annotation.{GET, POST}
import xitrum.util.SeriDeseri

// Actions ---------------------------------------------------------------------

// Request var for passing data from action to Scalate view
object RVTodoList extends RequestVar[TodoList]

@GET("todos")
class TodosIndex extends AppAction {
  def execute() {
    val todoList = TodoList.get()
    RVTodoList.set(todoList)
    respondView()
  }
}

@POST("todos")
class TodosSave extends AppAction {
  def execute() {
    val json     = param("model")
    val todoList = SeriDeseri.fromJson[TodoList](json).get

    // You can update the model on the browser like this:
    // val newTodoList = ...
    // respondJson(newTodoList)
    //
    // Whenever the model on the browser is updated, Knockout.js will automagically
    // update the UI!

    TodoList.update(todoList)
    jsRespondFlash("Todo list has been saved")
  }
}

// Model -----------------------------------------------------------------------

case class Todo(done: Boolean, desc: String)
case class TodoList(todos: Seq[Todo])

object TodoList {
  private var storage = TodoList(Seq(Todo(done = true, "Task1"), Todo(done = false, "Task2")))

  def get() = storage
  def update(todoList: TodoList) { storage = todoList }
}
