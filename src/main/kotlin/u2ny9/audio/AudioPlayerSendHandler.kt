package u2ny9.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer

class AudioPlayerSendHandler(private val ap: AudioPlayer) : AudioSendHandler {

    private var lastFrame: AudioFrame? = null


    override fun canProvide(): Boolean {
        lastFrame = ap.provide()
        return lastFrame != null
    }

    override fun provide20MsAudio(): ByteBuffer? {
        return ByteBuffer.wrap(lastFrame!!.data)
    }

    override fun isOpus(): Boolean {
        return true
    }

}