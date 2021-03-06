package com.fatihbaser.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatihbaser.retrofitkotlin.R
import com.fatihbaser.retrofitkotlin.adapter.RecyclerViewAdapter
import com.fatihbaser.retrofitkotlin.model.CryptoModel
import com.fatihbaser.retrofitkotlin.service.CryptoAPI
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() , RecyclerViewAdapter.Listener{
    private val BASE_URL="https://api.nomics.com/v1/"
    private var cryptoModels: ArrayList<CryptoModel>?=null
    private var recyclerViewAdapter : RecyclerViewAdapter? =null

    // disposable (tek kullanimlik)
    private var compositeDIsposable:  CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  2f410823829a2899de49fe78a4410f45
        //  https://api.nomics.com/v1/prices?key=2f410823829a2899de49fe78a4410f45


        compositeDIsposable= CompositeDisposable()
        //RecyclerView
        val layoutManager : RecyclerView.LayoutManager= LinearLayoutManager(this)
        recyklerView.layoutManager=layoutManager

        loadData()

    }

    private fun loadData(){
        val retrofit =Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(CryptoAPI :: class.java)

        compositeDIsposable?.add(retrofit.getData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponse))






        /*
                val service =retrofit.create(CryptoAPI::class.java)

        val call=service.getData()
        call.enqueue(object: Callback<List<CryptoModel>>{
            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) { //
                t.printStackTrace()
                println("olamdi")

            }

            override fun onResponse(call: Call<List<CryptoModel>>, response: Response<List<CryptoModel>>) {//eger cevap varsa

                if(response.isSuccessful){
                    response.body()?.let {
                        cryptoModels= ArrayList(it)


                        cryptoModels?.let {

                            recyclerViewAdapter= RecyclerViewAdapter(it,this@MainActivity)
                            recyklerView.adapter=recyclerViewAdapter
                        }


//                        for (cryptoModel : CryptoModel in cryptoModels!!){
//                            println(cryptoModel.currency)
//                            println(cryptoModel.price)
//                        }
                    }
                }

            }

        })*/
    }
    private fun handleResponse(cryptoList:List<CryptoModel>){
        cryptoModels= ArrayList(cryptoList)


        cryptoModels?.let {

            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            recyklerView.adapter = recyclerViewAdapter

        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked : ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {

        super.onDestroy()
        compositeDIsposable?.clear()
    }
}