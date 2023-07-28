package com.andikas.data.dao.personal.project

import com.andikas.models.request.personal.ProjectBody
import com.andikas.models.response.personal.ProjectResponse

interface ProjectDao {
    suspend fun allProjects(profileId: Long): List<ProjectResponse>
    suspend fun project(profileId: Long, id: Long): ProjectResponse?
    suspend fun addProject(profileId: Long, body: ProjectBody): Long
    suspend fun editProject(profileId: Long, id: Long, body: ProjectBody): Boolean
    suspend fun deleteProject(profileId: Long, id: Long): Boolean
}