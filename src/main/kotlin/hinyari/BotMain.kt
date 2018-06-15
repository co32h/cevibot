package hinyari

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.awt.TrayIcon

class BotMain(iconIn : TrayIcon, tokenIn : String, mainChIdIn : String, prefixIn : String, joinVCIdIn : String, targetChIdIn : String) : ListenerAdapter() {

    private val icon = iconIn
    private val token = tokenIn
    private val mainChId = mainChIdIn
    private val prefix = prefixIn
    private val joinVCId = joinVCIdIn
    private val targetChId = targetChIdIn

    companion object {
        private lateinit var mainCh : TextChannel
        private lateinit var cevibot : JDA
    }

    fun login() {
        cevibot = JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(this)
                .setGame(Game.playing(prefix + "helpでヘルプを表示"))
                .buildAsync()
    }

    override fun onReady(event : ReadyEvent) {
        try {
            mainCh = event.jda.getTextChannelById(mainChId)
            mainCh.sendMessage("起動完了しました").queue()
            event.jda.addEventListener(CommandListener(mainCh, prefix, joinVCId, targetChId))
            event.jda.removeEventListener(this)
        } catch (ex : Exception) {
            icon.displayMessage("cevibot", "mainChIdが不正な値です${System.lineSeparator()}アプリを終了してください", TrayIcon.MessageType.ERROR)
            cevibot.shutdown()
        }
    }

    fun exit() {
        mainCh.sendMessage("終了します").queue {
            cevibot.shutdown()
        }
    }
}

