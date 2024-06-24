package com.example.calculator

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.calculator.databinding.ActivityMainBinding
import java.io.File
import androidx.camera.core.CameraX


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var inputValue1: Double? = 0.0
    private var inputValue2: Double? = null
    private var currentOperator: Operator? = null
    private var result: Double? = null
    private var equation: StringBuilder = StringBuilder().append(ZERO)

    private var sequenciaPressionada = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()

        val videosDir = File(filesDir, "videos")
        if (!videosDir.exists()) {
            videosDir.mkdirs()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                }
            }
        } else {
            requestRuntimePermissionStorageLegacy()
        }



    }



    private fun setListeners() {
        for (button in getNumericButtons()) {
            button.setOnClickListener { onNumberClicked(button.text.toString())}
        }
        with(binding) {
            ButtonZero.setOnClickListener{ onZeroClicked() }
            ButtonDoubleZero.setOnClickListener { onDoubleZeroClicked() }
            ButtonDecimalPoint.setOnClickListener { onDecimalPointClicked() }
            ButtonAddition.setOnClickListener { onOperatorClicked(Operator.ADDITION) }
            ButtonSubtraction.setOnClickListener { onOperatorClicked(Operator.SUBTRACTION) }
            ButtonMultiplication.setOnClickListener { onOperatorClicked(Operator.MULTIPLICATION) }
            buttonDivision.setOnClickListener { onOperatorClicked(Operator.DIVISION) }
            ButtonEquals.setOnClickListener {
                onEqualsClicked()
                sequenciaPressionada += "="
                checksequence()
                sequenciaPressionada = ""

            }
            buttonAllClear.setOnClickListener { onAllClearClicked() }
            buttonPlusMinus.setOnClickListener { onPlusMinusClicked() }
            buttonPercentage.setOnClickListener { onPercentageClicked() }
            ButtonOne.setOnClickListener { onNumberClicked(numberText = "1") }
            ButtonTwo.setOnClickListener { onNumberClicked(numberText = "2") }
            ButtonThree.setOnClickListener { onNumberClicked(numberText = "3") }
            ButtonFour.setOnClickListener { onNumberClicked(numberText = "4") }
            ButtonFive.setOnClickListener { onNumberClicked(numberText = "5") }
            ButtonSix.setOnClickListener { onNumberClicked(numberText = "6") }
            ButtonSeven.setOnClickListener { onNumberClicked(numberText = "7") }
            ButtonEight.setOnClickListener { onNumberClicked(numberText = "8") }
            ButtonNine.setOnClickListener { onNumberClicked(numberText = "9") }
        }
    }




    private fun onPercentageClicked() {
        if(inputValue2 == null) {
            val percentage = getInputValue1() / 100
            inputValue1 = percentage
            equation.clear().append(percentage)
            updateInputOnDisplay()
        }else {
            val percentageOfValue1 = (getInputValue1() * getInputValue2()) / 100
            val percentageOfValue2 = getInputValue2() / 100
            result = when(requireNotNull(currentOperator)) {
                Operator.ADDITION -> getInputValue1() + percentageOfValue1
                Operator.SUBTRACTION -> getInputValue1() - percentageOfValue1
                Operator.MULTIPLICATION -> getInputValue1() * percentageOfValue2
                Operator.DIVISION -> getInputValue1() / percentageOfValue2
            }
            equation.clear().append(ZERO)
            updateResultOnDisplay(isPercentage = true)
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null
        }
    }

    //request permisson

    private fun requestRuntimePermissionStorageLegacy(): Boolean{
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
            return false
        }

        return true
    }

    private fun requestRuntimePermissionStorage(): Boolean{
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE), 12)
            return false
        }

        return true
    }

    private fun requestRuntimePermissionCamera(): Boolean{
        if(ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 11)
            return false
        }

        return true
    }

    private fun requestRuntimePermissionAudio(): Boolean{
        if(ActivityCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), 10)
            return false
        }

        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
            }
        }

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }

        if (requestCode == 11) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 13)
            }
        }
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), 13)
            }
        }
    }

    private fun onPlusMinusClicked() {
        if(equation.startsWith(MINUS)) {
            equation.deleteCharAt(0)
        } else {
            equation.insert(0, MINUS)
        }
        setInput()
        updateInputOnDisplay()
    }

    private fun onAllClearClicked() {
        inputValue1 = 0.0
        inputValue2 = null
        currentOperator = null
        result = null
        equation.clear().append(ZERO)
        clearDisplay()

        sequenciaPressionada = ""
    }

    private fun onOperatorClicked(operator: Operator) {
        onEqualsClicked()
        currentOperator = operator
    }

    private fun onEqualsClicked() {
        if (inputValue2 != null) {
            result = calculate()
            equation.clear().append(ZERO)
            updateResultOnDisplay()
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null
        } else {
            equation.clear().append(ZERO)
        }

    }

    private fun goToSGallery(){
        val intent = Intent(this, Sgallery::class.java)
        startActivity(intent)
    }

    fun checksequence() {

        if (sequenciaPressionada == "632=") {
            goToSGallery()
            sequenciaPressionada = ""
        }

        if (sequenciaPressionada == "478") {

        }

        if (sequenciaPressionada.length >= 4) {
            sequenciaPressionada = ""
        }

    }

    private fun calculate(): Double {
        return when(requireNotNull(currentOperator)) {
            Operator.ADDITION -> getInputValue1() + getInputValue2()
            Operator.SUBTRACTION-> getInputValue1() - getInputValue2()
            Operator.MULTIPLICATION -> getInputValue1() * getInputValue2()
            Operator.DIVISION -> getInputValue1() / getInputValue2()
        }
    }

    private fun onDecimalPointClicked() {
        if(equation.contains(DECIMAL_POINT)) return
        equation.append(DECIMAL_POINT)
        setInput()
        updateInputOnDisplay()
    }

    private fun onZeroClicked() {
        if(equation.startsWith(ZERO)) return
        onNumberClicked(ZERO)
    }

    private fun onDoubleZeroClicked() {
        if(equation.startsWith(ZERO)) return
        onNumberClicked(DOUBLE_ZERO)
    }

    private fun getNumericButtons() = with(binding) {
        arrayOf(
            ButtonOne,
            ButtonTwo,
            ButtonThree,
            ButtonFour,
            ButtonFive,
            ButtonSix,
            ButtonSeven,
            ButtonEight,
            ButtonNine
        )
    }

    private fun onNumberClicked(numberText: String) {
        if(equation.startsWith(ZERO)) {
            equation.deleteCharAt(0)
        }else if(equation.startsWith("$MINUS$ZERO")) {
            equation.deleteCharAt(1)
        }
        equation.append(numberText)
        setInput()
        updateInputOnDisplay()

        sequenciaPressionada += numberText
        checksequence()
    }

    private fun setInput() {
        if (currentOperator == null) {
            inputValue1 = equation.toString().toDouble()
        } else {
            inputValue2 = equation.toString().toDouble()
        }
    }



    private fun clearDisplay() {
        with(binding) {
            textInput.text = getFormattedDisplayValue(value = getInputValue1())
            textEquation.text   = null
        }
    }

    private fun updateResultOnDisplay(isPercentage: Boolean = false) {
        binding.textInput.text = getFormattedDisplayValue(value = result)
        var input2Text = getFormattedDisplayValue(value = getInputValue2())
        if (isPercentage) input2Text = "$input2Text${getString(R.string.percentage)}"
        binding.textEquation.text = String.format(
            "%s %s %s" ,
            getFormattedDisplayValue(value = getInputValue1()),
            getOperatorSymbol(),
            input2Text
        )
    }

    private fun updateInputOnDisplay() {
        if (result == null) {
            binding.textEquation.text = null
        }
        binding.textInput.text = equation
    }

    private fun getInputValue1() = inputValue1 ?: 0.0
    private fun getInputValue2() = inputValue2 ?: 0.0

    private fun getOperatorSymbol(): String {
        return when(requireNotNull(currentOperator)) {
            Operator.ADDITION -> getString(R.string.addition)
            Operator.SUBTRACTION -> getString(R.string.subtraction)
            Operator.MULTIPLICATION -> getString(R.string.multiplication)
            Operator.DIVISION -> getString(R.string.division)
        }
    }

    private fun getFormattedDisplayValue(value: Double?): String? {
        val originalValue = value ?: return null
        return if (originalValue % 1 == 0.0) {
            originalValue.toInt().toString()
        } else {
            originalValue.toString()
        }
    }



    enum class Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION

    }

    private companion object  {
        const val DECIMAL_POINT = "."
        const val ZERO = "0"
        const val DOUBLE_ZERO = "00"
        const val MINUS = "-"

        const val EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH"
    }
}