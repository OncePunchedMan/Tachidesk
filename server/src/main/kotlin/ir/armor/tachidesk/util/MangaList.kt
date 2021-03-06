package ir.armor.tachidesk.util

import ir.armor.tachidesk.database.dataclass.MangaDataClass
import ir.armor.tachidesk.database.table.MangaStatus
import ir.armor.tachidesk.database.table.MangaTable
import ir.armor.tachidesk.database.table.SourceTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun getMangaList(sourceId: Long, pageNum: Int = 1, popular: Boolean): List<MangaDataClass> {
    val source = getHttpSource(sourceId.toLong())
    val mangasPage = if (popular) {
        source.fetchPopularManga(pageNum).toBlocking().first()
    } else {
        if (source.supportsLatest)
            source.fetchLatestUpdates(pageNum).toBlocking().first()
        else
            throw Exception("Source $source doesn't support latest")
    }
    return transaction {
        return@transaction mangasPage.mangas.map { manga ->
            var mangaEntry = MangaTable.select { MangaTable.url eq manga.url }.firstOrNull()
            var mangaEntityId = if (mangaEntry == null) { // create manga entry
                MangaTable.insertAndGetId {
                    it[url] = manga.url
                    it[title] = manga.title

                    it[artist] = manga.artist
                    it[author] = manga.author
                    it[description] = manga.description
                    it[genre] = manga.genre
                    it[status] = manga.status
                    it[thumbnail_url] = manga.genre

                    it[sourceReference] = sourceId
                }.value
            } else {
                mangaEntry[MangaTable.id].value
            }

            MangaDataClass(
                    mangaEntityId,
                    sourceId.toLong(),

                    manga.url,
                    manga.title,
                    manga.thumbnail_url,

                    manga.initialized,

                    manga.artist,
                    manga.author,
                    manga.description,
                    manga.genre,
                    MangaStatus.valueOf(manga.status).name,
            )
        }
    }
}