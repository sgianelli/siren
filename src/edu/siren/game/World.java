package edu.siren.game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import edu.siren.core.tile.Layer;
import edu.siren.core.tile.Tile;
import edu.siren.core.tile.TriggerTile;
import edu.siren.game.entity.Entity;
import edu.siren.renderer.BufferType;
import edu.siren.renderer.Camera;
import edu.siren.renderer.Gui;
import edu.siren.renderer.Shader;

/**
 * A World object wraps the basics of the engine to create a simple way to
 * start an instance of the engine with predefined attributes for the camera,
 * as well as a collection of base shaders and world terrain data.
 *
 * @author Justin Van Horne
 */
public class World {
    private Set<Layer> layers;
    public Camera camera = new Camera(512.0f / 448.0f);
    public Shader worldShader;
    public ArrayList<Entity> entities = new ArrayList<Entity>();
    private int fboid = -1, fbotid = -1;
    private Gui gui = new Gui();
    private Shader guiShader = null;
    private Tile guiTile = new Tile(0.0f, 0.0f, 640.0f, 480.0f);

    public enum Environment {
        MORNING, AFTERNOON, DUSK, NIGHT
    };

    private Environment currentEnvironment = Environment.AFTERNOON;
    
    /**
     * Constructs a new world of a given width and height. Note that this
     * does not limit the size of the world, just defines an initial size.
     */
    public World(int width, int height) {
        try {
            layers = new TreeSet<Layer>();
            Layer layer = new Layer(BufferType.STATIC);
            layer.addTile(new Tile("res/tests/img/grass.png", -width/2, -height/2, width,
                    height));
            layers.add(layer);
            Random random = new Random();
            
            Keyboard.create();
            Mouse.create();
            
            for (int i = 0; i < 500; i++) {
                int x = random.nextInt(width) - random.nextInt(width*2);
                int y = random.nextInt(height) - random.nextInt(height*2);
                Tile tile = new TriggerTile(x, y, 64, 64, null);
                layer.addTile(tile);
            }

            for (int i = 0; i < 100; i++) {
                int x = random.nextInt(width) - random.nextInt(width*2);
                int y = random.nextInt(height) - random.nextInt(height*2);
                Tile tile = new Tile("res/tests/img/tree.png", x, y,
                        64.0f, 100.0f, 1, 1);
                layer.addTile(tile);
            }

            worldShader = new Shader("res/tests/glsl/basic.vert",
                    "res/tests/glsl/basic.frag");
            camera.position.m33 = 100.0f;
            camera.position.m30 = 0.0f;
            camera.bindToShader(worldShader);

            // Bind the 2D shader (FBO)
            guiShader = new Shader("res/tests/glsl/gui.vert",
                    "res/tests/glsl/gui.frag");
            gui.bindToShader(guiShader);
            
            guiTile.createInvertIndexVertexBuffer(1, 1);
           
            generateFBO();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
    
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
        // Allocate an actual frame buffer
        IntBuffer buffer = ByteBuffer.allocateDirect(1*4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL30.glGenFramebuffers(buffer);
        fboid = buffer.get();
        
        // Generate a new texture to render to
        fbotid = GL11.glGenTextures();
        GL13.glActiveTexture(fbotid);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbotid);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 640, 480,
                0, GL11.GL_RGBA, GL11.GL_INT, (ByteBuffer) null);

        // Bind the framebuffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboid);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                GL11.GL_TEXTURE_2D, fbotid, 0);      
        
        // Verify everything went okay
        int framebuffer = GL30.glCheckFramebufferStatus( GL30.GL_FRAMEBUFFER ); 
        switch ( framebuffer ) {
            case GL30.GL_FRAMEBUFFER_COMPLETE:
                break;
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                throw new RuntimeException( "FrameBuffer: " + fboid
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT exception" );
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                throw new RuntimeException( "FrameBuffer: " + fboid
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT exception" );
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                throw new RuntimeException( "FrameBuffer: " + fboid
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER exception" );
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
                throw new RuntimeException( "FrameBuffer: " + fboid
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER exception" );
            default:
                throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
        }
        
        guiTile.ivb.textureIDs = new int[2];
        guiTile.ivb.textureIDs[0] = GL13.GL_TEXTURE0;
        guiTile.ivb.textureIDs[1] = fbotid;
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    

    /**
     * Draws the layers, followed by the entities, and then the Hud
     */
    public void draw() {
        worldShader.use();
        
        if (fboid != -1) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboid);
            GL11.glViewport(0, 0, 640, 480);
        } 
        
        for (Layer layer : layers) {
            layer.draw();
        }
        
        for (Entity entity : entities) {
            entity.draw();
        }
        
        camera.think();
        
        worldShader.release();
        
        guiShader.use();
        
        if (fboid != -1) {            
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL11.glViewport(0, 0, 640, 480);
            guiTile.draw();
        }
        
        guiShader.release();
    }

    /**
     * Adds a new layer to the world.
     * 
     * @return Returns false if the layer already exists.
     */
    boolean addLayer(Layer layer) {
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
