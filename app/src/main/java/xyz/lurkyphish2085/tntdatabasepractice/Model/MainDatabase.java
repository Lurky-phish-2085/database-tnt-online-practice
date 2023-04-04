package xyz.lurkyphish2085.tntdatabasepractice.Model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Cart.class, Siomai_Inventory.class, TransactionLog.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {
    private static MainDatabase instance;
    public abstract Siomai_InventoryDao siomai_inventoryDao();
    public abstract CartDao cartDao();

    public abstract TransactionLogDao transactionLogDao();

    public static synchronized MainDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, MainDatabase.class, "MainDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PrePopulateDB(instance).execute();
        }
    };

    private static class PrePopulateDB extends AsyncTask<Void, Void, Void>{
        private Siomai_InventoryDao siomai_inventoryDao;

        private PrePopulateDB(MainDatabase db){
            siomai_inventoryDao = db.siomai_inventoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            siomai_inventoryDao.insert(new Siomai_Inventory("Beef", 35, 100, 0));
            siomai_inventoryDao.insert(new Siomai_Inventory("Japanese", 40, 100, 0));
            siomai_inventoryDao.insert(new Siomai_Inventory("Pork", 35, 100, 0));
            siomai_inventoryDao.insert(new Siomai_Inventory("Shark Fin", 40, 100, 0));
            siomai_inventoryDao.insert(new Siomai_Inventory("Crab", 45, 100, 0));
            return null;
        }
    }
}
