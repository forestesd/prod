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
import javax.inject.Singleton

@Module
class TimesApiModule {

    @Provides
    @Singleton
    fun provideTimesApiService(): TimesApiService {
        return RetrofitTimesInstance.api
    }

    @Provides
    @Singleton
    fun provideNewsRepository(api: TimesApiService): NewsRepository {
        return NewsRepository(api)
    }

    @Provides
    @Singleton
    fun provideNewsViewModel(repository: NewsRepository): NewsViewModel {
        return NewsViewModel(repository)
    }

}


@Module
class TickersApiModel{

    @Provides
    @Singleton
    fun provideTickersApiService(): TickersApiService{
        return RetrofitTickersInstance.api
    }

    @Provides
    @Singleton
    fun provideTickersRepository(api: TickersApiService):TickersRepository{
        return TickersRepository(api)
    }

    @Provides
    @Singleton
    fun provideTickersViewModel(repositiry: TickersRepository): TickersViewModel{
        return TickersViewModel(repositiry)
    }
}

@Component(modules = [TimesApiModule::class, TickersApiModel::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
}
