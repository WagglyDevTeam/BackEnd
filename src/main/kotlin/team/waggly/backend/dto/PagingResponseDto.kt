package team.waggly.backend.dto

open class PagingResponseDto<Any> {
    var code = 200
    var message: String = "OK"
    var status: String? = "200"
    var totalCount: Number = 0
    var totalPage: Number = 0
    var datas: Any? = null

    constructor()

    constructor(
            totalCount: Number,
            totalPage: Number,
            data: Any?,
    ) {
        this.datas = data
        this.totalCount = totalCount
        this.totalPage = totalPage
    }
}