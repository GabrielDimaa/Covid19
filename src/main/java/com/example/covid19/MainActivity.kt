package com.example.covid19

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_boletim.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

open class MainActivity : AppCompatActivity() {

  var lista = arrayListOf<Boletim>()
  var adapter = Boletim_Adapter(this, lista)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    readJson(this)
  }

  fun readJson(context: Context){
    var json: String?=null
    try {
      val inputStream: InputStream= context.assets.open("data.json")
      json = inputStream.bufferedReader().use { it.readText() }
      var jsonArray =JSONArray(json)
      for (i in 0 .. jsonArray.length()-1){
        var js = jsonArray.getJSONObject(i)
        val data = formatarData(js.getString("boletim").substring(0,10))
        val hora = js.getString("boletim").substring(11, 16)
        val obito = js.getInt("mortes")
        val casos = js.getInt("Confirmados")

        var boletim__: Boletim = Boletim(data = data, hora = hora, mortes = obito, confirmados = casos)

        lista.add(boletim__)
        initRecycler()
      }
    }
    catch (e : IOException){
    Log.e("Erro", "Impossivel ler JSON")
    }

  }
  fun formatarData(data: String): String {
    val diaString =data
    var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var date = LocalDate.parse(diaString)
    var formattedDate = date.format(formatter)
    return formattedDate
  }

  fun initRecycler() {
    lista_view.adapter = this.adapter
    val layout = LinearLayoutManager(this)
    lista_view.layoutManager = layout
  }

}