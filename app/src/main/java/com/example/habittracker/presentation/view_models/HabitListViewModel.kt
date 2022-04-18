package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.data.network.HabitApi
import com.example.habittracker.data.network.models.HabitItemApiModel
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.usecases.db.*
import com.example.habittracker.presentation.models.AddHabitDoneResult
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@MainActivityScope
class HabitListViewModel @Inject constructor(
    private val getHabitListFromDbUseCase: GetHabitListFromDbUseCase,
    private val upsertHabitToDbUseCase: UpsertHabitToDbUseCase,
    private val getHabitFromDbUseCase: GetHabitFromDbUseCase,
    private val deleteHabitFromDbUseCase: DeleteHabitFromDbUseCase,
    private val addHabitDoneToDbUseCase: AddHabitDoneToDbUseCase,
    private val deleteHabitDoneFromDbUseCase: DeleteHabitDoneFromDbUseCase,
    private val apiService: HabitApi
) : ViewModel() {

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    private val _showToastHabitDone = MutableLiveData<Event<AddHabitDoneResult>>()
    val showToastHabitDone: LiveData<Event<AddHabitDoneResult>>
        get() = _showToastHabitDone

    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<HabitItem>>

    fun addHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            upsertHabitToDbUseCase(habitItem)
        }
    }

    fun deleteHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            deleteHabitFromDbUseCase(habitItem)
        }
    }

    fun addHabitDone(habitDone: HabitDone) {
        viewModelScope.launch {
            val habitDoneIdAdded =  addHabitDoneToDbUseCase(habitDone)
            val habitItem = getHabitFromDbUseCase(habitDone.habitId)
            _showToastHabitDone.value = Event(AddHabitDoneResult(habitItem, habitDoneIdAdded))
        }
    }

    fun deleteHabitDone(habitDoneId: Int) {
        viewModelScope.launch {
            deleteHabitDoneFromDbUseCase(habitDoneId)
        }
    }

    fun getHabitList(habitTypeFilter: HabitType?) {
        habitList = Transformations.switchMap(habitListFilter) {
            getHabitListFromDbUseCase(habitTypeFilter, it).asLiveData()
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

    fun fetchHabits() {
        Log.d("99999", "start fetchHabits")
//        val api = ApiFactory.apiService
        viewModelScope.launch {

            val response: Response<List<HabitItemApiModel>>? = try {
                apiService.getHabitList()
            } catch (e: Exception) {
                Log.d("99999", "error $e")
                return@launch
            }
            Log.d("99999", "response code ${response?.code()}")
            Log.d("99999", "response errorBody ${response?.errorBody()}")
            Log.d("99999", "response body ${response?.body()}")
//            response.errorBody()

            response?.let {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("99999", "${response.body()}")

                    }
                } else {
                    val rb = response.errorBody()
                    Log.d("99999", "${rb}")
                }

            }

        }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}