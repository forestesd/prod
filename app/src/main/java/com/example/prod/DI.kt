package com.example.prod

import android.app.Application
import android.content.Context
import com.example.apis.data.NewsViewModel
import com.example.apis.data.RetrofitSearchTimesInstance
import com.example.apis.data.RetrofitTimesInstance
import com.example.apis.data.TimesApiService
import com.example.apis.data.repository.NewsRepository
import com.example.apis.domain.repository.NewsRepositoryInterface
import com.example.apis.domain.use_cases.GetFiltersUseCase
import com.example.apis.domain.use_cases.GetNewsPullToRefreshUseCase
import com.example.apis.domain.use_cases.GetNewsUseCase
import com.example.apis.domain.use_cases.GetSearchNewsUseCase
import com.example.financedate.data.db.FinanceDB
import com.example.financedate.data.db.GoalDAO
import com.example.financedate.data.db.TransactionDao
import com.example.financedate.data.repository.FinanceRepository
import com.example.financedate.domain.repository.FinanceRepositoryInterface
import com.example.financedate.domain.use_cases.AddGoalUseCase
import com.example.financedate.domain.use_cases.AddTransactionUseCase
import com.example.financedate.domain.use_cases.AllAmountUseCase
import com.example.financedate.domain.use_cases.DeleteGoalUseCase
import com.example.financedate.domain.use_cases.GetGoalProgressUseCase
import com.example.financedate.domain.use_cases.GetTransactionsUseCase
import com.example.notesdata.data.db.PostDatabase
import com.example.notesdata.data.repository.AddNoteRepository
import com.example.notesdata.data.repository.NotesRepository
import com.example.notesdata.domain.repository.AddNoteRepositoryInterface
import com.example.notesdata.domain.repository.NoteRepositoryInterface
import com.example.notesdata.domain.use_cases.CheckFavoritesUseCase
import com.example.notesdata.domain.use_cases.GetAllNotesUseCase
import com.example.notesdata.domain.use_cases.GetAllTagsUseCase
import com.example.notesdata.domain.use_cases.SaveNoteUseCase
import com.example.notesdata.domain.use_cases.ToggleFavoriteUseCase
import com.example.tickersapi.data.TickersViewModel
import com.example.tickersapi.data.remote.RetrofitTickersInstance
import com.example.tickersapi.data.remote.TickersApiService
import com.example.tickersapi.data.remote.TickersWebSocket
import com.example.tickersapi.data.repository.TickersRepository
import com.example.tickersapi.domain.repository.TickersRepositoryInterface
import com.example.tickersapi.domain.use_cases.GetCompanyInfoUseCase
import com.example.tickersapi.domain.use_cases.SearchCompanyUseCase
import com.example.tickersapi.domain.use_cases.WebSocketOpenUseCase
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
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
        @Named("searchApi") searchApiService: TimesApiService,
        context: Context
    ) = NewsRepository(timesApiService, searchApiService, context)


    @Provides
    @Singleton
    fun provideNewsViewModel(
        getNewsUseCase: GetNewsUseCase,
        getNewsPullToRefreshUseCase: GetNewsPullToRefreshUseCase,
        getSearchNewsUseCase: GetSearchNewsUseCase,
        getFiltersUseCase: GetFiltersUseCase
    ) = NewsViewModel(
        getNewsUseCase = getNewsUseCase,
        getNewsPullToRefreshUseCase = getNewsPullToRefreshUseCase,
        getSearchNewsUseCase = getSearchNewsUseCase,
        getFiltersUseCase = getFiltersUseCase
    )


    @Provides
    @Singleton
    fun getNewsUseCase(newsRepositoryInterface: NewsRepository) =
        GetNewsUseCase(newsRepositoryInterface)

    @Provides
    @Singleton
    fun getNewsPullToRefreshUseCase(newsRepositoryInterface: NewsRepositoryInterface) =
        GetNewsPullToRefreshUseCase(newsRepositoryInterface)

    @Provides
    @Singleton
    fun getSearchNewsUseCase(newsRepositoryInterface: NewsRepositoryInterface) =
        GetSearchNewsUseCase(newsRepositoryInterface)

    @Provides
    @Singleton
    fun provideGetFiltersUseCase(newsRepositoryInterface: NewsRepositoryInterface) =
        GetFiltersUseCase(newsRepositoryInterface)

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
    ) = TickersRepository(context, api, client, apiUrl)

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
    fun provideGoalDAO(financeDb: FinanceDB) = financeDb.goalDao()


    @Provides
    @Singleton
    fun provideTransactionDao(financeDb: FinanceDB) = financeDb.transactionDao()


    @Provides
    @Singleton
    fun provideFinanceDb(context: Context) = FinanceDB.getDB(context)


    @Provides
    @Singleton
    fun provideAddGoalUseCase(financeRepositoryInterface: FinanceRepositoryInterface) =
        AddGoalUseCase(financeRepositoryInterface)

    @Provides
    @Singleton
    fun provideAddTransactionUseCase(financeRepositoryInterface: FinanceRepositoryInterface) =
        AddTransactionUseCase(financeRepositoryInterface)

    @Provides
    @Singleton
    fun provideAllAmountUseCase(financeRepositoryInterface: FinanceRepositoryInterface) =
        AllAmountUseCase(financeRepositoryInterface)

    @Provides
    @Singleton
    fun provideDeleteGoalUseCase(financeRepositoryInterface: FinanceRepositoryInterface) =
        DeleteGoalUseCase(financeRepositoryInterface)

    @Provides
    @Singleton
    fun provideGetGoalUseCase(financeRepositoryInterface: FinanceRepositoryInterface) =
        GetGoalProgressUseCase(financeRepositoryInterface)

    @Provides
    @Singleton
    fun provideGetTransactionsUseCase(financeRepositoryInterface: FinanceRepositoryInterface) =
        GetTransactionsUseCase(financeRepositoryInterface)
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

    @Provides
    @Singleton
    fun provideGetAllNotesUseCase(noteRepositoryInterface: NoteRepositoryInterface) =
        GetAllNotesUseCase(noteRepositoryInterface)

    @Provides
    @Singleton
    fun provideGetAllTagsUseCase(noteRepositoryInterface: NoteRepositoryInterface) =
        GetAllTagsUseCase(noteRepositoryInterface)

    @Provides
    @Singleton
    fun provideCheckFavoritesUseCase(noteRepositoryInterface: NoteRepositoryInterface) =
        CheckFavoritesUseCase(noteRepositoryInterface)

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(noteRepositoryInterface: NoteRepositoryInterface) =
        ToggleFavoriteUseCase(noteRepositoryInterface)
}

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAddNoteRepository(addNoteRepository: AddNoteRepository): AddNoteRepositoryInterface

    @Binds
    @Singleton
    fun bindNewsRepository(newsRepository: NewsRepository): NewsRepositoryInterface

    @Binds
    @Singleton
    fun bindsTickersRepository(tickersRepository: TickersRepository): TickersRepositoryInterface

    @Binds
    @Singleton
    fun bindsNotesRepository(notesRepository: NotesRepository): NoteRepositoryInterface

    @Binds
    @Singleton
    fun bindFinanceRepository(financeRepository: FinanceRepository): FinanceRepositoryInterface
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

