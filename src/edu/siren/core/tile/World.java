package edu.siren.core.tile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.XRandR.Screen;

import edu.siren.game.Player;
import edu.siren.game.entity.Entity;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Font;
import edu.siren.renderer.Perspective2D;
import edu.siren.renderer.Shader;

/**
 * A World object wraps the basics of the engine to create a simple way to
 * start an instance of the engine with predefined attributes for the camera,
 * as well as a collection of base shaders and world terrain data.
 *
 * @author Justin Van Horne
 */
public abstract class World {
    protected Set<Layer> layers;
    public Camera camera = new Camera(512.0f / 448.0f);
    public Shader worldShader;
    public ArrayList<Entity> entities = new ArrayList<Entity>();
    public HashMap<String, Tile> tiles = new HashMap<String, Tile>();
    
    // FBO specific entries
    // TODO (justinvh): This shouldn't be here.
    private int fboid = -1, fbotid = -1;
    protected Perspective2D fboPerspective = new Perspective2D();
    protected Font font;
    private Shader fboShader = null;
    private Tile fboTile = new Tile(0.0f, 0.0f, 640.0f, 480.0f);

    public enum Environment {
        MORNING, AFTERNOON, DUSK, NIGHT
    };

    private Environment currentEnvironment = Environment.AFTERNOON;
    
    /**
     * Constructs a new world of a given width and height. Note that this
     * does not limit the size of the world, just defines an initial size.
     * @throws IOException 
     * @throws LWJGLException 
     */
    public World() throws IOException, LWJGLException {
        init();        
        create();
        setupShaders();
    }
    
    public void setupShaders() throws IOException {
        // Create a default world shader for normal camera transforms.
        worldShader = new Shader("res/tests/glsl/basic.vert",
                                 "res/tests/glsl/basic.frag");        
        camera.bindToShader(worldShader);

        // Create a default 2D perspective for drawing the FBO
        fboShader = new Shader("res/tests/glsl/2d-perspective.vert",
                               "res/tests/glsl/2d-perspective.frag");
        fboPerspective.bindToShader(fboShader);
        fboTile.createInvertIndexVertexBuffer(1, 1);       
        generateFBO();
    }

    public void init() throws LWJGLException, IOException {
        // First bind the keyboard and mouse via LWJGL
        Keyboard.create();
        Mouse.create();
        layers = new TreeSet<Layer>();        
        font = new Font("res/tests/fonts/nostalgia.png", 24);
    }

    public abstract void create() throws IOException;    
    /**
     * Environment transitions. These HSV values correspond to an environment.
     * Additional effects can be created by simply setting a new state.
     */
    public void changeEnvironment(Environment environment, double msec) {
        currentEnvironment = environment;
        switch (environment) {
        case MORNING:
            camera.hsvTransition(0.75f, 0.75f, 0.75f, msec);
            break;
        case AFTERNOON:
            camera.hsvTransition(1.0f, 1.0f, 1.0f, msec);
            break;
        case DUSK:
            camera.hsvTransition(0.50f, 0.50f, 0.75f, msec);
            break;
        case NIGHT:
            camera.hsvTransition(0.5f, 0.15f, 0.5f, msec);
            break;
        }
    }
    
    /**
     * It probably doesn't seem reasonable to generate an FBO here, but it
     * is actually a necessary evil since our IVB are individual and we want
     * to draw the entire scene (including entities) to the FBO
     */
    private void generateFBO() {
        // Allocate an actual frame buffer (wtf lines)
        IntBuffer buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL30.glGenFramebuffers(buffer);
        fboid = buffer.get();
        
        // Generate a new texture to render to
        fbotid = GL11.glGenTextures();
        GL13.glActiveTexture(fbotid);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbotid);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, Display.getWidth(), Display.getHeight(),
                0, GL11.GL_RGBA, GL11.GL_INT, (ByteBuffer) null);

        // Bind the FBO
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboid);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                GL11.GL_TEXTURE_2D, fbotid, 0);      
        
        fboTile.ivb.textureIDs = new int[2];
        fboTile.ivb.textureIDs[0] = GL13.GL_TEXTURE0;
        fboTile.ivb.textureIDs[1] = fbotid;
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    

    /**
     * Draws the layers, followed by the entities, and then the Hud
     */
    public void draw() {
        // Initial pass for the content
        worldShader.use();
        {            
            // If the FBO is enabled, then we want to render to it
            if (fboid != -1) {
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboid);
                GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            } 
            
            for (Layer layer : layers) {
                layer.draw();
            }
            
            for (Entity entity : entities) {
                entity.draw();
            }
                        
            camera.think();
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                camera.zoomIn();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                camera.zoomOut();
            }
        }                
        worldShader.release();
        
        // The FBO pass. Take our FBO texture and draw it
        if (fboid != -1) {
            fboShader.use();              
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            fboTile.draw();
            fboShader.release();
        }
    }

    /**
     * Adds a new layer to the world.
     * 
     * @return Returns false if the layer already exists.
     */
    public boolean addLayer(Layer layer) {
        layer.world = this;
        layer.extendHash(tiles);
        return layers.add(layer);
    }

    /**
     * Adds a new entity to the world.
     */
    public void addEntity(Entity entity) {
        entity.setWorld(this);
        entities.add(entity);
    }
    
    /**
     * Adds an explicit player and binds the camera
     * so the player can control the camera interactions.
     */
    public void addEntity(Player player) {
        player.bindCamera(this.camera);
        player.setWorld(this);
        entities.add(player);
    }

    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }

}
