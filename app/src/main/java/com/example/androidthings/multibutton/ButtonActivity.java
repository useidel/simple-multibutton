/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.multibutton;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;

/**
 * Example of using Button driver for toggling a LED.
 *
 * This activity initialize an InputDriver to emit key events when the button GPIO pin state change
 * and flip the state of the LED GPIO pin.
 *
 * You need to connect an LED and a push button switch to pins specified in {@link BoardDefaults}
 * according to the schematic provided in the sample README.
 */
public class ButtonActivity extends Activity {
    private static final String TAG = ButtonActivity.class.getSimpleName();

    private Gpio mLedGpio1;
    private Gpio mLedGpio2;
    private Gpio mLedGpio3;
    private ButtonInputDriver mButtonInputDriver1;
    private ButtonInputDriver mButtonInputDriver2;
    private ButtonInputDriver mButtonInputDriver3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting ButtonActivity");

        PeripheralManagerService pioService = new PeripheralManagerService();
        try {
            Log.i(TAG, "Configuring GPIO pins");
            mLedGpio1 = pioService.openGpio(BoardDefaults.getGPIOForLED1());
            mLedGpio1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio2 = pioService.openGpio(BoardDefaults.getGPIOForLED2());
            mLedGpio2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio3 = pioService.openGpio(BoardDefaults.getGPIOForLED3());
            mLedGpio3.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            Log.i(TAG, "Registering button driver");

            // Initialize and register the InputDriver that will emit SPACE key events
            // on GPIO state changes.
            mButtonInputDriver1 = new ButtonInputDriver(
                    BoardDefaults.getGPIOForButton1(),
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_A);
            mButtonInputDriver1.register();
            mButtonInputDriver2 = new ButtonInputDriver(
                    BoardDefaults.getGPIOForButton2(),
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_B);
            mButtonInputDriver2.register();
            mButtonInputDriver3 = new ButtonInputDriver(
                    BoardDefaults.getGPIOForButton3(),
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_C);
            mButtonInputDriver3.register();

        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pins", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_A) {
            // Turn on the LED
            setLedValue1(true);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_B) {
            // Turn on the LED
            setLedValue2(true);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_C) {
            // Turn on the LED
            setLedValue3(true);
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_A) {
            // Turn off the LED
            setLedValue1(false);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_B) {
            // Turn off the LED
            setLedValue2(false);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_C) {
            // Turn off the LED
            setLedValue3(false);
            return true;
        }


        return super.onKeyUp(keyCode, event);
    }

    /**
     * Update the value of the LED output.
     */
    private void setLedValue1(boolean value) {
        try {
            mLedGpio1.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }

    private void setLedValue2(boolean value) {
        try {
            mLedGpio2.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }
    private void setLedValue3(boolean value) {
        try {
            mLedGpio3.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (mButtonInputDriver1 != null) {
            mButtonInputDriver1.unregister();
            try {
                mButtonInputDriver1.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally{
                mButtonInputDriver1 = null;
            }
        }

        if (mLedGpio1 != null) {
            try {
                mLedGpio1.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally{
                mLedGpio1 = null;
            }
            mLedGpio1 = null;
        }
        if (mLedGpio2 != null) {
            try {
                mLedGpio2.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally{
                mLedGpio2 = null;
            }
            mLedGpio2 = null;
        }

        if (mLedGpio3 != null) {
            try {
                mLedGpio3.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally{
                mLedGpio3 = null;
            }
            mLedGpio3 = null;
        }

    }
}
