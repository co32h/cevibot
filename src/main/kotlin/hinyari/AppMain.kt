package hinyari

import com.sun.jna.platform.win32.COM.util.Factory
import hinyari.cs.IServiceControl
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.reflect.UndeclaredThrowableException
import java.util.*
import javax.imageio.ImageIO

fun main(args : Array<String>) {

    lateinit var cevibot : BotMain
    val ccs = Factory().createObject(IServiceControl::class.java)

    //----------タスクトレイアイコン作成----------
    val image = ImageIO.read(File("cevibot/icon.png"))

    val exitApp = MenuItem("終了")

    exitApp.addActionListener {
        try {
            cevibot.exit()
            ccs.CloseHost(0)
        } catch (ex : Exception) {
            //無視
        }
        System.exit(0)
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
        icon.displayMessage("cevibot", "コンフィグファイルが見つかりませんでした${System.lineSeparator()}5秒後にアプリを終了します", TrayIcon.MessageType.ERROR)
        Thread.sleep(5000)
        System.exit(0)
    }

    fis.close()
    isr.close()

    val token = prop.getProperty("token")
    val mainChId = prop.getProperty("mainChId")
    val prefix = prop.getProperty("prefix")
    val joinVCId = prop.getProperty("joinVCId")
    val targetChId = prop.getProperty("targetChId")

    prop.clear()

    //----------CS開始----------
    try {
        ccs.StartHost(false)
    } catch (ex : UndeclaredThrowableException) {
        //無視
    } catch (ex : Exception) {
        icon.displayMessage("cevibot", "CSが正常に開始しませんでした${System.lineSeparator()}手動でCSを起動してください", TrayIcon.MessageType.ERROR)
    }

    //----------Bot起動----------
    try {
        cevibot = BotMain(icon, token, mainChId, prefix, joinVCId, targetChId)
        cevibot.login()
    } catch (ex : Exception) {
        icon.displayMessage("cevibot", "Botの起動に失敗しました${System.lineSeparator()}5秒後にアプリを終了します", TrayIcon.MessageType.ERROR)
        Thread.sleep(5000)
        System.exit(0)
    }
}