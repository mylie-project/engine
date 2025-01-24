package mylie.math;

@SuppressWarnings({"unused"})record Vec3f(float x,float y,float z)implements Vec3<Float>{private static final Vec3f ZERO=new Vec3f(0,0,0);private static final Vec3f ONE=new Vec3f(1,1,1);private static final Vec3f UNIT_X=new Vec3f(1,0,0);private static final Vec3f UNIT_Y=new Vec3f(0,1,0);private static final Vec3f UNIT_Z=new Vec3f(0,0,1);private static final Vec3f NEGATIVE_ONE=new Vec3f(-1,-1,-1);private static final Vec3f NEGATIVE_UNIT_X=new Vec3f(-1,0,0);private static final Vec3f NEGATIVE_UNIT_Y=new Vec3f(0,-1,0);private static final Vec3f NEGATIVE_UNIT_Z=new Vec3f(0,0,-1);@Override public Vec3<Float>add(Vec3<Float>other){Vec3f b=cast(other);return Vec3.of(x+b.x,y+b.y,z+b.z);}

@Override public Vec3<Float>sub(Vec3<Float>other){Vec3f b=cast(other);return Vec3.of(x-b.x,y-b.y,z-b.z);}

@Override public Vec3<Float>mul(Vec3<Float>other){Vec3f b=cast(other);return Vec3.of(x*b.x,y*b.y,z*b.z);}

@Override public Vec3<Float>div(Vec3<Float>other){Vec3f b=cast(other);if(b.x==0||b.y==0||b.z==0){throw new ArithmeticException("Division by Zero");}return Vec3.of(x/b.x,y/b.y,z/b.z);}

@Override public Vec3<Float>mulAdd(Vec3<Float>other,Vec3<Float>factor){Vec3f b=cast(other);Vec3f c=cast(factor);return Vec3.of(x+b.x*c.x,y+b.y*c.y,z+b.z*c.z);}

@Override public Vec3<Float>negate(){return Vec3.of(-x,-y,-z);}

@Override public Vec3<Float>normalize(){float length=(float)Math.sqrt(x*x+y*y+z*z);if(length==0){throw new ArithmeticException("Cannot normalize a zero-length vector.");}return Vec3.of(x/length,y/length,z/length);}

@Override public Vec3<Float>max(Vec3<Float>other){Vec3f b=cast(other);return Vec3.of(Math.max(x,b.x),Math.max(y,b.y),Math.max(z,b.z));}

@Override public Vec3<Float>min(Vec3<Float>other){Vec3f b=cast(other);return Vec3.of(Math.min(x,b.x),Math.min(y,b.y),Math.min(z,b.z));}

@Override public Float getX(){return x;}

@Override public Float getY(){return y;}

@Override public Float getZ(){return z;}

@Override public Float dot(Vec3<Float>other){Vec3f b=cast(other);return x*b.x+y*b.y+z*b.z;}

@Override public Vec3<Float>cross(Vec3<Float>other){Vec3f b=cast(other);return Vec3.of(y*b.z-z*b.y,z*b.x-x*b.z,x*b.y-y*b.x);}

@Override public Vec2<Float>swizzle(Component x,Component y){return Vec2.of(componentValue(x),componentValue(y));}

@Override public Vec3<Float>swizzle(Component x,Component y,Component z){return Vec3.of(componentValue(x),componentValue(y),componentValue(z));}

@Override public Vec3<Float>zero(){return ZERO;}

@Override public Vec3<Float>one(){return ONE;}

@Override public Vec3<Float>negativeOne(){return NEGATIVE_ONE;}

@Override public Vec3<Float>unitX(){return UNIT_X;}

@Override public Vec3<Float>unitY(){return UNIT_Y;}

@Override public Vec3<Float>unitZ(){return UNIT_Z;}

@Override public Vec3<Float>negativeUnitX(){return NEGATIVE_UNIT_X;}

@Override public Vec3<Float>negativeUnitY(){return NEGATIVE_UNIT_Y;}

@Override public Vec3<Float>negativeUnitZ(){return NEGATIVE_UNIT_Z;}

private float componentValue(Component c){if(c==X)return x;if(c==Y)return y;if(c==Z)return z;throw new IllegalArgumentException("Component must be X, Y or Z");}

Vec3f cast(Vec3<Float>other){return(Vec3f)other;}}
