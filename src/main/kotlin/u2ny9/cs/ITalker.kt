package u2ny9.cs

import com.sun.jna.platform.win32.COM.util.annotation.ComMethod
import com.sun.jna.platform.win32.COM.util.annotation.ComObject
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty

@ComObject(progId = "CeVIO.Talk.RemoteService.TalkerV40")
interface ITalker {

    @ComProperty
    fun getAvailableCasts() : IStringArray

    @ComProperty
    fun setCast(cast : String)

    @ComMethod
    fun OutputWaveToFile(text : String, path : String) : Boolean

}
