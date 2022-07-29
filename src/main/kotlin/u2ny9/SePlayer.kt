package u2ny9

import com.opencsv.CSVReader
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import u2ny9.audio.TrackManager
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.logging.Logger

class SePlayer(loggerIn : Logger, mainChIn : TextChannel, targetChIdIn : String, tmIn : TrackManager, apIn : AudioPlayer) : ListenerAdapter() {

    private val logger = loggerIn
    private val mainCh = mainChIn
    private val targetChId = targetChIdIn
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

            mainCh.sendMessage("${path}が見つかりませんでした").queue()
            null
        }
    }

    override fun onMessageReactionAdd(event : MessageReactionAddEvent) {

        if (event.member!!.user.isBot
                or (event.channel != event.jda.getTextChannelById(targetChId))
                or !event.channelType.isGuild
                or (emojiSePair == null)) return

        if (emojiSePair!!.isEmpty()) return

        try {
            for (i in emojiSePair.indices) {

                if (event.reactionEmote.name == emojiSePair[i][0]) {

                    ap.volume = 10
                    tm.play(emojiSePair[i][1], logger)
                }
            }
        } catch (ex : Exception) {
            mainCh.sendMessage("エラーが発生しました${System.lineSeparator()}`$ex`").queue()
        }
    }
}