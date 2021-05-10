-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-assumenosideeffects class android.util.Log {
    public static int d(...);
}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontwarn android.support.v4.**
-dontwarn **CompatHoneycomb
-keep class android.support.v4.** { *; }
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
 }
-keepattributes InnerClasses, EnclosingMethod
-keep class com.gelitenight.waveview.library.**{ *; }
-keep class com.startapp.** {
      *;
}
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**
-keepattributes InnerClasses, EnclosingMethod
-keepclassmembers class com.supersonicads.sdk.controller.SupersonicWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.startapp.** {
      *;
}
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile
-dontwarn android.webkit.JavascriptInterface
 # Admob
 -keep class com.google.android.gms.ads.** { *; }
 # Google
 -keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
 -keep class com.google.android.gms.ads.identifier.** { *; }
 -dontwarn com.google.android.gms.**
 # Legacy
 -keep class org.apache.http.** { *; }
 -dontwarn org.apache.http.**
 -dontwarn android.net.http.**
 # Google Play Services library
 -keep class * extends java.util.ListResourceBundle {
     protected Object[][] getContents();
 }
 -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
 }
 -keepnames class * implements android.os.Parcelable
 -keepclassmembers class * implements android.os.Parcelable {
   public static final *** CREATOR;
 }
 -keep @interface android.support.annotation.Keep
 -keep @android.support.annotation.Keep class *
 -keepclasseswithmembers class * {
   @android.support.annotation.Keep <fields>;
 }
 -keepclasseswithmembers class * {
   @android.support.annotation.Keep <methods>;
 }
 -keep @interface com.google.android.gms.common.annotation.KeepName
 -keepnames @com.google.android.gms.common.annotation.KeepName class *
 -keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
 }
 -keep @interface com.google.android.gms.common.util.DynamiteApi
 -keep public @com.google.android.gms.common.util.DynamiteApi class * {
   public <fields>;
   public <methods>;
 }
 -keep class com.google.android.gms.common.GooglePlayServicesNotAvailableException {*;}
 -keep class com.google.android.gms.common.GooglePlayServicesRepairableException {*;}

 # Google Play Services library 9.0.0 only
 -dontwarn android.security.NetworkSecurityPolicy
 -keep public @com.google.android.gms.common.util.DynamiteApi class * { *; }

 # support-v4
 -keep class android.support.v4.app.Fragment { *; }
 -keep class android.support.v4.app.FragmentActivity { *; }
 -keep class android.support.v4.app.FragmentManager { *; }
 -keep class android.support.v4.app.FragmentTransaction { *; }
 -keep class android.support.v4.content.LocalBroadcastManager { *; }
 -keep class android.support.v4.util.LruCache { *; }
 -keep class android.support.v4.view.PagerAdapter { *; }
 -keep class android.support.v4.view.ViewPager { *; }
 -keep class android.support.v4.content.ContextCompat { *; }

 # support-v7-recyclerview
 -keep class android.support.v7.widget.RecyclerView { *; }
 -keep class android.support.v7.widget.LinearLayoutManager { *; }

 # Platform calls Class.forName on types which do not exist on Android to determine platform.
 -dontnote retrofit2.Platform
 # Platform used when running on Java 8 VMs. Will not be used at runtime.
 -dontwarn retrofit2.Platform$Java8
 # Retain generic type information for use by reflection by converters and adapters.
 -keepattributes Signature
 # Retain declared checked exceptions for use by a Proxy instance.
 -keepattributes Exceptions

 -dontwarn java.lang.invoke.*
 -dontwarn **$$Lambda$*