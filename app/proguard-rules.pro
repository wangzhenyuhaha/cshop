# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#BasePop配置
-dontwarn razerdp.basepopup.**
-keep class razerdp.basepopup.**{*;}

#AgentWeb混淆
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**
-keepclassmembers class com.just.agentweb.sample.common.AndroidInterface{ *; }

###相册配置混淆 start ###
#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }
#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*
###相册配置混淆 end ###

# 腾讯地图
#-keep class com.tencent.tencentmap.**{*;}
#-keep class com.tencent.map.**{*;}
#-keep class com.tencent.beacontmap.**{*;}
-keep class navsns.**{*;}
-dontwarn com.qq.**
-dontwarn com.tencent.**

# 腾讯地图定位
-keepclassmembers class ** {
    public void on*Event(...);
}

-keep class c.t.**{*;}
#-keep class com.tencent.map.geolocation.**{*;}

-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**

# CheckVersionLib
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# 项目混淆
-keep class * implements java.io.Serializable { *; }
-keep class com.fishagle.mkt.**.bean.** { *; }


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontoptimize

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.animation.Animation
-keep public class android.view.animation.AlphaAnimation
-keep public class android.webkit.WebView {*;}
-keep public class android.webkit.WebViewClient {*;}


-keepclassmembers class * extends android.webkit.WebViewClient {
  *;
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
  *;
}

-keepclassmembers class * extends android.app.Fragment {
public void *(android.view.View);
}

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod


-keepattributes Exceptions,InnerClasses,Signature
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepattributes EnclosingMethod

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public <fields>;
    public <methods>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * implements java.io.Serializable{
    public protected private *;
}


-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}


-keep class com.james.common.**{*;}
#-keep class com.lingmiao.shop.**{*;}
-keep class com.lingmiao.shop.business.common.bean.**{*;}
-keep class com.lingmiao.shop.business.goods.api.**{*;}
-keep class com.lingmiao.shop.business.login.api.**{*;}
-keep class com.lingmiao.shop.business.login.bean.**{*;}
-keep class com.lingmiao.shop.business.main.api.**{*;}
-keep class com.lingmiao.shop.business.main.bean.**{*;}
-keep class com.lingmiao.shop.business.me.api.**{*;}
-keep class com.lingmiao.shop.business.me.bean.**{*;}
-keep class com.lingmiao.shop.business.order.bean.**{*;}
-keep class com.lingmiao.shop.business.order.api.**{*;}
-keep class com.lingmiao.shop.business.wallet.api.**{*;}
-keep class com.lingmiao.shop.business.wallet.bean.**{*;}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface
    <methods>;
}

# retrofit
-keep public class com.squareup.okhttp.** {
    <fields>;
    <methods>;
}


# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
# okio
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

#eventbus
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }

# jpush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

# glide
-dontwarn com.bumptech.glide.**
-keep public class com.bumptech.glide.**{*; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
        **[] $VALUES;
        public *;
}

#androidx
-keep class com.google.android.material.** { *; }

-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*

# tencent map
#-keep class com.tencent.tencentmap.**{*;}
#-keep class com.tencent.map.**{*;}
#-keep class com.tencent.beacontmap.**{*;}
#-keep class com.tencent.lbssearch.object.**{*;}
-keep class com.qq.**{*;}
-keep class com.tencent.**{*;}
-keep class navsns.**{*;}
-dontwarn com.qq.**
-dontwarn com.tencent.**

# tencent location
-keepclassmembers class ** {
    public void on*Event(...);
}

-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}

-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**

# versionchecklib
 -keep class com.allenliu.versionchecklib.**{*;}

 #map
 #3D 地图 V5.0.0之后：
 -keep   class com.amap.api.maps.**{*;}
 -keep   class com.autonavi.**{*;}
 -keep   class com.amap.api.trace.**{*;}

 #定位
 -keep class com.amap.api.location.**{*;}
 -keep class com.amap.api.fence.**{*;}
 -keep class com.loc.**{*;}
 -keep class com.autonavi.aps.amapapi.model.**{*;}

 #搜索
 -keep   class com.amap.api.services.**{*;}

 #导航
 -keep class com.amap.api.navi.**{*;}
 -keep class com.autonavi.**{*;}

 #支付
 -keep class com.tencent.mm.opensdk.** {
     *;
 }

 -keep class com.tencent.wxop.** {
     *;
 }

 -keep class com.tencent.mm.sdk.** {
     *;
 }