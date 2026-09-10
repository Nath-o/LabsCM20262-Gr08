package co.edu.udea.compumovil.labs20262_gr08.lab1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import co.edu.udea.compumovil.labs20262_gr08.lab1.ui.theme.Labs20262Gr08Theme
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Labs20262Gr08Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PersonalDataScreen { names, lastNames, gender, birthdate, education ->
                        val intent = Intent(this, ContactDataActivity::class.java).apply {
                            putExtra("EXTRA_NAMES", names)
                            putExtra("EXTRA_LASTNAMES", lastNames)
                            putExtra("EXTRA_GENDER", gender)
                            putExtra("EXTRA_BIRTHDATE", birthdate)
                            putExtra("EXTRA_EDUCATION", education)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    onNext: (names: String, lastNames: String, gender: String, birthdate: String, education: String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var names by rememberSaveable { mutableStateOf("") }
    var lastNames by rememberSaveable { mutableStateOf("") }
    var selectedGender by rememberSaveable { mutableStateOf("") }
    var birthdate by rememberSaveable { mutableStateOf("") }
    var educationLevel by rememberSaveable { mutableStateOf("") }

    var showErrors by rememberSaveable { mutableStateOf(false) }
    var expandedEducation by remember { mutableStateOf(false) }

    val educationLevels = stringArrayResource(R.array.grados_escolaridad)
    val textFieldShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
                val newLocale = if (currentLocale.startsWith("en")) "es" else "en"
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLocale))
            }) {
                Text(text = stringResource(R.string.language_switch))
            }
        }

        Text(
            text = stringResource(R.string.title_personal_data),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = names,
            onValueChange = { names = it },
            label = { Text(stringResource(R.string.names_label) + " *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = showErrors && names.isBlank(),
            supportingText = { if (showErrors && names.isBlank()) Text(stringResource(R.string.mandatory_field)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = textFieldShape,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lastNames,
            onValueChange = { lastNames = it },
            label = { Text(stringResource(R.string.lastnames_label) + " *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = showErrors && lastNames.isBlank(),
            supportingText = { if (showErrors && lastNames.isBlank()) Text(stringResource(R.string.mandatory_field)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = textFieldShape,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.gender_label),
            modifier = Modifier.padding(top = 8.dp),
            fontWeight = FontWeight.Medium
        )
        Row(
            Modifier
                .selectableGroup()
                .padding(vertical = 8.dp)
        ) {
            val options = listOf(stringResource(R.string.gender_male), stringResource(R.string.gender_female))
            options.forEach { text ->
                Row(
                    Modifier
                        .selectable(
                            selected = (text == selectedGender),
                            onClick = { selectedGender = text },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedGender),
                        onClick = null
                    )
                    Text(text = text, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = birthdate,
            onValueChange = { },
            label = { Text(stringResource(R.string.birthdate_label) + " *") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            isError = showErrors && birthdate.isBlank(),
            supportingText = { if (showErrors && birthdate.isBlank()) Text(stringResource(R.string.mandatory_field)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            shape = textFieldShape,
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            birthdate = "$dayOfMonth/${month + 1}/$year"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expandedEducation,
            onExpandedChange = { expandedEducation = !expandedEducation }
        ) {
            OutlinedTextField(
                value = educationLevel,
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.education_label)) },
                leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEducation) },
                shape = textFieldShape,
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedEducation,
                onDismissRequest = { expandedEducation = false }
            ) {
                educationLevels.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level) },
                        onClick = {
                            educationLevel = level
                            expandedEducation = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (names.isNotBlank() && lastNames.isNotBlank() && birthdate.isNotBlank()) {
                    onNext(names, lastNames, selectedGender, birthdate, educationLevel)
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