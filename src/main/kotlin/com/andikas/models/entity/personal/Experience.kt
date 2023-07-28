package com.andikas.models.entity.personal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Experiences : LongIdTable() {
    val profileId = reference("profile_id", Profiles.id)
    val startYear = varchar("startYear", 128)
    val endYear = varchar("endYear", 128)
    val institution = varchar("institution", 128)
    val role = varchar("role", 128)
    val description = varchar("description", 1024)
}

class ExperienceEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ExperienceEntity>(Experiences)

    var profileId by Experiences.profileId
    var startYear by Experiences.startYear
    var endYear by Experiences.endYear
    var institution by Experiences.institution
    var role by Experiences.role
    var description by Experiences.description
}