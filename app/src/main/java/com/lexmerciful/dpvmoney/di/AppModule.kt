package com.lexmerciful.dpvmoney.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.lexmerciful.dpvmoney.R
import com.lexmerciful.dpvmoney.data.repository.AuthRepository
import com.lexmerciful.dpvmoney.data.repository.AuthRepositoryImpl
import com.lexmerciful.dpvmoney.data.repository.DatabaseRepository
import com.lexmerciful.dpvmoney.data.room.BankAccountDao
import com.lexmerciful.dpvmoney.data.room.TransactionDao
import com.lexmerciful.dpvmoney.data.room.TransactionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository {
        return impl
    }

    @Singleton
    @Provides
    fun provideTransactionDatabase(@ApplicationContext context: Context) : TransactionDatabase {
        return Room.databaseBuilder(context,
            TransactionDatabase::class.java, "${context.getString(R.string.app_name).lowercase(Locale.ROOT)}.db")
            .addMigrations()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBankAccountDao(database: TransactionDatabase): BankAccountDao {
        return database.bankAccountDao()
    }

    @Singleton
    @Provides
    fun provideTransactionDao(database: TransactionDatabase): TransactionDao {
        return database.transactionDao()
    }
}