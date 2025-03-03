package views

import views.html.helper.FieldConstructor

package object fieldsConstructors {
  val bFieldConstructor = FieldConstructor(f =>
    views.html.fieldsConstructors.bootstrapFieldConstructor(f)
  )
}
