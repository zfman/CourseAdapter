#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_zhuangfei_adapterlib_cppinterface_CppInterface_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from0 C++";
    return env->NewStringUTF(hello.c_str());
}
