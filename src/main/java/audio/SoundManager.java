package audio;

import game.config.GameConfig;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all game sounds and music.
 *
 * Responsibilities:
 * - Load and cache audio files (MP3 and WAV formats supported)
 * - Play sound effects (jump, slide, etc.)
 * - Play background music
 * - Control volume and playback
 *
 * Supported Audio Formats:
 * - MP3 (MPEG Layer 3 Audio)
 * - WAV (Waveform Audio File Format)
 */
public class SoundManager {

    // Singleton instance
    private static SoundManager instance;

    // Audio caches
    private final Map<String, Media> soundEffects = new HashMap<>();
    private final Map<String, Media> backgroundMusic = new HashMap<>();

    // Current background music player
    private MediaPlayer currentMusicPlayer;
    private String currentMusicName = "";  // Track which music is currently playing

    // Volume settings (0.0 to 1.0) - initialized from GameConfig
    private double sfxVolume = GameConfig.SFX_VOLUME * GameConfig.MASTER_VOLUME;
    private double musicVolume = GameConfig.MUSIC_VOLUME * GameConfig.MASTER_VOLUME;

    // Enable/disable sound - from GameConfig
    private boolean soundEnabled = GameConfig.AUDIO_ENABLED;

    private SoundManager() {
    }

    /**
     * Get singleton instance of SoundManager.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // =========================
    // SOUND EFFECT LOADING
    // =========================

    /**
     * Load a sound effect from resources.
     * Supports MP3 and WAV audio formats.
     *
     * @param name Identifier for the sound
     * @param path Resource path (e.g., "/audio/sfx/jump.mp3" or "/audio/sfx/jump.wav")
     */
    public void loadSoundEffect(String name, String path) {
        try {
            String resourcePath = getClass().getResource(path).toExternalForm();
            Media media = new Media(resourcePath);
            soundEffects.put(name, media);
        } catch (Exception e) {
            System.err.println("Failed to load sound effect: " + name + " from " + path);
        }
    }

    /**
     * Load background music from resources.
     * Supports MP3 and WAV audio formats.
     *
     * @param name Identifier for the music
     * @param path Resource path (e.g., "/audio/music/stage1.mp3" or "/audio/music/stage1.wav")
     */
    public void loadBackgroundMusic(String name, String path) {
        try {
            String resourcePath = getClass().getResource(path).toExternalForm();
            Media media = new Media(resourcePath);
            backgroundMusic.put(name, media);
        } catch (Exception e) {
            System.err.println("Failed to load background music: " + name + " from " + path);
        }
    }

    // =========================
    // SOUND EFFECT PLAYBACK
    // =========================

    /**
     * Play a sound effect once.
     *
     * @param soundName The identifier of the sound to play
     */
    public void playSoundEffect(String soundName) {
        if (!GameConfig.AUDIO_ENABLED || !soundEnabled) return;

        Media media = soundEffects.get(soundName);
        if (media == null) {
            System.err.println("Sound effect not found: " + soundName);
            return;
        }

        try {
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(sfxVolume);
            player.play();
        } catch (Exception e) {
            System.err.println("Failed to play sound effect: " + soundName);
        }
    }

    /**
     * Play jump sound effect.
     */
    public void playJumpSound() {
        playSoundEffect("jump");
    }

    /**
     * Play slide sound effect.
     */
    public void playSlideSound() {
        playSoundEffect("slide");
    }

    /**
     * Play collision/hit sound effect.
     */
    public void playHitSound() {
        playSoundEffect("hit");
    }

    /**
     * Play coin collection sound effect.
     */
    public void playCoinSound() {
        playSoundEffect("coin");
    }

    /**
     * Play jelly collection sound effect.
     */
    public void playJellySound() {
        playSoundEffect("jelly");
    }

    /**
     * Play game over sound effect.
     */
    public void playGameOverSound() {
        playSoundEffect("gameOver");
    }

    /**
     * Play button click sound effect.
     */
    public void playClickSound() {
        playSoundEffect("click");
    }

    // =========================
    // BACKGROUND MUSIC PLAYBACK
    // =========================

    /**
     * Play background music with looping.
     * Always stops current music and starts the new track.
     *
     * @param musicName The identifier of the music to play
     */
    public void playBackgroundMusic(String musicName) {
        if (!GameConfig.AUDIO_ENABLED || !soundEnabled) return;

        // Stop current music
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
        }

