package com.andikas.models.entity.personal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Certifications : LongIdTable() {
    val profileId = reference("profile_id", Profiles.id)
    val url = varchar("url", 1024)
    val description = varchar("description", 1024)
}

class CertificationEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CertificationEntity>(Certifications)

    var profileId by Certifications.profileId
    var url by Certifications.url
    var description by Certifications.description
}