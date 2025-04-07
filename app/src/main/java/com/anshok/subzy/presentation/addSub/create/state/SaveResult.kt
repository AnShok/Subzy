package com.anshok.subzy.presentation.addSub.create.state

sealed class SaveResult {
    data object Success : SaveResult()
    data object Duplicate : SaveResult()
    data object InvalidInput : SaveResult()
}

