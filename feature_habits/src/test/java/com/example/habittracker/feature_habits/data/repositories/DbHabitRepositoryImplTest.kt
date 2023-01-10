package com.example.habittracker.feature_habits.data.repositories

import com.example.habittracker.feature_habits.data.db.room.HabitDaoFake
import com.example.habittracker.feature_habits.data.repositories.DbHabitRepositoryImpl.Companion.ITEM_NOT_ADDED
import com.example.habittracker.core.domain.errors.Either.Failure
import com.example.habittracker.core.domain.errors.Either.Success
import com.example.habittracker.core.domain.errors.IoError.*
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.models.HabitListFilter
import com.example.habittracker.core.domain.models.HabitListOrderBy
import com.example.habittracker.core.domain.models.HabitListOrderBy.*
import com.example.habittracker.core.domain.models.HabitType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource

internal class DbHabitRepositoryImplTest {

    private lateinit var dbHabitRepositoryImpl: DbHabitRepositoryImpl
    private lateinit var habitDaoFake: HabitDaoFake
    private lateinit var habitToInsert: Habit

    @BeforeEach
    fun setUp() {
        habitDaoFake = HabitDaoFake()
        dbHabitRepositoryImpl =
            DbHabitRepositoryImpl(
                habitDaoFake
            )
        habitToInsert = habitDaoFake.habitToInsert.toHabit()
    }

    @ParameterizedTest
    @EnumSource(HabitType::class)
    @NullSource
    fun `getHabitList return lists of bad, good and both types habits`(habitTypeFilter: HabitType?) =
        runBlocking {
            habitDaoFake.initFilling()
            val emptyFilter =
                HabitListFilter(
                    orderBy = NAME_ASC,
                    search = EMPTY_SEARCH
                )
            val list = dbHabitRepositoryImpl.getHabitList(habitTypeFilter, emptyFilter).last()
            val actualHabitGood = list.find { it.type == HabitType.GOOD }
            val actualHabitBad = list.find { it.type == HabitType.BAD }
            when (habitTypeFilter) {
                HabitType.GOOD -> assertThat(actualHabitBad == null && actualHabitGood != null).isTrue()
                HabitType.BAD -> assertThat(actualHabitBad != null && actualHabitGood == null).isTrue()
                null -> assertThat(actualHabitBad != null && actualHabitGood != null).isTrue()
            }
        }

