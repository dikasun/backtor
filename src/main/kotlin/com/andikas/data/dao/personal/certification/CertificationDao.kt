package com.andikas.data.dao.personal.certification

import com.andikas.models.request.personal.CertificationBody
import com.andikas.models.response.personal.CertificationResponse

interface CertificationDao {
    suspend fun allCertifications(profileId: Long): List<CertificationResponse>
    suspend fun certification(profileId: Long, id: Long): CertificationResponse?
    suspend fun addCertification(profileId: Long, body: CertificationBody): Long
    suspend fun editCertification(profileId: Long, id: Long, body: CertificationBody): Boolean
    suspend fun deleteCertification(profileId: Long, id: Long): Boolean
}