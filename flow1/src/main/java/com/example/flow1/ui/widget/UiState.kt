package com.example.flow1.ui.widget

data class UiState(
    val type: EditType? = null,
    val text: String = "",
    var editState: EditState = EditState.EMPTY
) {
    enum class EditState {
        EMPTY,
        WRITTEN
    }
    enum class EditType {
        NAME,
        ADDRESS,
        PHONENUM
    }
}
