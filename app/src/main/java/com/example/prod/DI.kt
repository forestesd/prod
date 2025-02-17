package com.example.prod

import android.app.Application
import android.content.Context
import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.apis.RetrofitSearchTimesInstance
import com.example.apis.RetrofitTimesInstance
import com.example.apis.TimesApiService
import com.example.financedate.FinaceViewModel
import com.example.financedate.db.FinanceDB
import com.example.financedate.db.GoalDAO
import com.example.financedate.db.TransactionDao
import com.example.tickersapi.RetrofitTickersInstance
import com.example.tickersapi.TickersApiService
import com.example.tickersapi.TickersRepository
import com.example.tickersapi.TickersViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class TimesApiModule {

    @Provides
    @Singleton
    @Named("newsApi")
    fun provideTimesApiService(): TimesApiService {
        return RetrofitTimesInstance.api
    }

    @Provides
    @Singleton
    @Named("searchApi")
    fun provideSearchApiService(): TimesApiService {
        return RetrofitSearchTimesInstance.api
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        @Named("newsApi") timesApiService: TimesApiService,
        @Named("searchApi") searchApiService: TimesApiService
    ): NewsRepository {
        return NewsRepository(timesApiService, searchApiService)
    }

    @Provides
    @Singleton
    fun provideNewsViewModel(repository: NewsRepository): NewsViewModel {
        return NewsViewModel(repository)
    }

}


@Module
class TickersApiModel {
    @Provides
    @Singleton
    fun provideTickersApiService(): TickersApiService {
        return RetrofitTickersInstance.api
    }

    @Provides
    @Singleton
    fun provideTickersRepository(api: TickersApiService, context: Context): TickersRepository {
        return TickersRepository(context, api)
    }

    @Provides
    @Singleton
    fun provideTickersViewModel(repository: TickersRepository): TickersViewModel {
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

@Module
class DataBaseFinanceModule{

    @Provides
    @Singleton
    fun provideGoalDAO(financeDb: FinanceDB): GoalDAO{
        return financeDb.goalDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(financeDb: FinanceDB): TransactionDao{
        return financeDb.transactionDao()
    }
    @Provides
    @Singleton
    fun provideFinanceDb(context: Context): FinanceDB{
        return FinanceDB.getDB(context)
    }
}

@Component(modules = [TimesApiModule::class, TickersApiModel::class, DataBaseFinanceModule::class, AppModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(newsViewModel: NewsViewModel)
    fun inject(newsRepository: NewsRepository)
    fun inject(finaceViewModel: FinaceViewModel)
}
