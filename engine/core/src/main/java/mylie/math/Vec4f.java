package mylie.math;

@SuppressWarnings({"SuspiciousNameCombination", "unused"})
public record Vec4f(float x, float y, float z, float w) implements Vec4<Vec4f, Vec2f, Vec3f, Float> {

	@Override
	public Vec4f add(Vec4f other) {
		return new Vec4f(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
	}

	@Override
	public Vec4f sub(Vec4f other) {
		return new Vec4f(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
	}

	@Override
	public Vec4f mul(Vec4f other) {
		return new Vec4f(this.x * other.x, this.y * other.y, this.z * other.z, this.w * other.w);
	}

	@Override
	public Vec4f div(Vec4f other) {
		return new Vec4f(this.x / other.x, this.y / other.y, this.z / other.z, this.w / other.w);
	}

	@Override
	public float dot(Vec4f other) {
		return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
	}

	@Override
	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
	}

	@Override
	public Vec4f normalize() {
		float len = length();
		return new Vec4f(this.x / len, this.y / len, this.z / len, this.w / len);
	}

	@Override
	public Vec4f negate() {
		return new Vec4f(-this.x, -this.y, -this.z, -this.w);
	}

	@Override
	public Vec2f xx() {
		return new Vec2f(this.x, this.x);
	}

	@Override
	public Vec2f xy() {
		return new Vec2f(this.x, this.y);
	}

	@Override
	public Vec2f xz() {
		return new Vec2f(this.x, this.z);
	}

	@Override
	public Vec2f xw() {
		return new Vec2f(this.x, this.w);
	}

	@Override
	public Vec2f yx() {
		return new Vec2f(this.y, this.x);
	}

	@Override
	public Vec2f yy() {
		return new Vec2f(this.y, this.y);
	}

	@Override
	public Vec2f yz() {
		return new Vec2f(this.y, this.z);
	}

	@Override
	public Vec2f yw() {
		return new Vec2f(this.y, this.w);
	}

	@Override
	public Vec2f zx() {
		return new Vec2f(this.z, this.x);
	}

	@Override
	public Vec2f zy() {
		return new Vec2f(this.z, this.y);
	}

	@Override
	public Vec2f zz() {
		return new Vec2f(this.z, this.z);
	}

	@Override
	public Vec2f zw() {
		return new Vec2f(this.z, this.w);
	}

	@Override
	public Vec3f xyz() {
		return new Vec3f(this.x, this.y, this.z);
	}

	@Override
	public Vec3f xyw() {
		return new Vec3f(this.x, this.y, this.w);
	}

	@Override
	public Vec3f xzw() {
		return new Vec3f(this.x, this.z, this.w);
	}

	@Override
	public Vec3f xwx() {
		return new Vec3f(this.x, this.w, this.x);
	}

	@Override
	public Vec3f xwy() {
		return new Vec3f(this.x, this.w, this.y);
	}

	@Override
	public Vec3f xwz() {
		return new Vec3f(this.x, this.w, this.z);
	}

	@Override
	public Vec3f yzw() {
		return new Vec3f(this.y, this.z, this.w);
	}

	@Override
	public Vec3f yxz() {
		return new Vec3f(this.y, this.x, this.z);
	}

	@Override
	public Vec3f yzx() {
		return new Vec3f(this.y, this.z, this.x);
	}

	@Override
	public Vec3f ywx() {
		return new Vec3f(this.y, this.w, this.x);
	}

	@Override
	public Vec3f ywz() {
		return new Vec3f(this.y, this.w, this.z);
	}

	@Override
	public Vec3f yxy() {
		return new Vec3f(this.y, this.x, this.y);
	}

	@Override
	public Vec3f zyw() {
		return new Vec3f(this.z, this.y, this.w);
	}

	@Override
	public Vec3f zxy() {
		return new Vec3f(this.z, this.x, this.y);
	}

	@Override
	public Vec3f zyx() {
		return new Vec3f(this.z, this.y, this.x);
	}

	@Override
	public Vec3f zxz() {
		return new Vec3f(this.z, this.x, this.z);
	}

	@Override
	public Vec3f zxw() {
		return new Vec3f(this.z, this.x, this.w);
	}

	@Override
	public Vec3f wxy() {
		return new Vec3f(this.w, this.x, this.y);
	}

	@Override
	public Vec3f wxz() {
		return new Vec3f(this.w, this.x, this.z);
	}

	@Override
	public Vec3f wyz() {
		return new Vec3f(this.w, this.y, this.z);
	}

	@Override
	public Vec3f wzx() {
		return new Vec3f(this.w, this.z, this.x);
	}

	@Override
	public Vec3f wzy() {
		return new Vec3f(this.w, this.z, this.y);
	}

	@Override
	public Vec3f wyw() {
		return new Vec3f(this.w, this.y, this.w);
	}

	@Override
	public Vec3f wxw() {
		return new Vec3f(this.w, this.x, this.w);
	}

	@Override
	public Vec4f xxxx() {
		return new Vec4f(this.x, this.x, this.x, this.x);
	}

	@Override
	public Vec4f xxxy() {
		return new Vec4f(this.x, this.x, this.x, this.y);
	}

	@Override
	public Vec4f xxxz() {
		return new Vec4f(this.x, this.x, this.x, this.z);
	}

	@Override
	public Vec4f xxxw() {
		return new Vec4f(this.x, this.x, this.x, this.w);
	}

	@Override
	public Vec4f xxyx() {
		return new Vec4f(this.x, this.x, this.y, this.x);
	}

	@Override
	public Vec4f xxyy() {
		return new Vec4f(this.x, this.x, this.y, this.y);
	}

	@Override
	public Vec4f xxyz() {
		return new Vec4f(this.x, this.x, this.y, this.z);
	}

	@Override
	public Vec4f xxyw() {
		return new Vec4f(this.x, this.x, this.y, this.w);
	}

	@Override
	public Vec4f xxzx() {
		return new Vec4f(this.x, this.x, this.z, this.x);
	}

	@Override
	public Vec4f xxzy() {
		return new Vec4f(this.x, this.x, this.z, this.y);
	}

	@Override
	public Vec4f xxzz() {
		return new Vec4f(this.x, this.x, this.z, this.z);
	}

	@Override
	public Vec4f xxzw() {
		return new Vec4f(this.x, this.x, this.z, this.w);
	}

	@Override
	public Vec4f xxwx() {
		return new Vec4f(this.x, this.x, this.w, this.x);
	}

	@Override
	public Vec4f xxwy() {
		return new Vec4f(this.x, this.x, this.w, this.y);
	}

	@Override
	public Vec4f xxwz() {
		return new Vec4f(this.x, this.x, this.w, this.z);
	}

	@Override
	public Vec4f xxww() {
		return new Vec4f(this.x, this.x, this.w, this.w);
	}

	@Override
	public Vec4f xyxx() {
		return new Vec4f(this.x, this.y, this.x, this.x);
	}

	@Override
	public Vec4f xyxy() {
		return new Vec4f(this.x, this.y, this.x, this.y);
	}

	@Override
	public Vec4f xyxz() {
		return new Vec4f(this.x, this.y, this.x, this.z);
	}

	@Override
	public Vec4f xyxw() {
		return new Vec4f(this.x, this.y, this.x, this.w);
	}

	@Override
	public Vec4f xyyx() {
		return new Vec4f(this.x, this.y, this.y, this.x);
	}

	@Override
	public Vec4f xyyy() {
		return new Vec4f(this.x, this.y, this.y, this.y);
	}

	@Override
	public Vec4f xyyz() {
		return new Vec4f(this.x, this.y, this.y, this.z);
	}

	@Override
	public Vec4f xyyw() {
		return new Vec4f(this.x, this.y, this.y, this.w);
	}

	@Override
	public Vec4f xyzx() {
		return new Vec4f(this.x, this.y, this.z, this.x);
	}

	@Override
	public Vec4f xyzy() {
		return new Vec4f(this.x, this.y, this.z, this.y);
	}

	@Override
	public Vec4f xyzz() {
		return new Vec4f(this.x, this.y, this.z, this.z);
	}

	@Override
	public Vec4f xyzw() {
		return new Vec4f(this.x, this.y, this.z, this.w);
	}

	@Override
	public Vec4f xywx() {
		return new Vec4f(this.x, this.y, this.w, this.x);
	}

	@Override
	public Vec4f xywy() {
		return new Vec4f(this.x, this.y, this.w, this.y);
	}

	@Override
	public Vec4f xywz() {
		return new Vec4f(this.x, this.y, this.w, this.z);
	}

	@Override
	public Vec4f xyww() {
		return new Vec4f(this.x, this.y, this.w, this.w);
	}

	@Override
	public Vec4f xzxx() {
		return new Vec4f(this.x, this.z, this.x, this.x);
	}

	@Override
	public Vec4f xzxy() {
		return new Vec4f(this.x, this.z, this.x, this.y);
	}

	@Override
	public Vec4f xzxz() {
		return new Vec4f(this.x, this.z, this.x, this.z);
	}

	@Override
	public Vec4f xzxw() {
		return new Vec4f(this.x, this.z, this.x, this.w);
	}

	@Override
	public Vec4f xzyx() {
		return new Vec4f(this.x, this.z, this.y, this.x);
	}

	@Override
	public Vec4f xzyy() {
		return new Vec4f(this.x, this.z, this.y, this.y);
	}

	@Override
	public Vec4f xzyz() {
		return new Vec4f(this.x, this.z, this.y, this.z);
	}

	@Override
	public Vec4f xzyw() {
		return new Vec4f(this.x, this.z, this.y, this.w);
	}

	@Override
	public Vec4f xzzx() {
		return new Vec4f(this.x, this.z, this.z, this.x);
	}

	@Override
	public Vec4f xzzy() {
		return new Vec4f(this.x, this.z, this.z, this.y);
	}

	@Override
	public Vec4f xzzz() {
		return new Vec4f(this.x, this.z, this.z, this.z);
	}

	@Override
	public Vec4f xzzw() {
		return new Vec4f(this.x, this.z, this.z, this.w);
	}

	@Override
	public Vec4f xzwx() {
		return new Vec4f(this.x, this.z, this.w, this.x);
	}

	@Override
	public Vec4f xzwy() {
		return new Vec4f(this.x, this.z, this.w, this.y);
	}

	@Override
	public Vec4f xzwz() {
		return new Vec4f(this.x, this.z, this.w, this.z);
	}

	@Override
	public Vec4f xzww() {
		return new Vec4f(this.x, this.z, this.w, this.w);
	}

	@Override
	public Vec4f xwxx() {
		return new Vec4f(this.x, this.w, this.x, this.x);
	}

	@Override
	public Vec4f xwxy() {
		return new Vec4f(this.x, this.w, this.x, this.y);
	}

	@Override
	public Vec4f xwxz() {
		return new Vec4f(this.x, this.w, this.x, this.z);
	}

	@Override
	public Vec4f xwxw() {
		return new Vec4f(this.x, this.w, this.x, this.w);
	}

	@Override
	public Vec4f xwyx() {
		return new Vec4f(this.x, this.w, this.y, this.x);
	}

	@Override
	public Vec4f xwyy() {
		return new Vec4f(this.x, this.w, this.y, this.y);
	}

	@Override
	public Vec4f xwyz() {
		return new Vec4f(this.x, this.w, this.y, this.z);
	}

	@Override
	public Vec4f xwyw() {
		return new Vec4f(this.x, this.w, this.y, this.w);
	}

	@Override
	public Vec4f xwzx() {
		return new Vec4f(this.x, this.w, this.z, this.x);
	}

	@Override
	public Vec4f xwzy() {
		return new Vec4f(this.x, this.w, this.z, this.y);
	}

	@Override
	public Vec4f xwzz() {
		return new Vec4f(this.x, this.w, this.z, this.z);
	}

	@Override
	public Vec4f xwzw() {
		return new Vec4f(this.x, this.w, this.z, this.w);
	}

	@Override
	public Vec4f xwwx() {
		return new Vec4f(this.x, this.w, this.w, this.x);
	}

	@Override
	public Vec4f xwwy() {
		return new Vec4f(this.x, this.w, this.w, this.y);
	}

	@Override
	public Vec4f xwwz() {
		return new Vec4f(this.x, this.w, this.w, this.z);
	}

	@Override
	public Vec4f xwww() {
		return new Vec4f(this.x, this.w, this.w, this.w);
	}

	@Override
	public Vec4f yxxx() {
		return new Vec4f(this.y, this.x, this.x, this.x);
	}

	@Override
	public Vec4f yxxy() {
		return new Vec4f(this.y, this.x, this.x, this.y);
	}

	@Override
	public Vec4f yxxz() {
		return new Vec4f(this.y, this.x, this.x, this.z);
	}

	@Override
	public Vec4f yxxw() {
		return new Vec4f(this.y, this.x, this.x, this.w);
	}

	@Override
	public Vec4f yxyx() {
		return new Vec4f(this.y, this.x, this.y, this.x);
	}

	@Override
	public Vec4f yxyy() {
		return new Vec4f(this.y, this.x, this.y, this.y);
	}

	@Override
	public Vec4f yxyz() {
		return new Vec4f(this.y, this.x, this.y, this.z);
	}

	@Override
	public Vec4f yxyw() {
		return new Vec4f(this.y, this.x, this.y, this.w);
	}

	@Override
	public Vec4f yxzx() {
		return new Vec4f(this.y, this.x, this.z, this.x);
	}

	@Override
	public Vec4f yxzy() {
		return new Vec4f(this.y, this.x, this.z, this.y);
	}

	@Override
	public Vec4f yxzz() {
		return new Vec4f(this.y, this.x, this.z, this.z);
	}

	@Override
	public Vec4f yxzw() {
		return new Vec4f(this.y, this.x, this.z, this.w);
	}

	@Override
	public Vec4f yxwx() {
		return new Vec4f(this.y, this.x, this.w, this.x);
	}

	@Override
	public Vec4f yxwy() {
		return new Vec4f(this.y, this.x, this.w, this.y);
	}

	@Override
	public Vec4f yxwz() {
		return new Vec4f(this.y, this.x, this.w, this.z);
	}

	@Override
	public Vec4f yxww() {
		return new Vec4f(this.y, this.x, this.w, this.w);
	}

	@Override
	public Vec4f yyxx() {
		return new Vec4f(this.y, this.y, this.x, this.x);
	}

	@Override
	public Vec4f yyxy() {
		return new Vec4f(this.y, this.y, this.x, this.y);
	}

	@Override
	public Vec4f yyxz() {
		return new Vec4f(this.y, this.y, this.x, this.z);
	}

	@Override
	public Vec4f yyxw() {
		return new Vec4f(this.y, this.y, this.x, this.w);
	}

	@Override
	public Vec4f yyyx() {
		return new Vec4f(this.y, this.y, this.y, this.x);
	}

	@Override
	public Vec4f yyyy() {
		return new Vec4f(this.y, this.y, this.y, this.y);
	}

	@Override
	public Vec4f yyyz() {
		return new Vec4f(this.y, this.y, this.y, this.z);
	}

	@Override
	public Vec4f yyyw() {
		return new Vec4f(this.y, this.y, this.y, this.w);
	}

	@Override
	public Vec4f yyzx() {
		return new Vec4f(this.y, this.y, this.z, this.x);
	}

	@Override
	public Vec4f yyzy() {
		return new Vec4f(this.y, this.y, this.z, this.y);
	}

	@Override
	public Vec4f yyzz() {
		return new Vec4f(this.y, this.y, this.z, this.z);
	}

	@Override
	public Vec4f yyzw() {
		return new Vec4f(this.y, this.y, this.z, this.w);
	}

	@Override
	public Vec4f yywx() {
		return new Vec4f(this.y, this.y, this.w, this.x);
	}

	@Override
	public Vec4f yywy() {
		return new Vec4f(this.y, this.y, this.w, this.y);
	}

	@Override
	public Vec4f yywz() {
		return new Vec4f(this.y, this.y, this.w, this.z);
	}

	@Override
	public Vec4f yyww() {
		return new Vec4f(this.y, this.y, this.w, this.w);
	}

	@Override
	public Vec4f yzxx() {
		return new Vec4f(this.y, this.z, this.x, this.x);
	}

	@Override
	public Vec4f yzxy() {
		return new Vec4f(this.y, this.z, this.x, this.y);
	}

	@Override
	public Vec4f yzxz() {
		return new Vec4f(this.y, this.z, this.x, this.z);
	}

	@Override
	public Vec4f yzxw() {
		return new Vec4f(this.y, this.z, this.x, this.w);
	}

	@Override
	public Vec4f yzyx() {
		return new Vec4f(this.y, this.z, this.y, this.x);
	}

	@Override
	public Vec4f yzyy() {
		return new Vec4f(this.y, this.z, this.y, this.y);
	}

	@Override
	public Vec4f yzyz() {
		return new Vec4f(this.y, this.z, this.y, this.z);
	}

	@Override
	public Vec4f yzyw() {
		return new Vec4f(this.y, this.z, this.y, this.w);
	}

	@Override
	public Vec4f yzzx() {
		return new Vec4f(this.y, this.z, this.z, this.x);
	}

	@Override
	public Vec4f yzzy() {
		return new Vec4f(this.y, this.z, this.z, this.y);
	}

	@Override
	public Vec4f yzzz() {
		return new Vec4f(this.y, this.z, this.z, this.z);
	}

	@Override
	public Vec4f yzzw() {
		return new Vec4f(this.y, this.z, this.z, this.w);
	}

	@Override
	public Vec4f yzwx() {
		return new Vec4f(this.y, this.z, this.w, this.x);
	}

	@Override
	public Vec4f yzwy() {
		return new Vec4f(this.y, this.z, this.w, this.y);
	}

	@Override
	public Vec4f yzwz() {
		return new Vec4f(this.y, this.z, this.w, this.z);
	}

	@Override
	public Vec4f yzww() {
		return new Vec4f(this.y, this.z, this.w, this.w);
	}

	@Override
	public Vec4f ywxx() {
		return new Vec4f(this.y, this.w, this.x, this.x);
	}

	@Override
	public Vec4f ywxy() {
		return new Vec4f(this.y, this.w, this.x, this.y);
	}

	@Override
	public Vec4f ywxz() {
		return new Vec4f(this.y, this.w, this.x, this.z);
	}

	@Override
	public Vec4f ywxw() {
		return new Vec4f(this.y, this.w, this.x, this.w);
	}

	@Override
	public Vec4f ywyx() {
		return new Vec4f(this.y, this.w, this.y, this.x);
	}

	@Override
	public Vec4f ywyy() {
		return new Vec4f(this.y, this.w, this.y, this.y);
	}

	@Override
	public Vec4f ywyz() {
		return new Vec4f(this.y, this.w, this.y, this.z);
	}

	@Override
	public Vec4f ywyw() {
		return new Vec4f(this.y, this.w, this.y, this.w);
	}

	@Override
	public Vec4f ywzx() {
		return new Vec4f(this.y, this.w, this.z, this.x);
	}

	@Override
	public Vec4f ywzy() {
		return new Vec4f(this.y, this.w, this.z, this.y);
	}

	@Override
	public Vec4f ywzz() {
		return new Vec4f(this.y, this.w, this.z, this.z);
	}

	@Override
	public Vec4f ywzw() {
		return new Vec4f(this.y, this.w, this.z, this.w);
	}

	@Override
	public Vec4f ywwx() {
		return new Vec4f(this.y, this.w, this.w, this.x);
	}

	@Override
	public Vec4f ywwy() {
		return new Vec4f(this.y, this.w, this.w, this.y);
	}

	@Override
	public Vec4f ywwz() {
		return new Vec4f(this.y, this.w, this.w, this.z);
	}

	@Override
	public Vec4f ywww() {
		return new Vec4f(this.y, this.w, this.w, this.w);
	}

	@Override
	public Vec4f zxxx() {
		return new Vec4f(this.z, this.x, this.x, this.x);
	}

	@Override
	public Vec4f zxxy() {
		return new Vec4f(this.z, this.x, this.x, this.y);
	}

	@Override
	public Vec4f zxxz() {
		return new Vec4f(this.z, this.x, this.x, this.z);
	}

	@Override
	public Vec4f zxxw() {
		return new Vec4f(this.z, this.x, this.x, this.w);
	}

	@Override
	public Vec4f zxyx() {
		return new Vec4f(this.z, this.x, this.y, this.x);
	}

	@Override
	public Vec4f zxyy() {
		return new Vec4f(this.z, this.x, this.y, this.y);
	}

	@Override
	public Vec4f zxyz() {
		return new Vec4f(this.z, this.x, this.y, this.z);
	}

	@Override
	public Vec4f zxyw() {
		return new Vec4f(this.z, this.x, this.y, this.w);
	}

	@Override
	public Vec4f zxzx() {
		return new Vec4f(this.z, this.x, this.z, this.x);
	}

	@Override
	public Vec4f zxzy() {
		return new Vec4f(this.z, this.x, this.z, this.y);
	}

	@Override
	public Vec4f zxzz() {
		return new Vec4f(this.z, this.x, this.z, this.z);
	}

	@Override
	public Vec4f zxzw() {
		return new Vec4f(this.z, this.x, this.z, this.w);
	}

	@Override
	public Vec4f zxwx() {
		return new Vec4f(this.z, this.x, this.w, this.x);
	}

	@Override
	public Vec4f zxwy() {
		return new Vec4f(this.z, this.x, this.w, this.y);
	}

	@Override
	public Vec4f zxwz() {
		return new Vec4f(this.z, this.x, this.w, this.z);
	}

	@Override
	public Vec4f zxww() {
		return new Vec4f(this.z, this.x, this.w, this.w);
	}

	@Override
	public Vec4f zyxx() {
		return new Vec4f(this.z, this.y, this.x, this.x);
	}

	@Override
	public Vec4f zyxy() {
		return new Vec4f(this.z, this.y, this.x, this.y);
	}

	@Override
	public Vec4f zyxz() {
		return new Vec4f(this.z, this.y, this.x, this.z);
	}

	@Override
	public Vec4f zyxw() {
		return new Vec4f(this.z, this.y, this.x, this.w);
	}

	@Override
	public Vec4f zyyx() {
		return new Vec4f(this.z, this.y, this.y, this.x);
	}

	@Override
	public Vec4f zyyy() {
		return new Vec4f(this.z, this.y, this.y, this.y);
	}

	@Override
	public Vec4f zyyz() {
		return new Vec4f(this.z, this.y, this.y, this.z);
	}

	@Override
	public Vec4f zyyw() {
		return new Vec4f(this.z, this.y, this.y, this.w);
	}

	@Override
	public Vec4f zyzx() {
		return new Vec4f(this.z, this.y, this.z, this.x);
	}

	@Override
	public Vec4f zyzy() {
		return new Vec4f(this.z, this.y, this.z, this.y);
	}

	@Override
	public Vec4f zyzz() {
		return new Vec4f(this.z, this.y, this.z, this.z);
	}

	@Override
	public Vec4f zyzw() {
		return new Vec4f(this.z, this.y, this.z, this.w);
	}

	@Override
	public Vec4f zywx() {
		return new Vec4f(this.z, this.y, this.w, this.x);
	}

	@Override
	public Vec4f zywy() {
		return new Vec4f(this.z, this.y, this.w, this.y);
	}

	@Override
	public Vec4f zywz() {
		return new Vec4f(this.z, this.y, this.w, this.z);
	}

	@Override
	public Vec4f zyww() {
		return new Vec4f(this.z, this.y, this.w, this.w);
	}

	@Override
	public Vec4f zzxx() {
		return new Vec4f(this.z, this.z, this.x, this.x);
	}

	@Override
	public Vec4f zzxy() {
		return new Vec4f(this.z, this.z, this.x, this.y);
	}

	@Override
	public Vec4f zzxz() {
		return new Vec4f(this.z, this.z, this.x, this.z);
	}

	@Override
	public Vec4f zzxw() {
		return new Vec4f(this.z, this.z, this.x, this.w);
	}

	@Override
	public Vec4f zzyx() {
		return new Vec4f(this.z, this.z, this.y, this.x);
	}

	@Override
	public Vec4f zzyy() {
		return new Vec4f(this.z, this.z, this.y, this.y);
	}

	@Override
	public Vec4f zzyz() {
		return new Vec4f(this.z, this.z, this.y, this.z);
	}

	@Override
	public Vec4f zzyw() {
		return new Vec4f(this.z, this.z, this.y, this.w);
	}

	@Override
	public Vec4f zzzx() {
		return new Vec4f(this.z, this.z, this.z, this.x);
	}

	@Override
	public Vec4f zzzy() {
		return new Vec4f(this.z, this.z, this.z, this.y);
	}

	@Override
	public Vec4f zzzz() {
		return new Vec4f(this.z, this.z, this.z, this.z);
	}

	@Override
	public Vec4f zzzw() {
		return new Vec4f(this.z, this.z, this.z, this.w);
	}

	@Override
	public Vec4f zzwx() {
		return new Vec4f(this.z, this.z, this.w, this.x);
	}

	@Override
	public Vec4f zzwy() {
		return new Vec4f(this.z, this.z, this.w, this.y);
	}

	@Override
	public Vec4f zzwz() {
		return new Vec4f(this.z, this.z, this.w, this.z);
	}

	@Override
	public Vec4f zzww() {
		return new Vec4f(this.z, this.z, this.w, this.w);
	}

	@Override
	public Vec4f zwxx() {
		return new Vec4f(this.z, this.w, this.x, this.x);
	}

	@Override
	public Vec4f zwxy() {
		return new Vec4f(this.z, this.w, this.x, this.y);
	}

	@Override
	public Vec4f zwxz() {
		return new Vec4f(this.z, this.w, this.x, this.z);
	}

	@Override
	public Vec4f zwxw() {
		return new Vec4f(this.z, this.w, this.x, this.w);
	}

	@Override
	public Vec4f zwyx() {
		return new Vec4f(this.z, this.w, this.y, this.x);
	}

	@Override
	public Vec4f zwyy() {
		return new Vec4f(this.z, this.w, this.y, this.y);
	}

	@Override
	public Vec4f zwyz() {
		return new Vec4f(this.z, this.w, this.y, this.z);
	}

	@Override
	public Vec4f zwyw() {
		return new Vec4f(this.z, this.w, this.y, this.w);
	}

	@Override
	public Vec4f zwzx() {
		return new Vec4f(this.z, this.w, this.z, this.x);
	}

	@Override
	public Vec4f zwzy() {
		return new Vec4f(this.z, this.w, this.z, this.y);
	}

	@Override
	public Vec4f zwzz() {
		return new Vec4f(this.z, this.w, this.z, this.z);
	}

	@Override
	public Vec4f zwzw() {
		return new Vec4f(this.z, this.w, this.z, this.w);
	}

	@Override
	public Vec4f zwwx() {
		return new Vec4f(this.z, this.w, this.w, this.x);
	}

	@Override
	public Vec4f zwwy() {
		return new Vec4f(this.z, this.w, this.w, this.y);
	}

	@Override
	public Vec4f zwwz() {
		return new Vec4f(this.z, this.w, this.w, this.z);
	}

	@Override
	public Vec4f zwww() {
		return new Vec4f(this.z, this.w, this.w, this.w);
	}

	@Override
	public Vec4f wxxx() {
		return new Vec4f(this.w, this.x, this.x, this.x);
	}

	@Override
	public Vec4f wxxy() {
		return new Vec4f(this.w, this.x, this.x, this.y);
	}

	@Override
	public Vec4f wxxz() {
		return new Vec4f(this.w, this.x, this.x, this.z);
	}

	@Override
	public Vec4f wxxw() {
		return new Vec4f(this.w, this.x, this.x, this.w);
	}

	@Override
	public Vec4f wxyx() {
		return new Vec4f(this.w, this.x, this.y, this.x);
	}

	@Override
	public Vec4f wxyy() {
		return new Vec4f(this.w, this.x, this.y, this.y);
	}

	@Override
	public Vec4f wxyz() {
		return new Vec4f(this.w, this.x, this.y, this.z);
	}

	@Override
	public Vec4f wxyw() {
		return new Vec4f(this.w, this.x, this.y, this.w);
	}

	@Override
	public Vec4f wxzx() {
		return new Vec4f(this.w, this.x, this.z, this.x);
	}

	@Override
	public Vec4f wxzy() {
		return new Vec4f(this.w, this.x, this.z, this.y);
	}

	@Override
	public Vec4f wxzz() {
		return new Vec4f(this.w, this.x, this.z, this.z);
	}

	@Override
	public Vec4f wxzw() {
		return new Vec4f(this.w, this.x, this.z, this.w);
	}

	@Override
	public Vec4f wxwx() {
		return new Vec4f(this.w, this.x, this.w, this.x);
	}

	@Override
	public Vec4f wxwy() {
		return new Vec4f(this.w, this.x, this.w, this.y);
	}

	@Override
	public Vec4f wxwz() {
		return new Vec4f(this.w, this.x, this.w, this.z);
	}

	@Override
	public Vec4f wxww() {
		return new Vec4f(this.w, this.x, this.w, this.w);
	}

	@Override
	public Vec4f wyxx() {
		return new Vec4f(this.w, this.y, this.x, this.x);
	}

	@Override
	public Vec4f wyxy() {
		return new Vec4f(this.w, this.y, this.x, this.y);
	}

	@Override
	public Vec4f wyxz() {
		return new Vec4f(this.w, this.y, this.x, this.z);
	}

	@Override
	public Vec4f wyxw() {
		return new Vec4f(this.w, this.y, this.x, this.w);
	}

	@Override
	public Vec4f wyyx() {
		return new Vec4f(this.w, this.y, this.y, this.x);
	}

	@Override
	public Vec4f wyyy() {
		return new Vec4f(this.w, this.y, this.y, this.y);
	}

	@Override
	public Vec4f wyyz() {
		return new Vec4f(this.w, this.y, this.y, this.z);
	}

	@Override
	public Vec4f wyyw() {
		return new Vec4f(this.w, this.y, this.y, this.w);
	}

	@Override
	public Vec4f wyzx() {
		return new Vec4f(this.w, this.y, this.z, this.x);
	}

	@Override
	public Vec4f wyzy() {
		return new Vec4f(this.w, this.y, this.z, this.y);
	}

	@Override
	public Vec4f wyzz() {
		return new Vec4f(this.w, this.y, this.z, this.z);
	}

	@Override
	public Vec4f wyzw() {
		return new Vec4f(this.w, this.y, this.z, this.w);
	}

	@Override
	public Vec4f wywx() {
		return new Vec4f(this.w, this.y, this.w, this.x);
	}

	@Override
	public Vec4f wywy() {
		return new Vec4f(this.w, this.y, this.w, this.y);
	}

	@Override
	public Vec4f wywz() {
		return new Vec4f(this.w, this.y, this.w, this.z);
	}

	@Override
	public Vec4f wyww() {
		return new Vec4f(this.w, this.y, this.w, this.w);
	}

	@Override
	public Vec4f wzxx() {
		return new Vec4f(this.w, this.z, this.x, this.x);
	}

	@Override
	public Vec4f wzxy() {
		return new Vec4f(this.w, this.z, this.x, this.y);
	}

	@Override
	public Vec4f wzxz() {
		return new Vec4f(this.w, this.z, this.x, this.z);
	}

	@Override
	public Vec4f wzxw() {
		return new Vec4f(this.w, this.z, this.x, this.w);
	}

	@Override
	public Vec4f wzyx() {
		return new Vec4f(this.w, this.z, this.y, this.x);
	}

	@Override
	public Vec4f wzyy() {
		return new Vec4f(this.w, this.z, this.y, this.y);
	}

	@Override
	public Vec4f wzyz() {
		return new Vec4f(this.w, this.z, this.y, this.z);
	}

	@Override
	public Vec4f wzyw() {
		return new Vec4f(this.w, this.z, this.y, this.w);
	}

	@Override
	public Vec4f wzzx() {
		return new Vec4f(this.w, this.z, this.z, this.x);
	}

	@Override
	public Vec4f wzzy() {
		return new Vec4f(this.w, this.z, this.z, this.y);
	}

	@Override
	public Vec4f wzzz() {
		return new Vec4f(this.w, this.z, this.z, this.z);
	}

	@Override
	public Vec4f wzzw() {
		return new Vec4f(this.w, this.z, this.z, this.w);
	}

	@Override
	public Vec4f wzwx() {
		return new Vec4f(this.w, this.z, this.w, this.x);
	}

	@Override
	public Vec4f wzwy() {
		return new Vec4f(this.w, this.z, this.w, this.y);
	}

	@Override
	public Vec4f wzwz() {
		return new Vec4f(this.w, this.z, this.w, this.z);
	}

	@Override
	public Vec4f wzww() {
		return new Vec4f(this.w, this.z, this.w, this.w);
	}

	@Override
	public Vec4f wwxx() {
		return new Vec4f(this.w, this.w, this.x, this.x);
	}

	@Override
	public Vec4f wwxy() {
		return new Vec4f(this.w, this.w, this.x, this.y);
	}

	@Override
	public Vec4f wwxz() {
		return new Vec4f(this.w, this.w, this.x, this.z);
	}

	@Override
	public Vec4f wwxw() {
		return new Vec4f(this.w, this.w, this.x, this.w);
	}

	@Override
	public Vec4f wwyx() {
		return new Vec4f(this.w, this.w, this.y, this.x);
	}

	@Override
	public Vec4f wwyy() {
		return new Vec4f(this.w, this.w, this.y, this.y);
	}

	@Override
	public Vec4f wwyz() {
		return new Vec4f(this.w, this.w, this.y, this.z);
	}

	@Override
	public Vec4f wwyw() {
		return new Vec4f(this.w, this.w, this.y, this.w);
	}

	@Override
	public Vec4f wwzx() {
		return new Vec4f(this.w, this.w, this.z, this.x);
	}

	@Override
	public Vec4f wwzy() {
		return new Vec4f(this.w, this.w, this.z, this.y);
	}

	@Override
	public Vec4f wwzz() {
		return new Vec4f(this.w, this.w, this.z, this.z);
	}

	@Override
	public Vec4f wwzw() {
		return new Vec4f(this.w, this.w, this.z, this.w);
	}

	@Override
	public Vec4f wwwx() {
		return new Vec4f(this.w, this.w, this.w, this.x);
	}

	@Override
	public Vec4f wwwy() {
		return new Vec4f(this.w, this.w, this.w, this.y);
	}

	@Override
	public Vec4f wwwz() {
		return new Vec4f(this.w, this.w, this.w, this.z);
	}

	@Override
	public Vec4f wwww() {
		return new Vec4f(this.w, this.w, this.w, this.w);
	}
}
