package quickstart.action

import xitrum.annotation.{GET, POST}
import com.codahale.jerkson.Json

case class Todo(done: Boolean, desc: String)
case class TodoList(todos: Seq[Todo])

@GET("/todo")
class TodoAction extends AppAction {
  override def execute {
    val todos = Seq(Todo(true, "Task1"), Todo(false, "Task2"))
    val model = TodoList(todos)

    koApplyBindings(model, urlFor[TodoUpdateAction], """
      $('#new_todo_form').submit(function() {
        if ($('#new_todo_form').valid()) {
          var desc = $('#new_todo_desc').val();
          $('#new_todo_desc').val('');
          var todo = {done: false, desc: desc};
          model.todos.push(ko.mapping.fromJS(todo));
        }
        return false;
      });

      $('#save').click(sync);
    """)

    renderView(
      <h1>Todo list</h1>

      <div id="todos" data-bind="template: {name: 'todo-template', foreach: todos}"></div>
      <form id="new_todo_form">
        <input id="new_todo_desc" type="text" class="required" />
        <input id="new_todo_add" type="submit" value="Add" />
      </form>
      <br />
      <input id="save" type="button" value="Save" />

      <script type="text/html" id="todo-template">
        <div>
          <input type="checkbox" data-bind="checked: done" />
          <!-- ko if: done -->
          <strike><span data-bind="text: desc"></span></strike>
          <!-- /ko -->
          <!-- ko ifnot: done -->
          <span data-bind="text: desc"></span>
          <!-- /ko -->
        </div>
      </script>

      <div>
        <br />
        <hr />
        <p>In this sample, the server side only has to work with Scala data structures:</p>
        <pre><code>case class Todo(done: Boolean, desc: String)
case class TodoList(todos: Seq[Todo])</code></pre>
      </div>
    )
  }
}

@POST("/todo")
class TodoUpdateAction extends AppAction {
  override def execute {
    val json   = param("model")
    val model1 = Json.parse[TodoList](json)
    val todos1 = model1.todos
    val todos2 = todos1.map { todo => Todo(!todo.done, todo.desc) }
    val model2 = TodoList(todos2)
    renderJson(model2)  // The model on the browser will be updated
  }
}
