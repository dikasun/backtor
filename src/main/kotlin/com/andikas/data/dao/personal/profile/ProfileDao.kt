package com.andikas.data.dao.personal.profile

import com.andikas.models.request.personal.ProfileBody
import com.andikas.models.response.personal.ProfileResponse

interface ProfileDao {
    suspend fun allProfiles(): List<ProfileResponse>
    suspend fun profile(id: Long): ProfileResponse?
    suspend fun addProfile(body: ProfileBody): Long
    suspend fun editProfile(id: Long, body: ProfileBody): Boolean
    suspend fun deleteProfile(id: Long): Boolean
}