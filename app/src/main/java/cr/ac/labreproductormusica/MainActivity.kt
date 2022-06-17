package cr.ac.labreproductormusica

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object{
        var OPEN_DIRECTORY_REQUEST_CODE = 1
        lateinit var arrayCanciones:Array<DocumentFile>
        var orden : Int = 0
    }

    private lateinit var buttonPlay: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonStop: Button
    private lateinit var buttonSig: Button
    private lateinit var buttonAtras: Button

    private lateinit var textNombreCancion: TextView

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer()
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonStop = findViewById(R.id.buttonStop)
        buttonSig =findViewById(R.id.buttonSig)
        buttonAtras = findViewById(R.id.buttonAtras)
        textNombreCancion = findViewById(R.id.textNombreCancion)

        setOnClickListeners(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_DIRECTORY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK) {
                var directoryUri = data?.data ?: return
                //Log.e("Directorio: ", directoryUri.toString())
                val rootTree = DocumentFile.fromTreeUri(this, directoryUri)

                /*for(file in rootTree!!.listFiles()){
                    try {
                        file.name?.let { Log.e("Archivo: ", it) }

                        mediaPlayer.setDataSource(this, file.uri)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }catch (e: Exception){
                        Log.e("Error", "No se puede reproducir el archivo " + file.uri)
                    }

                }*/
                arrayCanciones = rootTree!!.listFiles() // al vector se le asignan las canciones
                textNombreCancion.text = arrayCanciones[orden].name
                //mediaPlayer.setDataSource(this, file.uri)
                mediaPlayer.setDataSource(this, arrayCanciones[1].uri)
                mediaPlayer.prepare()
                mediaPlayer.start()

            }
        }
    }

    private fun setOnClickListeners(context: Context) {
        buttonPlay.setOnClickListener {
            mediaPlayer.start()
            textNombreCancion.text = arrayCanciones[orden].name
        }

        buttonPause.setOnClickListener {
            mediaPlayer.pause()

        }

        buttonStop.setOnClickListener {
            mediaPlayer.stop()
        }
        buttonAtras.setOnClickListener{
            if(orden < 0){
                Toast.makeText(context, "primera cancion...", Toast.LENGTH_SHORT).show()
            }else{
                orden= arrayCanciones.size-1
            }
            parar(context)
            mediaPlayer.start()
            textNombreCancion.text = arrayCanciones[orden].name
        }
        buttonSig.setOnClickListener {
            if(orden < arrayCanciones.size-1){
                orden++
            }else{
                Toast.makeText(context, "Es la ultima...", Toast.LENGTH_SHORT).show()
            }
            parar(context)
            mediaPlayer.start()
            textNombreCancion.text = arrayCanciones[orden].name
        }
    }

    private fun parar(context: Context){
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(context, arrayCanciones[orden].uri)
    }
}