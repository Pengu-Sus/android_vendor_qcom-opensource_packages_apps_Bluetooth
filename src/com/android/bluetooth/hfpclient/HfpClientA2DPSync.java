/*
* Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
* SPDX-License-Identifier: BSD-3-Clause-Clear
*/

package com.android.bluetooth.hfpclient;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadsetClient;
import android.bluetooth.BluetoothHeadsetClientCall;
import android.content.Context;
import android.net.Uri;
import android.telecom.Connection;

import java.util.List;
import java.util.ArrayList;
import java.util.*;

public class HfpClientA2DPSync{
    private final HeadsetClientService hsClientService;
    private static final String TAG = "HfpClientA2DPSync";
    public boolean isA2dpStreamAllowed(){
        List <BluetoothHeadsetClientCall> currentCalls = getCurrentCalls();
        if((currentCalls.isEmpty()) == false)
            return false;
        List <BluetoothDevice> connectedDevices = hsClientService.getConnectedDevices();
        if(connectedDevices.isEmpty() == false) {
            for (BluetoothDevice currentDevice : connectedDevices) {
               if(hsClientService.getAudioState(currentDevice)
                   == BluetoothHeadsetClient.STATE_AUDIO_CONNECTED
                   || hsClientService.getAudioState(currentDevice)
                   == BluetoothHeadsetClient.STATE_AUDIO_CONNECTING) {
                  return false;
               }
            }
        }
        return true;
    }
    public boolean isScoActive(BluetoothDevice device) {
        int audioState = hsClientService.getAudioState(device);
        if(audioState == BluetoothHeadsetClient.STATE_AUDIO_CONNECTED
            || audioState == BluetoothHeadsetClient.STATE_AUDIO_CONNECTING) {
            return true;
        }
        return false;
    }

    public HfpClientA2DPSync(HeadsetClientService context) {
        hsClientService = context;
    }

    public List <BluetoothHeadsetClientCall> getCurrentCalls() {
        List <BluetoothHeadsetClientCall> callList =  new ArrayList<BluetoothHeadsetClientCall>();;
        List <BluetoothDevice> connectedDevices = hsClientService.getConnectedDevices();
        if(!(connectedDevices.isEmpty())) {
            for (BluetoothDevice mDevice : connectedDevices) {
                callList.addAll(hsClientService.getCurrentCalls(mDevice));
            }
            return callList;
        }
        return  Collections.emptyList();
    }

    public BluetoothDevice getCallingDevice() {
        BluetoothDevice callingDevice = null;
        List <BluetoothDevice> connectedDevices = hsClientService.getConnectedDevices();
        if(!(connectedDevices.isEmpty())) {
            for (BluetoothDevice mDevice : connectedDevices) {
                if(!hsClientService.getCurrentCalls(mDevice).isEmpty()) {
                    callingDevice = mDevice;
                }
            }
        }
        return callingDevice;
    }
}