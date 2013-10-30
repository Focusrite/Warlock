/**
 * File: warlock.graphic.ShaderProgram.java
 *
 * A helper class for loading shaders into OpenGL, adapted and modified version of a tutorial written
 * by Johannes Schaback aka Schabby.
 *
 * @ http://schabby.de/opengl-shader-example/ Last checked 2013-10-17.
 *
 */

package warlock.graphic;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;
import warlock.resource.ResourceHandler;

/**

 */
public class ShaderProgram {
   // OpenGL handle that will point to the executable shader program
   // that can later be used for rendering

   private int programId;

   public void init(String vertexShaderFilename, String fragmentShaderFilename) {
      // create the shader program. If OK, create vertex and fragment shaders
      programId = glCreateProgram();

      // load and compile the two shaders
      int vertShader = loadAndCompileShader(vertexShaderFilename, GL_VERTEX_SHADER);
      int fragShader = loadAndCompileShader(fragmentShaderFilename, GL_FRAGMENT_SHADER);

      // attach the compiled shaders to the program
      glAttachShader(programId, vertShader);
      glAttachShader(programId, fragShader);

      // now link the program
      glLinkProgram(programId);

      // validate linking
      if (glGetProgrami(programId, GL_LINK_STATUS) == GL11.GL_FALSE) {
         throw new RuntimeException("Could not link shader. Reason: " + glGetProgramInfoLog(programId, 1000));
      }

      // perform general validation that the program is usable
      glValidateProgram(programId);

      if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
         throw new RuntimeException("Could not validate shader. Reason: " + glGetProgramInfoLog(programId, 1000));
      }
   }

   /*
    * With the exception of syntax, setting up vertex and fragment shaders
    * is the same.
    * @param the name and path to the vertex shader
    */
   private int loadAndCompileShader(String filename, int shaderType) {
      //vertShader will be non zero if succefully created
      int handle = glCreateShader(shaderType);

      if (handle == 0) {
         throw new RuntimeException("Could not created shader of type " + shaderType + " for file " +
            filename + ". " + glGetProgramInfoLog(programId, 1000));
      }

      // load code from file into String
      String code = ResourceHandler.loadTextResource(filename);

      // upload code to OpenGL and associate code with shader
      glShaderSource(handle, code);

      // compile source code into binary
      glCompileShader(handle);

      // acquire compilation status
      int shaderStatus = glGetShaderi(handle, GL20.GL_COMPILE_STATUS);

      // check whether compilation was successful
      if (shaderStatus == GL11.GL_FALSE) {
         throw new IllegalStateException("Compilation error for shader [" + filename + "]. Reason: " +
            glGetShaderInfoLog(handle, 1000));
      }

      return handle;
   }

   public int getProgramId() {
      return programId;
   }
}