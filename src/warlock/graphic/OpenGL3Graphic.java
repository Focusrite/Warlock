/**
 * File: warlock.graphic.OpenGL3Graphic.java
 *
 * Extends Graphic to provide an LWJGL OpenGL3 implementation of the graphic needed to display this game
 * properly. Read "Graphic" for more info on the subject.
 *
 * Unless otherwise stated all x,y are centered on the figure being drawed.
 */
package warlock.graphic;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import warlock.camera.Camera;
import warlock.font.Font;
import warlock.font.FontDefinition;
import warlock.font.FontHandler;

/**
 *
 * @author Focusrite
 */
public class OpenGL3Graphic extends Graphic {

   private static final int SHADERMODE_COLOR = 0;
   private static final int SHADERMODE_TEXTURE = 1;
   private Map<String, VertexDefination> vertexDefinations = new HashMap<>();
   private static final int CIRCLE_DENSITY = 12;
   private static final float Z_ELEVATION = 0.01f;
   //Shader handles
   private ShaderProgram shader;
   private int mvpId;
   private int colorId;
   private int shaderModeId;

   /**
    * Initialize a new OpenGL3Graphic
    *
    * @param screenWidth
    * @param screenHeight
    */
   public OpenGL3Graphic(int screenWidth, int screenHeight) {
      super(screenWidth, screenHeight);
   }

   /**
    * Build a model matrix for an object to render.
    *
    * @param translate
    * @param rotation
    * @param scaling
    * @return the final matrix
    */
   public Matrix4f buildModelMatrix(Vector3f translate, double rotation, Vector3f scaling) {
      Matrix4f translateMatrix = new Matrix4f();
      translateMatrix.m30 = translate.x;
      translateMatrix.m31 = translate.y;
      translateMatrix.m32 = translate.z;

      Matrix4f scalingMatrix = new Matrix4f();
      scalingMatrix.m00 = scaling.x;
      scalingMatrix.m11 = scaling.y;
      scalingMatrix.m22 = scaling.z;

      //rotation around z-axis
      Matrix4f rotationMatrix = new Matrix4f();
      rotationMatrix.m00 = (float) Math.cos(rotation);
      rotationMatrix.m10 = -(float) Math.sin(rotation);
      rotationMatrix.m01 = (float) Math.sin(rotation);
      rotationMatrix.m11 = (float) Math.cos(rotation);

      Matrix4f left = translateMatrix;
      Matrix4f right = rotationMatrix;
      Matrix4f result = new Matrix4f();
      Matrix4f.mul(left, right, result);
      right = scalingMatrix;
      Matrix4f.mul(result, right, left);
      return left;
   }

   /**
    * Combine model matrix, view matrix and projectionmatrix to one MVP matrix.
    * @param model
    * @param camera
    * @return MVP matrix
    */
   public Matrix4f buildMVPMatrix(Matrix4f model, Camera camera) {
      //If screen coordinates are set to be used, disregard camera position
      Matrix4f viewMatrix = (isScreenCoordinates()) ? new Matrix4f() : camera.getViewMatrix();

      Matrix4f result = new Matrix4f();
      Matrix4f.mul(camera.getProjectionMatrix(), viewMatrix, result);
      Matrix4f.mul(result, model, result);
      return result;
   }

   /**
    * Draw a puny rectangle in a single color
    * @param x
    * @param y
    * @param w
    * @param h
    * @param rot
    * @param color
    */
   @Override
   public void drawRectangle(int x, int y, int w, int h, double rot, Color color) {
      drawRectangle(x, y, 0, w, h, rot, color);
   }

