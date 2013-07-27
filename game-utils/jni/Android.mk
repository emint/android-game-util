LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libnativeglestools
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS    := -Werror -O2
LOCAL_SRC_FILES := NativeGLESTools.cpp
LOCAL_LDLIBS    := -lGLESv2

include $(BUILD_SHARED_LIBRARY)