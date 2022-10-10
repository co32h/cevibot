package co32h.numeron

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GameListener(gameChIdIn : String) : ListenerAdapter() {

    private val gameChId = gameChIdIn
    private var variableNumberList = listOf<String>()
    private var turnCount = 1

    override fun onMessageReceived(event: MessageReceivedEvent) {

        if (event.author.isBot
            or (event.channel.id != gameChId)
            or !event.channelType.isGuild) return

        val textIn = event.message.contentRaw

        val txtCh = event.channel

        if (textIn == "quit") {
            txtCh.sendMessage("ゲームを終了します").queue {
                event.jda.removeEventListener(this)
            }
        }

        if (textIn.length != 3) return

        textIn.toCharArray().forEach { oya ->
            var isNum = false

            charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9').forEach { ko ->
                if (oya == ko) isNum = true
            }

            if (!isNum) {
                txtCh.sendMessage("適切な数列を入力してください").queue()
                return
            }
        }

        textIn.groupingBy { it }.eachCount().forEach {
            if (it.value > 1) {
                txtCh.sendMessage("数字の重複のない数列を入力してください").queue()
                return
            }
        }

        val checker = EatBiteChecker(textIn, variableNumberList)

        val comparingList = listOf(
            checker.outputNoHitList(),
            checker.outputBList(),
            checker.outputBBList(),
            checker.outputBBBList(),
            checker.outputEBList(),
            checker.outputEBBList(),
            checker.outputEList(),
            checker.outputEEList(),
            checker.outputPerfectList() )

        val selectedList = comparingList.maxByOrNull { it.count() }
        variableNumberList = selectedList!!

        when (variableNumberList) {

            checker.outputPerfectList() -> {

                txtCh.sendMessage("当たりです。消費ターン数: $turnCount ターン").queue()
                turnCount = 1
                variableNumberList = listOf()
            }
            checker.outputEEList() -> {
                txtCh.sendMessage("2EAT").queue()
                turnCount += 1
            }
            checker.outputEList() -> {
                txtCh.sendMessage("1EAT").queue()
                turnCount += 1
            }
            checker.outputEBBList() -> {
                txtCh.sendMessage("1EAT 2BITE").queue()
                turnCount += 1
            }
            checker.outputEBList() -> {
                txtCh.sendMessage("1EAT 1BITE").queue()
                turnCount += 1
            }
            checker.outputBBBList() -> {
                txtCh.sendMessage("3BITE").queue()
                turnCount += 1
            }
            checker.outputBBList() -> {
                txtCh.sendMessage("2BITE").queue()
                turnCount += 1
            }
            checker.outputBList() -> {
                txtCh.sendMessage("1BITE").queue()
                turnCount += 1
            }
            checker.outputNoHitList() -> {
                txtCh.sendMessage("0HIT").queue()
                turnCount += 1
            }
        }

        /*txtCh.sendMessage("3EAT→${checker.outputPerfectList().count()}個${System.lineSeparator()}" +
                "2EAT→${checker.outputEEList().count()}個${System.lineSeparator()}" +
                "1EAT→${checker.outputEList().count()}個${System.lineSeparator()}" +
                "1EAT2BITE→${checker.outputEBBList().count()}個${System.lineSeparator()}" +
                "1EAT1BITE→${checker.outputEBList().count()}個${System.lineSeparator()}" +
                "3BITE→${checker.outputBBBList().count()}個${System.lineSeparator()}" +
                "2BITE→${checker.outputBBList().count()}個${System.lineSeparator()}" +
                "1BITE→${checker.outputBList().count()}個${System.lineSeparator()}" +
                "NOHIT→${checker.outputNoHitList().count()}個${System.lineSeparator()}" +
                "").queue()
         */
    }
}