package com.example.habittracker.di.components

import com.example.habittracker.core_api.di.mediators.AppWithCoreFacade
import com.example.habittracker.db_api.di.mediators.AppWithDbFacade
import com.example.habittracker.network_api.di.mediators.AppWithNetworkFacade

interface AppWithFacade :
    AppWithNetworkFacade,
    AppWithDbFacade,
    AppWithCoreFacade