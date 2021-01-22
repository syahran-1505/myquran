package com.myalquran.activities
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.myalquran.R
import com.myalquran.adapter.SurahAdapter
import com.myalquran.adapter.SurahAdapter.onSelectData
import com.myalquran.model.ModelSurah
import com.myalquran.networking.Api
import kotlinx.android.synthetic.main.activity_list_surah.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class ListSurahActivity : AppCompatActivity(), onSelectData, View.OnClickListener {

    var surahAdapter: SurahAdapter? = null
    var progressDialog: ProgressDialog? = null
    var modelSurah: MutableList<ModelSurah> = ArrayList()
    var hariIni: String? = null
    var tanggal: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_surah)



        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Mohon Tunggu")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Sedang menampilkan data...")

        val dateNow = Calendar.getInstance().time
        hariIni = DateFormat.format("EEEE", dateNow) as String
        tanggal = DateFormat.format("d MMMM yyyy", dateNow) as String
        tvToday.setText("$hariIni,")
        tvDate.setText(tanggal)


        rvSurah.setLayoutManager(LinearLayoutManager(this))
        rvSurah.setHasFixedSize(true)



        //Methods get data
        listSurah()
    }

    private fun listSurah() {
        progressDialog!!.show()
        AndroidNetworking.get(Api.URL_LIST_SURAH)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    for (i in 0 until response.length()) {
                        try {
                            progressDialog!!.dismiss()
                            val dataApi = ModelSurah()
                            val jsonObject = response.getJSONObject(i)
                            dataApi.nomor = jsonObject.getString("nomor")
                            dataApi.nama = jsonObject.getString("nama")
                            dataApi.type = jsonObject.getString("type")
                            dataApi.ayat = jsonObject.getString("ayat")
                            dataApi.asma = jsonObject.getString("asma")
                            dataApi.arti = jsonObject.getString("arti")
                            dataApi.audio = jsonObject.getString("audio")
                            dataApi.keterangan = jsonObject.getString("keterangan")
                            modelSurah.add(dataApi)
                            showListSurah()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this@ListSurahActivity, "Gagal menampilkan data!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onError(anError: ANError) {
                    progressDialog!!.dismiss()
                    Toast.makeText(this@ListSurahActivity, "Tidak ada jaringan internet!",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showListSurah() {
        surahAdapter = SurahAdapter(this@ListSurahActivity, modelSurah, this)
        rvSurah!!.adapter = surahAdapter
    }

    override fun onSelected(modelSurah: ModelSurah) {
        val intent = Intent(this@ListSurahActivity, DetailSurahActivity::class.java)
        intent.putExtra("detailSurah", modelSurah)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        val menuItem = menu!!.findItem(R.id.search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }


}



