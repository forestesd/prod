package com.example.prod

import android.app.Application
import android.content.Context
import com.example.apis.data.repository.NewsRepository
import com.example.apis.data.NewsViewModel
import com.example.apis.data.RetrofitSearchTimesInstance
import com.example.apis.data.RetrofitTimesInstance
import com.example.apis.data.TimesApiService
import com.example.apis.domain.use_cases.GetNewsPullToRefreshUseCase
import com.example.apis.domain.use_cases.GetNewsUseCase
import com.example.apis.domain.use_cases.GetSearchNewsUseCase
import com.example.financedate.FinanceViewModel
import com.example.financedate.db.FinanceDB
import com.example.financedate.db.GoalDAO
import com.example.financedate.db.TransactionDao
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel
import com.example.notesdata.db.NewsDao
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostDatabase
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.TagDao
import com.example.tickersapi.RetrofitTickersInstance
import com.example.tickersapi.TickersApiService
import com.example.tickersapi.TickersRepository
import com.example.tickersapi.TickersViewModel
import com.example.tickersapi.TickersWebSocket
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
class TimesApiModule {

    @Provides
    @Singleton
    @Named("newsApi")
    fun provideTimesApiService() = RetrofitTimesInstance.api


    @Provides
    @Singleton
    @Named("searchApi")
    fun provideSearchApiService() = RetrofitSearchTimesInstance.api


    @Provides
    @Singleton
    fun provideNewsRepository(
        @Named("newsApi") timesApiService: TimesApiService,
        @Named("searchApi") searchApiService: TimesApiService
    ) = NewsRepository(timesApiService, searchApiService)


    @Provides
    @Singleton
    fun provideNewsViewModel(
        getNewsUseCase: GetNewsUseCase,
        getNewsPullToRefreshUseCase: GetNewsPullToRefreshUseCase,
        getSearchNewsUseCase: GetSearchNewsUseCase
    ) = NewsViewModel(
        getNewsUseCase = getNewsUseCase,
        getNewsPullToRefreshUseCase = getNewsPullToRefreshUseCase,
        getSearchNewsUseCase = getSearchNewsUseCase
    )


    @Provides
    @Singleton
    fun getNewsUseCase(newsRepository: NewsRepository) = GetNewsUseCase(newsRepository)

    @Provides
    @Singleton
    fun getNewsPullToRefreshUseCase(newsRepository: NewsRepository) = GetNewsPullToRefreshUseCase(newsRepository)

    @Provides
    @Singleton
    fun getSearchNewsUseCase(newsRepository: NewsRepository) = GetSearchNewsUseCase(newsRepository)

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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideApiUrl(): String {
        return "wss://ws.finnhub.io?"
    }

    @Provides
    @Singleton
    fun provideTickersRepository(
        api: TickersApiService,
        context: Context,
        client: OkHttpClient,
        apiUrl: String
    ): TickersRepository {
        return TickersRepository(context, api, client, apiUrl)
    }

    @Provides
    @Singleton
    fun provideTickersViewModel(
        repository: TickersRepository,
        tickersWebSocket: TickersWebSocket
    ): TickersViewModel {
        return TickersViewModel(repository, tickersWebSocket)
    }
}

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext
}

@Module
class DataBaseFinanceModule {

    @Provides
    @Singleton
    fun provideGoalDAO(financeDb: FinanceDB): GoalDAO {
        return financeDb.goalDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(financeDb: FinanceDB): TransactionDao {
        return financeDb.transactionDao()
    }

    @Provides
    @Singleton
    fun provideFinanceDb(context: Context): FinanceDB {
        return FinanceDB.getDB(context)
    }
}


@Module
class PostDatabaseModule {

    @Provides
    @Singleton
    fun providePostDatabase(context: Context): PostDatabase {
        return PostDatabase.getDB(context)
    }

    @Provides
    @Singleton
    fun providePostDao(postDatabase: PostDatabase): PostDao {
        return postDatabase.postDao()
    }

    @Provides
    @Singleton
    fun providePostImageDao(postDatabase: PostDatabase): PostImageDao {
        return postDatabase.postImageDao()
    }

    @Provides
    @Singleton
    fun provideTagDao(postDatabase: PostDatabase): TagDao {
        return postDatabase.tagDao()
    }

    @Provides
    @Singleton
    fun providePostTagDao(postDatabase: PostDatabase): PostTagDao {
        return postDatabase.postTagDao()
    }

    @Provides
    @Singleton
    fun provideNewsDao(postDatabase: PostDatabase): NewsDao {
        return postDatabase.newsDao()
    }
}

@Subcomponent(modules = [DataBaseFinanceModule::class])
interface FinanceComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FinanceComponent
    }

    fun inject(financeViewModel: FinanceViewModel)
}

@Subcomponent(modules = [PostDatabaseModule::class])
interface PostComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): PostComponent
    }

    fun inject(notesViewModel: NotesViewModel)
    fun inject(addNoteViewModel: AddNoteViewModel)
}


@Subcomponent(modules = [TimesApiModule::class])
interface TimesComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TimesComponent
    }

    fun inject(newsViewModel: NewsViewModel)
    fun inject(newsRepository: NewsRepository)
}

@Subcomponent(modules = [TickersApiModel::class])
interface TickersComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TickersComponent
    }

    fun inject(tickersViewModel: TickersViewModel)
}


@Singleton
@Component(
    modules = [
        AppModule::class,
        TimesApiModule::class,
        TickersApiModel::class,
        PostDatabaseModule::class,
        DataBaseFinanceModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}

