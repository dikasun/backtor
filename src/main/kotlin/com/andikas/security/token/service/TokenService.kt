package com.andikas.security.token.service

import com.andikas.security.token.TokenClaim
import com.andikas.security.token.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}