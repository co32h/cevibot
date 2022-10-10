package co32h

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sun.jna.platform.win32.COM.util.Factory
import co32h.audio.TrackManager
import co32h.cs.ITalker
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.reflect.UndeclaredThrowableException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class Tts(loggerIn : Logger,
          speeChIn : MessageChannel,
          tmIn : TrackManager,
          apIn : AudioPlayer) : ListenerAdapter() {

    private val logger = loggerIn
    private val speeCh = speeChIn
    private val tm = tmIn
    private val ap = apIn

    private val wavPath = "cevibot/temp/message.wav"

    override fun onMessageReceived(event : MessageReceivedEvent) {

        if (event.channel == speeCh) {
            speak(checkMessage(event.message.contentDisplay))
        }
    }

    override fun onGuildVoiceUpdate(event : GuildVoiceUpdateEvent) {

        var memberName = event.member.user.name
        if (event.member.nickname != null) memberName = event.member.nickname!!

        speak(
            when {
                event.channelJoined != null -> memberName + "が参加しました"
                else -> memberName + "が退出しました"
            }
        )
    }

    private fun checkMessage(messageIn : String) : String {

        var readMsg = messageIn

        //----------URL、メンション、絵文字、チャンネルリンク、長文、ファイルへの対処----------
        when {

            messageIn.matches(Regex("[\\s\\S]*http[\\s\\S]*")) -> readMsg = "URLが送信されました"

            messageIn.matches(Regex("[\\s\\S]*@[\\s\\S]*")) -> readMsg = "メンションが送信されました"

            messageIn.matches(Regex("[\\s\\S]*#[\\s\\S]*")) -> readMsg = "チャンネルのリンクが送信されました"

            messageIn.length > 100 -> readMsg = "100文字以上のメッセージが送信されました"

            messageIn == "" -> readMsg = "ファイルが送信されました"
        }

        return readMsg
    }

    private fun speak(messageIn : String) {

        try {
            val talker = Factory().createObject(ITalker :: class.java)

            val castList = talker.getAvailableCasts()

            talker.setCast(castList.At(Random().nextInt(castList.getLength())))

            if (talker.OutputWaveToFile(messageIn, wavPath)) {
                ap.volume = 50
                tm.play(wavPath, logger)
            }
        } catch (ex : UndeclaredThrowableException) {
            logger.log(Level.WARNING, "読み上げでエラーが発生しました1", ex)
        } catch (ex : Exception) {
            logger.log(Level.WARNING, "読み上げでエラーが発生しました", ex)
            speeCh.sendMessage("エラーが発生しました${System.lineSeparator()}開発者に問題を報告してください${System.lineSeparator()}`$ex`").queue()
        }
    }
}