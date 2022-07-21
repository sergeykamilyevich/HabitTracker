package com.example.habittracker.presentation.view_models

import android.text.SpannableStringBuilder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habittracker.R
import com.example.habittracker.data.repositories.CloudHabitRepositoryImpl
import com.example.habittracker.data.repositories.DbHabitRepositoryImpl
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.IoError.*
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.models.HabitListOrderBy.*
import com.example.habittracker.domain.usecases.common.*
import com.example.habittracker.domain.usecases.db.*
import com.example.habittracker.domain.usecases.network.*
import com.example.habittracker.presentation.models.AddHabitSnackBarData
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
internal class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    private val dbHabitRepository = mock<DbHabitRepositoryImpl>()
    private val addHabitDoneUseCase = mock<AddHabitDoneUseCase>()
    private val deleteHabitDoneUseCase = mock<DeleteHabitDoneUseCase>()
    private val deleteHabitUseCase = mock<DeleteHabitUseCase>()
    private val getHabitUseCase = mock<GetHabitUseCase>()
    private val getHabitListUseCase = mock<GetHabitListUseCase>()
    private val upsertHabitUseCase = mock<UpsertHabitUseCase>()
    private val deleteAllHabitsUseCase = mock<DeleteAllHabitsUseCase>()
    private val getUnfilteredHabitListUseCase = mock<GetUnfilteredHabitListUseCase>()
    private val dbUseCase = DbUseCase(
        dbHabitRepository,
        addHabitDoneUseCase,
        deleteHabitDoneUseCase,
        deleteHabitUseCase,
        getHabitUseCase,
        getHabitListUseCase,
        getUnfilteredHabitListUseCase,
        upsertHabitUseCase,
        deleteAllHabitsUseCase
    )

    private val cloudHabitRepository = mock<CloudHabitRepositoryImpl>()
    private val deleteAllHabitsFromCloudUseCase = mock<DeleteAllHabitsFromCloudUseCase>()
    private val deleteHabitFromCloudUseCase = mock<DeleteHabitFromCloudUseCase>()
    private val getHabitListFromCloudUseCase = mock<GetHabitListFromCloudUseCase>()
    private val postHabitDoneToCloudUseCase = mock<PostHabitDoneToCloudUseCase>()
    private val getCloudErrorUseCase = mock<GetCloudErrorUseCase>()
    private val cloudUseCase = CloudUseCase(
        cloudHabitRepository,
        deleteAllHabitsFromCloudUseCase,
        deleteHabitFromCloudUseCase,
        getHabitListFromCloudUseCase,
        postHabitDoneToCloudUseCase,
        getCloudErrorUseCase
    )
    private val syncAllFromCloudUseCase = mock<SyncAllFromCloudUseCase>()
    private val syncAllToCloudUseCase = mock<SyncAllToCloudUseCase>()
    private val putHabitAndSyncWithDbUseCase = mock<PutHabitAndSyncWithDbUseCase>()
    private val areCloudAndDbEqualUseCase = mock<AreCloudAndDbEqualUseCase>()
    private val syncUseCase = SyncUseCase(
        syncAllFromCloudUseCase,
        syncAllToCloudUseCase,
        putHabitAndSyncWithDbUseCase,
        areCloudAndDbEqualUseCase
    )

    private val application = RuntimeEnvironment.getApplication()
    private val resources = Resources.Base(application)
    private val habit: Habit = Habit(
        name = "habit name",
        description = "habit to insert",
        priority = HabitPriority.NORMAL,
        type = HabitType.GOOD,
        color = 1,
        recurrenceNumber = 1,
        recurrencePeriod = 1,
        id = 1
    )
    private val habitDone = HabitDone(
        habitId = 1,
        date = 1,
        habitUid = "uid",
    )
    private val habitList = mutableListOf<Habit>()

    init {
        initHabitList()
    }

    private fun initHabitList() {
        ('a'..'z').forEachIndexed { index, c ->
            habitList.add(
                Habit(
                    id = index + 1,
                    name = c.toString(),
                    description = c.toString(),
                    priority = HabitPriority.findPriorityById(index % 3),
                    type = HabitType.findTypeById(index % 2),
                    color = index,
                    recurrenceNumber = index,
                    recurrencePeriod = index * 2,
                    date = index,
                    uid = "uid $index"
                )
            )
        }
        habitList.shuffle()
    }

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(dbUseCase, cloudUseCase, syncUseCase, resources)
    }

    @After
    fun tearDown() {
    }

    private fun deleteHabitItemTest(
        dbReturn: Either<DeletingHabitError, Unit>,
        cloudReturn: Either<CloudError, Unit>,
        expected: String
    ) = runTest {
        Mockito.`when`(deleteHabitUseCase.invoke(habit))
            .thenReturn(dbReturn)
        Mockito.`when`(deleteHabitFromCloudUseCase.invoke(habit))
            .thenReturn(cloudReturn)
        mainViewModel.deleteHabitItem(habit)
        val actual = mainViewModel.ioError.getOrAwaitValue().transferIfNotHandled()
        if (expected != EXPECTED_EMPTY) {
            assertThat(actual).isEqualTo(expected)
            Mockito.reset(deleteHabitUseCase)
            Mockito.reset(deleteHabitFromCloudUseCase)
        }
    }

    @Test
    fun `deleteHabitItem set DeletingHabitError`() {
        val dbReturn = DeletingHabitError().failure()
        val cloudReturn = Unit.success()
        deleteHabitItemTest(dbReturn, cloudReturn, "Deleting habits failed")
    }

    @Test
    fun `deleteHabitItem set CloudError`() {
        val dbReturn = Unit.success()
        val cloudReturn = CloudError().failure()
        deleteHabitItemTest(dbReturn, cloudReturn, "Network error: ")
    }

    @Test
    fun `deleteHabitItem success deleted habit without ioError`() = runTest {
        val dbReturn = Unit.success()
        val cloudReturn = Unit.success()
        var liveDataException = false
        try {
            deleteHabitItemTest(dbReturn, cloudReturn, EXPECTED_EMPTY)
        } catch (e: Exception) {
            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
            liveDataException = true
        }
        Mockito.verify(deleteHabitUseCase, Mockito.times(1)).invoke(habit)
        Mockito.verify(deleteHabitFromCloudUseCase, Mockito.times(1)).invoke(habit)
        assertThat(liveDataException).isTrue()
        Mockito.reset(deleteHabitUseCase)
        Mockito.reset(deleteHabitFromCloudUseCase)
    }

    private fun deleteAllHabitsFromCloudTest(
        cloudReturn: Either<IoError, Unit>,
        expected: String
    ) = runTest {
        Mockito.`when`(deleteAllHabitsFromCloudUseCase.invoke())
            .thenReturn(cloudReturn)
        mainViewModel.deleteAllHabitsFromCloud()
        val actual = mainViewModel.ioError.getOrAwaitValue().transferIfNotHandled()
        if (expected != EXPECTED_EMPTY) {
            assertThat(actual).isEqualTo(expected)
            Mockito.reset(deleteAllHabitsFromCloudUseCase)
        }
    }

    @Test
    fun `deleteAllHabitsFromCloud set CloudError`() {
        val cloudReturn = CloudError().failure()
        deleteAllHabitsFromCloudTest(cloudReturn, "Network error: ")
    }

    @Test
    fun `deleteAllHabitsFromCloud set DeletingAllHabitsError`() {
        val cloudReturn = DeletingAllHabitsError().failure()
        deleteAllHabitsFromCloudTest(cloudReturn, "Deleting habits failed")
    }


    @Test
    fun `deleteAllHabitsFromCloud success deleted without ioError`() = runTest {
        val cloudReturn = Unit.success()
        var liveDataException = false
        try {
            deleteAllHabitsFromCloudTest(cloudReturn, EXPECTED_EMPTY)
        } catch (e: Exception) {
            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
            liveDataException = true
        }
        Mockito.verify(deleteAllHabitsFromCloudUseCase, Mockito.times(1)).invoke()
        assertThat(liveDataException).isTrue()
        Mockito.reset(deleteAllHabitsFromCloudUseCase)
    }

    private fun deleteAllHabitsFromDbTest(
        dbReturn: Either<IoError, Unit>,
        expected: String
    ) = runTest {
        Mockito.`when`(deleteAllHabitsUseCase.invoke()).thenReturn(dbReturn)
        mainViewModel.deleteAllHabitsFromDb()
        val actual = mainViewModel.ioError.getOrAwaitValue().transferIfNotHandled()
        if (expected != EXPECTED_EMPTY) {
            assertThat(actual).isEqualTo(expected)
            Mockito.reset(deleteAllHabitsUseCase)
        }
    }

    @Test
    fun `deleteAllHabitsFromDb set DeletingAllHabitsError`() {
        val dbReturn = DeletingAllHabitsError().failure()
        deleteAllHabitsFromDbTest(dbReturn, "Deleting habits failed")
    }

    @Test
    fun `deleteAllHabitsFromDb success deleted without ioError`() = runTest {
        val dbReturn = Unit.success()
        var liveDataException = false
        try {
            deleteAllHabitsFromDbTest(dbReturn, EXPECTED_EMPTY)
        } catch (e: Exception) {
            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
            liveDataException = true
        }
        Mockito.verify(deleteAllHabitsUseCase, Mockito.times(1)).invoke()
        assertThat(liveDataException).isTrue()
        Mockito.reset(deleteAllHabitsUseCase)
    }

    @Test
    fun `addHabitDone put SQL error in ioError from getHabitUseCase`() = runTest {
        val habitDoneIdAdded = DEFAULT_ID
        val error = SqlError()
        Mockito.`when`(addHabitDoneUseCase.invoke(habitDone)).thenReturn(habitDoneIdAdded.success())
        Mockito.`when`(getHabitUseCase.invoke(habitDone.habitId))
            .thenReturn(error.failure())

        mainViewModel.addHabitDone(habitDone)

        Mockito.verify(getHabitUseCase, Mockito.times(1)).invoke(habitDone.habitId)
        checkIfGetShowSnackbarHabitDoneException()
        checkIfGetIoError(error = error)

        Mockito.reset(addHabitDoneUseCase)
        Mockito.reset(getHabitUseCase)
    }

    @Test
    fun `addHabitDone put SQL error in ioError from addHabitDoneUseCase`() = runTest {
        val error = SqlError()
        Mockito.`when`(addHabitDoneUseCase.invoke(habitDone))
            .thenReturn(error.failure())

        mainViewModel.addHabitDone(habitDone)

        Mockito.verify(addHabitDoneUseCase, Mockito.times(1)).invoke(habitDone)
        checkIfGetShowSnackbarHabitDoneException()
        checkIfGetIoError(error = error)

        Mockito.reset(addHabitDoneUseCase)
    }

    private fun checkIfGetIoError(error: IoError) {
        val actual = mainViewModel.ioError.getOrAwaitValue().transferIfNotHandled()
        val expected = mainViewModel.eventErrorText(error).transferIfNotHandled()
        assertThat(actual).isNotNull()
        assertThat(expected).isNotNull()
        assertThat(actual).isEqualTo(expected)
    }

    private fun checkIfGetShowSnackbarHabitDoneException() {
        var liveDataException = false
        try {
            mainViewModel.showSnackbarHabitDone.getOrAwaitValue().transferIfNotHandled()
        } catch (e: Exception) {
            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
            liveDataException = true
        }
        assertThat(liveDataException).isTrue()
    }

    @Test
    fun `addHabitDone success added with snackbar message`() = runTest {
        val habitDoneIdAdded = DEFAULT_ID
        Mockito.`when`(addHabitDoneUseCase.invoke(habitDone)).thenReturn(habitDoneIdAdded.success())
        Mockito.`when`(getHabitUseCase.invoke(habitDone.habitId)).thenReturn(habit.success())
        mainViewModel.addHabitDone(habitDone)
        val actual =
            mainViewModel.showSnackbarHabitDone.getOrAwaitValue().transferIfNotHandled()
        val snackbarText = mainViewModel.snackbarText(habit)
        val newHabitDone = habitDone.copy(id = habitDoneIdAdded)
        assertThat(actual).isEqualTo(
            AddHabitSnackBarData(
                snackbarText = snackbarText,
                habitDone = newHabitDone
            )
        )
        Mockito.reset(addHabitDoneUseCase)
        Mockito.reset(getHabitUseCase)
    }

    private fun checkIfGetIoErrorException() {
        var liveDataException = false
        try {
            mainViewModel.ioError.getOrAwaitValue().transferIfNotHandled()
        } catch (e: Exception) {
            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
            liveDataException = true
        }
        assertThat(liveDataException).isTrue()
    }

    @Test
    fun `addHabitDoneToCloud success added without ioError`() = runTest {
        Mockito.`when`(postHabitDoneToCloudUseCase.invoke(habitDone)).thenReturn(Unit.success())

        mainViewModel.addHabitDoneToCloud(habitDone)

        checkIfGetIoErrorException()
        Mockito.reset(postHabitDoneToCloudUseCase)
    }

    @Test
    fun `addHabitDoneToCloud put Cloud error in ioError`() = runTest {
        val error = CloudError()
        Mockito.`when`(postHabitDoneToCloudUseCase.invoke(habitDone)).thenReturn(error.failure())

        mainViewModel.addHabitDoneToCloud(habitDone)

        checkIfGetIoError(error = error)
        Mockito.reset(postHabitDoneToCloudUseCase)
    }

    @Test
    fun `deleteHabitDone invoke deleteHabitDoneUseCase`() = runTest {
        val habitDoneId = DEFAULT_ID

        mainViewModel.deleteHabitDone(habitDoneId)

        Mockito.verify(deleteHabitDoneUseCase, Mockito.times(1)).invoke(habitDoneId)
    }

    private fun setupDefaultViewModelFilters() {
        mainViewModel.updateSearch(SpannableStringBuilder(EMPTY_SEARCH))
        mainViewModel.updateHabitListOrderBy(NAME_ASC)
    }

    @Test
    fun `getHabitList success get bad list`() {
        val habitTypeFilter = HabitType.BAD
        setupDefaultViewModelFilters()
        setUpMockGetHabitListUseCase(habitTypeFilter)

        mainViewModel.getHabitList(habitTypeFilter)
        val list = mainViewModel.habitList.getOrAwaitValue()

        checkGetHabitListByType(list, habitTypeFilter)
    }

    @Test
    fun `getHabitList success get good list`() {
        val habitTypeFilter = HabitType.GOOD
        setupDefaultViewModelFilters()
        setUpMockGetHabitListUseCase(habitTypeFilter)

        mainViewModel.getHabitList(habitTypeFilter)
        val list = mainViewModel.habitList.getOrAwaitValue()

        checkGetHabitListByType(list, habitTypeFilter)
    }

    @Test
    fun `getHabitList success get unfiltered list`() {
        val habitTypeFilter = null
        setupDefaultViewModelFilters()
        setUpMockGetHabitListUseCase(habitTypeFilter)

        mainViewModel.getHabitList(habitTypeFilter)
        val list = mainViewModel.habitList.getOrAwaitValue()

        checkGetHabitListByType(list, habitTypeFilter)
    }

    private fun checkGetHabitListByType(
        list: List<Habit>,
        habitTypeFilter: HabitType?
    ) {
        val actualHabitGood = list.find { it.type == HabitType.GOOD }
        val actualHabitBad = list.find { it.type == HabitType.BAD }
        when (habitTypeFilter) {
            HabitType.GOOD -> assertThat(actualHabitBad == null && actualHabitGood != null).isTrue()
            HabitType.BAD -> assertThat(actualHabitBad != null && actualHabitGood == null).isTrue()
            null -> assertThat(actualHabitBad != null && actualHabitGood != null).isTrue()
        }
    }

    @Test
    fun `getHabitList success get list with search filter`() {
        val search = SUCCESSFUL_SEARCH
        val isSuccessfulSearch = true
        getHabitListSearchTest(search, isSuccessfulSearch)

    }

    @Test
    fun `getHabitList get empty list with search filter`() {
        val search = UNSUCCESSFUL_SEARCH
        val isSuccessfulSearch = false
        getHabitListSearchTest(search, isSuccessfulSearch)
    }

    private fun getHabitListSearchTest(search: String, isSuccessfulSearch: Boolean) {
        val habitTypeFilter = null
        val testHabitToInsert = habit.copy(name = SUCCESSFUL_SEARCH)
        habitList.add(testHabitToInsert)
        mainViewModel.updateSearch(SpannableStringBuilder(search))
        mainViewModel.updateHabitListOrderBy(NAME_ASC)
        setUpMockGetHabitListUseCase(habitTypeFilter)

        mainViewModel.getHabitList(habitTypeFilter)
        val actual = mainViewModel.habitList.getOrAwaitValue()
        val expected = if (isSuccessfulSearch) listOf(testHabitToInsert) else listOf()

        assertThat(actual).isEqualTo(expected)

    }

    private fun setUpMockGetHabitListUseCase(habitTypeFilter: HabitType?) {
        val habitListFilter = mainViewModel.habitListFilter.value
        assertThat(habitListFilter).isNotNull()
        habitListFilter?.let {
            Mockito.`when`(getHabitListUseCase.invoke(habitTypeFilter, it))
                .thenReturn(getFakeHabitList(habitTypeFilter, it))
        }
    }

    @Test
    fun `getHabitList success get list sorted by NAME_ASC order`() {
        getHabitListByOrderTest(NAME_ASC)
    }

    @Test
    fun `getHabitList success get list sorted by NAME_DESC order`() {
        getHabitListByOrderTest(NAME_DESC)
    }

    @Test
    fun `getHabitList success get list sorted by TIME_CREATION_ASC order`() {
        getHabitListByOrderTest(TIME_CREATION_ASC)
    }

    @Test
    fun `getHabitList success get list sorted by TIME_CREATION_DESC order`() {
        getHabitListByOrderTest(TIME_CREATION_DESC)
    }

    @Test
    fun `getHabitList success get list sorted by PRIORITY_ASC order`() {
        getHabitListByOrderTest(PRIORITY_ASC)
    }

    @Test
    fun `getHabitList success get list sorted by PRIORITY_DESC order`() {
        getHabitListByOrderTest(PRIORITY_DESC)
    }

    private fun getHabitListByOrderTest(habitListOrderBy: HabitListOrderBy) {
        val habitTypeFilter = null
        mainViewModel.updateSearch(SpannableStringBuilder(EMPTY_SEARCH))
        mainViewModel.updateHabitListOrderBy(habitListOrderBy)
        setUpMockGetHabitListUseCase(habitTypeFilter)

        mainViewModel.getHabitList(habitTypeFilter)
        val list = mainViewModel.habitList.getOrAwaitValue()

        val assertRequirement: (Int) -> Unit = when (habitListOrderBy) {
            NAME_ASC -> { i -> assertThat(list[i].name <= list[i + 1].name).isTrue() }
            NAME_DESC -> { i -> assertThat(list[i].name >= list[i + 1].name).isTrue() }
            TIME_CREATION_ASC -> { i -> assertThat(list[i].date <= list[i + 1].date).isTrue() }
            TIME_CREATION_DESC -> { i -> assertThat(list[i].date >= list[i + 1].date).isTrue() }
            PRIORITY_ASC -> { i -> assertThat(list[i].priority.id <= list[i + 1].priority.id).isTrue() }
            PRIORITY_DESC -> { i -> assertThat(list[i].priority.id >= list[i + 1].priority.id).isTrue() }
        }
        for (i in 0 until list.size - 1) assertRequirement(i)
    }

    private fun getFakeHabitList(
        habitTypeFilter: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>> {
        val filteredByTypeList =
            if (habitTypeFilter != null) habitList.filter { it.type == habitTypeFilter } else habitList
        val filteredBySearchList =
            filteredByTypeList.filter { it.name.contains(habitListFilter.search) }
        val filteredList = when (habitListFilter.orderBy) {
            NAME_ASC -> filteredBySearchList.sortedBy { it.name }
            NAME_DESC -> filteredBySearchList.sortedByDescending { it.name }
            PRIORITY_ASC -> filteredBySearchList.sortedBy { it.priority.id }
            PRIORITY_DESC -> filteredBySearchList.sortedByDescending { it.priority.id }
            TIME_CREATION_ASC -> filteredBySearchList.sortedBy { it.date }
            TIME_CREATION_DESC -> filteredBySearchList.sortedByDescending { it.date }
        }
        return flow {
            emit(filteredList)
        }
    }

    @Test
    fun `updateHabitListOrderBy update habitListFilter`() {
        var habitListOrderBy = NAME_ASC
        mainViewModel.updateHabitListOrderBy(habitListOrderBy)
        val actualOrderBy = mainViewModel.habitListFilter.getOrAwaitValue().orderBy
        assertThat(actualOrderBy).isEqualTo(habitListOrderBy)

        habitListOrderBy = TIME_CREATION_ASC
        mainViewModel.updateHabitListOrderBy(habitListOrderBy)
        val newActualOrderBy = mainViewModel.habitListFilter.getOrAwaitValue().orderBy
        assertThat(newActualOrderBy).isEqualTo(habitListOrderBy)
    }

    @Test
    fun `updateSearch update habitListFilter`() {
        var search = EMPTY_SEARCH
        mainViewModel.updateSearch(SpannableStringBuilder(search))
        val actualSearch = mainViewModel.habitListFilter.getOrAwaitValue().search
        assertThat(actualSearch).isEqualTo(search)

        search = "random search"
        mainViewModel.updateSearch(SpannableStringBuilder(search))
        val actualNewSearch = mainViewModel.habitListFilter.getOrAwaitValue().search
        assertThat(actualNewSearch).isEqualTo(search)
    }

    @Test
    fun `uploadAllHabitsFromDbToCloud invoke syncAllToCloudUseCase with success in showResultToast`() =
        runTest {
            Mockito.`when`(syncAllToCloudUseCase.invoke()).thenReturn(Unit.success())
            mainViewModel.uploadAllHabitsFromDbToCloud()
            val actual = mainViewModel.showResultToast.getOrAwaitValue().transferIfNotHandled()
            val expected = resources.getString(R.string.sync_is_done)
            assertThat(actual).isNotNull()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `uploadAllHabitsFromDbToCloud invoke syncAllToCloudUseCase with failure in showResultToast`() =
        runTest {
            val error = CloudError().failure()
            Mockito.`when`(syncAllToCloudUseCase.invoke()).thenReturn(error)
            mainViewModel.uploadAllHabitsFromDbToCloud()
            val actual = mainViewModel.showResultToast.getOrAwaitValue().transferIfNotHandled()
            val expected = mainViewModel.eventErrorText(error.error).transferIfNotHandled()
            assertThat(actual).isNotNull()
            assertThat(expected).isNotNull()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `downloadAllHabitsFromCloudToDb invoke syncAllFromCloudUseCase with success in showResultToast`() =
        runTest {
            Mockito.`when`(syncAllFromCloudUseCase.invoke()).thenReturn(Unit.success())
            mainViewModel.downloadAllHabitsFromCloudToDb()
            val actual = mainViewModel.showResultToast.getOrAwaitValue().transferIfNotHandled()
            val expected = resources.getString(R.string.sync_is_done)
            assertThat(actual).isNotNull()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `downloadAllHabitsFromCloudToDb invoke syncAllFromCloudUseCase with failure in showResultToast`() =
        runTest {
            val error = CloudError().failure()
            Mockito.`when`(syncAllFromCloudUseCase.invoke()).thenReturn(error)
            mainViewModel.downloadAllHabitsFromCloudToDb()
            val actual = mainViewModel.showResultToast.getOrAwaitValue().transferIfNotHandled()
            val expected = mainViewModel.eventErrorText(error.error).transferIfNotHandled()
            assertThat(actual).isNotNull()
            assertThat(expected).isNotNull()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `compareCloudAndDb show success toast`() = runTest {
        Mockito.`when`(areCloudAndDbEqualUseCase.invoke()).thenReturn(true.success())
        mainViewModel.compareCloudAndDb()
        val actual = mainViewModel.showResultToast.getOrAwaitValue().transferIfNotHandled()
        val expected = resources.getString(R.string.cloud_and_db_are_equals)
        assertThat(actual).isNotNull()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `compareCloudAndDb show failure toast`() = runTest {
        val error = CloudError().failure()
        Mockito.`when`(areCloudAndDbEqualUseCase.invoke()).thenReturn(error)
        mainViewModel.compareCloudAndDb()
        val actual = mainViewModel.showResultToast.getOrAwaitValue().transferIfNotHandled()
        val expected = mainViewModel.eventErrorText(error.error).transferIfNotHandled()
        assertThat(actual).isNotNull()
        assertThat(expected).isNotNull()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `compareCloudAndDb show habits aren't sync AlertDialog`() = runTest {
        Mockito.`when`(areCloudAndDbEqualUseCase.invoke()).thenReturn(false.success())
        mainViewModel.compareCloudAndDb()
        val actual = mainViewModel.showHabitsAreNotSyncDialogAlert.getOrAwaitValue()
        val expected = Unit
        assertThat(actual).isNotNull()
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        private const val SUCCESSFUL_SEARCH = "success"
        private const val UNSUCCESSFUL_SEARCH = "fail"
        private const val EMPTY_SEARCH = ""
        private const val EXPECTED_EMPTY = ""
        private const val DEFAULT_ID = 1
    }
}