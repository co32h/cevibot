package hinyari

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sun.jna.platform.win32.COM.util.Factory
import hinyari.audio.TrackManager
import hinyari.cs.ITalker
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.lang.reflect.UndeclaredThrowableException
import java.util.*

class TTS(mainChIn : TextChannel, prefixIn : String, targetChIdIn : String, tmIn : TrackManager, apIn : AudioPlayer) : ListenerAdapter() {

    private val mainCh = mainChIn
    private val prefix = prefixIn
    private val targetChId = targetChIdIn
    private val tm = tmIn
    private val ap = apIn

    private val wavPath = "cevibot/temp/message.wav"

    override fun onMessageReceived(event : MessageReceivedEvent) {

        if (event.author.isBot
                or !event.channelType.isGuild
                or event.message.contentDisplay.startsWith(prefix)) return

        if (targetChId != "") {

            try {
                val targetCh = event.jda.getTextChannelById(targetChId)
                if (event.channel == targetCh) {
                    speak(checkMessage(event.message.contentDisplay))
                }
            } catch (ex : Exception) {
                mainCh.sendMessage("targetChIdが不正な値です${System.lineSeparator()}コンフィグファイルを見直してください").queue()
            }
        } else {
            speak(checkMessage(event.message.contentDisplay))
        }
    }

    override fun onGuildVoiceJoin(event : GuildVoiceJoinEvent) {

        var memberName = event.member.user.name
        if (event.member.nickname != null) memberName = event.member.nickname

        speak(memberName + "が参加しました")

    }

    override fun onGuildVoiceLeave(event : GuildVoiceLeaveEvent) {

        var memberName = event.member.user.name
        if (event.member.nickname != null) memberName = event.member.nickname

        speak(memberName + "が退出しました")

    }

    private fun checkMessage(messageIn : String) : String {

        val msg = messageIn
        var readMsg = msg

        //----------URL、メンション、絵文字、チャンネルリンク、長文への対処----------
        when {

            msg.matches(Regex("[\\s\\S]*http[\\s\\S]*")) -> readMsg = "URLが送信されました"

            msg.matches(Regex("[\\s\\S]*@[\\s\\S]*")) -> readMsg = "メンションが送信されました"

            msg.matches(Regex("[\\s\\S]*:.*:[\\s\\S]*")) -> readMsg = "絵文字が送信されました"

            msg.matches(Regex("[\\s\\S]*#[\\s\\S]*")) -> readMsg = "チャンネルのリンクが送信されました"

            msg.length > 100 -> readMsg = "100文字以上のメッセージが送信されました"

        }

        return readMsg
    }

    private fun speak(messageIn : String) {

        try {
            val talker = Factory().createObject(ITalker :: class.java)

            val castList = talker.getAvailableCasts()

            talker.setCast(castList.At(Random().nextInt(castList.getLength())))

            talker.OutputWaveToFile(messageIn, wavPath)

            ap.volume = 50
            tm.play(wavPath)

        } catch (ex : UndeclaredThrowableException) {
            //無視
        } catch (ex : Exception) {
            mainCh.sendMessage("エラーが発生しました${System.lineSeparator()}開発者に問題を報告してください${System.lineSeparator()}`$ex`").queue()
        }
    }
}