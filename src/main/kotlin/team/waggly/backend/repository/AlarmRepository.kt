package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Alarm

@Repository
interface AlarmRepository : JpaRepository<Alarm, Long> {
}