package team.waggly.backend.model

import java.util.*
import javax.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true, columnDefinition = "VARCHAR2(45)")
    val email: String,
    @Column
    var profileImgUrl: String,
    @Column(unique = true, columnDefinition = "VARCHAR2(15)")
    var nickName: String,
    @Column(columnDefinition = "VARCHAR2(30)")
    var password: String,
    @Column(columnDefinition = "VARCHAR2(6)") // ENUM 변경
    val gender: String,
    @Column
    var classNum: Int, // 보류
    @Column
    var activeStatus: String, // ENUM 변경
    @Column(nullable = true)
    var withdrawalDate: Date?,
    @Column
    val auth: String, //ENUM 변경
    @Column(nullable = true, columnDefinition = "VARCHAR2(100)")    //자기소개 몇자 내외?
    var introduction: String?
){

}