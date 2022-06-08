package team.waggly.backend.exception

import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import team.waggly.backend.dto.ResponseDto

class ErrorMessage (
    private val bindingResult: BindingResult
    ){
    var isError: Boolean = false
    var errorMsg: String = ""

    fun getError(): ErrorMessage {
        var msg: String = ""
        if (bindingResult.hasErrors()) {
            this.isError = true
            bindingResult.allErrors.forEach {
                val field = it as FieldError
                val message = it.defaultMessage
                val errorMessage = "${field.field} : $message "
                println("${field.field} : $message")
                msg += errorMessage
            }
            this.errorMsg = msg
        }
        return this
    }
}