        Media media = backgroundMusic.get(musicName);
        if (media == null) {
            System.err.println("Background music not found: " + musicName);
            return;
        }

        try {
            currentMusicPlayer = new MediaPlayer(media);
            currentMusicPlayer.setVolume(musicVolume);
            currentMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Loop indefinitely
            currentMusicPlayer.play();
            currentMusicName = musicName;  // Track currently playing music
        } catch (Exception e) {
            System.err.println("Failed to play background music: " + musicName);
        }
    }

    /**
     * Play background music only if different music is not already playing.
     * Allows music to continue across multiple screens without restarting.
     *
     * @param musicName The identifier of the music to play
     */
    public void playBackgroundMusicIfNotPlaying(String musicName) {
        if (!soundEnabled) return;

        // If the same music is already playing, don't restart it
        if (musicName.equals(currentMusicName) && currentMusicPlayer != null && currentMusicPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PLAYING)) {
            return;
        }

        // Different music or nothing playing, start the new music
        playBackgroundMusic(musicName);
    }

    /**
     * Play home screen music.
     */
    public void playHomeMusic() {
        playBackgroundMusic("home");
    }

    /**
     * Play selection screen music.
     */
    public void playSelectionMusic() {
        playBackgroundMusic("selection");
    }

    /**
     * Play stage music.
     *
     * @param stageIndex The stage number (0-4, converted to 1-5 for music files)
     */
    public void playStageMusic(int stageIndex) {
        playBackgroundMusic("stage" + (stageIndex + 1));
    }

    /**
     * Stop current background music.
     */
    public void stopBackgroundMusic() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer = null;
        }
    }

    // =========================
    // VOLUME CONTROL
    // =========================

    /**
     * Set sound effect volume (0.0 to 1.0).
     */
    public void setSFXVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    /**
     * Set background music volume (0.0 to 1.0).
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (currentMusicPlayer != null) {
            currentMusicPlayer.setVolume(this.musicVolume);
        }
    }

    /**
     * Get sound effect volume.
     */
    public double getSFXVolume() {
        return sfxVolume;
    }

    /**
     * Get background music volume.
     */
    public double getMusicVolume() {
        return musicVolume;
    }

    // =========================
    // SOUND CONTROL
    // =========================

    /**
     * Enable or disable all sounds.
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }

    /**
     * Check if sound is enabled.
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Pause background music.
     */
    public void pauseBackgroundMusic() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.pause();
        }
    }

    /**
     * Resume background music.
     */
    public void resumeBackgroundMusic() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.play();
        }
    }

    /**
     * Initialize all sound assets.
     * Call this during application startup.
     *
     * Audio formats: MP3 and WAV are both supported.
     * Change file extensions below or replace files to use WAV format instead.
     * Example: "/audio/sfx/jump.wav" instead of "/audio/sfx/jump.mp3"
     */
    public void initializeSounds() {
        // Load sound effects (change .mp3 to .wav if using WAV format)
        loadSoundEffect("jump", "/audio/sfx/jump.mp3");
        loadSoundEffect("slide", "/audio/sfx/slide.mp3");
        loadSoundEffect("hit", "/audio/sfx/hit.mp3");
        loadSoundEffect("coin", "/audio/sfx/coin.mp3");
        loadSoundEffect("jelly", "/audio/sfx/jelly.mp3");
        loadSoundEffect("gameOver", "/audio/sfx/game_over.mp3");
        loadSoundEffect("click", "/audio/sfx/click.mp3");

        // Load background music (change .mp3 to .wav if using WAV format)
        loadBackgroundMusic("home", "/audio/music/home.mp3");
        loadBackgroundMusic("selection", "/audio/music/selection.mp3");
        loadBackgroundMusic("stage1", "/audio/music/stage1.mp3");
        loadBackgroundMusic("stage2", "/audio/music/stage2.mp3");
        loadBackgroundMusic("stage3", "/audio/music/stage3.mp3");
        loadBackgroundMusic("stage4", "/audio/music/stage4.mp3");
        loadBackgroundMusic("stage5", "/audio/music/stage5.mp3");
    }
}
