package ru.gb.veber.kotlinmvvm.repository

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.room.*
import ru.gb.veber.kotlinmvvm.room.App.CreateDB.getHistoryDao
import java.util.*

private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH =
    "HistorySelect" // Часть пути (будем определять путь до HistoryEntity)

class MyContentProvider : ContentProvider() {

    private var authorities: String? = null // Адрес URI
    private lateinit var uriMatcher: UriMatcher

    private var entityContentType: String? = null // Набор строк
    private var entityContentItemType: String? = null // Одна строка

    private lateinit var contentUri: Uri // Адрес URI Provider

    override fun onCreate(): Boolean {

        authorities = context?.resources?.getString(R.string.authorities)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)

        uriMatcher.addURI(authorities, "$ENTITY_PATH/#", URI_ID)


        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"


        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")

        return true
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }
        return null
    }


    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {

        val historyDao: HistoryDao = getHistoryDao()
        val cursor = when (uriMatcher.match(uri)) {
            URI_ALL -> historyDao.getHistoryCursor() // Запрос к базе данных для
            URI_ID -> {
                val id = ContentUris.parseId(uri)
                historyDao.getHistoryCursor(id)
            }
            else -> {
                throw IllegalArgumentException("Wrong URI: $uri")
            }
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == URI_ALL) { "Wrong URI: $uri" }
        val historyDao = getHistoryDao()
        val entity = map(values)
        val id: Int = entity.id
        historyDao.insert(entity)

        val resultUri = ContentUris.withAppendedId(contentUri, id.toLong())
// Уведомляем ContentResolver, что данные по адресу resultUri изменились
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }

        val historyDao = getHistoryDao()
// Получаем идентификатор записи по адресу
        val id = ContentUris.parseId(uri)
// Удаляем запись по идентификатору
        historyDao.deleteById(id.toInt())
// Нотификация на изменение Cursor
        context?.contentResolver?.notifyChange(uri, null)
        return 1

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
        val historyDao = getHistoryDao()
        historyDao.update(map(values))
        context!!.contentResolver.notifyChange(uri, null)
        return 1
    }

    // Переводим ContentValues в HistoryEntity
    private fun map(values: ContentValues?): HistorySelect {
        return if (values == null) {
            HistorySelect(1, "def", 1, "def", 1.0, 1.0, "undefined")
        } else {
            val id = if (values.containsKey(ID)) values[ID] as Int else 0
            val city = values[CITY] as String
            val temperature = values[TEMPERATURE] as Int
            HistorySelect(id, city, temperature, "undefined", 1.0, 1.0, "undefined")
        }
    }
}