package team.waggly.backend.exception


class CustomException {

    class ValidatorException(
        val error: List<ValidatorExceptionReturnType>
    )

    class ValidatorExceptionReturnType(
        val code: String?,
        val message: String?,
    )
}
