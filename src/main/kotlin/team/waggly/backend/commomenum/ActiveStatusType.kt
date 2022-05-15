package team.waggly.backend.commomenum

enum class ActiveStatusType(str: String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        // BAN // 탈퇴 / 정지 회의 후 결정
}