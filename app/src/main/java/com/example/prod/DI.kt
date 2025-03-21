package com.example.prod

import android.app.Application
import android.content.Context
import com.example.apis.data.NewsViewModel
import com.example.apis.data.RetrofitSearchTimesInstance
import com.example.apis.data.RetrofitTimesInstance
import com.example.apis.data.TimesApiService
import com.example.apis.data.repository.NewsRepositoryInterface
import com.example.apis.domain.use_cases.GetNewsPullToRefreshUseCase
import com.example.apis.domain.use_cases.GetNewsUseCase
import com.example.apis.domain.use_cases.GetSearchNewsUseCase
import com.example.financedate.FinanceViewModel
import com.example.financedate.db.FinanceDB
import com.example.financedate.db.GoalDAO
import com.example.financedate.db.TransactionDao
import com.example.notesdata.data.AddNoteViewModel
import com.example.notesdata.data.NotesViewModel
import com.example.notesdata.data.db.NewsDao
import com.example.notesdata.data.db.PostDao
import com.example.notesdata.data.db.PostDatabase
import com.example.notesdata.data.repository.AddNoteRepository
import com.example.notesdata.domain.repository.AddNoteRepositoryInterface
import com.example.notesdata.domain.use_cases.SaveNoteUseCase
import com.example.tickersapi.data.TickersViewModel
import com.example.tickersapi.data.remote.RetrofitTickersInstance
import com.example.tickersapi.data.remote.TickersApiService
import com.example.tickersapi.data.remote.TickersWebSocket
import com.example.tickersapi.data.repository.TickersRepositoryInterface
import com.example.tickersapi.domain.use_cases.GetCompanyInfoUseCase
import com.example.tickersapi.domain.use_cases.SearchCompanyUseCase
import com.example.tickersapi.domain.use_cases.WebSocketOpenUseCase
import dagger.Binds
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
    ) = NewsRepositoryInterface(timesApiService, searchApiService)


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
    fun getNewsUseCase(newsRepositoryInterface: NewsRepositoryInterface) =
        GetNewsUseCase(newsRepositoryInterface)

    @Provides
    @Singleton
    fun getNewsPullToRefreshUseCase(newsRepository: NewsRepositoryInterface) =
        GetNewsPullToRefreshUseCase(newsRepository)

    @Provides
    @Singleton
    fun getSearchNewsUseCase(newsRepository: NewsRepositoryInterface) =
        GetSearchNewsUseCase(newsRepository)

}


@Module
class TickersApiModel {
    @Provides
    @Singleton
    fun provideTickersApiService() = RetrofitTickersInstance.api


    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder().build()


    @Provides
    @Singleton
    fun provideApiUrl() = "wss://ws.finnhub.io?"


    @Provides
    @Singleton
    fun provideTickersRepository(
        api: TickersApiService,
        context: Context,
        client: OkHttpClient,
        apiUrl: String
    ) = TickersRepositoryInterface(context, api, client, apiUrl)

    @Provides
    @Singleton
    fun provideGetCompanyInfoUseCase(tickersRepositoryInterface: TickersRepositoryInterface) =
        GetCompanyInfoUseCase(tickersRepositoryInterface)

    @Provides
    @Singleton
    fun provideSearchCompanyUseCase(tickersRepositoryInterface: TickersRepositoryInterface) =
        SearchCompanyUseCase(tickersRepositoryInterface)

    @Provides
    @Singleton
    fun provideWebSocketOpenUseCase(tickersRepositoryInterface: TickersRepositoryInterface) =
        WebSocketOpenUseCase(tickersRepositoryInterface)

    @Provides
    @Singleton
    fun provideTickersViewModel(
        getCompanyInfoUseCase: GetCompanyInfoUseCase,
        searchCompanyUseCase: SearchCompanyUseCase,
        webSocketOpenUseCase: WebSocketOpenUseCase,
        tickersWebSocket: TickersWebSocket
    ) = TickersViewModel(
        getCompanyInfoUseCase = getCompanyInfoUseCase,
        searchCompanyUseCase = searchCompanyUseCase,
        webSocketOpenUseCase = webSocketOpenUseCase,
        webSocket = tickersWebSocket
    )
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
    fun providePostDatabase(context: Context) = PostDatabase.getDB(context)


    @Provides
    @Singleton
    fun providePostDao(postDatabase: PostDatabase) = postDatabase.postDao()


    @Provides
    @Singleton
    fun providePostImageDao(postDatabase: PostDatabase) = postDatabase.postImageDao()


    @Provides
    @Singleton
    fun provideTagDao(postDatabase: PostDatabase) = postDatabase.tagDao()


    @Provides
    @Singleton
    fun providePostTagDao(postDatabase: PostDatabase) = postDatabase.postTagDao()


    @Provides
    @Singleton
    fun provideNewsDao(postDatabase: PostDatabase) = postDatabase.newsDao()

    @Provides
    @Singleton
    fun provideSaveNoteUseCase(addNoteRepositoryInterface: AddNoteRepositoryInterface) =
        SaveNoteUseCase(addNoteRepositoryInterface)
}

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAddNoteRepository(addNoteRepository: AddNoteRepository): AddNoteRepositoryInterface
}


@Singleton
@Component(
    modules = [
        AppModule::class,
        TimesApiModule::class,
        TickersApiModel::class,
        PostDatabaseModule::class,
        DataBaseFinanceModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}

