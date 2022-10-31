package com.volkov.epgrecycler

import android.view.KeyEvent

fun KeyEvent.onLeftPressed() =
    this.action == KeyEvent.ACTION_DOWN && (this.keyCode == KeyEvent.KEYCODE_DPAD_LEFT)

fun KeyEvent.onRightPressed() =
    this.action == KeyEvent.ACTION_DOWN && (this.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)

fun KeyEvent.onUpPressed() =
    this.action == KeyEvent.ACTION_DOWN && this.keyCode == KeyEvent.KEYCODE_DPAD_UP

fun KeyEvent.onDownPressed() =
    this.action == KeyEvent.ACTION_DOWN && this.keyCode == KeyEvent.KEYCODE_DPAD_DOWN