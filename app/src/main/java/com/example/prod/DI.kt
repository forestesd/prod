package com.example.prod

import android.app.Application
import android.content.Context
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
    fun provideTickersRepository(api: TickersApiService, context: Context):TickersRepository{
        return TickersRepository(context, api)
    }

    @Provides
    @Singleton
    fun provideTickersViewModel(repository: TickersRepository): TickersViewModel{
        return TickersViewModel(repository)
    }
}
@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext
}
@Component(modules = [TimesApiModule::class, TickersApiModel::class, AppModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
}
