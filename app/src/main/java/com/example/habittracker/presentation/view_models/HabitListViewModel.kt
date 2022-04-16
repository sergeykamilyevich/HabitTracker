package com.example.habittracker.presentation.view_models

import android.text.Editable
import androidx.lifecycle.*
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.usecases.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@MainActivityScope
class HabitListViewModel @Inject constructor(
    private val getHabitListUseCase: GetHabitListUseCase,
    private val upsertHabitItemUseCase: UpsertHabitItemUseCase,
    private val deleteHabitItemUseCase: DeleteHabitItemUseCase,
    private val addHabitDoneUseCase: AddHabitDoneUseCase,
    private val deleteHabitDoneUseCase: DeleteHabitDoneUseCase
) : ViewModel() {

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    var habitDoneIdAdded: Int? = null

    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<HabitItem>>

    fun addHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            upsertHabitItemUseCase(habitItem)
        }
    }

    fun deleteHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            deleteHabitItemUseCase(habitItem)
        }
    }

    fun addHabitDone(habitDone: HabitDone) {
        viewModelScope.launch {
            habitDoneIdAdded = addHabitDoneUseCase(habitDone)
        }
    }

    fun deleteHabitDone(habitDoneId: Int) {
        viewModelScope.launch {
            deleteHabitDoneUseCase(habitDoneId)
        }
    }

    fun getHabitList(habitTypeFilter: HabitType?) {
        habitList = Transformations.switchMap(habitListFilter) {
            getHabitListUseCase(habitTypeFilter, it).asLiveData()
        }
    }

    fun updateHabitListOrderBy(habitListOrderBy: HabitListOrderBy) {
        currentHabitListFilter.orderBy = habitListOrderBy
        _habitListFilter.value = currentHabitListFilter
    }

    fun updateFilter(input: Editable?) {
        currentHabitListFilter.search = input?.toString() ?: EMPTY_STRING
        _habitListFilter.value = currentHabitListFilter

    }

//    fun fetchHabits() {
//        val api = ApiFactory.apiService
//        viewModelScope.launch {
//            var response: Response<List<HabitItemApiModel>>? = try {
//                api.getHabitList()
//            } catch (e: Exception) {
//                Log.d("99999", "error $e")
//                return@launch
//            }
//
//            if (response?.isSuccessful == true && response.body() != null) {
//                Log.d("99999", "${response.body()}")
//                val rb = response.body()
//            } else {
//                Log.d("99999", "${response?.errorBody()}")
//                val rb = response?.errorBody() as? ErrorApiModel
//                Log.d("99999", "${rb}")
//            }

//            var response: Call<List<HabitItemApiModel>>? = try {
//                RetrofitHabit.retrofit?.getHabitList()
//            } catch (e: Exception) {
//                Log.d("99999", "error $e")
//                return@launch
//            }
//
//            if (response?.isSuccessful == true && response.body() != null) {
//                Log.d("99999", "${response.body()}")
//                val rb = response.body()
//            } else {
//                Log.d("99999", "${response?.errorBody()}")
//                val rb = response?.errorBody() as? ErrorApiModel
//                Log.d("99999", "${rb}")
//            }
//            Log.d("99999", "${response?.body()}")

//        }
//    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}