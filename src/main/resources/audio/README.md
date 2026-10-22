# Game Audio Assets

This directory contains all game sounds and music.

## Directory Structure

```
audio/
├── sfx/                    # Sound Effects
│   ├── jump.mp3
│   ├── slide.mp3
│   ├── hit.mp3
│   ├── coin.mp3
│   ├── game_over.mp3
│   └── click.mp3
└── music/                  # Background Music
    ├── home.mp3
    ├── selection.mp3
    ├── stage1.mp3
    ├── stage2.mp3
    ├── stage3.mp3
    ├── stage4.mp3
    └── stage5.mp3
```

## Sound Effects (sfx/)

- **jump.mp3** - Played when the cookie jumps
- **slide.mp3** - Played when the cookie slides down
- **hit.mp3** - Played when the cookie collides with an obstacle
- **coin.mp3** - Played when collecting a coin
- **game_over.mp3** - Played when the cookie dies
- **click.mp3** - Played when UI buttons are clicked (optional)

## Background Music (music/)

- **home.mp3** - Home screen music
- **selection.mp3** - Cookie and Stage selection screen music
- **stage1.mp3** - Stage 1 gameplay music
- **stage2.mp3** - Stage 2 gameplay music
- **stage3.mp3** - Stage 3 gameplay music
- **stage4.mp3** - Stage 4 gameplay music
- **stage5.mp3** - Stage 5 gameplay music

## Audio Format Requirements

### Supported Formats
- **MP3** (MPEG Layer 3) - Default, good compression
- **WAV** (Waveform Audio File) - Uncompressed, higher quality

### Audio Specifications

#### MP3 Format
- **Sample Rate**: 44.1 kHz recommended
- **Bitrate**: 128-256 kbps recommended for balance between quality and file size
- **Channels**: Mono or Stereo
- **Best for**: Background music, general sound effects

#### WAV Format
- **Sample Rate**: 44.1 kHz or higher
- **Bit Depth**: 16-bit or 24-bit
- **Channels**: Mono or Stereo
- **Best for**: High-quality sound effects, uncompressed audio

## Adding New Audio Files

1. Place audio files in the appropriate subdirectory (sfx/ or music/)
2. Ensure file names match the identifiers used in SoundManager.java
3. Use either MP3 or WAV format
4. If using WAV instead of MP3, update the file extensions in SoundManager.java:
   - Change `/audio/sfx/jump.mp3` to `/audio/sfx/jump.wav`
   - Change `/audio/music/stage1.mp3` to `/audio/music/stage1.wav`

## Integration

The SoundManager class automatically loads these assets during game initialization. The initializeSounds() method is called in CookieRunApp.start(), loading all audio files into memory for fast playback during gameplay.

### Example Audio Paths (in code):

#### Using MP3 Format
```java
// Sound effects
loadSoundEffect("jump", "/audio/sfx/jump.mp3");
loadSoundEffect("slide", "/audio/sfx/slide.mp3");

// Background music
loadBackgroundMusic("stage1", "/audio/music/stage1.mp3");
```

#### Using WAV Format
```java
// Sound effects
loadSoundEffect("jump", "/audio/sfx/jump.wav");
loadSoundEffect("slide", "/audio/sfx/slide.wav");

// Background music
loadBackgroundMusic("stage1", "/audio/music/stage1.wav");
```

## License

Audio files should be properly licensed or created for use in this game.
