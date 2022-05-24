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
        Thread {
            historyLiveData.postValue(AppState.Success(historyRepo.getAllHistory()))
        }.start()
    }

    fun deleteHistory() {
        Thread {
            historyRepo.deleteHistory()
            historyLiveData.postValue(AppState.Success(historyRepo.getAllHistory()))
        }.start()
    }
}