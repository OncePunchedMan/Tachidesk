package ir.armor.tachidesk.database

import ir.armor.tachidesk.Config
import ir.armor.tachidesk.database.table.ChapterTable
import ir.armor.tachidesk.database.table.ExtensionsTable
import ir.armor.tachidesk.database.table.MangaTable
import ir.armor.tachidesk.database.table.SourceTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DBMangaer {
    val db by lazy {
        Database.connect("jdbc:sqlite:${Config.dataRoot}/database.db", "org.sqlite.JDBC")
    }
}

fun makeDataBaseTables() {
    // mention db object to connect
    DBMangaer.db

    transaction {
        SchemaUtils.create(ExtensionsTable)
        SchemaUtils.create(SourceTable)
        SchemaUtils.create(MangaTable)
        SchemaUtils.create(ChapterTable)
    }
}