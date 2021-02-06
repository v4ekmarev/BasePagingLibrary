package com.paging.basepaginglibrary.ui.network.repositories

import com.paging.basepaginglibrary.ui.network.BaseResponse
import com.paging.basepaginglibrary.ui.network.MarvelService
import com.paging.basepaginglibrary.ui.network.response.CharacterResponse
import java.security.MessageDigest

private const val API_PUBLIC_KEY = "44d40ff9f5faf3ea541e326c1a154d84"
private const val API_PRIVATE_KEY = "482afc992d31523699a49d200eb90fb18022a1d3"
private const val HASH_FORMAT = "%s%s%s"

/**
 * Repository module for handling marvel API network operations [MarvelService].
 */
class MarvelRepository(
    val service: MarvelService
) {

    /**
     * Get all info of Marvel character.
     *
     * @param id A single character id.
     * @return Response for single character resource.
     */
    suspend fun getCharacter(id: Long): BaseResponse<CharacterResponse> {
        val timestamp = System.currentTimeMillis().toString()
        return service.getCharacter(
            id = id,
            apiKey = API_PUBLIC_KEY,
            hash = generateApiHash(timestamp),
            timestamp = timestamp
        )
    }

    /**
     * Get all Marvel characters by paged.
     *
     * @param offset Skip the specified number of resources in the result set.
     * @param limit Limit the result set to the specified number of resources.
     * @return Response for comic characters resource.
     */
    suspend fun getCharacters(offset: Int, limit: Int): BaseResponse<CharacterResponse> {
        val timestamp = System.currentTimeMillis().toString()
        return service.getCharacters(
            apiKey = API_PUBLIC_KEY,
            hash = generateApiHash(timestamp),
            timestamp = timestamp,
            offset = offset,
            limit = limit
        )
    }

    // ============================================================================================
    //  Private generators methods
    // ============================================================================================

    /**
     * Generate a md5 digest of the timestamp parameter, private API key and public.
     *
     * @param timestamp A digital current record of the time.
     * @return The MD5 Hash
     */
    private fun generateApiHash(timestamp: String) =
        HASH_FORMAT.format(timestamp, API_PRIVATE_KEY, API_PUBLIC_KEY).toMD5()

    fun String.toMD5() =
        MessageDigest
            .getInstance("MD5")
            .digest(toByteArray())
            .toHex()

    fun ByteArray.toHex() = joinToString("") {
        "%02x".format(it)
    }
}
