package co32h

import com.opencsv.CSVReader
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import co32h.audio.TrackManager
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.logging.Logger

class SePlayer(loggerIn : Logger,
               speeChIn : MessageChannel,
               tmIn : TrackManager,
               apIn : AudioPlayer) : ListenerAdapter() {

    private val logger = loggerIn
    private val speeCh = speeChIn
    private val tm = tmIn
    private val ap = apIn

    private val path = "cevibot/se.csv"
    private val emojiSePair = loader(path)

    private fun loader(pathIn : String) : List<Array<String>>? {

        return try {
            val fis = FileInputStream(pathIn)
            val isr = InputStreamReader(fis)

            val pairList = CSVReader(isr).readAll()

            fis.close()
            isr.close()

            pairList
        } catch (ex : Exception) {

            speeCh.sendMessage("${path}が見つかりませんでした").queue()
            null
        }
    }

    override fun onMessageReactionAdd(event : MessageReactionAddEvent) {

        if (event.member!!.user.isBot
                or (event.channel != speeCh)
                or !event.channelType.isGuild
                or (emojiSePair == null)) return

        if (emojiSePair!!.isEmpty()) return

        try {
            for (i in emojiSePair.indices) {

                if (event.emoji.name == emojiSePair[i][0]) {

                    ap.volume = 10
                    tm.play(emojiSePair[i][1], logger)
                }
            }
        } catch (ex : Exception) {
            speeCh.sendMessage("エラーが発生しました${System.lineSeparator()}`$ex`").queue()
        }
    }
}