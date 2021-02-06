package com.paging.basepaginglibrary.ui.main.model

import com.paging.basepaginglibrary.ui.network.BaseResponse
import com.paging.basepaginglibrary.ui.network.response.CharacterResponse
import com.paging.basepaginglibrary.ui.network.mapper.Mapper

private const val IMAGE_URL_FORMAT = "%s.%s"

/**
 * Helper class to transforms network response to visual model, catching the necessary data.
 *
 * @see Mapper
 */
class CharacterItemMapper : Mapper<BaseResponse<CharacterResponse>, List<CharacterItem>> {

    /**
     * Transform network response to [CharacterItem].
     *
     * @param from Network characters response.
     * @return List of parsed characters items.
     */
    override suspend fun map(from: BaseResponse<CharacterResponse>) =
        from.data.results.map {
            CharacterItem(
                id = it.id,
                name = it.name,
                description = it.description,
                imageUrl = IMAGE_URL_FORMAT.format(
                    it.thumbnail.path.replace("http", "https"),
                    it.thumbnail.extension
                )
            )
        }
}
