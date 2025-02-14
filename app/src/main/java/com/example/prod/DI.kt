package com.example.prod

import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.apis.RetrofitTimesInstance
import com.example.apis.TimesApiService
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TimesApiModule {

    @Provides
    fun provideTimesApiService(): TimesApiService {
        return RetrofitTimesInstance.api
    }

    @Provides
    fun provideNewsRepository(api: TimesApiService): NewsRepository {
        return NewsRepository(api)
    }

    @Provides
    fun provideNewsViewModel(repository: NewsRepository): NewsViewModel {
        return NewsViewModel(repository)
    }

}
@Singleton
@Component(modules = [TimesApiModule::class])
interface AppComponent {

    fun inject(app: MainActivity)


    fun inject(viewModel: NewsViewModel)

    fun getNewsRepository(): NewsRepository
    fun getNewsViewModel(): NewsViewModel
}