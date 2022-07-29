package u2ny9

import com.sun.jna.platform.win32.COM.util.Factory
import u2ny9.cs.IServiceControl
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*
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
    val logger = Logger.getLogger("u2ny9")
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

    val token = prop.getProperty("token")
    val mainChId = prop.getProperty("mainChId")
    val prefix = prop.getProperty("prefix")
    val joinVCId = prop.getProperty("joinVCId")
    val targetChId = prop.getProperty("targetChId")
    val gameChId = prop.getProperty("gameChId")

    prop.clear()

    //----------CS開始----------
    try {
        ccs.StartHost(false)
    } catch (ex : Exception) {
        //無視
    }

    //----------Bot起動----------
    try {
        cevibot = BotMain(logger, icon, token, mainChId, prefix, joinVCId, targetChId, gameChId)
        cevibot.login()
    } catch (ex : Exception) {
        logger.log(Level.WARNING, "Botの起動に失敗しました", ex)
        icon.displayMessage("cevibot", "Botの起動に失敗しました${System.lineSeparator()}5秒後にアプリを終了します", TrayIcon.MessageType.ERROR)
        Thread.sleep(5000)
        exitProcess(0)
    }
}