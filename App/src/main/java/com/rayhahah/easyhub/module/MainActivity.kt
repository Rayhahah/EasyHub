package com.rayhahah.easyhub.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rayhahah.baselib.ext.log
import com.rayhahah.baselib.ext.sub
import com.rayhahah.easyhub.R
import io.reactivex.Observable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var disposable = Observable.just("")
                .sub({ result ->
                })
        var hello="nihao"
        hello.log()
    }
}
