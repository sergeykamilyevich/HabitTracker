package com.example.habittracker.domain.usecases.network

import javax.inject.Inject

class NetworkUseCase @Inject constructor(
    val deleteHabitFromApiUseCase: DeleteHabitFromApiUseCase,
    val getHabitListFromApiUseCase: GetHabitListFromApiUseCase,
    val postHabitDoneToApiUseCase: PostHabitDoneToApiUseCase,
    val putHabitToApiUseCase: PutHabitToApiUseCase
) {
}