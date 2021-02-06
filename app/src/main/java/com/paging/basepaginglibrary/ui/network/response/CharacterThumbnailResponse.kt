package com.paging.basepaginglibrary.ui.network.response

/**
 * Marvel API character thumbnail network response.
 *
 * @param path The directory path of to the image.
 * @param extension The file extension for the image.
 */
data class CharacterThumbnailResponse(
    val path: String,
    val extension: String
)
