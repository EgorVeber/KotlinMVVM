package ru.gb.veber.kotlinmvvm.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.repository.LocalRepository
import ru.gb.veber.kotlinmvvm.repository.LocalRepositoryImp
import ru.gb.veber.kotlinmvvm.room.App.CreateDB.getHistoryDao

class ViewModelHistory(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepo: LocalRepository = LocalRepositoryImp(getHistoryDao())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(historyRepo.getAllHistory())
    }

    fun deleteHistory() {
        historyRepo.deleteHistory()
        historyLiveData.value = AppState.Success(historyRepo.getAllHistory())
    }
}