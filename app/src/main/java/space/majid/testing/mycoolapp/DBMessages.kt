package space.majid.testing.mycoolapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*


class DBMessages(context: Context) : ManagedSQLiteOpenHelper(context, "messages") {
    companion object {
        private var instance: DBMessages? = null
        @Synchronized fun getInstance(context: Context): DBMessages {
            if(instance == null) {
                instance = DBMessages(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable("messages", true,
                "id" to INTEGER + UNIQUE + PRIMARY_KEY,
                "sender" to TEXT,
                "body" to TEXT,
                "date" to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.dropTable("messages", true)
        onCreate(db)
    }
}

val Context.database: DBMessages get() = DBMessages.getInstance(applicationContext)