package com.example.prod

import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.apis.RetrofitTimesInstance
import com.example.apis.TimesApiService
import com.example.tickersapi.RetrofitTickersInstance
import com.example.tickersapi.TickersApiService
import com.example.tickersapi.TickersRepository
import com.example.tickersapi.TickersViewModel
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


@Module
@InstallIn(SingletonComponent::class)
class TickersApiModel{

    @Provides
    fun provideTickersApiService(): TickersApiService{
        return RetrofitTickersInstance.api
    }

    @Provides
    fun provideTickersRepository(api: TickersApiService):TickersRepository{
        return TickersRepository(api)
    }

    @Provides
    fun provideTickersViewModel(repositiry: TickersRepository): TickersViewModel{
        return TickersViewModel(repositiry)
    }
}
