import com.anshok.subzy.domain.logo.model.Logo
import com.anshok.subzy.util.ResourceLogo

sealed interface AddSubSearchState {
    data object Loading : AddSubSearchState
    data object LoadingNextPage : AddSubSearchState

    data class LocalOnly(val logos: List<Logo>) :
        AddSubSearchState // Показ только локальных данных

    data class Success(val logos: List<Logo>) :
        AddSubSearchState    // Комбинированный успех (локальные + API)

    data class LoadNextPage(val logos: List<Logo>) : AddSubSearchState
    data object NoMoreData : AddSubSearchState

    data object NothingFound : AddSubSearchState

    data class Error(
        val errorType: ResourceLogo.ResponseError,
        val message: String? = null
    ) : AddSubSearchState

    data class ErrorNextPage(
        val errorType: ResourceLogo.ResponseError,
        val message: String? = null
    ) : AddSubSearchState
}
