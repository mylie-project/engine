package mylie.math;

@SuppressWarnings("unused")
public interface Vec4<V extends Vec<V, N>, V2 extends Vec2<V2, N>, V3 extends Vec3<V3, V2, V3, N>, N extends Number>
		extends
			Vec<V, N> {

	V2 xx();
	V2 xy();
	V2 xz();
	V2 xw();
	V2 yx();
	V2 yy();
	V2 yz();
	V2 yw();
	V2 zx();
	V2 zy();
	V2 zz();
	V2 zw();

	V3 xyz();

	V3 xyw();

	V3 xzw();

	V3 xwx();

	V3 xwy();

	V3 xwz();

	V3 yzw();

	V3 yxz();

	V3 yzx();

	V3 ywx();

	V3 ywz();

	V3 yxy();

	V3 zyw();

	V3 zxy();

	V3 zyx();

	V3 zxz();

	V3 zxw();

	V3 wxy();

	V3 wxz();

	V3 wyz();

	V3 wzx();

	V3 wzy();

	V3 wyw();

	V3 wxw();

	V xxxx();

	V xxxy();

	V xxxz();

	V xxxw();

	V xxyx();

	V xxyy();

	V xxyz();

	V xxyw();

	V xxzx();

	V xxzy();

	V xxzz();

	V xxzw();

	V xxwx();

	V xxwy();

	V xxwz();

	V xxww();

	V xyxx();

	V xyxy();

	V xyxz();

	V xyxw();

	V xyyx();

	V xyyy();

	V xyyz();

	V xyyw();

	V xyzx();

	V xyzy();

	V xyzz();

	V xyzw();

	V xywx();

	V xywy();

	V xywz();

	V xyww();

	V xzxx();

	V xzxy();

	V xzxz();

	V xzxw();

	V xzyx();

	V xzyy();

	V xzyz();

	V xzyw();

	V xzzx();

	V xzzy();

	V xzzz();

	V xzzw();

	V xzwx();

	V xzwy();

	V xzwz();

	V xzww();

	V xwxx();

	V xwxy();

	V xwxz();

	V xwxw();

	V xwyx();

	V xwyy();

	V xwyz();

	V xwyw();

	V xwzx();

	V xwzy();

	V xwzz();

	V xwzw();

	V xwwx();

	V xwwy();

	V xwwz();

	V xwww();

	V yxxx();

	V yxxy();

	V yxxz();

	V yxxw();

	V yxyx();

	V yxyy();

	V yxyz();

	V yxyw();

	V yxzx();

	V yxzy();

	V yxzz();

	V yxzw();

	V yxwx();

	V yxwy();

	V yxwz();

	V yxww();

	V yyxx();

	V yyxy();

	V yyxz();

	V yyxw();

	V yyyx();

	V yyyy();

	V yyyz();

	V yyyw();

	V yyzx();

	V yyzy();

	V yyzz();

	V yyzw();

	V yywx();

	V yywy();

	V yywz();

	V yyww();

	V yzxx();

	V yzxy();

	V yzxz();

	V yzxw();

	V yzyx();

	V yzyy();

	V yzyz();

	V yzyw();

	V yzzx();

	V yzzy();

	V yzzz();

	V yzzw();

	V yzwx();

	V yzwy();

	V yzwz();

	V yzww();

	V ywxx();

	V ywxy();

	V ywxz();

	V ywxw();

	V ywyx();

	V ywyy();

	V ywyz();

	V ywyw();

	V ywzx();

	V ywzy();

	V ywzz();

	V ywzw();

	V ywwx();

	V ywwy();

	V ywwz();

	V ywww();

	V zxxx();

	V zxxy();

	V zxxz();

	V zxxw();

	V zxyx();

	V zxyy();

	V zxyz();

	V zxyw();

	V zxzx();

	V zxzy();

	V zxzz();

	V zxzw();

	V zxwx();

	V zxwy();

	V zxwz();

	V zxww();

	V zyxx();

	V zyxy();

	V zyxz();

	V zyxw();

	V zyyx();

	V zyyy();

	V zyyz();

	V zyyw();

	V zyzx();

	V zyzy();

	V zyzz();

	V zyzw();

	V zywx();

	V zywy();

	V zywz();

	V zyww();

	V zzxx();

	V zzxy();

	V zzxz();

	V zzxw();

	V zzyx();

	V zzyy();

	V zzyz();

	V zzyw();

	V zzzx();

	V zzzy();

	V zzzz();

	V zzzw();

	V zzwx();

	V zzwy();

	V zzwz();

	V zzww();

	V zwxx();

	V zwxy();

	V zwxz();

	V zwxw();

	V zwyx();

	V zwyy();

	V zwyz();

	V zwyw();

	V zwzx();

	V zwzy();

	V zwzz();

	V zwzw();

	V zwwx();

	V zwwy();

	V zwwz();

	V zwww();

	V wxxx();

	V wxxy();

	V wxxz();

	V wxxw();

	V wxyx();

	V wxyy();

	V wxyz();

	V wxyw();

	V wxzx();

	V wxzy();

	V wxzz();

	V wxzw();

	V wxwx();

	V wxwy();

	V wxwz();

	V wxww();

	V wyxx();

	V wyxy();

	V wyxz();

	V wyxw();

	V wyyx();

	V wyyy();

	V wyyz();

	V wyyw();

	V wyzx();

	V wyzy();

	V wyzz();

	V wyzw();

	V wywx();

	V wywy();

	V wywz();

	V wyww();

	V wzxx();

	V wzxy();

	V wzxz();

	V wzxw();

	V wzyx();

	V wzyy();

	V wzyz();

	V wzyw();

	V wzzx();

	V wzzy();

	V wzzz();

	V wzzw();

	V wzwx();

	V wzwy();

	V wzwz();

	V wzww();

	V wwxx();

	V wwxy();

	V wwxz();

	V wwxw();

	V wwyx();

	V wwyy();

	V wwyz();

	V wwyw();

	V wwzx();

	V wwzy();

	V wwzz();

	V wwzw();

	V wwwx();

	V wwwy();

	V wwwz();

	V wwww();

}
