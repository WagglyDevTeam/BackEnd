package team.waggly.backend.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, columnDefinition = "VARCHAR(45)")
    val email: String,

    @Column
    var profileImgUrl: String,

    @Column(unique = true, columnDefinition = "VARCHAR(15)")
    var nickName: String,

    @Column(columnDefinition = "VARCHAR(60)")
    var password: String,

    @Column(columnDefinition = "VARCHAR(6)")
    val gender: GenderType,

    @Column(columnDefinition = "INTEGER(3)")
    var classNum: Int,

    @Column(columnDefinition = "VARCHAR(10)")
    var activeStatus: UserActiveStatusType = UserActiveStatusType.ACTIVE,

    @Column(nullable = true)
    var withdrawalDate: LocalDateTime? = null,

    @Column(columnDefinition = "VARCHAR(20)")
    val auth: AuthType = AuthType.USER,

    @Column(nullable = true, columnDefinition = "VARCHAR(100)")    //자기소개 몇자 내외?
    var introduction: String? = ""// 추 후 협의 null vs ""
) {
    enum class GenderType {
        MALE,
        FEMALE
    }

    enum class UserActiveStatusType {
        ACTIVE,
        WITHDRAWAL,
        BAN
    }

    enum class AuthType {
        ADMIN,
        USER
    }
}