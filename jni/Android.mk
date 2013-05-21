LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := fibo
LOCAL_SRC_FILES := fibo.cpp
LOCAL_LDLIBS:=-llog 
include $(BUILD_SHARED_LIBRARY)
