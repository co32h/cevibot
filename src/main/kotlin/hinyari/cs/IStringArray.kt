package hinyari.cs

import com.sun.jna.platform.win32.COM.util.annotation.ComInterface
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty

@ComInterface
interface IStringArray {

    @ComProperty
    fun getLength() : Int

    @ComMethod
    fun At(index : Int) : String

    @ComMethod
    fun Duplicate() : IStringArray

}