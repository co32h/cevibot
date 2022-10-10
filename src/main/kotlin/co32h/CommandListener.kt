package co32h

import com.sun.jna.platform.win32.COM.util.Factory
import co32h.audio.TrackManager
import co32h.cs.ITalker
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import co32h.numeron.GameListener
import net.dv8tion.jda.api.entities.Guild
import java.awt.Color
import java.util.logging.Level
import java.util.logging.Logger

class CommandListener(loggerIn : Logger,
                      guildIn : Guild,
                      VCIdIn : String,
                      speeChIn : MessageChannel,
                      gameChIdIn : String) : ListenerAdapter() {

    private val logger = loggerIn
    private val guild = guildIn
    private val VCId = VCIdIn
    private val speeCh = speeChIn
    private val gameChId = gameChIdIn

    private val am = guild.audioManager

    private val tm = TrackManager()
    private val ap = tm.setup()

    private val tts = Tts(logger, speeCh, tm, ap)
    private val sePlayer = SePlayer(logger, speeCh, tm, ap)

    private val gameListener = GameListener(gameChId)

    override fun onSlashCommandInteraction(event : SlashCommandInteractionEvent ) {

        if (event.user.isBot) return

        when (event.name) {

            "help" -> {

                val embed = EmbedBuilder()
                    .setAuthor(event.jda.selfUser.name, null, event.jda.selfUser.avatarUrl)
                    .setTitle("cevibot コマンド一覧")
                    .setColor(Color.getHSBColor(0.88F, 0.32F, 0.81F))
                    .addField("help", "コマンド一覧を出力します", false)
                    .addField("join", "ボイスチャンネルに参加します", false)
                    .addField("exit", "ボイスチャンネルから退出します", false)
                    .addField("cast", "読み上げに利用可能なキャスト一覧を出力します", false)
                    .addField("game", "数当てゲームを開始します", false)
                event.replyEmbeds(embed.build()).setEphemeral(false).queue()
            }

            "join" -> {

                //スラッシュコマンドの引数が存在しない場合
                if (event.options.isEmpty()) {

                    if (VCId != "") {
                        guild.voiceChannels.forEach {
                            if (it == guild.getVoiceChannelById(VCId)) {
                                try {
                                    event.reply("ボイスチャンネルに接続します").queue()
                                    am.openAudioConnection(it)
                                    am.sendingHandler = tm.getSendHandler()
                                    ap.volume = 1
                                    event.jda.addEventListener(tts)
                                    event.jda.addEventListener(sePlayer)
                                } catch (ex : Exception) {
                                    event.reply("VC接続失敗しました").queue()
                                    logger.log(Level.WARNING, "VC接続失敗しました", ex)
                                }
                                return
                            }
                        }
                    } else {
                        event.reply("`config.txt`にて`VCId`の値を設定していない場合は`/join`実行時の引数に任意のボイスチャンネルを指定してください").queue()
                    }

                //スラッシュコマンドの引数"voice-channel"に指定がある場合
                } else if (event.getOption("voice-channel")!!.asString != ""){

                    guild.voiceChannels.forEach {
                        if (it == guild.getVoiceChannelsByName(event.getOption("voice-channel")!!.asString, true)[0]) {
                            try {
                                event.reply("ボイスチャンネルに接続します").queue()
                                am.openAudioConnection(it)
                                am.sendingHandler = tm.getSendHandler()
                                ap.volume = 1
                                event.jda.addEventListener(tts)
                                event.jda.addEventListener(sePlayer)
                            } catch (ex : Exception) {
                                event.reply("VC接続失敗しました").queue()
                                logger.log(Level.WARNING, "VC接続失敗しました", ex)
                            }
                            return
                        }
                    }
                }/*

                event.jda.registeredListeners.forEach {
                    if (it == tts) {
                        event.reply("既にボイスチャンネルに接続しています").queue()
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

                    event.guild!!.voiceChannels.forEach {
                        if (it == VC) {
                            try {
                                event.reply("ボイスチャンネルに接続します").queue()
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

                event.reply("VCIdの値が不正です${System.lineSeparator()}コンフィグファイルを見直してください").queue()
                }
            */}

            "exit" -> {

                if (am.isConnected) {
                    event.reply("ボイスチャンネルから退出します").queue()
                    am.closeAudioConnection()
                    event.jda.removeEventListener(tts)
                    event.jda.removeEventListener(sePlayer)
                } else {
                    event.reply("既に退出しています").queue()
                }
            }

            "cast" -> {

                val talker = Factory().createObject(ITalker :: class.java)
                val cast = talker.getAvailableCasts()

                var castList = "```${System.lineSeparator()}"

                for (i in 0 until cast.getLength()) {
                    castList += cast.At(i) + System.lineSeparator()
                }

                castList += "```"

                event.reply(castList).queue()
            }

            "game" -> {

                if(event.jda.registeredListeners.contains(gameListener)) {

                    event.reply("既にゲームが開始されています").queue()
                    return

                } else {

                    event.reply("数当てゲームを始めます。ゲーム用チャンネルに移動します。終了するときは`quit`と送信してください").queue {

                        event.jda.addEventListener(gameListener)
                    }
                }
            }
        }
    }
}