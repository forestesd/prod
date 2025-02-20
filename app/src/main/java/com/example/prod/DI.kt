package com.example.prod

import android.app.Application
import android.content.Context
import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.apis.RetrofitSearchTimesInstance
import com.example.apis.RetrofitTimesInstance
import com.example.apis.TimesApiService
import com.example.financedate.FinanceViewModel
import com.example.financedate.db.FinanceDB
import com.example.financedate.db.GoalDAO
import com.example.financedate.db.TransactionDao
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostDatabase
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.TagDao
import com.example.tickersapi.RetrofitTickersInstance
import com.example.tickersapi.TickersApiService
import com.example.tickersapi.TickersRepository
import com.example.tickersapi.TickersViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
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


@Module
class PostDatabaseModile{

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
}

@Component(modules = [TimesApiModule::class, TickersApiModel::class, DataBaseFinanceModule::class , PostDatabaseModile::class, AppModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(newsViewModel: NewsViewModel)
    fun inject(newsRepository: NewsRepository)
    fun inject(financeViewModel: FinanceViewModel)
    fun inject(notesViewModel: NotesViewModel)
    fun inject(addNoteViewModel: AddNoteViewModel)
}
