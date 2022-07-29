package u2ny9

import com.sun.jna.platform.win32.COM.util.Factory
import u2ny9.audio.TrackManager
import u2ny9.cs.ITalker
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import u2ny9.numeron.GameListener
import java.awt.Color
import java.util.logging.Level
import java.util.logging.Logger

class CommandListener(loggerIn : Logger, mainChIn : TextChannel, prefixIn : String, joinVCIdIn : String, targetChIdIn : String, gameChIdIn : String) : ListenerAdapter() {

    private val logger = loggerIn
    private val mainCh = mainChIn
    private val prefix = prefixIn
    private val joinVCId = joinVCIdIn
    private val targetChId = targetChIdIn
    private val gameChId = gameChIdIn

    private val am = mainCh.guild.audioManager

    private val tm = TrackManager()
    private val ap = tm.setup()

    private val tts = Tts(logger, mainCh, prefix, targetChId, tm, ap, joinVCId)
    private val sePlayer = SePlayer(logger, mainCh, targetChId, tm, ap)

    private val gameListener = GameListener(gameChId)

    override fun onMessageReceived(event : MessageReceivedEvent) {

        if (event.author.isBot
                or !event.message.contentRaw.startsWith(prefix)
                or (event.channel != mainCh)
                or !event.channelType.isGuild) return

        when (event.message.contentRaw) {

            prefix + "help" -> {

                val embed = EmbedBuilder()
                        .setAuthor(event.jda.selfUser.name, null, event.jda.selfUser.avatarUrl)
                        .setTitle("cevibot コマンド一覧")
                        .setColor(Color.getHSBColor(0.88F, 0.32F, 0.81F))
                        .addField(prefix + "help", "ヘルプメッセージを表示します", false)
                        .addField(prefix + "join", "コンフィグで指定されたボイスチャンネルに入ります", false)
                        .addField(prefix + "exit", "ボイスチャンネルから退出します", false)
                        .addField(prefix + "cast", "利用可能なキャストの一覧を表示します", false)
                mainCh.sendMessage(embed.build()).queue()
            }

            prefix + "join" -> {

                event.jda.registeredListeners.forEach {
                    if (it == tts) {
                        mainCh.sendMessage("既にボイスチャンネルに接続しています").queue()
                        return
                    }
                }

                if (joinVCId == "") {

                    mainCh.sendMessage("参加するボイスチャンネルの名前を入力してください${System.lineSeparator()}キャンセルするには`${prefix}cancel`と送信してください").queue {

                        event.jda.addEventListener(object : ListenerAdapter() {

                            override fun onMessageReceived(event : MessageReceivedEvent) {

                                if (event.author.isBot
                                        or (event.channel != mainCh)
                                        or !event.channelType.isGuild) return

                                if (event.message.contentRaw == prefix + "cancel") {
                                    mainCh.sendMessage("ボイスチャンネルへの参加を中断します").queue {
                                        event.jda.removeEventListener(this)
                                    }
                                    return
                                }

                                event.guild.voiceChannels.forEach {
                                    if (it.name == event.message.contentRaw) {
                                        try {
                                            mainCh.sendMessage("${it.name}に接続します").queue()
                                            am.openAudioConnection(it)
                                            am.sendingHandler = tm.getSendHandler()
                                            event.jda.addEventListener(tts)
                                            event.jda.addEventListener(sePlayer)
                                            event.jda.removeEventListener(this)
                                        } catch (ex : Exception) {
                                            logger.log(Level.WARNING, "VC接続失敗しました", ex)
                                        }
                                        return
                                    }
                                }

                                mainCh.sendMessage("`${event.message.contentRaw}`に該当するボイスチャンネルが見つかりませんでした${System.lineSeparator()}コマンドを終了します").queue()
                                event.jda.removeEventListener(this)
                            }
                        })
                    }
                } else {

                    event.guild.voiceChannels.forEach {
                        if (it.id == joinVCId) {
                            try {
                                mainCh.sendMessage("ボイスチャンネルに接続します").queue()
                                am.openAudioConnection(it)
                                am.sendingHandler = tm.getSendHandler()
                                ap.volume = 1
                                event.jda.addEventListener(tts)
                                event.jda.addEventListener(sePlayer)
                            } catch (ex : Exception) {
                                logger.log(Level.WARNING, "VC接続失敗しました", ex)
                            }
                            return
                        }
                    }

                    mainCh.sendMessage("joinVCIdの値が不正です${System.lineSeparator()}コンフィグファイルを見直してください").queue()
                }
            }

            prefix + "exit" -> {

                if (am.isConnected) {
                    mainCh.sendMessage("ボイスチャンネルから退室します").queue()
                    am.closeAudioConnection()
                    event.jda.removeEventListener(tts)
                    event.jda.removeEventListener(sePlayer)
                }
            }

            prefix + "cast" -> {

                val talker = Factory().createObject(ITalker :: class.java)
                val cast = talker.getAvailableCasts()

                var castList = "```${System.lineSeparator()}"

                for (i in 0 until cast.getLength()) {
                    castList += cast.At(i) + System.lineSeparator()
                }

                castList += "```"

                mainCh.sendMessage(castList).queue()
            }

            prefix + "game" -> {

                if(event.jda.registeredListeners.contains(gameListener)) {

                    mainCh.sendMessage("既にゲームが開始されています").queue()
                    return

                } else {

                    mainCh.sendMessage("数当てゲームを始めます。ゲーム用チャンネルに移動します。終了するときは`quit`と送信してください").queue {

                        event.jda.addEventListener(gameListener)
                    }
                }
            }
        }
    }
}