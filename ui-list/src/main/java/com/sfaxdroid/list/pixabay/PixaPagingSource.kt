package com.sfaxdroid.list.pixabay

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PixaPagingSource @Inject constructor(
    private val useCase: GetPixaWallpapersUseCase,
    private val search: PixaSearch,
) : PagingSource<Int, PixaItem>() {

    override fun getRefreshKey(state: PagingState<Int, PixaItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixaItem> {
        return try {
            val currentPage = params.key ?: FIRST_PAGE
            val response = useCase
                .getResult(
                    search = search,
                    page = currentPage
                )
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}