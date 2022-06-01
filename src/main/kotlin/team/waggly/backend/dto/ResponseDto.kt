package team.waggly.backend.dto

open class ResponseDto<Any> {
    var code = 200
    var message: String = "OK"
    var status: String? = "200"
    var datas: Any? = null

    constructor()

    constructor(
            datas: Any?
    ) {
        this.datas = datas
    }

    constructor(
            datas: Any?,
            code: Int
    ) {
        this.datas = datas
        this.code = code
    }

    constructor(
        datas: Any?,
        message: String,
        code: Int
    ) {
        this.datas = datas
        this.message = message
        this.code = code
    }
}