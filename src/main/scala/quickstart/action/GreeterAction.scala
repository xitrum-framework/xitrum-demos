package quickstart.action

import scala.xml.NodeBuffer
import xitrum.imperatively.ImperativelyXitrum
import xitrum.imperatively.SessionHolder
import xitrum.annotation.GET

@GET("/greeter")
class GreeterAction extends AppAction {

  val greeter = new Greeter

  override def execute {
    SessionHolder.set(session)
    val params: Map[String, String] = textParams.filterNot(x => x._2.isEmpty).map(x => (x._1, x._2.head)).toMap
    renderView(greeter.nextStep(params))
  }
}

class Greeter extends ImperativelyXitrum {

  def workflow(): NodeBuffer @imp = {
    val (name, age) = getNameAndAge()
    val book = getBook()

    <h1>Greeter</h1>
    <div>Hello { name }, you are { age } years old and your favorite book is { book }!</div>
  }

  def getNameAndAge(params: Map[String, String] = Map.empty): (String, String) @imp = {

    var inputs: Map[String, String] = Map.empty
    var errors: Map[String, String] = Map.empty
    
    var name: String = ""
    var age: String = ""
   
    validateName(params) match {
      case Left(fields) => inputs = inputs ++ fields.inputs; errors = errors ++ fields.errors
      case Right(_name)  => name = _name
    }

    validateAge(params) match {
      case Left(fields) => inputs = inputs ++ fields.inputs; errors = errors ++ fields.errors
      case Right(_age)  => age = _age
    }
    
    if (inputs.size > 0) getNameAndAge(params ++ prompt(form(inputs, errors)))
    else (name, age)
  }
  
  case class Fields(inputs: Map[String, String], errors: Map[String, String])
  
  def validateName(params: Map[String, String] = Map.empty): Either[Fields, String] =
    params.get("Your name") match {
      case None                          => Left(Fields(Map("Your name" -> ""), Map.empty))
      case Some(str) if str.trim.isEmpty => Left(Fields(Map("Your name" -> ""), Map("Your name" -> "You must provide your name.")))
      case Some(name)                    => Right(name.trim)
    }
  
  def validateAge(params: Map[String, String] = Map.empty): Either[Fields, String] =
    params.get("Your age") match {
      case None                                  => Left(Fields(Map("Your age" -> ""), Map.empty))
      case Some(str) if str.trim.isEmpty         => Left(Fields(Map("Your age" -> ""), Map("Your age" -> "You must provide your age.")))
      case Some(age) if age.trim.matches("\\d+") => Right(age.trim)
      case Some(str)                             => Left(Fields(Map("Your age" -> str), Map("Your age" -> "Your age must be a number.")))
    }

  def getBook(params: Map[String, String] = Map.empty): String @imp =
    params.get("Favorite book") match {
      case None                          => getBook(prompt(form(Map("Favorite book" -> ""), Map.empty)))
      case Some(str) if str.trim.isEmpty => getBook(prompt(form(Map("Favorite book" -> ""), Map("Favorite book" -> "You must provide the title."))))
      case Some(book)                    => book.trim
    }

  def form(inputs: Map[String, String], errors: Map[String, String]): NodeBuffer =
    <h1>Greeter</h1>
    <form>
      {
        inputs.map { input =>
          <div style="clear:both;margin-bottom:10px">
            <span style="float:left;width:100px;text-align:right;margin-right:10px">{ input._1 }:</span>
            <span style="float:left;"><input name={ input._1 } value={ input._2 } />
          {
            errors.get(input._1) match {
              case None      =>
              case Some(msg) => <span style="margin:10px;color:red">{ msg }</span>
            }
          }
            </span>
          </div>
        }.toSeq
      }
      <div style="clear:both">
        <span style="float:left;margin-left:110px;margin-top:10px"><input type="submit" /></span>
      </div>
    </form>
}

