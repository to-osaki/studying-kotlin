package com.toosaki.androidspeechrecognizer

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity()
{
    private var permitted = false
    private lateinit var label : TextView
    private lateinit var recognizer : SpeechRecognizer

    private val recognitionListener: RecognitionListener = object : RecognitionListener
    {
        override fun onResults(results: Bundle)
        {
            val values = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if(values.isNullOrEmpty()) {
                label.text = null
            }
            else {
                label.text = values?.joinToString("\n")
            }
        }
        override fun onReadyForSpeech(p0: Bundle?) {
            label.text = "ready"
        }

        override fun onRmsChanged(p0: Float) {
            label.text = "rms changed"
        }

        override fun onBufferReceived(p0: ByteArray?) {
            label.text = "buffer received"
        }

        override fun onPartialResults(p0: Bundle?) {
            label.text = "partial results"
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            label.text = "event:${p0}"
        }

        override fun onBeginningOfSpeech() {
            label.text = "begin"
        }

        override fun onEndOfSpeech() {
            label.text = "end"
        }

        override fun onError(p0: Int) {
            label.text = "error:${p0}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizer.setRecognitionListener(recognitionListener)

        label = findViewById<TextView>(R.id.label_main);
        findViewById<Button>(R.id.button_main).setOnClickListener {
            when(this.permitted) {
                true -> startListening()
                else -> return@setOnClickListener
            }
        }

        checkPermission()
    }

    private fun startListening()
    {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.JAPAN.toString())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speech")
        //startActivityForResult(intent, 0)
        recognizer.startListening(intent)
    }

    // startActivityForResult ->
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && requestCode == 0 && resultCode == Activity.RESULT_OK)
        {
            val candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (candidates!!.isNotEmpty())
            {
                label.text = candidates[0]
            }
        }
    }

    private fun checkPermission()
    {
        var recordPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        this.permitted = (recordPermission == PackageManager.PERMISSION_GRANTED)
        if (!this.permitted)
        {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO))
            {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.RECORD_AUDIO),
                        0)
            }
        }
        else
        {
            label.text = "need permission"
        }
    }

    // requestPermissions ->
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.any {it == PackageManager.PERMISSION_GRANTED})
        {
            permitted = true
        }
    }
}