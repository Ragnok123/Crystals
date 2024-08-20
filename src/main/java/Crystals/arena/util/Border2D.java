package Crystals.arena.util;

import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import java.beans.ConstructorProperties;

public class Border2D {
      private double minX;
      private double minZ;
      private double maxX;
      private double maxZ;

      public Border2D(Vector2 v1, Vector2 v2) {
            this.minX = Math.min(v1.x, v2.x);
            this.minZ = Math.min(v1.y, v2.y);
            this.maxX = Math.max(v1.x, v2.x);
            this.maxZ = Math.max(v1.y, v2.y);
      }

      public Border2D(Vector3 v1, Vector3 v2) {
            this.minX = Math.min(v1.x, v2.x);
            this.minZ = Math.min(v1.z, v2.z);
            this.maxX = Math.max(v1.x, v2.x);
            this.maxZ = Math.max(v1.z, v2.z);
      }

      public boolean isVectorInside(Vector3 v) {
            return v.x >= this.minX && v.x <= this.maxX && v.z >= this.minZ && v.z <= this.maxZ;
      }

      public boolean isVectorInside(Vector2 v) {
            return v.x >= this.minX && v.x <= this.maxX && v.y >= this.minZ && v.y <= this.maxZ;
      }

      @ConstructorProperties({"minX", "minZ", "maxX", "maxZ"})
      public Border2D(double minX, double minZ, double maxX, double maxZ) {
            this.minX = minX;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxZ = maxZ;
      }

      public double getMinX() {
            return this.minX;
      }

      public double getMinZ() {
            return this.minZ;
      }

      public double getMaxX() {
            return this.maxX;
      }

      public double getMaxZ() {
            return this.maxZ;
      }
}