   /**
    * Draw a puny rectangle in a single color
    * @param x
    * @param y
    * @param z
    * @param w
    * @param h
    * @param rot
    * @param color
    */
   @Override
   public void drawRectangle(int x, int y, int z, int w, int h, double rot, Color color) {
      VertexDefination def = vertexDefinations.get("rectangle");
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y, Z_ELEVATION * z), rot, new Vector3f(w / 2, h / 2, 1)), getCamera());
      drawDefination(def, mvp);
   }

   /**
    * Draw the outlines of a rectangle.
    * @param x
    * @param y
    * @param w
    * @param h
    * @param thickness
    * @param rot
    * @param color
    */
   @Override
   public void drawUnfilledRectangle(int x, int y, int w, int h, int thickness, double rot, Color color) {
      drawUnfilledRectangle(x, y, 0, w, h, thickness, rot, color);
   }

   /**
    * Draw the outlines of a rectangle
    * @param x
    * @param y
    * @param z
    * @param w
    * @param h
    * @param thickness
    * @param rot
    * @param color
    */
   @Override
   public void drawUnfilledRectangle(int x, int y, int z, int w, int h, int thickness, double rot, Color color) {
      VertexDefination def = vertexDefinations.get("rectangle");
      bindColor(color);

      //Left
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x - (w / 2), y, Z_ELEVATION * z), rot, new Vector3f(thickness, h / 2, 1)), getCamera());
      drawDefination(def, mvp);

      //Right
      mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x + (w / 2), y, Z_ELEVATION * z), rot, new Vector3f(thickness, h / 2, 1)), getCamera());
      drawDefination(def, mvp);

      //Top
      mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y + (h / 2), Z_ELEVATION * z), rot, new Vector3f(w / 2, thickness, 1)), getCamera());
      drawDefination(def, mvp);

      //Bottom
      mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y - (h / 2), Z_ELEVATION * z), rot, new Vector3f(w / 2, thickness, 1)), getCamera());
      drawDefination(def, mvp);
   }

   /**
    * Draw a rectangle with outline of another color
    * @param x
    * @param y
    * @param w
    * @param h
    * @param thickness
    * @param rot
    * @param fill
    * @param border
    */
   @Override
   public void drawBorderedRectangle(int x, int y, int w, int h, int thickness, double rot, Color fill, Color border) {
      drawBorderedRectangle(x, y, 0, w, h, thickness, rot, fill, border);
   }

   /**
    * Draw a rectangle with outline of another color
    * @param x
    * @param y
    * @param z
    * @param w
    * @param h
    * @param thickness
    * @param rot
    * @param fill
    * @param border
    */
   @Override
   public void drawBorderedRectangle(int x, int y, int z, int w, int h, int thickness, double rot, Color fill, Color border) {
      drawRectangle(x, y, z, w, h, rot, fill);
      drawUnfilledRectangle(x, y, z, w, h, thickness, rot, border);
   }

   /**
    * Draw a circle
    * @param x
    * @param y
    * @param r
    * @param color
    */
   @Override
   public void drawCircle(int x, int y, int r, Color color) {
      drawCircle(x, y, 0, r, color);
   }

   /**
    * Draw a circle
    * @param x
    * @param y
    * @param z
    * @param r
    * @param color
    */
   @Override
   public void drawCircle(int x, int y, int z, int r, Color color) {
      VertexDefination def = vertexDefinations.get("circle");
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y, Z_ELEVATION * z), 0, new Vector3f(r, r, 1)), getCamera());
      drawDefination(def, mvp);
   }

   /**
    * Draw a triangle
    * @param x
    * @param y
    * @param w
    * @param h
    * @param rot
    * @param color
    */
   @Override
   public void drawTriangle(int x, int y, int w, int h, double rot, Color color) {
      drawTriangle(x, y, 0, w, h, rot, color);
   }

   /**
    * Draw a triangle
    * @param x
    * @param y
    * @param z
    * @param w
    * @param h
    * @param rot
    * @param color
    */
   @Override
   public void drawTriangle(int x, int y, int z, int w, int h, double rot, Color color) {
      VertexDefination def = vertexDefinations.get("triangle");
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(new Vector3f(x, y, Z_ELEVATION * z), rot, new Vector3f(w / 2, h / 2, 1)), getCamera());
      drawDefination(def, mvp);
   }

   /**
    * Draw a vertex definition and modify it based on the mvp matrix.
    * @param def
    * @param mvp
    */
   public void drawDefination(VertexDefination def, Matrix4f mvp) {
      //Bind
      def.bindDefiniaton();
      //Bind MVP to shader
      FloatBuffer fBuffer = BufferUtils.createFloatBuffer(16);
      mvp.store(fBuffer);
      fBuffer.flip();
      glUniformMatrix4(mvpId, false, fBuffer);
      //Draw
      glDrawArrays(def.getDrawMethod(), 0, def.getDefinitionSize());
      //Cleanup
      def.unbind();
   }

   /**
    * Send a color to the shader for use when drawing7
    * @param color
    */
   private void bindColor(Color color) {
      float[] arr = color.toFloatArray();
      glUniform4f(colorId, arr[0], arr[1], arr[2], arr[3]); //rgba
   }

   /**
    * Initialize the OpenGL instance with the correct flags used for this project and load shader
    */
   @Override
   public void init() {
      //Init vbo
      createVertexDefinations();

      //2D Initialization
      glEnable(GL_TEXTURE_2D);
      glEnable(GL_DEPTH_TEST);
      glDisable(GL_LIGHTING);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      glEnable(GL_BLEND);
      glClearColor(0.1f, 0.1f, 0.1f, 0.0f);

      //Init shaders
      loadShaders("shaders/vertexshader.glsl", "shaders/fragmentshader.glsl");
   }

   /**
    * Bind a texture for use in the shader.
    * @param tex
    */
   private void bindTexture(Texture tex) {
      glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
   }

   /**
    * Release the bound texture from the shader
    */
   private void unbindTexture() {
      glBindTexture(GL_TEXTURE_2D, 0);
   }

   /**
    * Create the "normal" vertex definitions (ie both VBO and VAO) that's used for basic geometrical
    * rendering.
    */
   private void createVertexDefinations() {
      //Rectangle
      float vertices[] = {
         -1.0f, 1.0f, 0.0f, //upper left
         1.0f, 1.0f, 0.0f, //upper right
         -1.0f, -1.0f, 0.0f //lower left
      };
      float uv[] = {
         0.0f, 0.0f,
         1.0f, 0.0f,
         0.0f, 1.0f};
      VertexDefination triangle = new VertexDefination(vertices, uv, GL_TRIANGLES);
      vertexDefinations.put("triangle", triangle);

      //Rectangle
      vertices = new float[]{
         -1.0f, 1.0f, 0.0f,
         1.0f, 1.0f, 0.0f,
         -1.0f, -1.0f, 0.0f,
         -1.0f, -1.0f, 0.0f,
         1.0f, 1.0f, 0.0f,
         1.0f, -1.0f, 0.0f,};
      uv = new float[]{
         0.0f, 0.0f,
         1.0f, 0.0f,
         0.0f, 1.0f,
         0.0f, 1.0f,
         1.0f, 0.0f,
         1.0f, 1.0f
      };
      VertexDefination rectangle = new VertexDefination(vertices, uv, GL_TRIANGLES); //Because textures are fucked with triangle_strip
      vertexDefinations.put("rectangle", rectangle);

      //Circle
      vertices = new float[(CIRCLE_DENSITY + 2) * 3];
      uv = new float[(CIRCLE_DENSITY + 2) * 2];
      //Center
      vertices[0] = 0.0f;
      vertices[1] = 0.0f;
      vertices[2] = 0.0f;
      uv[0] = 0.5f;
      uv[1] = 0.5f;
      //Outer vertices
      for (int i = 1; i <= CIRCLE_DENSITY + 1; i++) {
         float theta = (float) (2.0f * Math.PI * i / (float) CIRCLE_DENSITY);

         float x = (float) Math.cos(theta);//calculate the x component
         float y = (float) Math.sin(theta);//calculate the y component

         vertices[i * 3] = x;
         vertices[i * 3 + 1] = y;
         vertices[i * 3 + 2] = 0.0f;
         uv[i * 2] = (x / 2) + uv[0]; //Clamp to [0,1]
         uv[i * 2 + 1] = (y / 2) + uv[1]; //Clamp to [0,1]

      }
      VertexDefination circle = new VertexDefination(vertices, uv, GL_TRIANGLE_FAN);
      vertexDefinations.put("circle", circle);
   }

   /**
    * Resize made, only currently ran on window creation
    */
   @Override
   public void resize() {
      //2D Scene
      glViewport(0, 0, getScreenWidth(), getScreenHeight());
      glMatrixMode(GL_PROJECTION);
   }

   /**
    * Set which mode the shader is in, either SHADERMODE_COLOR or SHADERMODE_TEXTURE.
    * In SHADERMODE_COLOR mode texture info is discarded if bound and only color affects a fragment's
    * color.
    * In SHADERMODE_TEXTURE the texture color value is multiplied with the color value (so the color
    * is used as color intensity).
    * @param mode
    */
   private void setShaderMode(int mode) {
      glUniform1i(shaderModeId, mode);
   }

   /**
    * Clear the buffer rendered to and setup rendering with shader.
    */
   @Override
   public void preRender() {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      glUseProgram(shader.getProgramId());
      glLoadIdentity();
      setShaderMode(SHADERMODE_COLOR);
   }

   /**
    * Unused currently
    */
   @Override
   public void postRender() { }

   /**
    * Load a shader program.
    * @param vertexPath
    * @param fragmentPath
    */
   public void loadShaders(String vertexPath, String fragmentPath) {
      shader = new ShaderProgram();
      shader.init(vertexPath, fragmentPath);
      mvpId = glGetUniformLocation(shader.getProgramId(), "MVP");
      colorId = glGetUniformLocation(shader.getProgramId(), "color");
      shaderModeId = glGetUniformLocation(shader.getProgramId(), "shaderMode");
   }

   /**
    * Draw text.
    * @param font
    * @param x
    * @param y
    * @param z
    * @param text
    * @param size
    * @param color
    */
   @Override
   public void drawText(String font, int x, int y, int z, String text, int size, Color color) {
      drawText(font, x, y, z, text, size, color, false);
   }

   /**
    * Draw text.
    * @param font
    * @param x
    * @param y
    * @param z
    * @param text
    * @param size
    * @param color
    * @param centered
    */
   @Override
   public void drawText(String font, int x, int y, int z, String text, int size, Color color, boolean centered) {
      FontHandler.drawFont(this, font, x, y, z, text, size, color, centered);
   }

   /**
    * Draw a single character of font
    * @param c
    * @param f
    * @param x
    * @param y
    * @param z
    * @param size
    * @param color
    */
   @Override
   public void drawCharacter(char c, Font f, int x, int y, int z, int size, Color color) {
      FontDefinition def = f.getDefinition(c);
      setShaderMode(SHADERMODE_TEXTURE);
      float sizeFactor = size / (f.getSize() * 2.0f);
      bindTexture(f.getTexture());
      bindColor(color);
      Matrix4f mvp = buildMVPMatrix(buildModelMatrix(
         new Vector3f(x + (def.getWidth() / 2), y  - def.getOffset() - (def.getHeight() / 2), //def.getHeight() - size
         Z_ELEVATION * z), 0, //Pos
         new Vector3f(def.getWidth() * sizeFactor, def.getHeight() * sizeFactor, 1)), getCamera()); //Size
      drawDefination(def.getDefinition(), mvp);
      unbindTexture();
      setShaderMode(SHADERMODE_COLOR);
   }

   /**
    * Draw texture. Based on rectangle draw but with a bound texture on top.
    * @param texture
    * @param x
    * @param y
    * @param z
    * @param w
    * @param h
    * @param rot
    * @param tint
    */
   @Override
   public void drawTexture(Texture texture, int x, int y, int z, int w, int h, double rot, Color tint) {
      setShaderMode(SHADERMODE_TEXTURE);
      bindTexture(texture);
      drawRectangle(x, y, z, w, h, rot, tint);
      unbindTexture();
      setShaderMode(SHADERMODE_COLOR);
   }

   /**
    * Draw texture. Based on rectangle draw but with a bound texture on top.
    * @param texture
    * @param x
    * @param y
    * @param z
    * @param w
    * @param h
    * @param rot
    */
   @Override
   public void drawTexture(Texture texture, int x, int y, int z, int w, int h, double rot) {
      drawTexture(texture, x, y, z, w, h, rot, Color.WHITE);
   }

   /**
    * Draw texture. Based on rectangle draw but with a bound texture on top.
    * @param texture
    * @param x
    * @param y
    * @param w
    * @param h
    * @param rot
    */
   @Override
   public void drawTexture(Texture texture, int x, int y, int w, int h, double rot) {
      drawTexture(texture, x, y, 0, w, h, rot);
   }
}
