package hinyari.audio

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack

class TrackManager {

    companion object {
        private val apm = DefaultAudioPlayerManager()
        private lateinit var ap : AudioPlayer
    }

    fun setup() : AudioPlayer {

        apm.registerSourceManager(LocalAudioSourceManager())

        AudioSourceManagers.registerLocalSource(apm)

        ap = apm.createPlayer()

        return ap

    }

    fun play(path : String) {

        apm.loadItem(path, object : AudioLoadResultHandler {

            override fun trackLoaded(track: AudioTrack?) {
                ap.playTrack(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist?) {
            }

            override fun noMatches() {
            }

            override fun loadFailed(exception: FriendlyException?) {
            }
        })
    }
}