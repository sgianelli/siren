# Siren #

Siren is a [University of Arizona CS335-FALL12]
final project that satisfies the [Tactical Role Playing Game] assignment.

## Requirements ##

Siren is packaged with [LWJGL], [EasyOGG], and [JSON]. The minimum runtime and 
hardware configurations require:

- Java 1.6
- OpenGL 3.2

## Licensing ##

This project is freeware and open-source. Sprites, music, sounds, and any art 
is attributed to the rightful owners and is copyrighted accordingly. See the 
CONTRIBUTORS.md for both direct and indirect contributors to this project.

## Engine Details ##

This section provides a high-level overview of each of the sub-packages within 
Siren. Click any of the [jump]() links to read more details about the 
implementation as well as any other notes. [Doxygen] 
is supported on this project and may be generated with your favorite 
documentation generation tool.

### Audio [(jump)](#audio) ###
The audio section details the implementation of the WAV, Ogg, and nature of 
the sound threading. This engine uses the `EasyOgg` interface to play Oggs and 
the native Java Sound package for small WAV files.

### Core [(jump)](#core) ###
The core of the engine describe the mechanics necessary to run the game. This 
includes the basic `geometries`, `sprite/animation` handling, and `tile` 
generation. 

### Game [(jump)](#game) ###
The game component is the literal game that runs on top of the engine. This 
component will detail the worlds, players, and more. This section includes 
`ai`, `characters`, `entity`, `gui`, `players`, and `world`.

### Gui [(jump)](#gui) ###
The Gui implementation acts like a light-weight and minimalistic `JFrame` that 
interacts directly with the renderer and provides minimal overhead for draw 
routines. This section includes `Element`, `Image`, `Text`, and `Window`.

### Renderer [(jump)](#renderer) ###
The renderer is the core part of the OpenGL integration with the engine. This 
includes designating `VBO`, `VAO`, `FBO`, `Shader`, `Fonts`, `Drawables`, and 
various `Camera` implementations. The core of all draw routines filter into 
the `IndexVertexBuffer` object.

### Tests [(jump)](#tests) ###
The tests demonstrate various functionality in the engine with isolated 
component testing. For every major top-level component (as listed above) there 
are designated functionality tests.

## Audio ##
The `audio` package is composed of a single class `AudioUtil` with static 
methods for playing one of the following: `midi`, `wav`, or `ogg`. The MIDI 
and WAV formats are supported through Java internally, but only support up to 
1s second sound-bites. The Ogg interface is through the external `EasyOGG` 
package.

There are various `playX` methods where `X` is either `Midi`, `Wav`, or `Ogg` 
as well as `playBackgroundX` methods that allow you to run the a sound file in 
the background. The functions are wrapped with interrupt catching, so you 
should be able to quickly kill a playing audio thread.

*Example:* TODO

## Core ##

The core of the engine describe the mechanics necessary to run any game 
powered by the engine.

### `geom` ###
The `geom` package contains geometry-specific classes used throughout the 
engine to provide geospatial functionality in a 2D-fashion. This package is 
composed of `Point` and `Rectangle`.

The `Point` class is a simple abstraction of XYZ coordinate system. The 
`Rectangle` class provides functionality outside the typical rectangle object; 
these functionalities include bounding-box tests, overlap, and intersection.

### `sprite` ###
The sprite package describes the core functionality in drawing any 
animation-like. This package includes classes like `Animation`, `Sprite`, and 
`SpriteSheet`. The `Animation` class, and its dependencies (`AnimationFrame`, 
`AnimationSequence`, and `AnimationFrame`) all provide intuitive interfaces to 
describing a sequence of animation from a `SpriteSheet` or individual 
textures.

The `SpriteSheet` class acts as a utility method for integrating sprite-sheets 
in both a manual, or automatic fashion via the HTML5 web-app [Stitches]. Each 
sprite-sheet provides functionality for extracting individual sprites or 
animations.

All sprites have a bounding box associated with them. Each bounding box will 
re-align themselves per animation. This allows you to have fine control over 
the bound box intersections without sacrificing animation sequences due to 
variable animation sizes.

### `tile` ###
The `sprite` package derives its base functionality from the `tile` package 
which provides basic drawing of 2D-rectangular objects to the screen. The 
basics of the `Tile` class use the `IndexVertexBuffer` object to generate 
stride-friendly arrays suitable for VBO/VAO access. Each tile mandates an 
`XYRGBAST` dataset.

The `World` class is also within the `tile` package, which acts like a manager 
class--as well as provides additional 'usability' aide in rendering the world, 
defining boundary drawings, and so on. The `game.worlds` package will subclass 
the `World` class and overload the `create` method for drawing new worlds.

## Game ##
## Gui ##
## Renderer ##
## Tests ##

[LWJGL]: http://www.lwjgl.org/
[EasyOGG]: http://www.cokeandcode.com/index.html?page=libs
[JSON]: http://www.json.org/java/
[Tactical Role Playing Game]: http://goo.gl/bMWA3
[University of Arizona CS335-FALL12]: https://sites.google.com/site/csc335fall12/
[Doxygen]: http://www.doxygen.org
[Stitches]: http://draeton.github.com/stitches/
