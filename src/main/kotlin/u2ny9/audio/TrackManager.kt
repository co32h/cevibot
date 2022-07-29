package u2ny9.audio

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import java.util.logging.Level
import java.util.logging.Logger

class TrackManager {

    companion object {
        private val apm : AudioPlayerManager = DefaultAudioPlayerManager()
        private lateinit var ap : AudioPlayer
    }

    fun getSendHandler() : AudioPlayerSendHandler {
        return AudioPlayerSendHandler(ap)
    }

    fun setup() : AudioPlayer {

        apm.registerSourceManager(LocalAudioSourceManager())
        AudioSourceManagers.registerLocalSource(apm)
        AudioSourceManagers.registerRemoteSources(apm)

        ap = apm.createPlayer()
        return ap
    }

    fun play(path : String, logger : Logger) {

        apm.loadItem(path, object : AudioLoadResultHandler {

            override fun trackLoaded(track : AudioTrack?) {
                ap.playTrack(track)
                logger.log(Level.INFO, "読み上げ成功")
            }

            override fun playlistLoaded(playlist : AudioPlaylist?) {
            }

            override fun noMatches() {
                logger.log(Level.WARNING, "URLの不正:$path")
            }

            override fun loadFailed(exception : FriendlyException?) {
                logger.log(Level.WARNING, "楽曲のロードに失敗:$exception")
            }
        })
    }
}