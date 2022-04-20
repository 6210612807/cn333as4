package com.example.unitconverter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unitconverter.R
import com.example.unitconverter.viewmodels.MassViewModel
import com.example.unitconverter.viewmodels.TemperatureViewModel

@Composable
fun MassConverter() {
    val viewModel: MassViewModel = viewModel()
    val strKilo = stringResource(id = R.string.kilo)
    val strPound = stringResource(id = R.string.pound)
    val currentValue = viewModel.mass.observeAsState(viewModel.mass.value ?:"")
    val scale = viewModel.scale.observeAsState(viewModel.scale.value ?:R.string.kilo)
    var result by rememberSaveable { mutableStateOf("") }

    val enabled by remember(currentValue.value) {
        mutableStateOf(!viewModel.getMassAsFloat().isNaN())
    }
    val calc = {
        val temp = viewModel.convert()
        result = if (temp.isNaN())
            ""
        else
            "$temp${
                if (scale.value == R.string.kilo)
                    strPound
                else strKilo
            }"
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MassTextField(
            mass = currentValue,
            modifier = Modifier.padding(bottom = 16.dp),
            callback = calc,
            viewModel = viewModel
        )
        MassScaleButtonGroup(
            selected = scale,
            modifier = Modifier.padding(bottom = 16.dp)
        ) { resId: Int -> viewModel.setScale(resId) }
        Button(
            onClick = calc,
            enabled = enabled
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.h3
            )
        }
    }

}
@Composable
fun MassTextField(
    mass: State<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit,
    viewModel: MassViewModel
) {
    TextField(
        value = mass.value,
        onValueChange = {
            viewModel.setMass(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder_mass))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = {
            callback()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun MassScaleButtonGroup(
    selected: State<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    val sel = selected.value
    Row(modifier = modifier) {
        MassRadioButton(
            selected = sel == R.string.kilo,
            resId = R.string.kilo,
            onClick = onClick
        )
        MassRadioButton(
            selected = sel == R.string.pound,
            resId = R.string.pound,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun MassRadioButton(
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}