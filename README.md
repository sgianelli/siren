# Siren #

Siren is a [University of Arizona CS335-FALL12](https://sites.google.com/site/csc335fall12/) 
final project that satisfies the [Tactical Role Playing Game](http://goo.gl/bMWA3) assignment.

## Requirements ##

Siren is packaged with [LWJGL](http://www.lwjgl.org/), 
[EasyOGG](http://www.cokeandcode.com/index.html?page=libs), and 
[JSON](http://www.json.org/java/). The minimum runtime and hardware 
configurations require:

- Java 1.6
- OpenGL 3.2

## Licensing ##

This project is freeware and open-source. Sprites, music, sounds, and any art 
is attributed to the rightful owners and is copyrighted accordingly. See the 
CONTRIBUTORS.md for both direct and indirect contributors to this project.

## Engine Details ##

This section provides a high-level overview of each of the sub-packages within 
Siren. Click any of the [jump]() links to read more details about the 
implementation as well as any other notes. [Doxygen](http://www.doxygen.org) 
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
## Game ##
## Gui ##
## Renderer ##
## Tests ##