    @ParameterizedTest
    @EnumSource(HabitListOrderBy::class)
    fun `getHabitList return habit lists sorted by order from HabitListOrderBy`(habitListOrderBy: HabitListOrderBy) =
        runBlocking {
            habitDaoFake.initFilling()
            val habitTypeFilter = null
            val emptyFilter =
                HabitListFilter(
                    orderBy = habitListOrderBy,
                    search = EMPTY_SEARCH
                )
            val list = dbHabitRepositoryImpl.getHabitList(habitTypeFilter, emptyFilter).last()
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

    @Test
    fun `getHabitList return habit search result`() = runBlocking {
        habitDaoFake.initFilling()
        habitDaoFake.insertHabit(habitDaoFake.habitToInsert)
        val habitTypeFilter = null
        val searchString = habitToInsert.name
        val searchFilter =
            HabitListFilter(
                orderBy = NAME_ASC,
                search = searchString
            )
        val listFromRepository =
            dbHabitRepositoryImpl.getHabitList(habitTypeFilter, searchFilter).last()
        assertThat(listFromRepository).isNotEmpty()
        val unfilteredList = habitDaoFake.getUnfilteredList()?.map { it.toHabit() }
        assertThat(unfilteredList).isNotNull()
        if (unfilteredList != null) {
            val filteredList = unfilteredList.filter { it.name.contains(searchString) }
            println(filteredList)
            assertThat(listFromRepository.sortedBy { it.name }).isEqualTo(filteredList.sortedBy { it.name })
        }
    }

    @Test
    fun `getHabitList return empty search result`() = runBlocking {
        habitDaoFake.initFilling()
        val habitTypeFilter = null
        val searchFilter =
            HabitListFilter(
                orderBy = NAME_ASC,
                search = WRONG_SEARCH
            )
        val list = dbHabitRepositoryImpl.getHabitList(habitTypeFilter, searchFilter).last()
        assertThat(list.isEmpty()).isTrue()
    }

    @Test
    fun `getUnfilteredList return the same habit list from repository as in dao`() = runBlocking {
        habitDaoFake.initFilling()
        val repositoryHabitList = dbHabitRepositoryImpl.getUnfilteredList()
        assertThat(repositoryHabitList is Success).isTrue()
        if (repositoryHabitList is Success) {
            val daoHabitWithDoneList = habitDaoFake.getUnfilteredList()
            val daoHabitList = daoHabitWithDoneList?.map { it.toHabit() }
            assertThat(repositoryHabitList.result).isEqualTo(daoHabitList)
        }
    }

    @Test
    fun `getUnfilteredList habit list wasn't received`() = runBlocking {
        habitDaoFake.setErrorReturn()
        val habitList = dbHabitRepositoryImpl.getUnfilteredList()
        assertThat(habitList is Failure).isTrue()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `getHabitById return habit by id(true) or failure(false)`(isSuccess: Boolean) =
        runBlocking {
            val habitId = habitDaoFake.insertHabit(habitDaoFake.habitToInsert).toInt()
            val habitIdToSearch = if (isSuccess) habitId else HabitDaoFake.ERROR_HABIT_ID
            val result = dbHabitRepositoryImpl.getHabitById(habitIdToSearch)
            if (isSuccess) assertThat(result is Success).isTrue()
            else assertThat(result is Failure).isTrue()
        }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `upsertHabit insert habit and return new id (true) or failure(false)`(isSuccess: Boolean) =
        runBlocking {
            if (!isSuccess) habitDaoFake.setErrorReturn()
            val result =
                dbHabitRepositoryImpl.upsertHabit(habitToInsert)
            if (isSuccess) assertThat(result is Success && result.result != 0).isTrue()
            else assertThat(result is Failure).isTrue()
        }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `upsertHabit update habit and return ITEM_NOT_ADDED(true) or failure(false)`(isSuccess: Boolean) =
        runBlocking {
            val insertResult = dbHabitRepositoryImpl.upsertHabit(habitToInsert)
            assertThat(insertResult is Success).isTrue()
            if (insertResult is Success) {
                val habitToUpdate = if (isSuccess) habitToInsert.copy(
                    id = insertResult.result,
                    name = "new habit name"
                ) else habitToInsert.copy(
                    id = insertResult.result,
                    name = "error habit"
                )
                val updateResult = dbHabitRepositoryImpl.upsertHabit(habitToUpdate)
                if (isSuccess) assertThat(
                    updateResult is Success
                            && updateResult.result == ITEM_NOT_ADDED
                ).isTrue()
                else assertThat(updateResult is Failure).isTrue()
            }
        }

    @Test
    fun `upsertHabit return HabitAlreadyExistsError for insert if habit name is duplicated`() =
        runBlocking {
            val firstInsertResult = dbHabitRepositoryImpl.upsertHabit(habitToInsert)
            assertThat(firstInsertResult is Success).isTrue()
            val secondInsertResult = dbHabitRepositoryImpl.upsertHabit(habitToInsert)
            assertThat(
                secondInsertResult is Failure
                        && secondInsertResult.error is HabitAlreadyExistsError
            ).isTrue()
        }

    @Test
    fun `upsertHabit return HabitAlreadyExistsError for update if habit name is duplicated`() =
        runBlocking {
            val firstHabit = habitToInsert
            val firstHabitNewId = dbHabitRepositoryImpl.upsertHabit(firstHabit)
            assertThat(firstHabitNewId is Success).isTrue()
            val secondHabit = firstHabit.copy(name = "second habit")
            val secondHabitNewId = dbHabitRepositoryImpl.upsertHabit(secondHabit)
            assertThat(secondHabitNewId is Success).isTrue()
            if (firstHabitNewId is Success && secondHabitNewId is Success) {
                val duplicateHabitToUpdate = secondHabit.copy(
                    id = secondHabitNewId.result,
                    name = firstHabit.name
                )
                val duplicateUpdateResult =
                    dbHabitRepositoryImpl.upsertHabit(duplicateHabitToUpdate)
                assertThat(
                    duplicateUpdateResult is Failure
                            && duplicateUpdateResult.error is HabitAlreadyExistsError
                ).isTrue()
            }

        }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `deleteHabit delete habit and return success(true) or failure(false)`(isSuccess: Boolean) =
        runBlocking {
            if (!isSuccess) habitDaoFake.setErrorDeleteReturn()
            val habitDbModelToInsert = habitDaoFake.habitToInsert
            val newHabitId = habitDaoFake.insertHabit(habitDbModelToInsert).toInt()
            val findHabit = habitDaoFake.findHabit(habitDbModelToInsert)
            assertThat(findHabit).isNotNull()
            val result = dbHabitRepositoryImpl.deleteHabit(
                habitDbModelToInsert.copy(id = newHabitId).toHabit()
            )
            if (isSuccess) {
                assertThat(result is Success).isTrue()
                val postFind = habitDaoFake.findHabit(habitDbModelToInsert)
                assertThat(postFind).isNull()
            } else {
                assertThat(result is Failure && result.error is DeletingHabitError).isTrue()
            }
        }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `deleteAllHabits delete all habits and return success(true) or failure(false)`(isSuccess: Boolean) =
        runBlocking {
            if (!isSuccess) habitDaoFake.setErrorDeleteReturn()
            habitDaoFake.initFilling()
            val result = dbHabitRepositoryImpl.deleteAllHabits()
            if (isSuccess) {
                assertThat(result is Success).isTrue()
                val listAfterDelete = habitDaoFake.getUnfilteredList()
                assertThat(listAfterDelete).isEmpty()
            } else {
                assertThat(result is Failure && result.error is DeletingAllHabitsError).isTrue()
            }
        }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `addHabitDone insert habit and return new id (true) or failure(false)`(isSuccess: Boolean) =
        runBlocking {
            val habitDoneToInsert = habitDaoFake.habitDoneToInsert
            if (!isSuccess) habitDaoFake.setErrorReturn()
            val result =
                dbHabitRepositoryImpl.addHabitDone(habitDoneToInsert.toHabitDone("habitUid"))
            if (isSuccess) assertThat(result is Success && result.result != 0).isTrue()
            else assertThat(result is Failure).isTrue()
        }

    @Test
    fun `deleteHabitDone delete habitDone`() = runBlocking {
        val habitDoneToInsert = habitDaoFake.habitDoneToInsert
        val habitDoneNewId = habitDaoFake.insertHabitDone(habitDoneToInsert).toInt()
        dbHabitRepositoryImpl.deleteHabitDone(habitDoneNewId)
        val find = habitDaoFake.findHabitDone(habitDoneToInsert)
        assertThat(find).isNull()
    }

    companion object {
        private const val EMPTY_SEARCH = ""
        private const val WRONG_SEARCH = "wrong search"
    }
}

