package cn.xihan.extensionstest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import cn.xihan.extensionstest.databinding.ActivityMainBinding

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 1:49
 * @介绍 :
 */
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(this, OkservableActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this, BannerActivity::class.java))
        }

        binding.button3.setOnClickListener {
            startActivity(Intent(this, KvActivity::class.java))
        }

    }

}