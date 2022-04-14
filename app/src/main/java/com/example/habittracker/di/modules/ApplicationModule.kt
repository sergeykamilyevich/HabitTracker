package com.example.habittracker.di.modules


//@Module
//class ApplicationModule {
////
////    @Provides
////    @Singleton
////    fun provideViewModelFactory(
////        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
////    ) = object : ViewModelProvider.Factory {
////        override fun <T : ViewModel> create(modelClass: Class<T>): T {
////            return requireNotNull(providers[modelClass as Class<out ViewModel>]).get() as T
////        }
////    }
//}