package com.andikas.models.entity.personal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Projects : LongIdTable() {
    val profileId = reference("profile_id", Profiles.id)
    val description = varchar("description", 1024)
}

class ProjectEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ProjectEntity>(Projects)

    var profileId by Projects.profileId
    var description by Projects.description
}