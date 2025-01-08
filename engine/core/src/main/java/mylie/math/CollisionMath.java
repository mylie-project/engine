package mylie.math;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class CollisionMath {
	private CollisionMath() {
	}
	public static boolean collides(Plane plane, AlignedBox box) {
		// Get the AABB's center and half extents
		Vector3f boxCenter = new Vector3f(box.min()).add(box.max()).mul(0.5f);
		Vector3f boxHalfExtent = new Vector3f(box.max()).sub(boxCenter);

		// Get the plane's normal and distance
		Vector4f data = plane.data();

		// Calculate the projection interval radius of the AABB onto the plane normal
		float r = boxHalfExtent.x * Math.abs(data.x) + boxHalfExtent.y * Math.abs(data.y)
				+ boxHalfExtent.z * Math.abs(data.z);

		// Calculate the signed distance from the AABB center to the plane
		Vector3f xyz = data.xyz(new Vector3f());
		float d = xyz.dot(boxCenter) - data.z;

		// Check for collision
		return Math.abs(d) <= r; // Collision occurs if the distance is within the projection interval
	}

	public static boolean collides(AlignedBox box, Frustum frustum) {
		Vector3f[] corners = new Vector3f[8];

		// Compute all corners of the AABB
		corners[0] = new Vector3f(box.min().x, box.min().y, box.min().z); // Min corner
		corners[1] = new Vector3f(box.min().x, box.min().y, box.max().z);
		corners[2] = new Vector3f(box.min().x, box.max().y, box.min().z);
		corners[3] = new Vector3f(box.min().x, box.max().y, box.max().z);
		corners[4] = new Vector3f(box.max().x, box.min().y, box.min().z);
		corners[5] = new Vector3f(box.max().x, box.min().y, box.max().z);
		corners[6] = new Vector3f(box.max().x, box.max().y, box.min().z);
		corners[7] = new Vector3f(box.max().x, box.max().y, box.max().z);

		// Check if at least one corner of the AABB is inside the Frustum
		for (Vector3f corner : corners) {
			if (frustum.nearPlane().distanceToPoint(corner) >= 0 && frustum.farPlane().distanceToPoint(corner) >= 0
					&& frustum.leftPlane().distanceToPoint(corner) >= 0
					&& frustum.rightPlane().distanceToPoint(corner) >= 0
					&& frustum.topPlane().distanceToPoint(corner) >= 0
					&& frustum.bottomPlane().distanceToPoint(corner) >= 0) {
				return true; // A corner is inside the Frustum
			}
		}

		// No corners are inside; check if the Frustum intersects the AABB
		return collides(frustum.nearPlane(), box) || collides(frustum.farPlane(), box)
				|| collides(frustum.leftPlane(), box) || collides(frustum.rightPlane(), box)
				|| collides(frustum.topPlane(), box) || collides(frustum.bottomPlane(), box);
	}

	public static boolean collides(OrientedBox obb, AlignedBox aabb) {
		// Center of the OBB and AABB
		Vector3f obbCenter = obb.center();
		Vector3f aabbCenter = new Vector3f(aabb.min()).add(aabb.max()).mul(0.5f);

		// Half-sizes of the AABB
		Vector3f aabbHalfSize = new Vector3f(aabb.max()).sub(aabbCenter);

		// Axes of the OBB
		Vector3f[] obbAxes = obb.axes(); // Unit vectors representing OBB's local axes
		Vector3f obbHalfSize = obb.halfSizes();

		// Translation vector between OBB and AABB centers
		Vector3f translation = new Vector3f(aabbCenter).sub(obbCenter);

		// Matrix representing the absolute values of rotations from OBB axes to AABB
		// axes
		float[][] rotation = new float[3][3];
		float[][] absRotation = new float[3][3];

		// Populate rotation matrices
		for (int i = 0; i < 3; i++) {
			rotation[i][0] = obbAxes[i].dot(new Vector3f(1, 0, 0)); // OBB axis i projected onto AABB x-axis
			rotation[i][1] = obbAxes[i].dot(new Vector3f(0, 1, 0)); // OBB axis i projected onto AABB y-axis
			rotation[i][2] = obbAxes[i].dot(new Vector3f(0, 0, 1)); // OBB axis i projected onto AABB z-axis

			absRotation[i][0] = Math.abs(rotation[i][0]) + 1e-6f; // Small tolerance to counter numerical error
			absRotation[i][1] = Math.abs(rotation[i][1]) + 1e-6f;
			absRotation[i][2] = Math.abs(rotation[i][2]) + 1e-6f;
		}

		// Test AABB axes (x, y, z)
		for (int i = 0; i < 3; i++) {
			float ra = aabbHalfSize.get(i);
			float rb = obbHalfSize.x * absRotation[0][i] + obbHalfSize.y * absRotation[1][i]
					+ obbHalfSize.z * absRotation[2][i];
			float t = Math.abs(translation.dot(new Vector3f((i == 0 ? 1 : 0), (i == 1 ? 1 : 0), (i == 2 ? 1 : 0))));
			if (t > ra + rb) {
				return false; // No collision along this axis
			}
		}

		// Test OBB axes
		for (int i = 0; i < 3; i++) {
			float ra = obbHalfSize.get(i);
			float rb = aabbHalfSize.x * absRotation[i][0] + aabbHalfSize.y * absRotation[i][1]
					+ aabbHalfSize.z * absRotation[i][2];
			float t = Math.abs(translation.dot(obbAxes[i]));
			if (t > ra + rb) {
				return false; // No collision along this axis
			}
		}

		// Test cross products of OBB and AABB axes
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				float ra = obbHalfSize.get((i + 1) % 3) * absRotation[(i + 1) % 3][j]
						+ obbHalfSize.get((i + 2) % 3) * absRotation[(i + 2) % 3][j];
				float rb = aabbHalfSize.get((j + 1) % 3) * absRotation[i][(j + 1) % 3]
						+ aabbHalfSize.get((j + 2) % 3) * absRotation[i][(j + 2) % 3];
				float t = Math.abs(translation.dot(new Vector3f(
						obbAxes[i].cross(new Vector3f((j == 0 ? 1 : 0), (j == 1 ? 1 : 0), (j == 2 ? 1 : 0))))));
				if (t > ra + rb) {
					return false; // No collision along this cross-product axis
				}
			}
		}

		return true; // If no separating axis is found, the OBB and AABB intersect
	}

	public static boolean collides(Frustum frustum, OrientedBox box) {
		// Get all 8 corners of the oriented box
		Vector3f[] corners = box.corners();

		// If any corner is inside the frustum, return true immediately
		for (Vector3f corner : corners) {
			if (collides(frustum, corner)) {
				return true;
			}
		}

		// Check if the box intersects with any of the frustum's planes
		return collides(frustum.nearPlane(), box) || collides(frustum.farPlane(), box)
				|| collides(frustum.leftPlane(), box) || collides(frustum.rightPlane(), box)
				|| collides(frustum.topPlane(), box) || collides(frustum.bottomPlane(), box);
	}

	public static boolean collides(Plane plane, OrientedBox box) {
		Vector3f[] corners = box.corners(); // Get the 8 corners of the box
		boolean anyPositive = false;
		boolean anyNegative = false;

		// Check the distance of each corner from the plane
		for (Vector3f corner : corners) {
			float distance = plane.distanceToPoint(corner);
			if (distance > 0) {
				anyPositive = true;
			}
			if (distance < 0) {
				anyNegative = true;
			}
			if (anyPositive && anyNegative) {
				// If box intersects the plane (some corners positive, some negative)
				return true;
			}
		}

		// If all corners are on one side, the box does not intersect the plane
		return false;
	}

	public static boolean collides(Frustum frustum, Vector3f point) {
		// Check if the point is inside all six planes of the frustum
		return frustum.nearPlane().distanceToPoint(point) >= 0 && frustum.farPlane().distanceToPoint(point) >= 0
				&& frustum.leftPlane().distanceToPoint(point) >= 0 && frustum.rightPlane().distanceToPoint(point) >= 0
				&& frustum.topPlane().distanceToPoint(point) >= 0 && frustum.bottomPlane().distanceToPoint(point) >= 0;
	}

	public static boolean collides(Plane plane, Vector3f point) {
		// Distance of the point from the plane
		float distance = plane.distanceToPoint(point);
		return distance >= 0; // True if on or in front of the plane
	}

	public static boolean collides(OrientedBox box1, OrientedBox box2) {
		Vector3f[] axes1 = box1.axes(); // Axes of box1 (3 normalized vectors)
		Vector3f[] axes2 = box2.axes(); // Axes of box2 (3 normalized vectors)
		Vector3f center1 = box1.center();
		Vector3f center2 = box2.center();
		Vector3f halfSizes1 = box1.halfSizes();
		Vector3f halfSizes2 = box2.halfSizes();

		// Compute the vector between the centers of the boxes
		Vector3f d = new Vector3f();
		center2.sub(center1, d); // d = center2 - center1

		// Matrix storing dot products between axes of the two boxes
		float[][] R = new float[3][3];
		float[][] AbsR = new float[3][3]; // Absolute values, to handle floating-point precision issues

		// Compute R and AbsR matrices
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				R[i][j] = axes1[i].dot(axes2[j]); // Compute dot product of axes1[i] and axes2[j]
				AbsR[i][j] = Math.abs(R[i][j]) + 1e-6f; // Add small epsilon to avoid precision errors
			}
		}

		// Test each axis of box1 (separating axis: axes1[i])
		for (int i = 0; i < 3; i++) {
			float projection1 = halfSizes1.get(i);
			float projection2 = halfSizes2.x * AbsR[i][0] + halfSizes2.y * AbsR[i][1] + halfSizes2.z * AbsR[i][2];
			float distance = Math.abs(d.dot(axes1[i]));

			if (distance > projection1 + projection2) {
				return false; // Separating axis found
			}
		}

		// Test each axis of box2 (separating axis: axes2[j])
		for (int j = 0; j < 3; j++) {
			float projection1 = halfSizes1.x * AbsR[0][j] + halfSizes1.y * AbsR[1][j] + halfSizes1.z * AbsR[2][j];
			float projection2 = halfSizes2.get(j);
			float distance = Math.abs(d.dot(axes2[j]));

			if (distance > projection1 + projection2) {
				return false; // Separating axis found
			}
		}

		// Test cross products of axes (15 cross-product axes)
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				// Cross product of axes1[i] and axes2[j]
				Vector3f axis = new Vector3f();
				axes1[i].cross(axes2[j], axis);

				float projection1 = halfSizes1.get((i + 1) % 3) * AbsR[(i + 2) % 3][j]
						+ halfSizes1.get((i + 2) % 3) * AbsR[(i + 1) % 3][j];
				float projection2 = halfSizes2.get((j + 1) % 3) * AbsR[i][(j + 2) % 3]
						+ halfSizes2.get((j + 2) % 3) * AbsR[i][(j + 1) % 3];
				float distance = Math.abs(d.dot(axis));

				if (distance > projection1 + projection2) {
					return false; // Separating axis found
				}
			}
		}

		// No separating axis found, the boxes must be colliding
		return true;
	}

	public static boolean collides(Sphere sphere, Plane plane) {
		// Get the distance from the sphere's center to the plane
		float distance = plane.distanceToPoint(sphere.center());
		return Math.abs(distance) <= sphere.radius(); // Collision occurs if distance <= radius
	}

	public static boolean collides(Sphere sphere, AlignedBox box) {
		Vector3f center = sphere.center();
		float radius = sphere.radius();

		// Clamp the sphere's center to the AABB to find the closest point
		float closestX = Math.max(box.min().x, Math.min(center.x, box.max().x));
		float closestY = Math.max(box.min().y, Math.min(center.y, box.max().y));
		float closestZ = Math.max(box.min().z, Math.min(center.z, box.max().z));

		// Calculate the squared distance from the sphere's center to the closest point
		float distanceSquared = center.distanceSquared(closestX, closestY, closestZ);

		// Collision occurs if the distance is less than or equal to the sphere's radius
		return distanceSquared <= radius * radius;
	}

	public static boolean collides(Ray ray, Sphere sphere) {
		Vector3f center = sphere.center();
		float radius = sphere.radius();

		// Vector from ray origin to the sphere's center
		Vector3f L = new Vector3f(center).sub(ray.origin());

		// Project L onto the ray direction
		float tClosest = L.dot(ray.direction());

		// Closest point on the ray to the sphere's center
		Vector3f closestPoint = new Vector3f(ray.direction()).mul(tClosest).add(ray.origin());

		// Calculate squared distance from closest point to sphere center
		float distanceSquared = closestPoint.distanceSquared(center);

		// Check collision: distance from ray to sphere's center <= sphere's radius
		return distanceSquared <= radius * radius;
	}

	public static boolean collides(Ray ray, AlignedBox box) {
		Vector3f min = box.min();
		Vector3f max = box.max();

		// Perform ray-box intersection test using slab method
		float tMin = (min.x - ray.origin().x) / ray.direction().x;
		float tMax = (max.x - ray.origin().x) / ray.direction().x;
		if (tMin > tMax) {
			float temp = tMin;
			tMin = tMax;
			tMax = temp;
		}

		float tMinY = (min.y - ray.origin().y) / ray.direction().y;
		float tMaxY = (max.y - ray.origin().y) / ray.direction().y;
		if (tMinY > tMaxY) {
			float temp = tMinY;
			tMinY = tMaxY;
			tMaxY = temp;
		}

		if (tMin > tMaxY || tMinY > tMax) {
			return false; // No intersection
		}

		tMin = Math.max(tMin, tMinY);
		tMax = Math.min(tMax, tMaxY);

		float tMinZ = (min.z - ray.origin().z) / ray.direction().z;
		float tMaxZ = (max.z - ray.origin().z) / ray.direction().z;
		if (tMinZ > tMaxZ) {
			float temp = tMinZ;
			tMinZ = tMaxZ;
			tMaxZ = temp;
		}

		return !(tMin > tMaxZ) && !(tMinZ > tMax); // No intersection
		// Intersection occurs
	}

	public static boolean collides(Sphere sphere1, Sphere sphere2) {
		// Calculate the squared distance between the two sphere centers
		float distanceSquared = sphere1.center().distanceSquared(sphere2.center());

		// Get combined radius of both spheres
		float combinedRadius = sphere1.radius() + sphere2.radius();

		// Check collision: distance between centers <= combined radius
		return distanceSquared <= combinedRadius * combinedRadius;
	}

	public static boolean collides(AlignedBox box1, AlignedBox box2) {
		// Check for overlap on each axis
		return box1.max().x >= box2.min().x && box1.min().x <= box2.max().x && box1.max().y >= box2.min().y
				&& box1.min().y <= box2.max().y && box1.max().z >= box2.min().z && box1.min().z <= box2.max().z;
	}

	public static boolean collides(Frustum frustum, Sphere sphere) {
		// Get the center and radius of the sphere
		Vector3f center = sphere.center();
		float radius = sphere.radius();

		// Check if the sphere intersects any of the frustum's planes
		Plane[] planes = frustum.planes();
		for (Plane plane : planes) {
			float distance = plane.distanceToPoint(center);
			if (distance < -radius) {
				return false; // Sphere is completely outside of this plane
			}
		}

		// If it doesn't lie outside of any plane, the sphere intersects or is inside
		// the frustum
		return true;
	}

	public static boolean collides(Frustum frustum, Ray ray) {
		// Check if the ray intersects the frustum by testing it against each plane
		Plane[] planes = frustum.planes();
		Vector3f rayOrigin = ray.origin();
		Vector3f rayDirection = ray.direction();

		float tMin = Float.NEGATIVE_INFINITY;
		float tMax = Float.POSITIVE_INFINITY;

		for (Plane plane : planes) {
			// Calculate the distance from ray origin to the plane
			Vector3f normal = plane.data().xyz(new Vector3f());
			float distance = plane.data().w();
			float denominator = normal.dot(rayDirection);

			if (Math.abs(denominator) > 1e-6) { // Ray is not parallel to the plane
				float t = -(rayOrigin.dot(normal) + distance) / denominator;
				if (denominator < 0) {
					tMin = Math.max(tMin, t); // Update entry point
				} else {
					tMax = Math.min(tMax, t); // Update exit point
				}
				if (tMin > tMax) {
					return false; // Ray misses the frustum
				}
			} else { // Ray is parallel to the plane
				float signedDistance = rayOrigin.dot(normal) + distance;
				if (signedDistance < 0) {
					return false; // Ray origin is outside and pointing away
				}
			}
		}

		// If we pass all the planes, the ray intersects the frustum
		return true;
	}

	public static boolean collides(Frustum frustum1, Frustum frustum2) {
		// Get all the 8 corners of both frustums
		Vector3f[] corners1 = frustum1.corners();
		Vector3f[] corners2 = frustum2.corners();

		// Check if any corner of frustum1 is inside frustum2
		for (Vector3f corner : corners1) {
			if (collides(frustum2, corner)) {
				return true;
			}
		}

		// Check if any corner of frustum2 is inside frustum1
		for (Vector3f corner : corners2) {
			if (collides(frustum1, corner)) {
				return true;
			}
		}

		// Check if the planes of one frustum intersect the other frustum
		Plane[] planes1 = frustum1.planes();
		Plane[] planes2 = frustum2.planes();

		for (Plane plane : planes1) {
			for (Vector3f corner : corners2) {
				if (plane.distanceToPoint(corner) < 0) {
					return false; // One corner of frustum2 is outside a plane of frustum1
				}
			}
		}

		for (Plane plane : planes2) {
			for (Vector3f corner : corners1) {
				if (plane.distanceToPoint(corner) < 0) {
					return false; // One corner of frustum1 is outside a plane of frustum2
				}
			}
		}

		// If no separating plane is found, frustums collide
		return true;
	}

	public static boolean collides(Frustum frustum, Plane plane) {
		// Get all corners of the frustum
		Vector3f[] corners = frustum.corners();

		// Check the position of each corner relative to the plane
		int inFront = 0;
		int behind = 0;

		for (Vector3f corner : corners) {
			float distance = plane.distanceToPoint(corner);
			if (distance < 0) {
				behind++; // Corner is behind the plane
			} else {
				inFront++; // Corner is in front of or on the plane
			}

			// If we have corners both in front and behind, the frustum intersects the plane
			if (inFront > 0 && behind > 0) {
				return true; // Collision occurs (intersection)
			}
		}

		// If all corners are on one side of the plane, there is no collision
		return false;
	}

	public static boolean collides(Ray ray, Plane plane) {
		Vector3f planeNormal = plane.data().xyz(new Vector3f());
		float planeD = plane.data().w();
		Vector3f rayOrigin = ray.origin();
		Vector3f rayDirection = ray.direction();

		// Step 1: Calculate the denominator of the intersection equation (rayDirection
		// . planeNormal)
		float denom = rayDirection.dot(planeNormal);

		// Step 2: If denom is 0, the ray is parallel to the plane (no intersection)
		if (Math.abs(denom) < 1e-6) { // Use a small epsilon for floating-point precision
			return false;
		}

		// Step 3: Calculate the distance from the ray origin to the plane
		float t = -(rayOrigin.dot(planeNormal) + planeD) / denom;

		// Step 4: Check if the intersection point lies in the range of the ray
		return t >= 0; // t < 0 would mean the intersection is "behind" the ray's origin
	}

	public static boolean collides(Ray ray1, Ray ray2) {
		// Extract ray properties
		Vector3f origin1 = ray1.origin();
		Vector3f dir1 = ray1.direction();
		Vector3f origin2 = ray2.origin();
		Vector3f dir2 = ray2.direction();

		// Compute the cross product of the directions
		Vector3f dirCross = new Vector3f();
		dir1.cross(dir2, dirCross);

		// If the cross product is zero (length < epsilon), the rays are parallel
		if (dirCross.lengthSquared() < 1e-6)
			return false;

		// Find the vector between the origins
		Vector3f originDiff = new Vector3f(origin2).sub(origin1);

		// Compute the scalars for the closest points on the rays
		float a = dir1.dot(dir1);
		float b = dir1.dot(dir2);
		float c = dir2.dot(dir2);
		float d = dir1.dot(originDiff);
		float e = dir2.dot(originDiff);

		float denominator = (a * c - b * b);

		// If the denominator is close to zero, the rays are skew but do not intersect
		if (Math.abs(denominator) < 1e-6)
			return false;

		// Compute the parametric values along each ray for their closest points
		float t1 = (b * e - c * d) / denominator;
		float t2 = (a * e - b * d) / denominator;

		// Calculate the closest points on each ray
		Vector3f closestPoint1 = new Vector3f(dir1).mul(t1).add(origin1);
		Vector3f closestPoint2 = new Vector3f(dir2).mul(t2).add(origin2);

		// Check if the closest points are within a small threshold (to account for
		// numerical errors)
		return closestPoint1.distanceSquared(closestPoint2) < 1e-6;
	}

	public static boolean collides(Ray ray, OrientedBox box) {
		// Step 1: Transform the Ray into Box's Local Space

		// Get box's axes (local x, y, z in world space)
		Vector3f[] axes = box.axes(); // Returns {xAxis, yAxis, zAxis} as normalized vectors
		Vector3f center = box.center(); // Box center in world space

		// Translate ray origin relative to the box's center
		Vector3f rayOriginToCenter = new Vector3f(ray.origin()).sub(center);

		// Transform ray origin into the box's local space
		Vector3f localRayOrigin = new Vector3f(rayOriginToCenter.dot(axes[0]), // Project onto x-axis
				rayOriginToCenter.dot(axes[1]), // Project onto y-axis
				rayOriginToCenter.dot(axes[2]) // Project onto z-axis
		);

		// Transform ray direction into the box's local space
		Vector3f localRayDirection = new Vector3f(ray.direction().dot(axes[0]), // Project onto x-axis
				ray.direction().dot(axes[1]), // Project onto y-axis
				ray.direction().dot(axes[2]) // Project onto z-axis
		);

		// Step 2: Perform Ray-AABB Intersection Test in Local Space

		Vector3f boxMin = new Vector3f(box.halfSizes()).negate().add(box.center()); // Local space min corner
																					// (-halfExtents)
		Vector3f boxMax = new Vector3f(box.halfSizes()).add(box.center()); // Local space max corner (+halfExtents)

		float tMin = (boxMin.x - localRayOrigin.x) / localRayDirection.x;
		float tMax = (boxMax.x - localRayOrigin.x) / localRayDirection.x;

		if (tMin > tMax) {
			float temp = tMin;
			tMin = tMax;
			tMax = temp;
		}

		float tyMin = (boxMin.y - localRayOrigin.y) / localRayDirection.y;
		float tyMax = (boxMax.y - localRayOrigin.y) / localRayDirection.y;

		if (tyMin > tyMax) {
			float temp = tyMin;
			tyMin = tyMax;
			tyMax = temp;
		}

		if ((tMin > tyMax) || (tyMin > tMax)) {
			return false; // No collision
		}

		if (tyMin > tMin) {
			tMin = tyMin;
		}

		if (tyMax < tMax) {
			tMax = tyMax;
		}

		float tzMin = (boxMin.z - localRayOrigin.z) / localRayDirection.z;
		float tzMax = (boxMax.z - localRayOrigin.z) / localRayDirection.z;

		if (tzMin > tzMax) {
			float temp = tzMin;
			tzMin = tzMax;
			tzMax = temp;
		}

		return (!(tMin > tzMax)) && (!(tzMin > tMax)); // No collision

		// Step 3: Success - Ray intersects the Oriented Box
	}

	public static boolean collides(Sphere sphere, OrientedBox box) {
		// Step 1: Transform the Sphere's Center into Box's Local Space
		Vector3f[] axes = box.axes();
		Vector3f center = box.center(); // Box center in world space
		Vector3f sphereCenter = sphere.center(); // Sphere center in world space

		// Translate sphere center relative to the box's center
		Vector3f sphereCenterToBoxCenter = new Vector3f(sphereCenter).sub(center);

		// Transform sphere center into the box's local space
		Vector3f localSphereCenter = new Vector3f(sphereCenterToBoxCenter.dot(axes[0]), // Project onto x-axis
				sphereCenterToBoxCenter.dot(axes[1]), // Project onto y-axis
				sphereCenterToBoxCenter.dot(axes[2]) // Project onto z-axis
		);

		// Step 2: Clamp the Sphere's Center to the Box's Local AABB
		Vector3f boxMin = box.center().sub(box.halfSizes(), new Vector3f());
		Vector3f boxMax = box.center().add(box.halfSizes(), new Vector3f());

		Vector3f closestPointOnBox = new Vector3f(Math.clamp(localSphereCenter.x, boxMin.x, boxMax.x),
				Math.clamp(localSphereCenter.y, boxMin.y, boxMax.y),
				Math.clamp(localSphereCenter.z, boxMin.z, boxMax.z));

		// Step 3: Compute the Distance from Local Sphere Center to Closest Point
		float distanceSquared = closestPointOnBox.distanceSquared(localSphereCenter);

		// Step 4: Check if the Distance is Smaller than the Sphere's Radius
		return distanceSquared <= sphere.radiusSquared();
	}

	private static Vector3f getPointOnPlane(Plane plane) {
		// Try to get a point such that one coordinate is 0
		Vector3f normal = new Vector3f(plane.data().x, plane.data().y, plane.data().z);
		float d = -plane.data().w;

		if (Math.abs(normal.x) > 1e-6f) {
			return new Vector3f(-d / normal.x, 0, 0);
		} else if (Math.abs(normal.y) > 1e-6f) {
			return new Vector3f(0, -d / normal.y, 0);
		} else {
			return new Vector3f(0, 0, -d / normal.z);
		}
	}

	public static boolean collides(Plane plane1, Plane plane2) {
		// Helper: Extract plane normals and d values
		Vector3f n1 = new Vector3f(plane1.data().x, plane1.data().y, plane1.data().z); // Plane 1 normal
		Vector3f n2 = new Vector3f(plane2.data().x, plane2.data().y, plane2.data().z); // Plane 2 normal

		// Step 1: Check if the planes are parallel
		Vector3f direction = n1.cross(n2, new Vector3f()); // Direction of the line (cross product of the normals)

		if (direction.lengthSquared() < 1e-6f) { // Normals are parallel
			// Step 2: Check if planes are coincident
			Vector3f pointOnPlane1 = getPointOnPlane(plane1); // Get a random point on plane 1
			float distanceToPlane2 = plane2.distanceToPoint(pointOnPlane1); // Check this point with Plane 2

			return Math.abs(distanceToPlane2) < 1e-6f; // Planes are coincident (overlapping)
			// Planes are parallel but not coincident
		}

		// Step 3: Planes intersect along a line
		return true;
	}

	public static boolean collides(OrientedBox box, Sphere sphere) {
		return collides(sphere, box);
	}

	public static boolean collides(OrientedBox box, Ray ray) {
		return collides(ray, box);
	}

	public static boolean collides(Plane plane, Frustum frustum) {
		return collides(frustum, plane);
	}

	public static boolean collides(Ray ray, Frustum frustum) {
		return collides(frustum, ray);
	}

	public static boolean collides(Sphere sphere, Frustum frustum) {
		return collides(frustum, sphere);
	}

	public static boolean collides(Plane plane, Ray ray) {
		return collides(ray, plane);
	}

	// Reversal for Plane and AlignedBox
	public static boolean collides(AlignedBox box, Plane plane) {
		return collides(plane, box);
	}

	// Reversal for Frustum and AlignedBox
	public static boolean collides(Frustum frustum, AlignedBox box) {
		return collides(box, frustum);
	}

	// Reversal for AlignedBox and OrientedBox
	public static boolean collides(AlignedBox aabb, OrientedBox obb) {
		return collides(obb, aabb);
	}

	// Reversal for OrientedBox and Frustum
	public static boolean collides(OrientedBox box, Frustum frustum) {
		return collides(frustum, box);
	}

	// Reversal for OrientedBox and Plane
	public static boolean collides(OrientedBox box, Plane plane) {
		return collides(plane, box);
	}

	// Reversal for Vector3f and Frustum
	public static boolean collides(Vector3f point, Frustum frustum) {
		return collides(frustum, point);
	}

	// Reversal for Vector3f and Plane
	public static boolean collides(Vector3f point, Plane plane) {
		return collides(plane, point);
	}

	// Reversal for Sphere and Plane
	public static boolean collides(Plane plane, Sphere sphere) {
		return collides(sphere, plane);
	}

	// Reversal for Sphere and AlignedBox
	public static boolean collides(AlignedBox box, Sphere sphere) {
		return collides(sphere, box);
	}

	// Reversal for Sphere and Ray
	public static boolean collides(Sphere sphere, Ray ray) {
		return collides(ray, sphere);
	}

	// Reversal for AlignedBox and Ray
	public static boolean collides(AlignedBox box, Ray ray) {
		return collides(ray, box);
	}

}
