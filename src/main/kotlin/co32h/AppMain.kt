package co32h

import com.sun.jna.platform.win32.COM.util.Factory
import co32h.cs.IServiceControl
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeoutException
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter
import javax.imageio.ImageIO
import kotlin.system.exitProcess

fun main() {

    lateinit var cevibot : BotMain
    val ccs = Factory().createObject(IServiceControl::class.java)

    //----------ロガー取得----------
    val logger = Logger.getLogger("co32h")
    val handler = FileHandler("cevibot/log.txt")
    logger.addHandler(handler)
    val formatter = SimpleFormatter()
    handler.formatter = formatter

    logger.log(Level.INFO, "ロガー取得成功")

    //----------タスクトレイアイコン作成----------
    val image = ImageIO.read(File("cevibot/icon.png"))

    val exitApp = MenuItem("終了")

    exitApp.addActionListener {
        try {
            cevibot.exit()
            ccs.CloseHost(0)
        } catch (ex : Exception) {
            logger.log(Level.WARNING, "アプリ終了時にエラーが発生しました", ex)
        }
        exitProcess(0)
    }

    val menu = PopupMenu()
    menu.add(exitApp)

    val icon = TrayIcon(image, "cevibot", menu)
    icon.isImageAutoSize = true

    val tray = SystemTray.getSystemTray()
    tray.add(icon)

    logger.log(Level.INFO, "タスクトレイアイコン作成成功")

    //----------コンフィグファイルの読み込み----------
    val prop = Properties()
    lateinit var fis : FileInputStream
    lateinit var isr : InputStreamReader

    try {
        fis = FileInputStream("cevibot/config.txt")
        isr = InputStreamReader(fis)
        prop.load(isr)
    } catch (ex : FileNotFoundException) {
        logger.log(Level.WARNING, "コンフィグファイルが見つかりませんでした", ex)
        icon.displayMessage("cevibot", "コンフィグファイルが見つかりませんでした${System.lineSeparator()}5秒後にアプリを終了します", TrayIcon.MessageType.ERROR)
        Thread.sleep(5000)
        exitProcess(0)
    }

    fis.close()
    isr.close()

    logger.log(Level.INFO, "コンフィグファイル読込開始")

    val token = prop.getProperty("token")
    val guildId = prop.getProperty("guildId")
    val VCId = prop.getProperty("VCId")
    val speeChId = prop.getProperty("speeChId")
    val gameChId = prop.getProperty("gameChId")

    prop.clear()

    logger.log(Level.INFO, "コンフィグファイル読込成功")

    //----------CS開始----------
    try {
        ccs.StartHost(false)
    } catch (ex : TimeoutException) {
        logger.log(Level.INFO, "CeVIO CCS起動成功")
        logger.log(Level.INFO, ex.toString())
    } catch (ex : Exception) {
        //無視
    }
    logger.log(Level.INFO, "CeVIO CCS起動成功")

    //----------Bot起動----------
    try {
        cevibot = BotMain(logger, icon, token, guildId, VCId, speeChId, gameChId)
        cevibot.login()
        logger.log(Level.INFO, "Botの起動に成功しました")
    } catch (ex : Exception) {
        logger.log(Level.WARNING, "Botの起動に失敗しました", ex)
        icon.displayMessage("cevibot", "Botの起動に失敗しました${System.lineSeparator()}5秒後にアプリを終了します", TrayIcon.MessageType.ERROR)
        Thread.sleep(5000)
        exitProcess(0)
    }
}