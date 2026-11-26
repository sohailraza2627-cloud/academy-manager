-keepattributes *Annotation*
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public abstract ** *();
}
-keep class com.academy.manager.data.db.** { *; }
-dontwarn java.lang.invoke.StringConcatFactory
