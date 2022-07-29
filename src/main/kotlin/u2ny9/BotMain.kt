package u2ny9

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.TrayIcon
import java.util.logging.Level
import java.util.logging.Logger

class BotMain(loggerIn : Logger, iconIn : TrayIcon, tokenIn : String, mainChIdIn : String, prefixIn : String, joinVCIdIn : String, targetChIdIn : String, gameChIdIn : String) : ListenerAdapter() {

    private val logger = loggerIn
    private val icon = iconIn
    private val token = tokenIn
    private val mainChId = mainChIdIn
    private val prefix = prefixIn
    private val joinVCId = joinVCIdIn
    private val targetChId = targetChIdIn
    private val gameChId = gameChIdIn

    companion object {
        private lateinit var mainCh : TextChannel
        private lateinit var cevibot : JDA
    }

    fun login() {
        cevibot = JDABuilder.createDefault(token)
                .addEventListeners(this)
                .build()
    }

    override fun onReady(event : ReadyEvent) {
        try {
            mainCh = event.jda.getTextChannelById(mainChId)!!
            mainCh.sendMessage("起動完了しました").queue {
                logger.log(Level.INFO, "Bot起動完了")
            }
            event.jda.addEventListener(CommandListener(logger, mainCh, prefix, joinVCId, targetChId, gameChId))
            event.jda.removeEventListener(this)
        } catch (ex : Exception) {
            logger.log(Level.WARNING, "mainChIdが不正", ex)
            icon.displayMessage("cevibot", "mainChIdが不正な値です${System.lineSeparator()}アプリを終了してください", TrayIcon.MessageType.ERROR)
            cevibot.shutdown()
        }
    }

    fun exit() {
        mainCh.sendMessage("終了します").queue {
            logger.log(Level.INFO, "終了します")
            cevibot.shutdown()
        }
    }
}

