package co.edu.udea.compumovil.labs20262_gr08.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.edu.udea.compumovil.labs20262_gr08.lab1.ui.theme.Labs20262Gr08Theme
import android.app.DatePickerDialog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import co.edu.udea.compumovil.labs20262_gr08.lab1.databinding.ActivityMainBinding
import co.edu.udea.compumovil.labs20262_gr08.lab1.databinding.PersonalDataActivityBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: PersonalDataActivityBinding
    private var fechaSeleccionada: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PersonalDataActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        aplicarInsetsDelSistema()
        configurarSpinner()
        configurarFechaNacimiento()
        configurarBotonSiguiente()
    }

    private fun aplicarInsetsDelSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootContainer) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                barras.top + 8,
                view.paddingRight,
                barras.bottom + 8
            )
            insets
        }
    }

    private fun configurarSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.grados_escolaridad,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spGrado.adapter = adapter
    }

    private fun configurarFechaNacimiento() {
        binding.btnCambiarFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anioActual = calendario.get(Calendar.YEAR)
            val mesActual = calendario.get(Calendar.MONTH)
            val diaActual = calendario.get(Calendar.DAY_OF_MONTH)

            val dialogo = DatePickerDialog(
                this,
                {_,anio, mes, dia ->
                    fechaSeleccionada = "%02d/02d/%d".format(dia, mes+1, anio)
                    binding.tvFechaNacimiento.text ="*Fecha de Nacimiento: $fechaSeleccionada"
                },
                anioActual, mesActual, diaActual
            )
            dialogo.show()
        }
    }

    private fun configurarBotonSiguiente() {
        binding.btnSiguiente.setOnClickListener {
            if(validarCamposObligatorios()){
                Toast.makeText(this, "Datos válidos, continuar...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun validarCamposObligatorios(): Boolean {
        val nombres = binding.etNombres.text.toString().trim()
        val apellidos = binding.etApellidos.text.toString().trim()

        if(nombres.isEmpty()){
            binding.etNombres.error = "Este campo es obligatorio"
            return false
        }
        if(apellidos.isEmpty()){
            binding.etApellidos.error = "Este campo es obligatorio"
            return false
        }
        if(fechaSeleccionada == null){
            Toast.makeText(this, "Debes seleccionar tu fecha de nacimiento", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
