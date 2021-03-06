package halfpipe.scalaexample

import view.ViewContext
import halfpipe.ScalaApplication

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 2:04 PM
 */
object ExampleScalaApp extends ScalaApplication[Context] {
  protected override def getViewContext: Class[_] = classOf[ViewContext]
}
