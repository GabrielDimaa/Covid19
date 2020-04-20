package com.example.covid19

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

open class MainActivity : AppCompatActivity() {

  var lista = arrayListOf<Boletim>()
  var adapter = Boletim_Adapter(this, lista)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    readJson(this)
    list(lista)
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
      }
    }
    catch (e : IOException){
    Log.e("Erro", "Impossivel ler JSON")
    }

  }

  fun list(lista: ArrayList<Boletim>) {
    lista.reverse()
    initRecycler()
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