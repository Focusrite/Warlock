/**
 * File: warlock.camera.Camera.java
 *
 * The class keeping track of the camera position, viewport and similar things. Also responsible for
 * building the viewMatrix and projectionMatrix used for the graphic library to render everything at
 * it's correct place.
 */
package warlock.camera;

import org.lwjgl.util.vector.*;

/**
 *
 * @author Focusrite
 */
public class Camera {

   private Vector3f lookAt;
   private Vector3f cameraPosition;
   private Vector3f tilt;

   private Matrix4f viewMatrix;
   private Matrix4f projectionMatrix;

   private float width;
   private float height;

   private static final float EYE_X = 0;
   private static final float EYE_Y = 0;
   private static final float EYE_Z = 0;
   private static final float LOOKAT_Z = 1;
   private static final float NEAR = -1.0f;
   private static final float FAR = 1.0f;

   public static int SCROLL_ZONE_SIZE = 40;

   /**
    * Creates a new camera
    *
    * @param x the x value initialized at, both lookAt and cameraPosition
    * @param y the y value initialized at, both lookAt and cameraPosition
    * @param width, the viewport width
    * @param height, the viewport height
    */
   public Camera(float x, float y, float width, float height) {
      viewMatrix = new Matrix4f();
      projectionMatrix = new Matrix4f();
      this.width = width;
      this.height = height;
      reset();
      move(x, y); //builds view matrix
      buildProjectionMatrix();
   }

   /**
    * Reset this camera to (0,0)
    */
   public final void reset() {
      lookAt = new Vector3f(EYE_X, EYE_Y, LOOKAT_Z);
      cameraPosition = new Vector3f(EYE_X, EYE_Y, EYE_Z);
      tilt = new Vector3f(0.0f, 1.0f, 0.0f);
      buildViewMatrix();
      buildProjectionMatrix();
   }

   /**
    * Move camera lookAt and cameraPosition dx, dy, dz.
    * @param dx
    * @param dy
    * @param dz
    */
   public void move(float dx, float dy, float dz) {
      lookAt.x += dx;
      lookAt.y += dy;
      cameraPosition.x = lookAt.x;
      cameraPosition.y = lookAt.y;
      cameraPosition.z += dz;
      buildViewMatrix(); //Update view matrix
   }

   /**
    * @param x, new cameraPosition and lookAt x value
    */
   public void setX(float x) {
      this.lookAt.x = x;
      this.cameraPosition.x = x;
      buildViewMatrix(); //Update view matrix
   }

   /**
    * @param y, new cameraPosition and lookAt y value
    */
   public void setY(float y) {
      this.lookAt.y = y;
      this.cameraPosition.y = y;
      buildViewMatrix(); //Update view matrix
   }

   /**
    * Shortand for move(dx,dy, 0)
    * @param dx
    * @param dy
    */
   public final void move(float dx, float dy) {
      move(dx, dy, 0);
   }

   /**
    * @return current lookAt x value
    */
   public float getLookAtX() {
      return lookAt.x;
   }

   /**
    * @return current lookAt y value
    */
   public float getLookAtY() {
      return lookAt.y;
   }

   /**
    * @return current lookAt y value
    */
   public float getLookAtZ() {
      return lookAt.z;
   }

   /**
    * @return current cameraPosition x value
    */
   public float getCameraPositionX() {
      return cameraPosition.x;
   }

   /**
    * @return current cameraPosition y value
    */
   public float getCameraPositionY() {
      return cameraPosition.y;
   }

   /**
    * @return current cameraPosition z value
    */
   public float getCameraPositionZ() {
      return cameraPosition.z;
   }

   /**
    * Rebuilds the viewmatrix
    */
   public final void buildViewMatrix() {
      //Calculation vectors
      Vector3f zaxis = new Vector3f();
      Vector3f.sub(lookAt, cameraPosition, zaxis);
      zaxis.normalise();

      Vector3f xaxis = new Vector3f();
      Vector3f.cross(tilt, zaxis, xaxis);
      xaxis.normalise();

      Vector3f yaxis = new Vector3f();
      Vector3f.cross(zaxis, xaxis, yaxis);

      //Matrices
      Matrix4f orientation = new Matrix4f();
      Matrix4f tranlation = new Matrix4f();
      Matrix4f result = new Matrix4f();

      //Orientation
      orientation.m00 = xaxis.x;
      orientation.m10 = xaxis.y;
      orientation.m20 = xaxis.z;

      orientation.m01 = yaxis.x;
      orientation.m11 = yaxis.y;
      orientation.m21 = yaxis.z;

      orientation.m02 = zaxis.x;
      orientation.m12 = zaxis.y;
      orientation.m22 = zaxis.z;

      //Translation
      tranlation.m30 = -cameraPosition.x;
      tranlation.m31 = -cameraPosition.y;
      tranlation.m32 = -cameraPosition.z;

      Matrix4f.mul(tranlation, orientation, result);
      viewMatrix = result;
   }

   /**
    * Rebuilds the projection matrix
    */
   public final void buildProjectionMatrix() {
      //orthogonal projection matrix
      Matrix4f projection = new Matrix4f();
      //Some linear algebra and aid from "Introduction to 3D game programming" by Frank Luna..
      projection.m00 = 2 / width;
      projection.m11 = 2 / height;
      projection.m22 = -2 / (FAR - NEAR);

      projection.m30 = -1;
      projection.m31 = -1;
      projection.m32 = -(FAR + NEAR) / (FAR - NEAR);
      projectionMatrix = projection;
   }

   /**
    * @return view matrix
    */
   public Matrix4f getViewMatrix() {
      return viewMatrix;
   }

   /**
    * @return projection matrix
    */
   public Matrix4f getProjectionMatrix() {
      return projectionMatrix;
   }

   /**
    * @return viewport width
    */
   public float getWidth() {
      return width;
   }

   /**
    * @return viewport height
    */
   public float getHeight() {
      return height;
   }
}
