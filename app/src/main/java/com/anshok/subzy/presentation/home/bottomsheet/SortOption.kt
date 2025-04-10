package com.anshok.subzy.presentation.home.bottomsheet

/**
 * Перечисление типов сортировки, доступных в приложении.
 * Используется в SortBottomSheet и ViewModel для выбора пользовательского порядка отображения.
 */
enum class SortOption {
    DATE,
    NAME,
    PRICE;

    fun toggleDirection(current: SortDirection): SortDirection {
        return if (current == SortDirection.ASC) SortDirection.DESC else SortDirection.ASC
    }
}

/**
 * Направление сортировки:
 * - ASC: по возрастанию (например, от А до Я или от меньшего к большему)
 * - DESC: по убыванию
 */
enum class SortDirection {
    ASC,
    DESC
}

/**
 * Расширение для быстрого переключения направления сортировки.
 *
 * @return Противоположное значение (ASC → DESC, DESC → ASC)
 */
fun SortDirection.toggle(): SortDirection {
    return if (this == SortDirection.ASC) SortDirection.DESC else SortDirection.ASC
}

