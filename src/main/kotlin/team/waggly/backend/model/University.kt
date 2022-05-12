package team.waggly.backend.model

import javax.persistence.*

@Entity
class University (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val Id: Long? = null,

//    @Column(columnDefinition = "VARCHAR(30)")
//    val universityCode: String,

    @Column(columnDefinition = "VARCHAR(30)")
    val universityName: String,

    @Column(columnDefinition = "VARCHAR(255)")
    val universityEmail: String,

)