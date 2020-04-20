package com.example.covid19

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_estisticas.*
import org.json.JSONArray
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainEstatisticas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estisticas)

        val actionbar = supportActionBar
        var estatisticas = getString(R.string.estatisticas);

        actionbar!!.title = estatisticas

        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val data = intent.getStringExtra("Data")
        throughJson(this, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun throughJson(context: Context, data: String) {
        var json: String?=null
        val inputStream: InputStream = context.assets.open("data.json")
        json = inputStream.bufferedReader().use { it.readText() }
        var jsonArray = JSONArray(json)

        for (i in 0 .. jsonArray.length()-1){
            var js = jsonArray.getJSONObject(i)
            var dia = formatarData(js.getString("boletim").substring(0,10))
            if(data.equals(dia)) {
                val hora = js.getString("boletim").substring(11, 16)
                val obito = js.getInt("mortes")
                val suspeitos = js.getInt("Suspeitos")
                val confirmados = js.getInt("Confirmados")
                val curados = js.getInt("Curados")
                val monitoramento = js.getInt("Monitoramento")
                val descartados = js.getInt("Descartados")
                val sDomiciliar = js.getInt("Sdomiciliar")
                val sHopitalar = js.getInt("Shopitalar")
                val cHospitalar = js.getInt("Chospitalar")

                viewData.text = data
                viewCHospitalar.text = cHospitalar.toString()
                viewConfirm.text = confirmados.toString()
                viewCurados.text = curados.toString()
                viewDescartados.text = descartados.toString()
                viewMonitoramento.text = monitoramento.toString()
                viewSDomiciliar.text = sDomiciliar.toString()
                viewSuspeitos.text = suspeitos.toString()
                viewObitos.text = obito.toString()
                viewHora.text = hora
                viewSHospitalar.text = sHopitalar.toString()
            }
        }
    }

    fun formatarData(data: String): String {
        val diaString= data
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
    }
}
