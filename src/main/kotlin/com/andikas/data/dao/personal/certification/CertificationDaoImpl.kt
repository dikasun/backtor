package com.andikas.data.dao.personal.certification

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.personal.CertificationEntity
import com.andikas.models.entity.personal.ProfileEntity
import com.andikas.models.entity.personal.Profiles
import com.andikas.models.request.personal.CertificationBody
import com.andikas.models.response.personal.CertificationResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection

class CertificationDaoImpl : CertificationDao {
    override suspend fun allCertifications(profileId: Long): List<CertificationResponse> = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.certifications.map { entity -> mapToResponse(entity) }
        } ?: emptyList()
    }

    override suspend fun certification(profileId: Long, id: Long): CertificationResponse? = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.certifications.find { certification -> certification.id.value == id }?.id?.let { certificationId ->
                CertificationEntity.findById(certificationId)?.let { entity -> mapToResponse(entity) }
            }
        }
    }

    override suspend fun addCertification(profileId: Long, body: CertificationBody): Long = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            SizedCollection(
                it.certifications + body.let {
                    CertificationEntity.new {
                        this.url = body.url
                        this.description = body.description
                        this.profileId = EntityID(profileId, Profiles)
                    }
                }
            )
        }
        profileId
    }

    override suspend fun editCertification(profileId: Long, id: Long, body: CertificationBody): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.certifications.find { certification -> certification.id.value == id }?.id?.let { certificationId ->
                CertificationEntity.findById(certificationId)?.let { entity ->
                    entity.url = body.url
                    entity.description = body.description
                }
            }
        } != null
    }

    override suspend fun deleteCertification(profileId: Long, id: Long): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.certifications.find { certification -> certification.id.value == id }?.id?.let { certificationId ->
                CertificationEntity.findById(certificationId)?.delete()
            }
        } != null
    }

    private fun mapToResponse(entity: CertificationEntity) = CertificationResponse(
        id = entity.id.value,
        url = entity.url,
        description = entity.description
    )
}

val certificationDao: CertificationDao = CertificationDaoImpl()