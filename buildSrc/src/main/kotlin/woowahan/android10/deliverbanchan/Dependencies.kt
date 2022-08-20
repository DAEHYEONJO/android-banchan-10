package woowahan.android10.deliverbanchan

object Versions {
    const val KOTLIN_VERSION = "1.6.10"
    const val KOTLINX_COROUTINES = "1.6.1"
    const val BUILD_GRADLE = "4.2.1"
    const val COMPILE_SDK_VERSION = 32
    const val BUILD_TOOLS_VERSION = "30.0.3"
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION = 32

    const val CORE_KTX = "1.7.0"
    const val APP_COMPAT = "1.4.2"
    const val ACTIVITY_KTX = "1.5.0"
    const val FRAGMENT_KTX = "1.5.0"
    const val LIFECYCLE_KTX = "2.5.0"
    const val ROOM = "2.4.3"
    const val PAGING_VERSION = "3.2.0-alpha02"

    const val HILT = "2.38.1"
    const val MATERIAL = "1.6.1"

    const val RETROFIT = "2.9.0"
    const val OKHTTP = "4.10.0"

    const val JUNIT = "4.13.2"
    const val ANDROID_JUNIT = "1.1.3"
    const val ESPRESSO_CORE = "3.4.0"

    const val CONSTRAINT_LAYOUT_VERSION = "2.1.4"

    const val GLIDE = "4.13.2"

    const val KOTLINX_SERIALIZATION = "1.2.1"
    const val CONVERTER_KOTLINX_SERIALIZATION = "0.8.0"

    const val WINDOW = "1.0.0"

    const val RECYCLERVIEW = "1.2.0-rc01"

    const val WORK_MANAGER = "2.7.1"
    const val HILT_WORK = "1.0.0"
    const val HILT_COMPILER = "1.0.0"
}

object Kotlin {
    const val KOTLIN_STDLIB      = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN_VERSION}"
    const val COROUTINES_CORE    = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLINX_COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLINX_COROUTINES}"
}

object AndroidX {
    const val CORE_KTX                = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val APP_COMPAT              = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val CONSTRAINT_LAYOUT       = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT_VERSION}"

    const val ACTIVITY_KTX            = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val FRAGMENT_KTX            = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"
    const val RUNTIME_KTX             = "androidx.lifecycle:lifecycle-runtime-ktx"

    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_KTX}"
    const val LIFECYCLE_LIVEDATA_KTX  = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE_KTX}"

    const val ROOM_RUNTIME            = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX                = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMPILER           = "androidx.room:room-compiler:${Versions.ROOM}"

    const val WINDOW                  = "androidx.window:window:${Versions.WINDOW}"
    const val RECYCLERVIEW            = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"

    const val WORK_MANAGER            = "androidx.work:work-runtime-ktx:${Versions.WORK_MANAGER}"
    const val HILT_WORK               = "androidx.hilt:hilt-work:${Versions.HILT_WORK}"
    const val HILT_COMPILER           = "androidx.hilt:hilt-compiler:${Versions.HILT_COMPILER}"

    const val PAGING_RUNTIME          = "androidx.paging:paging-runtime:${Versions.PAGING_VERSION}"
    const val PAGING_RUNTIME_KTX      = "androidx.paging:paging-runtime-ktx:${Versions.PAGING_VERSION}"
    const val PAGING_COMMON           = "androidx.paging:paging-common:${Versions.PAGING_VERSION}"
}

object Google {
    const val HILT_ANDROID          = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"

    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
}

object Libraries {
    const val RETROFIT                   = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER_GSON    = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val OKHTTP                     = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val KOTLINX_SERIALIZATION      = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}"
    const val RETROFIT_CONVERTER_KOTLINX_SERIALIZATION = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.CONVERTER_KOTLINX_SERIALIZATION}"

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
}

object UnitTest {
    const val JUNIT         = "junit:junit:${Versions.JUNIT}"
}

object AndroidTest {
    const val ANDROID_JUNIT = "androidx.test.ext:junit:${Versions.ANDROID_JUNIT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}