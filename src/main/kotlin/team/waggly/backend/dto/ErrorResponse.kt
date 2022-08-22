package team.waggly.backend.dto

class ErrorResponse(
    var code: Int = 0,
    var message: String? = null
) {
    constructor(code: Int) : this() {
        this.code = code
    }
}
