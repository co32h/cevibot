package co32h

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.requests.GatewayIntent
import java.awt.TrayIcon
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class BotMain(loggerIn : Logger,
              iconIn : TrayIcon,
              tokenIn : String,
              guildIdIn : String,
              VCIdIn : String,
              speeChIdIn : String,
              gameChIdIn : String) : ListenerAdapter() {

    private val logger = loggerIn
    private val icon = iconIn
    private val token = tokenIn
    private val guildId = guildIdIn
    private val VCId = VCIdIn
    private val speeChId = speeChIdIn
    private val gameChId = gameChIdIn

    companion object {
        private lateinit var cevibot : JDA
        private lateinit var speeCh : MessageChannel
        private val intentCollection = mutableListOf(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.MESSAGE_CONTENT,)
    }

    fun login() {
        cevibot = JDABuilder.createLight(token, intentCollection)
                .addEventListeners(this)
                .build()

        cevibot.awaitReady()

        val joinCommandOption = OptionData(OptionType.STRING, "voice-channel", "cevibotが参加するVC")
        val guild = cevibot.getGuildById(guildId)!!

        guild.voiceChannels.forEach {
            joinCommandOption.addChoice(it.name, it.name.lowercase())
        }

        guild.updateCommands().addCommands(
            Commands.slash("help", "コマンド一覧を出力します"),
            Commands.slash("join", "ボイスチャンネルに参加します")
                .addOptions(joinCommandOption),
            Commands.slash("exit", "ボイスチャンネルから退出します"),
            Commands.slash("cast", "読み上げに利用可能なキャスト一覧を出力します"),
            Commands.slash("game", "数当てゲームを開始します")
        ).queue()
}

    override fun onReady(event : ReadyEvent) {
        try {
            speeCh = event.jda.getTextChannelById(speeChId)!!
            speeCh.sendMessage("起動完了しました").queue {
                logger.log(Level.INFO, "Bot起動完了")
            }
            val guild = cevibot.getGuildById(guildId)!!
            event.jda.addEventListener(CommandListener(logger, guild, VCId, speeCh, gameChId))
            event.jda.removeEventListener(this)
        } catch (ex : Exception) {
            logger.log(Level.WARNING, "speeChIdが不正", ex)
            icon.displayMessage("cevibot", "speeChIdが不正な値です${System.lineSeparator()}アプリを終了してください", TrayIcon.MessageType.ERROR)
            cevibot.shutdown()
        }
    }

    fun exit() {
        speeCh.sendMessage("終了します").queue {
            logger.log(Level.INFO, "終了します")
            cevibot.shutdown()
        }
    }
}

