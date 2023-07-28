package com.andikas.models.entity.personal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Profiles : LongIdTable() {
    var picture = varchar("picture", 128)
    var name = varchar("name", 128)
    var role = varchar("role", 128)
    var description = varchar("description", 1024)
}

class ProfileEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ProfileEntity>(Profiles)

    var picture by Profiles.picture
    var name by Profiles.name
    var role by Profiles.role
    var description by Profiles.description

    val skills by SkillEntity referrersOn Skills.profileId
    val experiences by ExperienceEntity referrersOn Experiences.profileId
    val educations by EducationEntity referrersOn Educations.profileId
    val projects by ProjectEntity referrersOn Projects.profileId
    val certifications by CertificationEntity referrersOn Certifications.profileId
}