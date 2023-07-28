package com.andikas.plugins

import com.andikas.models.request.auth.AuthBody
import com.andikas.models.request.personal.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<AuthBody> { body ->
            when {
                body.username.isEmpty() -> ValidationResult.Invalid("Username cannot be empty")
                body.password.isEmpty() -> ValidationResult.Invalid("Password cannot be empty")
                body.password.length < 8 -> ValidationResult.Invalid("Password must be greater than 8")
                else -> ValidationResult.Valid
            }
        }
        validate<ProfileBody> { body ->
            when {
                body.name.isEmpty() -> ValidationResult.Invalid("Name cannot be empty")
                body.role.isEmpty() -> ValidationResult.Invalid("Role cannot be empty")
                body.description.isEmpty() -> ValidationResult.Invalid("Description cannot be empty")
                else -> ValidationResult.Valid
            }
        }
        validate<SkillBody> { body ->
            when {
                body.name.isEmpty() -> ValidationResult.Invalid("Name cannot be empty")
                else -> ValidationResult.Valid
            }
        }
        validate<ProjectBody> { body ->
            when {
                body.description.isEmpty() -> ValidationResult.Invalid("Description cannot be empty")
                else -> ValidationResult.Valid
            }
        }
        validate<ExperienceBody> { body ->
            when {
                body.startYear.isEmpty() -> ValidationResult.Invalid("Start Year cannot be empty")
                body.endYear.isEmpty() -> ValidationResult.Invalid("End Year cannot be empty")
                body.institution.isEmpty() -> ValidationResult.Invalid("Institution cannot be empty")
                body.role.isEmpty() -> ValidationResult.Invalid("Role cannot be empty")
                body.description.isEmpty() -> ValidationResult.Invalid("Description cannot be empty")
                else -> ValidationResult.Valid
            }
        }
        validate<EducationBody> { body ->
            when {
                body.year.isEmpty() -> ValidationResult.Invalid("Year cannot be empty")
                body.institution.isEmpty() -> ValidationResult.Invalid("Institution cannot be empty")
                body.role.isEmpty() -> ValidationResult.Invalid("Role cannot be empty")
                body.description.isEmpty() -> ValidationResult.Invalid("Description cannot be empty")
                else -> ValidationResult.Valid
            }
        }
        validate<CertificationBody> { body ->
            when {
                body.description.isEmpty() -> ValidationResult.Invalid("Description cannot be empty")
                body.url.isEmpty() -> ValidationResult.Invalid("Url cannot be empty")
                else -> ValidationResult.Valid
            }
        }
    }
}