package com.andikas.data.dao.personal.project

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.personal.ProfileEntity
import com.andikas.models.entity.personal.Profiles
import com.andikas.models.entity.personal.ProjectEntity
import com.andikas.models.request.personal.ProjectBody
import com.andikas.models.response.personal.ProjectResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection

class ProjectDaoImpl : ProjectDao {
    override suspend fun allProjects(profileId: Long): List<ProjectResponse> = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.projects.map { entity -> mapToResponse(entity) }
        } ?: emptyList()
    }

    override suspend fun project(profileId: Long, id: Long): ProjectResponse? = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.projects.find { project -> project.id.value == id }?.id?.let { projectId ->
                ProjectEntity.findById(projectId)?.let { entity -> mapToResponse(entity) }
            }
        }
    }

    override suspend fun addProject(profileId: Long, body: ProjectBody): Long = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            SizedCollection(
                it.projects + body.let {
                    ProjectEntity.new {
                        this.description = body.description
                        this.profileId = EntityID(profileId, Profiles)
                    }
                }
            )
        }
        profileId
    }

    override suspend fun editProject(profileId: Long, id: Long, body: ProjectBody): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.projects.find { project -> project.id.value == id }?.id?.let { projectId ->
                ProjectEntity.findById(projectId)?.let { entity ->
                    entity.description = body.description
                }
            }
        } != null
    }

    override suspend fun deleteProject(profileId: Long, id: Long): Boolean = dbQuery {
        ProfileEntity.findById(profileId)?.let {
            it.projects.find { project -> project.id.value == id }?.id?.let { projectId ->
                ProjectEntity.findById(projectId)?.delete()
            }
        } != null
    }

    private fun mapToResponse(entity: ProjectEntity) = ProjectResponse(
        id = entity.id.value,
        description = entity.description
    )
}

val projectDao: ProjectDao = ProjectDaoImpl()