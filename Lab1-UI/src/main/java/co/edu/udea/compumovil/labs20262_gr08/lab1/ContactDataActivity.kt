package co.edu.udea.compumovil.labs20262_gr08.lab1

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.udea.compumovil.labs20262_gr08.lab1.ui.theme.Labs20262Gr08Theme

class ContactDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val names = intent.getStringExtra("EXTRA_NAMES") ?: ""
        val lastNames = intent.getStringExtra("EXTRA_LASTNAMES") ?: ""
        val gender = intent.getStringExtra("EXTRA_GENDER") ?: ""
        val birthdate = intent.getStringExtra("EXTRA_BIRTHDATE") ?: ""
        val education = intent.getStringExtra("EXTRA_EDUCATION") ?: ""

        setContent {
            Labs20262Gr08Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactDataScreen(
                        onFinish = { phone, address, email, country, city ->
                            // Imprimir en Logcat utilizando los recursos traducidos del sistema
                            Log.i("Lab1_Info", "========================================")
                            Log.i("Lab1_Info", getString(R.string.log_personal_info))
                            Log.i("Lab1_Info", "${getString(R.string.log_names)} $names")
                            Log.i("Lab1_Info", "${getString(R.string.log_lastnames)} $lastNames")
                            Log.i("Lab1_Info", "${getString(R.string.log_gender)} $gender")
                            Log.i("Lab1_Info", "${getString(R.string.log_birthdate)} $birthdate")
                            if (education.isNotBlank()) Log.i("Lab1_Info", "${getString(R.string.log_education)} $education")
                            Log.i("Lab1_Info", "----------------------------------------")
                            Log.i("Lab1_Info", getString(R.string.log_contact_info))
                            Log.i("Lab1_Info", "${getString(R.string.log_phone)} $phone")
                            if (address.isNotBlank()) Log.i("Lab1_Info", "${getString(R.string.log_address)} $address")
                            Log.i("Lab1_Info", "${getString(R.string.log_email)} $email")
                            Log.i("Lab1_Info", "${getString(R.string.log_country)} $country")
                            if (city.isNotBlank()) Log.i("Lab1_Info", "${getString(R.string.log_city)} $city")
                            Log.i("Lab1_Info", "========================================")
                        },
                        onDialogDismiss = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDataScreen(
    onFinish: (phone: String, address: String, email: String, country: String, city: String) -> Unit,
    onDialogDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }

    var showErrors by rememberSaveable { mutableStateOf(false) }
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var expandedCountry by remember { mutableStateOf(false) }
    var expandedCity by remember { mutableStateOf(false) }

    // Validaciones
    val isPhoneValid = phone.isNotBlank() && phone.all { it.isDigit() }
    val isEmailValid = email.isBlank() || Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val countries = listOf(
        "Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Costa Rica", "Cuba",
        "Ecuador", "El Salvador", "Guatemala", "Haití", "Honduras", "México",
        "Nicaragua", "Panamá", "Paraguay", "Perú", "Puerto Rico", "República Dominicana",
        "Uruguay", "Venezuela"
    )
    val cities = listOf(
        "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Soledad",
        "Cúcuta", "Ibagué", "Soacha", "Bucaramanga", "Villavicencio", "Pereira",
        "Santa Marta", "Valledupar", "Bello", "Montería", "Pasto", "Manizales"
    )

    val filteredCountries = countries.filter { it.contains(country, ignoreCase = true) }
    val filteredCities = cities.filter { it.contains(city, ignoreCase = true) }

    val textFieldShape = RoundedCornerShape(16.dp)

    // Modal de Confirmación Internacionalizado
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.dialog_success_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.dialog_success_msg),
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onDialogDismiss()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.title_contact_data),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // * Teléfono (Con validación de números únicamente)
            val isPhoneError = showErrors && (!isPhoneValid)
            OutlinedTextField(
                value = phone,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) phone = input
                },
                label = { Text(stringResource(R.string.phone_label) + " *") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                isError = isPhoneError,
                supportingText = {
                    if (showErrors && phone.isBlank()) {
                        Text(stringResource(R.string.mandatory_field))
                    } else if (showErrors && !phone.all { it.isDigit() }) {
                        Text(stringResource(R.string.invalid_phone))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = textFieldShape,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dirección (Sin sugerencias de texto)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.address_label)) },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = textFieldShape,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password, // Desactiva la barra de sugerencias del teclado
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // * Email
            val isEmailError = showErrors && (email.isBlank() || !isEmailValid)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_label) + " *") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = isEmailError,
                supportingText = {
                    if (showErrors && email.isBlank()) {
                        Text(stringResource(R.string.mandatory_field))
                    } else if (showErrors && !isEmailValid) {
                        Text(stringResource(R.string.invalid_email))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = textFieldShape,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // * País
            ExposedDropdownMenuBox(
                expanded = expandedCountry && filteredCountries.isNotEmpty(),
                onExpandedChange = { expandedCountry = it }
            ) {
                OutlinedTextField(
                    value = country,
                    onValueChange = {
                        country = it
                        expandedCountry = true
                    },
                    label = { Text(stringResource(R.string.country_label) + " *") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    isError = showErrors && country.isBlank(),
                    supportingText = { if (showErrors && country.isBlank()) Text(stringResource(R.string.mandatory_field)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountry) },
                    shape = textFieldShape,
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                ExposedDropdownMenu(
                    expanded = expandedCountry && filteredCountries.isNotEmpty(),
                    onDismissRequest = { expandedCountry = false }
                ) {
                    filteredCountries.forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = {
                                country = selection
                                expandedCountry = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ciudad
            ExposedDropdownMenuBox(
                expanded = expandedCity && filteredCities.isNotEmpty(),
                onExpandedChange = { expandedCity = it }
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = {
                        city = it
                        expandedCity = true
                    },
                    label = { Text(stringResource(R.string.city_label)) },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCity) },
                    shape = textFieldShape,
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                ExposedDropdownMenu(
                    expanded = expandedCity && filteredCities.isNotEmpty(),
                    onDismissRequest = { expandedCity = false }
                ) {
                    filteredCities.forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = {
                                city = selection
                                expandedCity = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Siguiente con validación estricta de teléfono y correo
            Button(
                onClick = {
                    if (isPhoneValid && email.isNotBlank() && isEmailValid && country.isNotBlank()) {
                        onFinish(phone, address, email, country, city)
                        showSuccessDialog = true
                    } else {
                        showErrors = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = textFieldShape
            ) {
                Text(text = stringResource(R.string.btn_next), fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}