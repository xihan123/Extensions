package cn.xihan.extensionstest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import cn.xihan.extensionstest.databinding.ActivityOkservableBinding
import cn.xihan.lib.okservable.collection.observableList
import timber.log.Timber

class OkservableActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOkservableBinding.inflate(layoutInflater) }

    val items = mutableListOf("1", "2", "3", "4", "5").observableList {
        add { index, element ->
            Timber.tag("xihantest").d("add[$index]: $element")
        }
        remove { index, element ->
            Timber.tag("xihantest").d("remove[$index]: $element")
        }
        clear { elements ->
            Timber.tag("xihantest").d("clear: $elements")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            items += (items.size + 1).toString()
        }

        binding.btnRemove.setOnClickListener {
            items.removeAt(0)
        }

        binding.btnClean.setOnClickListener {
            items.clear()
        }

    }

}