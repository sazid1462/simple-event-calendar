package com.github.sazid1462.simpleeventcalendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.github.sazid1462.simpleeventcalendar.AppExecutors
import androidx.annotation.NonNull
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


@Database(entities = arrayOf(Event::class), version = 2)
abstract class EventRoomDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    /**
     * Check whether the database already exists and expose it via [.getDatabaseCreated]
     */
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    fun getDatabaseCreated(): LiveData<Boolean> {
        return mIsDatabaseCreated
    }

    companion object {

        @JvmStatic @Volatile private var INSTANCE: EventRoomDatabase? = null
        private const val DATABASE_NAME: String = "event_database"

        @JvmStatic
        fun getInstance(context: Context, executors: AppExecutors?): EventRoomDatabase {
            if (INSTANCE == null) {
                synchronized(EventRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = buildDatabase(context.applicationContext, executors)
                        INSTANCE?.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return this.INSTANCE!!
        }

        /**
         * Build the database. [Builder.build] only sets up the database configuration and
         * creates a new instance of the database.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(
            appContext: Context,
            executors: AppExecutors?
        ): EventRoomDatabase {
            return Room.databaseBuilder(appContext, EventRoomDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        executors?.diskIO()?.execute {
                            // Add a delay to simulate a long-running operation
                            addDelay()
                            // Generate the data for pre-population
                            val database = EventRoomDatabase.getInstance(appContext, executors)

                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated()
                        }
                    }
                })
                .fallbackToDestructiveMigration()
//            .addMigrations(MIGRATION_1_2)
                .build()
        }

        private fun addDelay() {
            try {
                Thread.sleep(4000)
            } catch (ignored: InterruptedException) {
            }

        }
    }
}