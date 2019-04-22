package nz.co.trademe.techtest.data.offline;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import nz.co.trademe.techtest.data.offline.dao.CategoryDao;
import nz.co.trademe.techtest.data.offline.model.Category;

/**
 * Database Factory class
 */
@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static String DB_NAME = "trademetask.db";
    public abstract CategoryDao categoryDao();

    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
