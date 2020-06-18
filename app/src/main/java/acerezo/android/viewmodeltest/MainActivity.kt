package acerezo.android.viewmodeltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val myModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myModel.uiState.observe(this, Observer(::updateUi))
    }

    private fun updateUi(uiState: UiState) {
        // Update the Ui here
    }
}
