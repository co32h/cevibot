package u2ny9

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sun.jna.platform.win32.COM.util.Factory
import u2ny9.audio.TrackManager
import u2ny9.cs.ITalker
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.reflect.UndeclaredThrowableException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class Tts(loggerIn : Logger, mainChIn : TextChannel, prefixIn : String, targetChIdIn : String, tmIn : TrackManager, apIn : AudioPlayer, joinVCIdIn : String) : ListenerAdapter() {

    private val logger = loggerIn
    private val mainCh = mainChIn
    private val prefix = prefixIn
    private val targetChId = targetChIdIn
    private val tm = tmIn
    private val ap = apIn
    private val joinVCId = joinVCIdIn

    private val wavPath = "cevibot/temp/message.wav"

    override fun onMessageReceived(event : MessageReceivedEvent) {

        if (event.author.isBot
                or !event.channelType.isGuild
                or event.message.contentRaw.startsWith(prefix)) return

        if (targetChId != "") {

            try {
                val targetCh = event.jda.getTextChannelById(targetChId)
                if (event.channel == targetCh) {
                    speak(checkMessage(event.message.contentRaw))
                }
            } catch (ex : Exception) {
                logger.log(Level.WARNING, "targetChIdが不正", ex)
                mainCh.sendMessage("targetChIdが不正な値です${System.lineSeparator()}コンフィグファイルを見直してください").queue()
            }
        } else {
            speak(checkMessage(event.message.contentRaw))
        }
    }

    override fun onGuildVoiceJoin(event : GuildVoiceJoinEvent) {

        if (event.channelJoined.id != joinVCId) return

        var memberName = event.member.user.name
        if (event.member.nickname != null) memberName = event.member.nickname!!

        speak(memberName + "が参加しました")
    }

    override fun onGuildVoiceLeave(event : GuildVoiceLeaveEvent) {

        if (event.channelLeft.id != joinVCId) return

        var memberName = event.member.user.name
        if (event.member.nickname != null) memberName = event.member.nickname!!

        speak(memberName + "が退出しました")
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
            mainCh.sendMessage("エラーが発生しました${System.lineSeparator()}開発者に問題を報告してください${System.lineSeparator()}`$ex`").queue()
        }
    }
}