package co32h.cs

import com.sun.jna.platform.win32.COM.util.annotation.ComMethod
import com.sun.jna.platform.win32.COM.util.annotation.ComObject

@ComObject(progId = "CeVIO.Talk.RemoteService.ServiceControlV40")
interface IServiceControl {

    @ComMethod
    fun StartHost(boolean : Boolean) = Int

    @ComMethod
    fun CloseHost(int: Int)

}
