package edu.siren.renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Constructs a GLSL shader suitable for loading into the Siren engine.
 * Expects all vertex shaders to define a fragment shader.
 *
 * @author Justin Van Horne <justinvh@gmail.com>
 */
public class Shader {
    public int vid, fid, program;
    public static int activeProgram;

    /**
     * Constructs a new Shader with a defined vertex and fragment file.
     *
     * @param vertex The vertex file
     * @param fragment The fragment file
     * @throws IOException If either of the shaders are not found.
     */
    public Shader(String vertex, String fragment) throws IOException {
        // Load the vertex shader
        vid = loadShader(vertex, GL20.GL_VERTEX_SHADER);

        // Load the fragment shader
        fid = loadShader(fragment, GL20.GL_FRAGMENT_SHADER);

        // Create the program
        program = GL20.glCreateProgram();

        // Link and attach everything
        GL20.glAttachShader(program, vid);
        GL20.glAttachShader(program, fid);
        GL20.glLinkProgram(program);

        // All vertex shaders must define these attributes
        GL20.glBindAttribLocation(program, 0, "position");
        GL20.glBindAttribLocation(program, 1, "color");
        GL20.glBindAttribLocation(program, 2, "tex");

        // Validate the linked program
        GL20.glValidateProgram(program);
    }

    /**
     * Enable this shader.
     */
    public void use() {
        GL20.glUseProgram(program);
        activeProgram = program;
    }

    /**
     * Disable this shader.
     */
    public void release() {
        GL20.glUseProgram(0);
    }

    /**
     * Loads a vertex or fragment shader and returns its GL id.
     *
     * @param filename The shader to load
     * @param type The type of shader this is (vertex or fragment)
     * @return The vertex or shader GL id.
     * @throws IOException Thrown if the file `filename` is not found.
     */
    @SuppressWarnings("deprecation")
    private int loadShader(String filename, int type) throws IOException {
        StringBuilder shaderSource = new StringBuilder();

        // Read in the shader
        String line = null;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while ((line = reader.readLine()) != null) 
            shaderSource.append(line).append("\n");
        reader.close();

        // Create the shader and compile it
        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, shaderSource);
        GL20.glCompileShader(id);

        // Make sure nothing bad happened, we do a full exit if a shader
        // fails. This probably is not ideal, but if a shader fails then
        // we are royally screwed.
        if (GL20.glGetShader(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(id, 1000));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }

        return id;
    }

    /**
     * Update a uniform in the fragment shader.
     *
     * @param string The uniform name in the fragment shader
     * @param mat Replace the uniform value with this matrix
     */
    public void update(String string, Matrix4f mat) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.store(buffer);
        buffer.flip();
        int uniform = GL20.glGetUniformLocation(program, string);
        GL20.glUniformMatrix4(uniform, false, buffer);
    }

    /**
     * Update a uniform in the fragment shader.
     *
     * @param string The uniform name in the fragment shader
     * @param vec Replace the uniform value with this vec
     */
    public void update(String string, Vector3f vec) {
        int uniform = GL20.glGetUniformLocation(program, string);
        GL20.glUniform3f(uniform, vec.x, vec.y, vec.z);
    }

    public static int getActiveShader() {
        return activeProgram;
    }

}
