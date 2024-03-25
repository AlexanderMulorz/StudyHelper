package com.common.studyhelper

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.common.studyhelper.databinding.FragmentSecondBinding
import com.common.studyhelper.helper.GPTAPIHandler
import com.google.android.material.R.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.floor

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var buttonClicked = false
    private lateinit var jsonString: String
    private var counter= 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gptapihandler = GPTAPIHandler()

        val activity = context as MainActivity
        val transcript = activity.getTranscript()

        if (transcript != null) {
            gptapihandler.getQuestion(transcript){ response ->

                activity?.runOnUiThread{
                    jsonString = response

                    val jsonReturnValue = JSONObject(response)
                    val questionList = jsonReturnValue.getJSONArray("question_list")
                    val jsonObject = JSONObject(questionList[counter].toString())
                    val question = jsonObject.getString("question")
                    val correctAnswer = jsonObject.getString("correct_answer")
                    val answers = jsonObject.getJSONArray("false_answers")
                    answers.put(3,correctAnswer)

                    val shuffeledAnswers = shuffle(answers)

                    binding.textQuestion.text=question
                    binding.button.text=shuffeledAnswers[0]
                    binding.button2.text=shuffeledAnswers[1]
                    binding.button3.text = shuffeledAnswers[2]
                    binding.button4.text = shuffeledAnswers[3]


                    binding.layout1.visibility=View.VISIBLE
                    binding.layout2.visibility=View.VISIBLE

                    binding.button.setOnClickListener { onClick(binding.button, correctAnswer) }
                    binding.button2.setOnClickListener { onClick(binding.button2, correctAnswer) }
                    binding.button3.setOnClickListener { onClick(binding.button3, correctAnswer) }
                    binding.button4.setOnClickListener { onClick(binding.button4, correctAnswer) }


                }

            }
        }

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            reset()
            binding.buttonNext.visibility=View.INVISIBLE
        }
    }

    private fun reset(){
        if(counter < 2){
            counter++

        binding.button.setBackgroundColor(getResources().getColor(color.design_default_color_primary))
        binding.button2.setBackgroundColor(getResources().getColor(color.design_default_color_primary))
        binding.button3.setBackgroundColor(getResources().getColor(color.design_default_color_primary))
        binding.button4.setBackgroundColor(getResources().getColor(color.design_default_color_primary))
        buttonClicked=false

        val jsonReturnValue = JSONObject(jsonString)
        val questionList = jsonReturnValue.getJSONArray("question_list")
        val jsonObject = JSONObject(questionList[counter].toString())
        val question = jsonObject.getString("question")
        val correctAnswer = jsonObject.getString("correct_answer")
        val answers = jsonObject.getJSONArray("false_answers")
        answers.put(3,correctAnswer)

        val shuffeledAnswers = shuffle(answers)

        binding.textQuestion.text=question
        binding.button.text=shuffeledAnswers[0]
        binding.button2.text=shuffeledAnswers[1]
        binding.button3.text = shuffeledAnswers[2]
        binding.button4.text = shuffeledAnswers[3]

        binding.button.setOnClickListener { onClick(binding.button, correctAnswer) }
        binding.button2.setOnClickListener { onClick(binding.button2, correctAnswer) }
        binding.button3.setOnClickListener { onClick(binding.button3, correctAnswer) }
        binding.button4.setOnClickListener { onClick(binding.button4, correctAnswer) }
        }else{//np more questions
            binding.layout1.visibility=View.INVISIBLE
            binding.layout2.visibility=View.INVISIBLE
            binding.textQuestion.text="No more Questions"
        }

    }

    fun onClick(view:View, correctAnswer:String){
        val clickedButton: Button = view as Button
        if(!buttonClicked){
            if(clickedButton.text.toString() == correctAnswer){
                clickedButton.setBackgroundColor(Color.parseColor("#008000"))
            }else{
                clickedButton.setBackgroundColor(Color.parseColor("#ff0000"))
                if(binding.button.text == correctAnswer){
                    binding.button.setBackgroundColor(Color.parseColor("#008000"))
                }else if(binding.button2.text == correctAnswer){
                    binding.button2.setBackgroundColor(Color.parseColor("#008000"))
                }else if(binding.button3.text == correctAnswer){
                    binding.button3.setBackgroundColor(Color.parseColor("#008000"))
                }else if(binding.button3.text == correctAnswer){
                    binding.button3.setBackgroundColor(Color.parseColor("#008000"))
                }
            }
            buttonClicked=true
            binding.buttonNext.visibility=View.VISIBLE
        }

    }
    private fun shuffle(a: JSONArray): Array<String> {
        var j = 0
        var i = 0
        var array:Array<String> = arrayOf("", "", "", "")
        for (i in 0 until a.length()){
            j = floor(Math.random() * (i + 1)).toInt();
            var x = a[i];
            array[i] = a[j] as String;
            array[j] = x.toString();
        }
        return array;
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}