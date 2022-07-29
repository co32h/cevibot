package u2ny9.numeron

class EatBiteChecker(playerAnswerIn : String, variableListIn : List<String>) {

    private val playerAnswer = playerAnswerIn
    private val variableList = variableListIn

    private val usedNumber = playerAnswer.toCharArray().toList()
    private val unusedNumber = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9').minus(usedNumber)

    fun outputPerfectList() : List<String> {

        return listOf(playerAnswer)

    }

    fun outputEEList() : List<String> {

        val preList = mutableListOf<String>()

        for(i in playerAnswer.indices) {

            for (n in unusedNumber.indices) {

                preList.add(playerAnswer.replace(playerAnswer[i], unusedNumber[n]))
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputEList() : List<String> {

        val preList = mutableListOf<String>()

        for (i in playerAnswer.indices) {

            for (n in unusedNumber.indices) {

                val preString = playerAnswer.replace(playerAnswer[i], unusedNumber[n])
                val refreshedUnusedNumber = unusedNumber.minus(unusedNumber[n])

                if (playerAnswer.indices.last != i) {

                    for (x in refreshedUnusedNumber.indices) {

                        preList.add(preString.replace(playerAnswer[i + 1], refreshedUnusedNumber[x]))
                    }

                } else {

                    for (x in refreshedUnusedNumber.indices) {

                        preList.add(preString.replace(playerAnswer[0], refreshedUnusedNumber[x]))
                    }
                }
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputEBBList() : List<String> {

        val preList = mutableListOf<String>()

        for (i in playerAnswer.indices) {

            val temp = 'x'
            val indexedNumber = playerAnswer[i]

            if (playerAnswer.indices.last != i) {

                val indexedSecondNumber = playerAnswer[i + 1]
                val preString = playerAnswer.replace(playerAnswer[i], temp)
                    .replace(playerAnswer[i + 1], indexedNumber)
                    .replace(temp, indexedSecondNumber)

                preList.add(preString)

            } else {

                val indexedSecondNumber = playerAnswer[0]
                val preString = playerAnswer.replace(playerAnswer[i], temp)
                    .replace(playerAnswer[0], indexedNumber)
                    .replace(temp, indexedSecondNumber)

                preList.add(preString)
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputEBList() : List<String> {

        val preList = mutableListOf<String>()

        for (i in playerAnswer.indices) {

            if (playerAnswer.indices.last != i) {

                val movingNumber1 = playerAnswer[i]
                val movingNumber2 = playerAnswer[i + 1]

                for (n in unusedNumber.indices) {

                    val preString1 = playerAnswer.replace(playerAnswer[i], unusedNumber[n])
                        .replace(playerAnswer[i + 1], movingNumber1)
                    preList.add(preString1)

                    val preString2 = playerAnswer.replace(playerAnswer[i + 1], unusedNumber[n])
                        .replace(playerAnswer[i], movingNumber2)
                    preList.add(preString2)

                }
            } else {

                val movingNumber1 = playerAnswer[i]
                val movingNumber2 = playerAnswer[0]

                for (n in unusedNumber.indices) {

                    val preString1 = playerAnswer.replace(playerAnswer[i], unusedNumber[n])
                        .replace(playerAnswer[0], movingNumber1)
                    preList.add(preString1)

                    val preString2 = playerAnswer.replace(playerAnswer[0], unusedNumber[n])
                        .replace(playerAnswer[i], movingNumber2)
                    preList.add(preString2)
                }
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputBBBList() : List<String> {

        val preList = mutableListOf<String>()
        val loopString = playerAnswer + playerAnswer

        for (i in 1 until playerAnswer.length) {

            preList.add(loopString.substring(i, i + playerAnswer.length))
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputBBList() : List<String> {

        val preList = mutableListOf<String>()
        val loopString = playerAnswer + playerAnswer

        for (i in 1 until playerAnswer.length) {

            val trimmedString = loopString.substring(i, i + playerAnswer.length)

            for (n in playerAnswer.indices) {

                for (x in unusedNumber.indices) {

                    preList.add(trimmedString.replace(trimmedString[n], unusedNumber[x]))

                }
            }
        }

        val reversedLoopString = loopString.reversed() + playerAnswer.reversed()

        for (i in playerAnswer.indices) {

            val startIndex = i + playerAnswer.length - 1
            val preString = reversedLoopString.substring(startIndex, startIndex + 3)

            for (n in unusedNumber.indices) {

                preList.add(preString.replace(preString[i], unusedNumber[n]))
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputBList() : List<String> {

        val preList = mutableListOf<String>()

        for (i in playerAnswer.indices) {

            for (n in unusedNumber.indices) {

                val preString = playerAnswer.replace(playerAnswer[i], unusedNumber[n])
                val refreshedUnusedNumber = unusedNumber.minus(unusedNumber[n])

                if (playerAnswer.indices.last != i) {

                    for (x in refreshedUnusedNumber.indices) {

                        val secondString = preString.replace(playerAnswer[i + 1], refreshedUnusedNumber[x])
                        val loopString = secondString + secondString

                        for (y in 1 until playerAnswer.length) {

                            preList.add(loopString.substring(y, y + playerAnswer.length))
                        }
                    }
                } else {

                    for (x in refreshedUnusedNumber.indices) {

                        val secondString = preString.replace(playerAnswer[0], refreshedUnusedNumber[x])
                        val loopString = secondString + secondString

                        for (y in 1 until playerAnswer.length) {

                            preList.add(loopString.substring(y, y + playerAnswer.length))
                        }
                    }
                }
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }

    fun outputNoHitList() : List<String> {

        val preList = mutableListOf<String>()

        for (i in unusedNumber.indices) {

            val preString = playerAnswer.replace(playerAnswer[0], unusedNumber[i])
            val bUnusedNumber = unusedNumber.minus(unusedNumber[i])

            for (n in bUnusedNumber.indices) {

                val secondString = preString.replace(playerAnswer[1], bUnusedNumber[n])
                val cUnusedNumber = bUnusedNumber.minus(bUnusedNumber[n])

                for (x in cUnusedNumber.indices) {

                    preList.add(secondString.replace(playerAnswer[2], cUnusedNumber[x]))
                }
            }
        }

        return if (variableList.isEmpty()) preList
        else preList.filter { variableList.contains(it) }
    }
}