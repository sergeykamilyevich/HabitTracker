//package com.example.habittracker.feature_habits.presentation.view_models
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.habittracker.core.domain.errors.Either
//import com.example.habittracker.core.domain.errors.IoError
//import com.example.habittracker.core.domain.errors.IoError.*
//import com.example.habittracker.core.domain.errors.failure
//import com.example.habittracker.core.domain.errors.success
//import com.example.habittracker.core.domain.models.Habit
//import com.example.habittracker.core.domain.models.HabitPriority
//import com.example.habittracker.core.domain.models.HabitType
//import com.example.habittracker.feature_habits.data.repositories.DbHabitRepositoryImpl
//import com.example.habittracker.feature_habits.domain.usecases.common.*
//import com.example.habittracker.feature_habits.domain.usecases.db.*
//import com.example.habittracker.feature_habits.domain.usecases.network.*
//import com.example.habittracker.network_impl.repositories.CloudHabitRepositoryImpl
//import com.google.common.truth.Truth.assertThat
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mockito
//import org.mockito.kotlin.mock
//import org.robolectric.RuntimeEnvironment
//import java.util.concurrent.TimeoutException
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(AndroidJUnit4::class)
//class MainViewModelTest2 {
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var mainViewModel: MainViewModel
//
//    private val dbHabitRepository = mock<DbHabitRepositoryImpl>()
//    private val addHabitDoneUseCase = mock<AddHabitDoneUseCase>()
//    private val deleteHabitDoneUseCase = mock<DeleteHabitDoneUseCase>()
//    private val deleteHabitUseCase = mock<DeleteHabitUseCase>()
//    private val getHabitUseCase = mock<GetHabitUseCase>()
//    private val getHabitListUseCase = mock<GetHabitListUseCase>()
//    private val upsertHabitUseCase = mock<UpsertHabitUseCase>()
//    private val deleteAllHabitsUseCase = mock<DeleteAllHabitsUseCase>()
//    private val getUnfilteredHabitListUseCase = mock<GetUnfilteredHabitListUseCase>()
//    private val dbUseCase = DbUseCase(
//        dbHabitRepository,
//        addHabitDoneUseCase,
//        deleteHabitDoneUseCase,
//        deleteHabitUseCase,
//        getHabitUseCase,
//        getHabitListUseCase,
//        getUnfilteredHabitListUseCase,
//        upsertHabitUseCase,
//        deleteAllHabitsUseCase
//    )
//
//    private val cloudHabitRepository = mock<CloudHabitRepositoryImpl>()
//    private val deleteAllHabitsFromCloudUseCase = mock<DeleteAllHabitsFromCloudUseCase>()
//    private val deleteHabitFromCloudUseCase = mock<DeleteHabitFromCloudUseCase>()
//    private val getHabitListFromCloudUseCase = mock<GetHabitListFromCloudUseCase>()
//    private val postHabitDoneToCloudUseCase = mock<PostHabitDoneToCloudUseCase>()
//    private val getCloudErrorUseCase = mock<GetCloudErrorUseCase>()
//    private val cloudUseCase = CloudUseCase(
//        cloudHabitRepository,
//        deleteAllHabitsFromCloudUseCase,
//        deleteHabitFromCloudUseCase,
//        getHabitListFromCloudUseCase,
//        postHabitDoneToCloudUseCase,
//        getCloudErrorUseCase
//    )
//    private val syncAllFromCloudUseCase = mock<SyncAllFromCloudUseCase>()
//    private val syncAllToCloudUseCase = mock<SyncAllToCloudUseCase>()
//    private val putHabitAndSyncWithDbUseCase = mock<PutHabitAndSyncWithDbUseCase>()
//    private val areCloudAndDbEqualUseCase = mock<AreCloudAndDbEqualUseCase>()
//    private val syncUseCase = SyncUseCase(
//        syncAllFromCloudUseCase,
//        syncAllToCloudUseCase,
//        putHabitAndSyncWithDbUseCase,
//        areCloudAndDbEqualUseCase
//    )
//
//    private val application = RuntimeEnvironment.getApplication()
//    private val resources = Resources.Base(application)
//    private val habit: Habit = Habit(
//        name = "habit name",
//        description = "habit to insert",
//        priority = HabitPriority.NORMAL,
//        type = HabitType.GOOD,
//        color = 1,
//        recurrenceNumber = 1,
//        recurrencePeriod = 1
//    )
//
//    @Before
//    fun setUp() {
//        mainViewModel = MainViewModel(dbUseCase, cloudUseCase, syncUseCase, resources)
//    }
//
//    @After
//    fun tearDown() {
////        Mockito.reset(dbUseCase)
////        Mockito.reset(cloudUseCase)
////        Mockito.reset(getCloudErrorUseCase)
////        Mockito.reset(syncUseCase)
//    }
//
//    private fun deleteHabitItemTest(
//        dbReturn: Either<DeletingHabitError, Unit>,
//        cloudReturn: Either<CloudError, Unit>
//    ): String? {
//        runTest {
//            Mockito.`when`(deleteHabitUseCase.invoke(habit))
//                .thenReturn(dbReturn)
//            Mockito.`when`(deleteHabitFromCloudUseCase.invoke(habit))
//                .thenReturn(cloudReturn)
//            mainViewModel.deleteHabitItem(habit)
//        }
//        val actual = mainViewModel.ioError.getOrAwaitValue()
//        return actual.transferIfNotHandled()
//    }
//
//    @Test
//    fun `deleteHabitItem return db error`() = runTest {
//        val dbReturn = DeletingHabitError().failure()
//        val cloudReturn = Unit.success()
//        val actual = deleteHabitItemTest(dbReturn, cloudReturn)
//        assertThat(actual).isEqualTo("Deleting habits failed")
//    }
//
//    @Test
//    fun `deleteHabitItem return cloud error`() = runTest {
//        val dbReturn = Unit.success()
//        val cloudReturn = CloudError().failure()
//        val actual = deleteHabitItemTest(dbReturn, cloudReturn)
//        assertThat(actual).isEqualTo("Network error: ")
//    }
//
//    @Test
//    fun `deleteHabitItem success deleted habit from db and cloud`() = runTest {
//        val dbReturn = Unit.success()
//        val cloudReturn = Unit.success()
//        var liveDataException = false
//        try {
//            deleteHabitItemTest(dbReturn, cloudReturn)
//        } catch (e: Exception) {
//            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
//            liveDataException = true
//        }
//        Mockito.verify(deleteHabitUseCase, Mockito.times(1)).invoke(habit)
//        Mockito.verify(deleteHabitFromCloudUseCase, Mockito.times(1)).invoke(habit)
//        assertThat(liveDataException).isTrue()
//    }
//
//    private fun deleteAllHabitsFromCloudTest(
//        cloudReturn: Either<IoError, Unit>
//    ): String? {
//        runTest {
//            Mockito.`when`(deleteAllHabitsFromCloudUseCase.invoke())
//                .thenReturn(cloudReturn)
//            mainViewModel.deleteAllHabitsFromCloud()
//        }
//        val actual = mainViewModel.ioError.getOrAwaitValue()
//        return actual.transferIfNotHandled()
//    }
//
//    @Test
//    fun `deleteAllHabitsFromCloud return cloud error`() = runTest {
//        val cloudReturn = CloudError().failure()
//        val actual = deleteAllHabitsFromCloudTest(cloudReturn)
//        assertThat(actual).isEqualTo("Network error: ")
//    }
//
//    @Test
//    fun `deleteAllHabitsFromCloud return DeletingAllHabitsError`() = runTest {
//        val cloudReturn = DeletingAllHabitsError().failure()
//        val actual = deleteAllHabitsFromCloudTest(cloudReturn)
//        assertThat(actual).isEqualTo("Deleting habits failed")
//    }
//
//    @Test
//    fun `deleteAllHabitsFromCloud success deleted`() = runTest {
//        val cloudReturn = Unit.success()
//        var liveDataException = false
//        try {
//            deleteAllHabitsFromCloudTest(cloudReturn)
//        } catch (e: Exception) {
//            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
//            liveDataException = true
//        }
//        Mockito.verify(deleteAllHabitsFromCloudUseCase, Mockito.times(1)).invoke()
//        assertThat(liveDataException).isTrue()
//    }
//
//
//    private fun deleteAllHabitsFromDbTest(
//        dbReturn: Either<IoError, Unit>
//    ): String? {
//        runTest {
//            Mockito.`when`(deleteAllHabitsUseCase.invoke()).thenReturn(dbReturn)
//            mainViewModel.deleteAllHabitsFromDb()
//        }
//        val actual = mainViewModel.ioError.getOrAwaitValue()
//        return actual.transferIfNotHandled()
//    }
//
//    @Test
//    fun `deleteAllHabitsFromDb return DeletingAllHabitsError`() = runTest {
//        val dbReturn = DeletingAllHabitsError().failure()
//        val actual = deleteAllHabitsFromDbTest(dbReturn)
//        assertThat(actual).isEqualTo("Deleting habits failed")
//    }
//
//    @Test
//    fun `deleteAllHabitsFromDb success deleted`() = runTest {
//        val dbReturn = Unit.success()
//        var liveDataException = false
//        try {
//            deleteAllHabitsFromDbTest(dbReturn)
//        } catch (e: Exception) {
//            assertThat(e is TimeoutException && e.message == "LiveData value was never set").isTrue()
//            liveDataException = true
//        }
//        Mockito.verify(deleteAllHabitsUseCase, Mockito.times(1)).invoke()
//        assertThat(liveDataException).isTrue()
//    }
//
//}