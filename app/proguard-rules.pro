-dontwarn **
-dontwarn android.support.**
-dontwarn androidx.**

-verbose
-allowaccessmodification
-repackageclasses

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class androidx.compose.ui.platform.ComposeView {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepattributes *Annotation*
-keepattributes Signature,InnerClasses,EnclosingMethod
-keepattributes SourceFile,
                LineNumberTable,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                AnnotationDefault

-renamesourcefileattribute SourceFile
-dontwarn com.google.errorprone.annotations.*
-keep,allowobfuscation,allowshrinking class retrofit2.Call
-keep,allowshrinking class * extends androidx.compose.ui.node.ModifierNodeElement {}