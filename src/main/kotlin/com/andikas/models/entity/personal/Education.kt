package com.andikas.models.entity.personal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Educations : LongIdTable() {
    val profileId = reference("profile_id", Profiles.id)
    val year = varchar("year", 128)
    val institution = varchar("institution", 128)
    val role = varchar("role", 128)
    val description = varchar("description", 1024)
}

class EducationEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EducationEntity>(Educations)

    var profileId by Educations.profileId
    var year by Educations.year
    var institution by Educations.institution
    var role by Educations.role
    var description by Educations.description
}