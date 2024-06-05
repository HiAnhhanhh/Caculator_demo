package com.example.caculator_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.caculator_demo.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {


    private var inputValue_2: Double? = null
    private var inputValue_1: Double? = null
    private lateinit var binding: ActivityMainBinding
    private var currentOperator : Operator? = null
    private var result : Double? = null
    private var equation : StringBuilder = StringBuilder().append(ZERO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun onAllClearClicked (){
        inputValue_1 = 0.0
        inputValue_2 = null
        currentOperator = null
        result = result
        equation.clear().append(ZERO)
        clearDisplay()
    }

    private fun clearDisplay() {
        with(binding){
            textInputTv.text= getFormattedDisplayValue(value = getInputValue1())
            textEquationTv.text = null
        }
    }

    private fun onOperatorClicked(operator : Operator){
        onEqualsClicked()
        currentOperator = operator
    }


    private fun onEqualsClicked() {
        if(inputValue_2 != null){
            result = calculate()
            equation.clear().append(ZERO)
            updateResultOnDisplay()
            inputValue_1 = result
            result = null
            inputValue_2 = null
            currentOperator = null
        }else{
            equation.clear().append(ZERO)
        }
    }

    private fun calculate() : Double {
        return when (requireNotNull(currentOperator)){
            Operator.ADDITION -> getInputValue1()+getInputValue2()
            Operator.SUBTRACTION -> getInputValue1() - getInputValue2()
            Operator.MULTIPLICATION -> getInputValue1() * getInputValue2()
            Operator.DIVISION -> getInputValue1() / getInputValue2()
        }
    }

    private fun setListener (){
        for(button in getNumericButton()){
            button.setOnClickListener {
                Log.d("check_button", "setListener: "+ button.text)
                onNumberClicked(button.text.toString())
            }
            with(binding){
                zeroBtn.setOnClickListener{
                    onNumberClicked(ZERO)
                }
                pointBtn.setOnClickListener {
                    onDecimalPointClicked()
                }
                plusBtn.setOnClickListener{
                    onOperatorClicked(Operator.ADDITION)
                }
                equalsBtn.setOnClickListener{
                    onEqualsClicked()
                }
                multiplicationBtn.setOnClickListener {
                    onOperatorClicked(Operator.MULTIPLICATION)
                }
                divisionBtn.setOnClickListener {
                    onOperatorClicked(Operator.DIVISION)
                }
                subtractionBtn.setOnClickListener {
                    onOperatorClicked(Operator.SUBTRACTION)
                }
                allClearBtn.setOnClickListener {
                    onAllClearClicked()
                }
            }
        }
    }

    private fun onDecimalPointClicked() {
        if(equation.contains(DECIMAL_POINT)) return
        equation.append(DECIMAL_POINT)
        setInput()
        updateInputOnDisplay()
    }

    private fun onNumberClicked(numberText: String){
        if(equation.startsWith(ZERO)){
            equation.deleteCharAt(0)
        } else if(equation.startsWith("$MINUS$ZERO")){
            equation.deleteCharAt(1)
        }
        equation.append(numberText)
        setInput()
        updateInputOnDisplay()
    }

    private fun setInput() {
        if(currentOperator == null){
            inputValue_1=equation.toString().toDouble()
        }else{
            inputValue_2 = equation.toString().toDouble()
        }

    }

    private fun getNumericButton() = with(binding){
        arrayOf(
            oneBtn,
            twoBtn,
            threeBtn,
            fourBtn,
            fiveBtn,
            sixBtn,
            sevenBtn,
            eightBtn,
            nineBtn,
        )
    }

    private fun updateResultOnDisplay(isPercentage:Boolean = false){
        binding.textInputTv.text = getFormattedDisplayValue(value = result)
        var input2Text = getFormattedDisplayValue(value = getInputValue2())
        if(isPercentage) input2Text = "$input2Text${getString(R.string.percent)}"
        binding.textEquationTv.text = String.format(
            "%s %s %s",
            getFormattedDisplayValue(value = getInputValue1()),
            getOperatorSymbol(),
            input2Text
        )
    }

    private fun updateInputOnDisplay(){
        if(result == null){
            binding.textInputTv.text = null
        }
        binding.textInputTv.text = equation
    }

    private fun getInputValue1() = inputValue_1 ?: 0.0
    private fun getInputValue2() = inputValue_2 ?: 0.0


    private fun getOperatorSymbol() : String {
        return when (requireNotNull(currentOperator)){
            Operator.ADDITION -> getString(R.string.plus_btn)
            Operator.SUBTRACTION -> getString(R.string.subtraction)
            Operator.DIVISION -> getString(R.string.division)
            Operator.MULTIPLICATION -> getString(R.string.multiplication)
        }
    }


    private fun getFormattedDisplayValue (value : Double?) : String? {
        val originalValue = value?: return null
        return if( originalValue % 1 == 0.0){
            originalValue.toInt().toString()
        }else{
            originalValue.toString()
        }
    }

    enum class Operator {
        ADDITION,SUBTRACTION, MULTIPLICATION, DIVISION
    }

    private companion object{
        const val DECIMAL_POINT ="."
        const val ZERO ="0"
        const val MINUS ="-"
    }
}