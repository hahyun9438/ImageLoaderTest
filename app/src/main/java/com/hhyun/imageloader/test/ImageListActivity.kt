package com.hhyun.imageloader.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hhyun.imageloader.Util
import com.hhyun.imageloader.databinding.ActivityImageListBinding

class ImageListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityImageListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()
    }

    private fun setAdapter() {

        val list = arrayListOf<String>(
            "https://cdn.pixabay.com/photo/2022/12/25/09/52/winter-forest-7677111_1280.jpg",
            "https://cdn.pixabay.com/photo/2022/12/20/05/27/european-crested-tit-7667110__340.jpg",
            "https://cdn.pixabay.com/photo/2020/12/28/20/45/frozen-lake-5868472_640.jpg",
            "https://cdn.pixabay.com/photo/2021/01/06/19/17/forest-5895422__340.jpg",
            "https://cdn.pixabay.com/photo/2022/01/06/22/30/new-year-6920394__340.jpg",
            "https://cdn.pixabay.com/photo/2021/12/05/12/29/christmas-tree-6847584_640.jpg",
            "https://cdn.pixabay.com/photo/2022/01/08/14/53/town-6924142__340.jpg",
            "https://cdn.pixabay.com/photo/2022/11/20/11/39/forest-7604096_640.jpg",
            "https://cdn.pixabay.com/photo/2022/12/13/17/55/robin-7653878_640.jpg",
            "https://cdn.pixabay.com/photo/2022/12/01/22/02/bird-7629852__480.jpg",
            "https://cdn.pixabay.com/photo/2022/10/08/06/31/hungarian-parliament-building-7506436__480.jpg",
            "https://cdn.pixabay.com/photo/2022/11/05/20/18/zebra-7572734__480.jpg",
            "https://cdn.pixabay.com/photo/2022/12/12/19/14/paris-7651738__480.jpg",
            "https://cdn.pixabay.com/photo/2022/01/13/07/06/house-6934544__480.jpg",
            "https://cdn.pixabay.com/photo/2022/12/06/00/25/beach-7637946__480.jpg",
            "https://cdn.pixabay.com/photo/2022/11/28/20/52/bird-7623166__480.jpg",
            "https://cdn.pixabay.com/photo/2022/11/30/13/16/tel-aviv-7626789__480.jpg",
            "https://cdn.pixabay.com/photo/2022/12/02/14/13/desert-7630943__480.jpg",
            "https://cdn.pixabay.com/photo/2021/10/23/23/27/dead-sea-6736592__480.jpg",
            "https://cdn.pixabay.com/photo/2022/12/01/17/52/sea-7629517__480.jpg",
            "https://cdn.pixabay.com/photo/2022/05/16/10/08/blue-tit-7199966__480.jpg",
            "https://cdn.pixabay.com/photo/2022/12/03/12/11/coffee-7632568__480.jpg",
            "https://cdn.pixabay.com/photo/2022/11/21/19/23/flower-7607979__480.jpg",
            "https://cdn.pixabay.com/photo/2022/11/17/17/11/sea-7598498__480.jpg",
            "https://cdn.pixabay.com/photo/2022/11/19/11/53/rose-7601873__480.jpg",
            "https://cdn.pixabay.com/photo/2022/01/24/18/57/mountains-6964409__480.jpg",
            "https://cdn.pixabay.com/photo/2022/10/18/12/05/clouds-7530090__480.jpg",
            "https://cdn.pixabay.com/photo/2021/11/24/17/39/seal-6821775__480.jpg",
            "https://cdn.pixabay.com/photo/2022/10/19/02/48/girl-7531494__480.jpg",
            "https://cdn.pixabay.com/photo/2022/10/22/06/03/mountains-7538471__480.jpg",
        )

        val deviceWidth = Util.getDeviceWidth(this)
        val height = (deviceWidth * 2.21f).toInt()

        val listAdapter = ImageListAdapter(Glide.with(this), height)

        binding.rv.apply {
            layoutManager = PreCacheLayoutManger(height)
            adapter = listAdapter
        }

        listAdapter.submitList(list)

    }


    inner class PreCacheLayoutManger(private val mHeight: Int): LinearLayoutManager(this) {

        override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
            val spaceHeight = (mHeight * 1.2f).toInt()
            return spaceHeight
        }
    }

}